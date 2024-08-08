package com.chilly.main_svc.service;

import com.chilly.main_svc.dto.UserDto;
import com.chilly.main_svc.mapper.UserDtoModelMapper;
import com.chilly.main_svc.model.User;
import com.chilly.main_svc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoModelMapper userMapper;

    public void createUser(UserDto userDto) {
        User user = userMapper.toModel(userDto);
        userRepository.save(user);
    }

    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }
}
