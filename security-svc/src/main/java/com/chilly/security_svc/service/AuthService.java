package com.chilly.security_svc.service;

import com.chilly.security_svc.dto.LoginRequest;
import com.chilly.security_svc.dto.RegisterRequest;
import com.chilly.security_svc.dto.TokenResponse;
import com.chilly.security_svc.dto.UserDto;
import com.chilly.security_svc.error.UserNotSavedError;
import com.chilly.security_svc.model.User;
import com.chilly.security_svc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WebClient webClient;

    public void registerUser(RegisterRequest request) {
        User user = userRepository.save(
                User.builder()
                        .email(request.getEmail())
                        .phoneNumber(request.getPhoneNumber())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build()
        );

        UserDto dto = UserDto.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .build();

        try {
            webClient.post()
                    .uri("http://localhost:8080/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }
        catch (Exception e) {
            log.warn("unable to save user because of exception {}, message: {}", e.getClass(), e.getMessage());
            throw new UserNotSavedError("request to main service failed");
        }


        log.info("new user registered: {}", dto);
    }

    public TokenResponse loginUser(LoginRequest request) {

        log.info("logging in user with credentials ({}, {})", request.getPhoneNumber(), request.getPassword());

        // TODO authenticate user

        return TokenResponse.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();
    }
}
