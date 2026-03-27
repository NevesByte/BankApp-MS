package com.login_ms.login_ms.services;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
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
import com.login_ms.login_ms.mapper.UserMapper;
import com.login_ms.login_ms.producers.UserDataProducer;
import com.login_ms.login_ms.producers.UserProducer;
import com.login_ms.login_ms.repository.UserEntityRepository;
import com.login_ms.login_ms.repository.UserRepository;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserEntityRepository userEntityRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtEncoder jwtEncoder;
    @Autowired
    UserProducer userProducer;
    @Autowired
    UserDataProducer userDataProducer;

    
    public ResponseEntity<UserLoginResponseDto> loginUser(UserLoginDto userLoginDto){
        Optional<UserLoginEntity> user = userRepository.findByEmail(userLoginDto.email());
        if(!user.isPresent() || !user.get().isLoginCorrect(userLoginDto, passwordEncoder)){
            throw new RuntimeException("ERRO: Usuário não existe ou sua senha está incorreta !");
        }
        
        var now = Instant.now();
        var expiresIn = 300L;

        /*
        Para Roles:
        var scopes = user.get().getRoles()
                         .stream().map(Role::getName)
                         .collect(Collectors.joining(" "));
        */

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend") 
                .subject(user.get().getIdUser().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                //.claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        userProducer.publishMessageEmailLogin(userLoginDto);
        return ResponseEntity.ok(new UserLoginResponseDto(jwtValue, expiresIn));
    }

    @Transactional
    public ResponseEntity<Void> saveUser(UserSignUpDto user){

        Optional<UserLoginEntity> existing = userRepository.findByEmail(user.email());
        if(existing.isPresent()){
            return ResponseEntity.status(409).build();
        }

        String senhaCriptografada = passwordEncoder.encode(user.password());

        UserLoginEntity userLoginEntity = new UserLoginEntity();
        BeanUtils.copyProperties(user, userLoginEntity);
        userLoginEntity.setPassword(senhaCriptografada);
        userRepository.save(userLoginEntity);

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setPassword(senhaCriptografada);
        userEntity.setBalance(new BigDecimal("0"));
        userEntity.setAccountStatus(AccountStatus.ACTIVE);
        userEntity.setLoginAttempts(0);
        userEntity.setBlockedUntil(null);
        userEntity.setCreatedAt(LocalDateTime.now());
        String accountNumber = UUID.randomUUID().toString();
        userEntity.setAccountNumber(accountNumber);
        userEntity.setAgency("0001");
        userEntity.setEmailVerified(false);
        userEntity.setActive(true);
        userEntityRepository.save(userEntity);
        
        userProducer.publishMessageEmailSignUp(userEntity);
        userDataProducer.userData(userEntity);
        return ResponseEntity.ok().build();
    }
}
