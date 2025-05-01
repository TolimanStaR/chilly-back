package com.chilly.security_svc.service;

import com.chilly.security_svc.model.RefreshToken;
import com.chilly.security_svc.model.Role;
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

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
        checkEmailAndPhone(request.getEmail(), request.getPhoneNumber());

        User user = userRepository.save(buildUserFromRequest(mapToInternal(request)));
        UserDto dto = convertToUserDto(user, request);

        try {
            sendUserDtoToMainService(dto);
            log.info("user (id={}) saved", dto.getId());
        } catch (Exception e) {
            userRepository.deleteById(user.getId());
            throw new CallFailedException("request to main service failed");
        }
    }

    public Long registerUser(RegisterInternalRequest request) {
        log.info("calling register user internally");
        checkEmailAndPhone(request.getEmail(), request.getPhoneNumber());
        return userRepository.save(buildUserFromRequest(request))
                .getId();
    }

    public UsernameData getUsernameData(Long userId) {
        User user = getUserByIdOrThrow(userId);
        return UsernameData.builder()
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }

    public TokenResponse loginUser(LoginRequest request) {
        User authenticatedUser = authenticateUser(request);
        checkRequestedRole(authenticatedUser, request.getRole());
        return generateTokensForUser(authenticatedUser);
    }

    public void changeUsername(Long userId, LoginInfoChangeRequest request) {
        checkEmailAndPhone(request.getEmail(), request.getPhone());
        User user = getUserByIdOrThrow(userId);
        checkAndChange(request.getEmail(), user.getEmail(), user::setEmail);
        checkAndChange(request.getPhone(), user.getPhoneNumber(), user::setPhoneNumber);

        try {
            changeInfoInMainService(convertToInternal(userId, request));
        }
        catch (Exception e) {
            throw new CallFailedException("request to main service failed");
        }
    }

    public void changeRoles(String username, ChangeRolesRequest roles) {
        Set<Role> newRoles;
        try {
            newRoles = roles.getNewRoles().stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
        }
        catch (IllegalArgumentException e) {
            throw new WrongDataException("got incorrect roles");
        }
        User user = userFindService.loadByPhoneOrByEmail(username);
        if (roles.isIncludeCurrent()) {
            newRoles.addAll(user.getRole());
        }
        List<Role> mergedRoles = newRoles.stream().toList();
        user.setRole(mergedRoles);
        userRepository.save(user);
    }

    public List<String> getMyRoles(Long userId) {
        User user = getUserByIdOrThrow(userId);
        return user.getRole().stream()
                .map(Role::name)
                .toList();
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

    private void checkRequestedRole(User user, String roleFromRequest) {
        Role requestedRole = Role.USER;
        try {
            if (roleFromRequest != null) {
                requestedRole = Role.valueOf(roleFromRequest);
            }
        }
        catch (IllegalArgumentException e) {
            throw new WrongDataException("requested role doesn't exist");
        }
        if (!user.getRole().contains(requestedRole)) {
            throw new WrongDataException("User doesn't have requested role");
        }
    }

    private User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchEntityException("no user with id = " + userId));
    }

    private void checkEmailAndPhone(String email, String phone) {
        if (phone == null || email == null) {
            throw new EmptyDataException("to be registered user should have phone and email");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EntityExistsException("email already in use");
        }
        if (userRepository.findByPhoneNumber(phone).isPresent()) {
            throw new EntityExistsException("phone number already in use");
        }
    }

    private User buildUserFromRequest(RegisterInternalRequest request) {
        List<String> roles = request.getRoles();
        if (roles == null) {
            roles = List.of();
        }
        return User.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roles.stream().map(Role::valueOf).toList())
                .build();
    }

    private RegisterInternalRequest mapToInternal(RegisterRequest request) {
        return RegisterInternalRequest.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(request.getPassword())
                .roles(List.of(Role.USER.name()))
                .build();
    }

    private LoginInfoChangeInternalRequest convertToInternal(Long userId, LoginInfoChangeRequest request) {
        return LoginInfoChangeInternalRequest.builder()
                .id(userId)
                .phone(request.getPhone())
                .email(request.getEmail())
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

    private void changeInfoInMainService( LoginInfoChangeInternalRequest request) {
        webClient.put()
                .uri("http://main-svc/api/user/internal/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
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

    private void checkAndChange(String newValue, String oldValue, Consumer<String> setter) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
        }
    }
}
