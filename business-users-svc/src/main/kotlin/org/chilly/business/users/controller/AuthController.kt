package org.chilly.business.users.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.chilly.business.users.service.AuthService
import org.chilly.common.dto.BusinessUserDto
import org.chilly.common.dto.RegisterBusinessUserRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Suppress("UNUSED")
@Tag(name = "BusinessUser", description = "business users API")
@RestController
@RequestMapping("/api/business_users")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "register new business user and associated company")
    fun register(
        @RequestBody request: RegisterBusinessUserRequest
    ) {
        authService.register(request)
    }

    @GetMapping("me")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "get data of user")
    fun getMe(
        @RequestHeader("UserId") userId: Long
    ): BusinessUserDto {
        return authService.getMe(userId)
    }
}