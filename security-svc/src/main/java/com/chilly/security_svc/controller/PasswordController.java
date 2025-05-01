package com.chilly.security_svc.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.chilly.security_svc.service.PasswordService;
import org.chilly.common.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Passwords", description = "passwords related API")
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @Operation(summary = "change user's password")
    @PutMapping("password")
    @SecurityRequirement(name = "Bearer Authentication")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(
            @RequestHeader("UserId") Long userId,
            @RequestBody ChangePasswordRequest request
    ) {
        passwordService.changePassword(userId, request);
    }

    @Operation(summary = "send password recovery code to email")
    @PostMapping("email_code")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public void sendPasswordRecoveryCode(@RequestBody RecoveryCodeSendRequest request) {
        passwordService.sendRecoveryCode(request);
    }

    @Operation(summary = "check whether password recovery is allowed for code and email")
    @PostMapping("email_code/verification")
    public CodeVerificationResponse verifyCode(@RequestBody CodeVerificationRequest request) {
        return passwordService.verifyCode(request);
    }

    @Operation(summary = "check whether password recovery is allowed for code and email")
    @PutMapping("password/recovery")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public void recoverPassword(@RequestBody PasswordRecoveryRequest request) {
        passwordService.recoverPassword(request);
    }
}
