package com.login_ms.login_ms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.login_ms.login_ms.dto.UserLoginDto;
import com.login_ms.login_ms.dto.UserLoginResponseDto;
import com.login_ms.login_ms.dto.UserSignUpDto;
import com.login_ms.login_ms.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints de cadastro e login")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar conta", description = "Cadastra um novo usuario no sistema")
    public ResponseEntity<Void> saveUser(@RequestBody @Valid UserSignUpDto dto) {
        userService.saveUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar", description = "Realiza login e devolve token JWT")
    public ResponseEntity<UserLoginResponseDto> loginUser(@RequestBody @Valid UserLoginDto dto) {
        return ResponseEntity.ok(userService.loginUser(dto));
    }
}
