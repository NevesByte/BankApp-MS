package com.login_ms.login_ms.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import com.login_ms.login_ms.dto.UserLoginDto;
import com.login_ms.login_ms.dto.UserSignUpDto;
import com.login_ms.login_ms.entity.UserEntity;
import com.login_ms.login_ms.entity.UserLoginEntity;
import com.login_ms.login_ms.enums.AccountTypeEnum;
import com.login_ms.login_ms.exceptions.ConflictException;
import com.login_ms.login_ms.exceptions.InvalidCredentialsException;
import com.login_ms.login_ms.producers.UserDataProducer;
import com.login_ms.login_ms.producers.UserProducer;
import com.login_ms.login_ms.repository.UserEntityRepository;
import com.login_ms.login_ms.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserEntityRepository userEntityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtEncoder jwtEncoder;
    @Mock
    private UserProducer userProducer;
    @Mock
    private UserDataProducer userDataProducer;

    @InjectMocks
    private UserService userService;

    private UserSignUpDto signUpDto;

    @BeforeEach
    void setUp() {
        signUpDto = new UserSignUpDto(
            "Victor",
            "12345678909",
            "Maria",
            "victor@email.com",
            "11999999999",
            "123456",
            LocalDateTime.now().minusYears(20),
            AccountTypeEnum.CORRENTE
        );
    }

    @Test
    void shouldThrowConflictWhenEmailAlreadyExists() {
        when(userRepository.findByEmail(signUpDto.email())).thenReturn(Optional.of(new UserLoginEntity()));

        assertThrows(ConflictException.class, () -> userService.saveUser(signUpDto));

        verify(userRepository, never()).save(any(UserLoginEntity.class));
        verify(userEntityRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void shouldThrowInvalidCredentialsWhenPasswordIsWrong() {
        UserLoginEntity entity = new UserLoginEntity();
        entity.setIdUser(UUID.randomUUID());
        entity.setEmail("victor@email.com");
        entity.setPassword("encoded-password");

        when(userRepository.findByEmail("victor@email.com")).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("wrong", "encoded-password")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
            () -> userService.loginUser(new UserLoginDto("victor@email.com", "wrong")));
    }

    @Test
    void shouldSaveUserAndPublishMessages() {
        when(userRepository.findByEmail(signUpDto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpDto.password())).thenReturn("encoded-password");

        userService.saveUser(signUpDto);

        verify(userRepository).save(any(UserLoginEntity.class));
        verify(userEntityRepository).save(any(UserEntity.class));
        verify(userProducer).publishMessageEmailSignUp(any(UserEntity.class));
        verify(userDataProducer).userData(any(UserEntity.class));
    }

    @Test
    void shouldThrowInvalidCredentialsWhenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@email.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
            () -> userService.loginUser(new UserLoginDto("missing@email.com", "123456")));
    }

    @Test
    void shouldLoginAndReturnTokenWhenCredentialsAreValid() {
        UserLoginEntity entity = new UserLoginEntity();
        entity.setIdUser(UUID.randomUUID());
        entity.setEmail("victor@email.com");
        entity.setPassword("encoded-password");

        Jwt jwt = new Jwt(
            "jwt-token",
            Instant.now(),
            Instant.now().plusSeconds(300),
            Map.of("alg", "none"),
            Map.of("sub", entity.getIdUser().toString())
        );

        when(userRepository.findByEmail("victor@email.com")).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("123456", "encoded-password")).thenReturn(true);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        var response = userService.loginUser(new UserLoginDto("victor@email.com", "123456"));

        assertEquals("jwt-token", response.token());
        assertEquals(3600L, response.expiresIn());
        verify(userProducer).publishMessageEmailLogin(any(UserLoginDto.class));
    }
}
