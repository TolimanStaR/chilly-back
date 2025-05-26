package com.chilly.main_svc.controller;

import com.chilly.main_svc.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chilly.common.dto.ChangeInfoRequest;
import org.chilly.common.dto.LoginInfoChangeInternalRequest;
import org.chilly.common.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto testUser;
    private ChangeInfoRequest changeInfoRequest;
    private LoginInfoChangeInternalRequest loginInfoChangeRequest;

    @BeforeEach
    void setUp() {
        testUser = UserDto.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .build();

        changeInfoRequest = new ChangeInfoRequest();
        changeInfoRequest.setFirstname("Jane");
        changeInfoRequest.setLastname("Smith");

        loginInfoChangeRequest = new LoginInfoChangeInternalRequest(
                1L, "new.email@example.com", "+9876543210"
        );
    }

    @Test
    void getMe_ShouldReturnUserInfo() throws Exception {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(get("/api/user/me")
                        .header("UserId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("John")))
                .andExpect(jsonPath("$.lastname", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.phoneNumber", is("+1234567890")));

        verify(userService).getUserById(1L);
    }

    @Test
    void changeUserInfo_ShouldCallServiceMethod() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/user/me")
                        .header("UserId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeInfoRequest)))
                .andExpect(status().isOk());

        verify(userService).changeUser(eq(1L), any(ChangeInfoRequest.class));
    }

    @Test
    void changeLoginInfo_ShouldCallServiceMethod() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/user/internal/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginInfoChangeRequest)))
                .andExpect(status().isOk());

        verify(userService).changeLoginInfo(any(LoginInfoChangeInternalRequest.class));
    }

    @Test
    void addUser_ShouldCallServiceMethodAndReturnCreatedStatus() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/user/internal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated());

        verify(userService).createUser(any(UserDto.class));
    }

    @Test
    void allUsers_ShouldReturnListOfUsers() throws Exception {
        // Arrange
        UserDto user2 = UserDto.builder()
                .id(2L)
                .firstname("Alice")
                .lastname("Johnson")
                .email("alice.j@example.com")
                .phoneNumber("+1987654321")
                .build();

        List<UserDto> users = List.of(testUser, user2);
        when(userService.findAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstname", is("John")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstname", is("Alice")));

        verify(userService).findAllUsers();
    }

    @Test
    void getMe_ShouldHandleMissingHeader() throws Exception {
        // Act & Assert - Missing UserId header
        mockMvc.perform(get("/api/user/me"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    void changeUserInfo_ShouldHandleInvalidRequestBody() throws Exception {
        // Act & Assert - Invalid JSON
        mockMvc.perform(put("/api/user/me")
                        .header("UserId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    void addUser_ShouldHandleInvalidUser() throws Exception {
        // Arrange - Creating an invalid user without required fields
        UserDto invalidUser = new UserDto();

        // Act & Assert
        mockMvc.perform(post("/api/user/internal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isCreated()); // As there's no validation in the controller

        verify(userService).createUser(any(UserDto.class));
    }
}
