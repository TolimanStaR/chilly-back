package com.chilly.main_svc.controller;

import com.chilly.main_svc.dto.UserDto;
import com.chilly.main_svc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void addUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
    }

    @GetMapping
    List<UserDto> allUsers() {
        return userService.findAllUsers();
    }
}
