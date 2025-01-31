package com.chilly.security_svc.service;

import com.chilly.security_svc.model.RefreshToken;
import com.chilly.security_svc.model.User;
import com.chilly.security_svc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chilly.common.dto.*;
import org.chilly.common.exception.*;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

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
            throw new EmptyDataException("to be registered user should have phone and email");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EntityExistsException("email already in use");
        }

        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new EntityExistsException("phone number already in use");
        }

        User user = userRepository.save(buildUserFromRequest(request));
        UserDto dto = convertToUserDto(user, request);

        try {
            sendUserDtoToMainService(dto);
            log.info("user (id={}) saved", dto.getId());
        } catch (Exception e) {
            userRepository.deleteById(user.getId());
            throw new CallFailedException("request to main service failed");
        }
    }

    public TokenResponse loginUser(LoginRequest request) {
        // TODO check if user exists
        User authenticatedUser = authenticateUser(request);
        return generateTokensForUser(authenticatedUser);
    }

    public TokenResponse refresh(RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getToken())
                .orElseThrow(() -> new UnauthorizedAccessException("no user for token"));

        if (refreshTokenService.isExpired(refreshToken)) {
            throw new UnauthorizedAccessException("refresh token is expired");
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
                .uri("http://main-svc/api/user/internal")
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

    public void changeUsername(Long userId, LoginInfoChangeRequest request) {
        if (request.getPhone() == null && request.getEmail() == null) {
            throw new EmptyDataException("email or phone need to be specified");
        }
        if (!checkUniqueEmail(request.getEmail())) {
            throw new EntityExistsException("email " + request.getEmail() + " already in use");
        }
        if (!checkUniquePhone(request.getPhone())) {
            throw new EntityExistsException("phone " + request.getPhone() + " already in use");
        }

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new NoSuchEntityException("no user with id = " + userId));

        checkAndChange(request.getEmail(), user.getEmail(), user::setEmail);
        checkAndChange(request.getPhone(), user.getPhoneNumber(), user::setPhoneNumber);

        try {
            changeInfoInMainService(convertToInternal(userId, request));
        }
        catch (Exception e) {
            throw new CallFailedException("request to main service failed");
        }
    }

    private void checkAndChange(String newValue, String oldValue, Consumer<String> setter) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
        }
    }

    private void changeInfoInMainService( LoginInfoChangeInternalRequest request) {
        webClient.put()
                .uri("http://main-svc/api/user/internal/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private LoginInfoChangeInternalRequest convertToInternal(Long userId, LoginInfoChangeRequest request) {
        return LoginInfoChangeInternalRequest.builder()
                .id(userId)
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
    }


    private boolean checkUniqueEmail(String email) {
        return email == null || userRepository.findByEmail(email).isEmpty();
    }

    private boolean checkUniquePhone(String phone) {
        return phone == null || userRepository.findByPhoneNumber(phone).isEmpty();
    }
}
