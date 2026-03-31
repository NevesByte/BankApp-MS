package com.login_ms.login_ms.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.login_ms.login_ms.dto.UserLoginDto;
import com.login_ms.login_ms.dto.UserLoginResponseDto;
import com.login_ms.login_ms.dto.UserSignUpDto;
import com.login_ms.login_ms.entity.UserEntity;
import com.login_ms.login_ms.entity.UserLoginEntity;
import com.login_ms.login_ms.enums.AccountStatus;
import com.login_ms.login_ms.exceptions.ConflictException;
import com.login_ms.login_ms.exceptions.InvalidCredentialsException;
import com.login_ms.login_ms.producers.UserDataProducer;
import com.login_ms.login_ms.producers.UserProducer;
import com.login_ms.login_ms.repository.UserEntityRepository;
import com.login_ms.login_ms.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private static final long EXPIRES_IN_SECONDS = 300L;

    private final UserRepository userRepository;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final UserProducer userProducer;
    private final UserDataProducer userDataProducer;

    public UserService(
        UserRepository userRepository,
        UserEntityRepository userEntityRepository,
        PasswordEncoder passwordEncoder,
        JwtEncoder jwtEncoder,
        UserProducer userProducer,
        UserDataProducer userDataProducer
    ) {
        this.userRepository = userRepository;
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.userProducer = userProducer;
        this.userDataProducer = userDataProducer;
    }

    public UserLoginResponseDto loginUser(UserLoginDto userLoginDto) {
        var user = userRepository.findByEmail(userLoginDto.email())
            .orElseThrow(() -> new InvalidCredentialsException("Usuario nao existe ou senha incorreta."));

        if (!user.isLoginCorrect(userLoginDto, passwordEncoder)) {
            throw new InvalidCredentialsException("Usuario nao existe ou senha incorreta.");
        }

        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
            .issuer("login-ms")
            .subject(user.getIdUser().toString())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(EXPIRES_IN_SECONDS))
            .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        userProducer.publishMessageEmailLogin(userLoginDto);
        return new UserLoginResponseDto(jwtValue, EXPIRES_IN_SECONDS);
    }

    @Transactional
    public void saveUser(UserSignUpDto user) {
        if (userRepository.findByEmail(user.email()).isPresent()) {
            throw new ConflictException("Ja existe usuario cadastrado com este email.");
        }

        String encryptedPassword = passwordEncoder.encode(user.password());

        UserLoginEntity userLoginEntity = new UserLoginEntity();
        BeanUtils.copyProperties(user, userLoginEntity);
        userLoginEntity.setPassword(encryptedPassword);
        userRepository.save(userLoginEntity);

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setPassword(encryptedPassword);
        userEntity.setBalance(new BigDecimal("0.00"));
        userEntity.setAccountStatus(AccountStatus.ACTIVE);
        userEntity.setLoginAttempts(0);
        userEntity.setBlockedUntil(null);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setAccountNumber(UUID.randomUUID().toString());
        userEntity.setAgency("0001");
        userEntity.setEmailVerified(false);
        userEntity.setActive(true);

        userEntityRepository.save(userEntity);
        userProducer.publishMessageEmailSignUp(userEntity);
        userDataProducer.userData(userEntity);
    }
}
