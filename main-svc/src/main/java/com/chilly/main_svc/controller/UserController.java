package com.chilly.main_svc.controller;

import com.chilly.main_svc.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chilly.common.dto.ChangeInfoRequest;
import org.chilly.common.dto.LoginInfoChangeInternalRequest;
import org.chilly.common.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "User related API")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "retrieve information about logged user")
    @GetMapping("me")
    public UserDto getMe(@RequestHeader("UserId") Long userId) {
        return userService.getUserById(userId);
    }

    @Operation(summary = "change info about logged user")
    @PutMapping("me")
    @Transactional
    public void changeUserInfo(@RequestHeader("UserId") Long userId, @RequestBody ChangeInfoRequest newInfo) {
        userService.changeUser(userId, newInfo);
    }

    @Hidden
    @Transactional
    @PutMapping("/internal/login")
    public void changeLoginInfo(@RequestBody LoginInfoChangeInternalRequest request) {
        userService.changeLoginInfo(request);
    }

    @Hidden
    @PostMapping("/internal")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
    }

    @Hidden
    @GetMapping
    public List<UserDto> allUsers() {
        return userService.findAllUsers();
    }

}
