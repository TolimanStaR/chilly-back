package com.chilly.security_svc.service;

import com.chilly.security_svc.dto.*;
import com.chilly.security_svc.error.*;
import com.chilly.security_svc.model.RefreshToken;
import com.chilly.security_svc.model.User;
import com.chilly.security_svc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserFindService userFindService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final WebClient webClient;

    public void registerUser(RegisterRequest request) {
        if (request.getPhoneNumber() == null || request.getEmail() == null) {
            throw new NoUsernameProvidedException("to be registered user should have phone and email");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExitsException("email already in use");
        }

        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new UserAlreadyExitsException("phone number already in use");
        }

        User user = userRepository.save(buildUserFromRequest(request));
        UserDto dto = convertToUserDto(user, request);

        try {
            sendUserDtoToMainService(dto);
            log.info("user (id=" + dto.getId() + ") saved");
        } catch (Exception e) {
            userRepository.deleteById(user.getId());
            throw new UserNotSavedError("request to main service failed");
        }
    }

    public TokenResponse loginUser(LoginRequest request) {
        User authenticatedUser = authenticateUser(request);
        return generateTokensForUser(authenticatedUser);
    }

    public TokenResponse refresh(RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getToken())
                .orElseThrow(() -> new NoUserForRefreshTokenException("no user for token"));

        if (refreshTokenService.isExpired(refreshToken)) {
            throw new ExpiredRefreshTokenException("refresh token is expired");
        }
        User user = refreshToken.getUser();
        return generateTokensForUser(user);
    }

    private User buildUserFromRequest(RegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }

    private UserDto convertToUserDto(User user, RegisterRequest request) {
        return UserDto.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .build();
    }

    private void sendUserDtoToMainService(UserDto dto) {
        webClient.post()
                .uri("http://main-svc/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private User authenticateUser(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        return userFindService.loadByPhoneOrByEmail(request.getUsername());
    }


    private TokenResponse generateTokensForUser(User user) {
        final String accessToken = jwtService.generateToken(user, user.getId());
        final String refreshToken = refreshTokenService.generateRefreshToken(user);
        return new TokenResponse(accessToken, refreshToken);
    }
}
