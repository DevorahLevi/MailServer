package com.example.mailserver.user.service;

import com.example.mailserver.exception.InvalidCredentialsException;
import com.example.mailserver.user.model.LoginRequest;
import com.example.mailserver.user.builder.UserBuilder;
import com.example.mailserver.user.entity.User;
import com.example.mailserver.user.model.CreateUserRequest;
import com.example.mailserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserBuilder userBuilder;
    private final UserRepository userRepository;

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User findUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
    }

    public String createNewUser(CreateUserRequest createUserRequest) {
        User newUser = userRepository.save(userBuilder.build(createUserRequest));
        return newUser.getId().toString();
    }

    public ResponseEntity<String> login(LoginRequest loginRequest) {
        try {
            User user = userRepository.findByUsername(loginRequest.getUserName()).orElseThrow(EntityNotFoundException::new);

            if (!validPassword(user, loginRequest.getPassword())) {
                throw new InvalidCredentialsException();
            }
            return ResponseEntity.status(OK).body(user.getId().toString());

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(UNAUTHORIZED).body("Credentials are not registered. Please create an account.");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(UNAUTHORIZED).body("Invalid credentials. Please try again.");
        }
    }

    protected boolean validPassword(User user, String password) {
        return password.equals(user.getPassword());
    }
}
