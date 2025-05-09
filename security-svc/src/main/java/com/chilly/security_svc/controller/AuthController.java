package com.chilly.security_svc.controller;

import com.chilly.security_svc.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chilly.common.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Auth", description = "authentication and authorization API")
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "register new user")
    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterRequest request) {
        authService.registerUser(request);
    }

    @Operation(
            summary = "log in existing user",
            description = "generates token pair for user by username that can be either phone or email and password"
    )
    @PostMapping("login")
    @Transactional
    public TokenResponse login(@RequestBody LoginRequest request) {
        return authService.loginUser(request);
    }

    @Operation(summary = "get new token pair using refresh token")
    @PostMapping("refresh")
    @Transactional
    public TokenResponse refresh(@RequestBody RefreshRequest request) {
        return authService.refresh(request);
    }

    @Operation(summary = "change either email or phone number of logged-in user")
    @PutMapping("username")
    @SecurityRequirement(name = "Bearer Authentication")
    @Transactional
    public void changeLogin(
            @RequestHeader("UserId") Long userId,
            @RequestBody LoginInfoChangeRequest request
    ) {
        authService.changeUsername(userId, request);
    }

    @PutMapping("roles")
    @Transactional
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "changes roles of given user, only admin access")
    public void changeRole(
            @RequestParam("username") String username,
            @RequestBody ChangeRolesRequest roles
    ) {
        authService.changeRoles(username, roles);
    }

    @GetMapping("roles")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "get the roles available to the user")
    public List<String> changeRole(
            @RequestHeader("UserId") Long userId
    ) {
        return authService.getMyRoles(userId);
    }

    /* Internal */

    @Operation(summary = "register new user internal, returns id of registered user", hidden = true)
    @PostMapping("register/internal")
    public Long registerInternal(@RequestBody RegisterInternalRequest request) {
        return authService.registerUser(request);
    }

    @Operation(summary = "get username data by user id", hidden = true)
    @GetMapping("me/{userId}/internal")
    public UsernameData getUsernameData(@PathVariable("userId") Long userId) {
        return authService.getUsernameData(userId);
    }
}
