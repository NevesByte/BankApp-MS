package com.login_ms.login_ms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.login_ms.login_ms.dto.UserLoginDto;
import com.login_ms.login_ms.dto.UserLoginResponseDto;
import com.login_ms.login_ms.dto.UserSignUpDto;
import com.login_ms.login_ms.entity.UserEntity;
import com.login_ms.login_ms.services.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> saveUser(@RequestBody @Valid UserSignUpDto dto){
        return userService.saveUser(dto);
    }

    @PostMapping("/login")
    public  ResponseEntity<UserLoginResponseDto> loginUser(@RequestBody @Valid UserLoginDto dto){
        return userService.loginUser(dto);
    }
}
