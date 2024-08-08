package com.chilly.main_svc.mapper;

import com.chilly.main_svc.dto.UserDto;
import com.chilly.main_svc.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoModelMapper {

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }

    public User toModel(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .build();
    }
}
