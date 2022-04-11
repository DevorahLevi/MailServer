package com.example.mailserver.user.orchestrator;

import com.example.mailserver.user.builder.UserBuilder;
import com.example.mailserver.user.entity.User;
import com.example.mailserver.user.model.ChangePasswordRequest;
import com.example.mailserver.user.model.CreateUserRequest;
import com.example.mailserver.user.model.LoginRequest;
import com.example.mailserver.user.service.UserService;
import com.example.mailserver.validation.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
public class UserOrchestratorTest {

    @Mock
    UserService userService;

    @Mock
    UserBuilder userBuilder;

    @Mock
    ValidationService validationService;

    @InjectMocks
    UserOrchestrator subject;

    @Test
    public void createNewUser_buildsAndSavesNewUser() {
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .password("password")
                .build();

        User user = User.builder().build();
        User savedUser = User.builder().id(UUID.randomUUID()).build();

        ResponseEntity<String> expected = ResponseEntity.status(OK).body(savedUser.getId().toString());

        when(validationService.validPassword(anyString())).thenReturn(true);
        when(userBuilder.build(any(CreateUserRequest.class))).thenReturn(user);
        when(userService.save(any(User.class))).thenReturn(savedUser);

        ResponseEntity<String> actual = subject.createNewUser(createUserRequest);

        verify(validationService).validPassword("password");
        verify(userBuilder).build(createUserRequest);
        verify(userService).save(user);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void createNewUser_throwsExceptionWhenPasswordIsInvalid() {
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .password("password")
                .build();

        ResponseEntity<String> expected = ResponseEntity.status(BAD_REQUEST).body("Invalid password, does not match password requirements.");

        when(validationService.validPassword(anyString())).thenReturn(false);

        ResponseEntity<String> actual = subject.createNewUser(createUserRequest);

        verify(validationService).validPassword("password");
        verifyNoInteractions(userBuilder);
        verifyNoInteractions(userService);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void login_validatesUsernameAndPassword() {
        LoginRequest loginRequest = LoginRequest.builder()
                .userName("username")
                .password("password")
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .password("password")
                .build();

        ResponseEntity<String> expected = ResponseEntity.status(OK).body(user.getId().toString());

        when(userService.findByUsername(anyString())).thenReturn(user);
        when(validationService.passwordMatch(any(User.class), anyString())).thenReturn(true);

        ResponseEntity<String> actual = subject.login(loginRequest);

        verify(userService).findByUsername("username");
        verify(validationService).passwordMatch(user, "password");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void login_throwsEntityNotFoundException_whenUserDoesNotExist() {
        LoginRequest loginRequest = LoginRequest.builder()
                .userName("username")
                .build();

        ResponseEntity<String> expected = ResponseEntity
                .status(UNAUTHORIZED)
                .body("Credentials are not registered. Please create an account.");

        when(userService.findByUsername(anyString())).thenThrow(new EntityNotFoundException());

        ResponseEntity<String> actual = subject.login(loginRequest);

        verify(userService).findByUsername("username");
        verifyNoInteractions(validationService);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void login_throwsInvalidCredentialsException_whenPasswordIsInvalid() {
        LoginRequest loginRequest = LoginRequest.builder()
                .userName("username")
                .password("wrongPassword")
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .password("password")
                .build();

        ResponseEntity<String> expected = ResponseEntity
                .status(UNAUTHORIZED)
                .body("Invalid credentials. Please try again.");

        when(userService.findByUsername(anyString())).thenReturn(user);
        when(validationService.passwordMatch(any(User.class), anyString())).thenReturn(false);

        ResponseEntity<String> actual = subject.login(loginRequest);

        verify(userService).findByUsername("username");
        verify(validationService).passwordMatch(user, "wrongPassword");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void changePassword() {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .userId(userId.toString())
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();

        User user = User.builder()
                .id(userId)
                .build();

        ResponseEntity<String> expected = ResponseEntity.status(OK).body("Password updated successfully");

        when(userService.findById(any(UUID.class))).thenReturn(user);
        when(validationService.passwordMatch(any(User.class), anyString())).thenReturn(true);
        when(validationService.validPassword(anyString())).thenReturn(true);

        ResponseEntity<String> actual = subject.changePassword(changePasswordRequest);

        verify(userService).findById(userId);
        verify(validationService).passwordMatch(user, "oldPassword");
        verify(validationService).validPassword("newPassword");
        verify(userService).updatePassword(userId.toString(), "newPassword");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void changePassword_throwsExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .userId(userId.toString())
                .build();

        EntityNotFoundException exception = new EntityNotFoundException();
        ResponseEntity<String> expected = ResponseEntity
                .status(BAD_REQUEST)
                .body("Failed to find user associated with changePasswordRequest");

        when(userService.findById(any(UUID.class))).thenThrow(exception);

        ResponseEntity<String> actual = subject.changePassword(changePasswordRequest);

        verify(userService).findById(userId);
        verifyNoInteractions(validationService);
        verifyNoMoreInteractions(userService);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void changePassword_throwsExceptionWhenOldPasswordIsIncorrect() {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .userId(userId.toString())
                .oldPassword("oldPassword")
                .build();

        User user = User.builder().build();
        ResponseEntity<String> expected = ResponseEntity
                .status(UNAUTHORIZED)
                .body("Invalid credentials provided - cannot update password");

        when(userService.findById(any(UUID.class))).thenReturn(user);
        when(validationService.passwordMatch(any(User.class), anyString())).thenReturn(false);

        ResponseEntity<String> actual = subject.changePassword(changePasswordRequest);

        verify(userService).findById(userId);
        verify(validationService).passwordMatch(user, "oldPassword");
        verifyNoMoreInteractions(validationService);
        verifyNoMoreInteractions(userService);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void changePassword_throwsExceptionWhenNewPasswordNotValid() {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .userId(userId.toString())
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();

        User user = User.builder().build();
        ResponseEntity<String> expected = ResponseEntity
                .status(UNAUTHORIZED)
                .body("New password does not meet requirements for password update");

        when(userService.findById(any(UUID.class))).thenReturn(user);
        when(validationService.passwordMatch(any(User.class), anyString())).thenReturn(true);
        when(validationService.validPassword(anyString())).thenReturn(false);

        ResponseEntity<String> actual = subject.changePassword(changePasswordRequest);

        verify(userService).findById(userId);
        verify(validationService).passwordMatch(user, "oldPassword");
        verify(validationService).validPassword("newPassword");
        verifyNoMoreInteractions(userService);

        assertThat(actual).isEqualTo(expected);
    }
}