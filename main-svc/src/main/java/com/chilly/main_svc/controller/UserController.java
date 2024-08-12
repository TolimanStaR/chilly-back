package com.chilly.main_svc.controller;

import com.chilly.main_svc.dto.UserDto;
import com.chilly.main_svc.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "User related API")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @Hidden
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void addUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
    }

    @Operation(summary = "list all available users")
    @GetMapping
    List<UserDto> allUsers() {
        return userService.findAllUsers();
    }
}
