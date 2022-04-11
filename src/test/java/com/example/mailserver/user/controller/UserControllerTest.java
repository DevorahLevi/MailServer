package com.example.mailserver.user.controller;

import com.example.mailserver.user.model.ChangePasswordRequest;
import com.example.mailserver.user.model.LoginRequest;
import com.example.mailserver.user.model.CreateUserRequest;
import com.example.mailserver.user.orchestrator.UserOrchestrator;
import com.example.mailserver.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UserService userService;

    @Mock
    UserOrchestrator userOrchestrator;

    @InjectMocks
    UserController subject;

    @Test
    public void userExists_callsUserService() {
        when(userService.userExists(anyString())).thenReturn(true);

        boolean actual = subject.userExists("username");
        assertThat(actual).isTrue();

        verify(userService).userExists("username");
    }

    @Test
    public void createNewUser_callsUserService() {
        CreateUserRequest createUserRequest = CreateUserRequest.builder().build();
        ResponseEntity<String> expectedResponseEntity = ResponseEntity.status(HttpStatus.OK).body("body");

        when(userOrchestrator.createNewUser(any(CreateUserRequest.class))).thenReturn(expectedResponseEntity);

        ResponseEntity<String> actual = subject.createNewUser(createUserRequest);
        assertThat(actual).isEqualTo(expectedResponseEntity);

        verify(userOrchestrator).createNewUser(createUserRequest);
    }

    @Test
    public void login_callsUserService() {
        LoginRequest loginRequest = LoginRequest.builder().build();
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.OK).body("body");

        when(userOrchestrator.login(any(LoginRequest.class))).thenReturn(expected);

        ResponseEntity<String> actual = subject.login(loginRequest);
        assertThat(actual).isEqualTo(expected);

        verify(userOrchestrator).login(loginRequest);
    }

    @Test
    public void changePassword() {
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder().build();

        subject.changePassword(changePasswordRequest);

        verify(userOrchestrator).changePassword(changePasswordRequest);
    }
}