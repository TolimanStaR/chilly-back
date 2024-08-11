package com.chilly.main_svc.controller;

import com.chilly.main_svc.dto.UserDto;
import com.chilly.main_svc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void addUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
    }

    @GetMapping
    List<UserDto> allUsers(@RequestHeader("userId") Long userId) {
        log.info("user id={} requesting all user list", userId);
        return userService.findAllUsers();
    }
}
