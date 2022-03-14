package com.example.mailserver.user.service;

import com.example.mailserver.user.model.LoginRequest;
import com.example.mailserver.user.builder.UserBuilder;
import com.example.mailserver.user.entity.User;
import com.example.mailserver.user.model.CreateUserRequest;
import com.example.mailserver.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserBuilder userBuilder;

    @Mock
    UserRepository userRepository;

    @Spy
    @InjectMocks
    UserService subject;

    @Test
    public void userExists_returnsTrueWhenExistingUser() {
        User user = User.builder().build();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        boolean actual = subject.userExists("username");
        assertThat(actual).isTrue();

        verify(userRepository).findByUsername("username");
    }

    @Test
    public void userExists_returnsFalseWhenNoExistingUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        boolean actual = subject.userExists("username");
        assertThat(actual).isFalse();

        verify(userRepository).findByUsername("username");
    }

    @Test
    public void findUserById() {
        UUID userId = UUID.randomUUID();
        User expectedUser = User.builder().build();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(expectedUser));

        User actual = subject.findUserById(userId);

        verify(userRepository).findById(userId);
        assertThat(actual).isEqualTo(expectedUser);
    }

    @Test
    public void createNewUser_buildsAndSavesNewUser() {
        CreateUserRequest createUserRequest = CreateUserRequest.builder().build();
        User user = User.builder().build();
        User savedUser = User.builder().id(UUID.randomUUID()).build();

        when(userBuilder.build(any(CreateUserRequest.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        String actual = subject.createNewUser(createUserRequest);
        String expected = savedUser.getId().toString();
        assertThat(actual).isEqualTo(expected);

        verify(userBuilder).build(createUserRequest);
        verify(userRepository).save(user);
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

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        doReturn(true).when(subject).validPassword(any(User.class), anyString());

        ResponseEntity<String> actual = subject.login(loginRequest);
        assertThat(actual).isEqualTo(expected);

        verify(userRepository).findByUsername("username");
        verify(subject).validPassword(user, "password");
    }

    @Test
    public void login_throwsEntityNotFoundException_whenUserDoesNotExist() {
        LoginRequest loginRequest = LoginRequest.builder()
                .userName("username")
                .build();

        ResponseEntity<String> expected = ResponseEntity
                .status(UNAUTHORIZED)
                .body("Credentials are not registered. Please create an account.");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        ResponseEntity<String> actual = subject.login(loginRequest);
        assertThat(actual).isEqualTo(expected);

        verify(userRepository).findByUsername("username");
        verify(subject, never()).validPassword(any(User.class), anyString());
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

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        doReturn(false).when(subject).validPassword(any(User.class), anyString());

        ResponseEntity<String> actual = subject.login(loginRequest);
        assertThat(actual).isEqualTo(expected);

        verify(userRepository).findByUsername("username");
        verify(subject).validPassword(user, "wrongPassword");
    }

    @Test
    public void validPassword_returnsTrue_whenPasswordMatches() {
        User user = User.builder()
                .password("password")
                .build();

        boolean actual = subject.validPassword(user, "password");
        assertThat(actual).isTrue();
    }

    @Test
    public void validPassword_returnsFalse_whenPasswordDoesNotMatch() {
        User user = User.builder()
                .password("password")
                .build();

        boolean actual = subject.validPassword(user, "wrongPassword");
        assertThat(actual).isFalse();
    }
}