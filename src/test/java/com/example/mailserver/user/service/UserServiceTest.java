package com.example.mailserver.user.service;

import com.example.mailserver.user.builder.UserBuilder;
import com.example.mailserver.user.entity.User;
import com.example.mailserver.user.repository.UserRepository;
import com.example.mailserver.validation.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserBuilder userBuilder;

    @Mock
    UserRepository userRepository;

    @Mock
    ValidationService validationService;

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

        verify(userRepository).findByUsername("username");
        assertThat(actual).isFalse();
    }

    @Test
    public void findById() {
        UUID userId = UUID.randomUUID();
        User expectedUser = User.builder().build();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(expectedUser));

        User actual = subject.findById(userId);

        verify(userRepository).findById(userId);
        assertThat(actual).isEqualTo(expectedUser);
    }

    @Test
    public void findByUsername() {
        User expectedUser = User.builder().build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(expectedUser));

        User actual = subject.findByUsername("username");

        verify(userRepository).findByUsername("username");
        assertThat(actual).isEqualTo(expectedUser);
    }

    @Test
    public void save() {
        User userToBeSaved = User.builder().build();
        User expectedUser = User.builder().build();

        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        subject.save(userToBeSaved);

        verify(userRepository).save(userToBeSaved);
    }

    @Test
    public void updatePassword() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().build();
        User updatedUser = User.builder()
                .password("password")
                .build();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        subject.updatePassword(userId.toString(), "password");

        verify(userRepository).findById(userId);
        verify(userRepository).save(updatedUser);
    }

    @Test
    public void updatePassword_throwsEntityNotFoundException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        try {
            subject.updatePassword(userId.toString(), "password");
            fail("Expected exception not thrown");
        } catch (EntityNotFoundException e) {
            verify(userRepository).findById(userId);
            verifyNoMoreInteractions(userRepository);
        }
    }
}