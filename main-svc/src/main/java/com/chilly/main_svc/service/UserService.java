package com.chilly.main_svc.service;

import com.chilly.main_svc.mapper.UserDtoModelMapper;
import com.chilly.main_svc.model.User;
import com.chilly.main_svc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chilly.common.dto.ChangeInfoRequest;
import org.chilly.common.dto.LoginInfoChangeInternalRequest;
import org.chilly.common.dto.UserDto;
import org.chilly.common.exception.NoSuchEntityException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoModelMapper userMapper;

    public void createUser(UserDto userDto) {
        User user = userMapper.toModel(userDto);
        userRepository.save(user);

        log.info("new user saved: {}", userDto);
    }

    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUserById(Long userId) {
        return userMapper.toDto(findUserOrException(userId));
    }

    public void changeUser(Long userId, ChangeInfoRequest newInfo) {
        User user = findUserOrException(userId);
        checkAndChange(newInfo.getFirstname(), user.getFirstname(), user::setFirstname);
        checkAndChange(newInfo.getLastname(), user.getLastname(), user::setLastname);
    }

    public void changeLoginInfo(LoginInfoChangeInternalRequest request) {
        User user = findUserOrException(request.getId());
        checkAndChange(request.getEmail(), user.getEmail(), user::setEmail);
        checkAndChange(request.getPhone(), user.getPhoneNumber(), user::setPhoneNumber);
    }

    public User findUserOrException(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("No user with id =" + id));
    }

    private <T> void checkAndChange(T value, T oldValue, Consumer<T> setter) {
        if (value != null && oldValue != value) {
            setter.accept(value);
        }
    }
}
