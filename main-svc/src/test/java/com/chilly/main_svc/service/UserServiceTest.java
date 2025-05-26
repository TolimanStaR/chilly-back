package com.chilly.main_svc.service;

import com.chilly.main_svc.mapper.UserDtoModelMapper;
import com.chilly.main_svc.model.User;
import com.chilly.main_svc.repository.UserRepository;
import org.chilly.common.dto.ChangeInfoRequest;
import org.chilly.common.dto.LoginInfoChangeInternalRequest;
import org.chilly.common.dto.UserDto;
import org.chilly.common.exception.NoSuchEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDtoModelMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .phoneNumber("+1234567890")
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .quizAnswers(new LinkedHashSet<>())
                .build();

        testUserDto = UserDto.builder()
                .id(1L)
                .phoneNumber("+1234567890")
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .build();
    }

    @Test
    void createUser_ShouldSaveUserToRepository() {
        // Arrange
        when(userMapper.toModel(any(UserDto.class))).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.createUser(testUserDto);

        // Assert
        verify(userMapper).toModel(testUserDto);
        verify(userRepository).save(testUser);
    }

    @Test
    void findAllUsers_ShouldReturnAllUsersAsDtos() {
        // Arrange
        List<User> userList = List.of(testUser);
        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.toDto(any(User.class))).thenReturn(testUserDto);

        // Act
        List<UserDto> result = userService.findAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUserDto, result.get(0));
        verify(userRepository).findAll();
        verify(userMapper).toDto(testUser);
    }

    @Test
    void getUserById_ShouldReturnUserDtoWhenFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.toDto(any(User.class))).thenReturn(testUserDto);

        // Act
        UserDto result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testUserDto, result);
        verify(userRepository).findById(1L);
        verify(userMapper).toDto(testUser);
    }

    @Test
    void getUserById_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchEntityException exception = assertThrows(
                NoSuchEntityException.class,
                () -> userService.getUserById(999L)
        );
        assertEquals("No user with id =999", exception.getMessage());
        verify(userRepository).findById(999L);
        verifyNoInteractions(userMapper);
    }

    @Test
    void changeUser_ShouldUpdateUserDetails() {
        // Arrange
        ChangeInfoRequest changeRequest = new ChangeInfoRequest();
        changeRequest.setFirstname("Jane");
        changeRequest.setLastname("Smith");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        userService.changeUser(1L, changeRequest);

        // Assert
        assertEquals("Jane", testUser.getFirstname());
        assertEquals("Smith", testUser.getLastname());
        verify(userRepository).findById(1L);
    }

    @Test
    void changeUser_ShouldNotUpdateNullValues() {
        // Arrange
        ChangeInfoRequest changeRequest = new ChangeInfoRequest();
        changeRequest.setFirstname("Jane");
        // lastname is null

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        userService.changeUser(1L, changeRequest);

        // Assert
        assertEquals("Jane", testUser.getFirstname());
        assertEquals("Doe", testUser.getLastname()); // Should remain unchanged
        verify(userRepository).findById(1L);
    }

    @Test
    void changeUser_ShouldNotUpdateIfValueIsSame() {
        // Arrange
        ChangeInfoRequest changeRequest = new ChangeInfoRequest();
        changeRequest.setFirstname("John"); // Same as current value
        changeRequest.setLastname("Smith");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        userService.changeUser(1L, changeRequest);

        // Assert
        assertEquals("John", testUser.getFirstname()); // Should remain unchanged
        assertEquals("Smith", testUser.getLastname());
        verify(userRepository).findById(1L);
    }

    @Test
    void changeLoginInfo_ShouldUpdateEmailAndPhone() {
        // Arrange
        LoginInfoChangeInternalRequest request = new LoginInfoChangeInternalRequest(
                1L, "new.email@example.com", "+9876543210"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        userService.changeLoginInfo(request);

        // Assert
        assertEquals("new.email@example.com", testUser.getEmail());
        assertEquals("+9876543210", testUser.getPhoneNumber());
        verify(userRepository).findById(1L);
    }

    @Test
    void changeLoginInfo_ShouldNotUpdateNullValues() {
        // Arrange
        LoginInfoChangeInternalRequest request = new LoginInfoChangeInternalRequest(
                1L, null, "+9876543210"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        userService.changeLoginInfo(request);

        // Assert
        assertEquals("john.doe@example.com", testUser.getEmail()); // Should remain unchanged
        assertEquals("+9876543210", testUser.getPhoneNumber());
        verify(userRepository).findById(1L);
    }

    @Test
    void findUserOrException_ShouldReturnUserWhenFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.findUserOrException(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userRepository).findById(1L);
    }

    @Test
    void findUserOrException_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchEntityException exception = assertThrows(
                NoSuchEntityException.class,
                () -> userService.findUserOrException(999L)
        );
        assertEquals("No user with id =999", exception.getMessage());
        verify(userRepository).findById(999L);
    }

    @Test
    void checkAndChange_ShouldApplyChangeWhenValueIsNotNullAndDifferent() {
        // Arrange
        String newValue = "New Value";
        String oldValue = "Old Value";
        Consumer<String> setter = mock();

        // Act
        userService.checkAndChange(newValue, oldValue, setter);

        // Assert
        verify(setter).accept(newValue);
    }

    @Test
    void checkAndChange_ShouldNotApplyChangeWhenValueIsNull() {
        // Arrange
        String oldValue = "Old Value";
        Consumer<String> setter = mock();

        // Act
        userService.checkAndChange(null, oldValue, setter);

        // Assert
        verifyNoInteractions(setter);
    }

    @Test
    void checkAndChange_ShouldNotApplyChangeWhenValueIsSameAsOldValue() {
        // Arrange
        String newValue = "Same Value";
        String oldValue = "Same Value";
        Consumer<String> setter = mock();

        // Act
        userService.checkAndChange(newValue, oldValue, setter);

        // Assert
        verifyNoInteractions(setter);
    }
}
