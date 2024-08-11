package com.chilly.security_svc.controller;

import com.chilly.security_svc.dto.LoginRequest;
import com.chilly.security_svc.dto.RefreshRequest;
import com.chilly.security_svc.dto.RegisterRequest;
import com.chilly.security_svc.dto.TokenResponse;
import com.chilly.security_svc.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterRequest request) {
        authService.registerUser(request);
    }

    @PostMapping("login")
    @Transactional
    public TokenResponse login(@RequestBody LoginRequest request) {
        return authService.loginUser(request);
    }

    @PostMapping("refresh")
    @Transactional
    public TokenResponse refresh(@RequestBody RefreshRequest request) {
        return authService.refresh(request);
    }


}
