package com.example.mailserver.user.orchestrator;

import com.example.mailserver.config.exception.InvalidCredentialsException;
import com.example.mailserver.config.exception.InvalidPasswordException;
import com.example.mailserver.user.builder.UserBuilder;
import com.example.mailserver.user.entity.User;
import com.example.mailserver.user.model.ChangePasswordRequest;
import com.example.mailserver.user.model.CreateUserRequest;
import com.example.mailserver.user.model.LoginRequest;
import com.example.mailserver.user.service.UserService;
import com.example.mailserver.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Component
@RequiredArgsConstructor
public class UserOrchestrator {

    private final UserService userService;
    private final UserBuilder userBuilder;
    private final ValidationService validationService;

    public ResponseEntity<String> createNewUser(CreateUserRequest createUserRequest) {
        try {
            if (!validationService.validPassword(createUserRequest.getPassword())) {
                throw new InvalidPasswordException();
            }

            User newUser = userService.save(userBuilder.build(createUserRequest));
            return ResponseEntity.status(OK).body(newUser.getId().toString());

        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(BAD_REQUEST).body("Invalid password, does not match password requirements.");
        }
    }

    public ResponseEntity<String> login(LoginRequest loginRequest) {
        try {
            User user = userService.findByUsername(loginRequest.getUserName());

            if (!validationService.passwordMatch(user, loginRequest.getPassword())) {
                throw new InvalidCredentialsException();
            }
            return ResponseEntity.status(OK).body(user.getId().toString());

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(UNAUTHORIZED).body("Credentials are not registered. Please create an account.");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(UNAUTHORIZED).body("Invalid credentials. Please try again.");
        }
    }

    public ResponseEntity<String> changePassword(ChangePasswordRequest changePasswordRequest) {
        try {
            User user = userService.findById(UUID.fromString(changePasswordRequest.getUserId()));

            if (!validationService.passwordMatch(user, changePasswordRequest.getOldPassword())) {
                throw new InvalidCredentialsException("Invalid credentials provided - cannot update password");
            }

            if (!validationService.validPassword(changePasswordRequest.getNewPassword())) {
                throw new InvalidPasswordException("New password does not meet requirements for password update");
            }

            userService.updatePassword(user.getId().toString(), changePasswordRequest.getNewPassword());
            return ResponseEntity.status(OK).body("Password updated successfully");

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST).body("Failed to find user associated with changePasswordRequest");
        } catch (Exception e) {
            return ResponseEntity.status(UNAUTHORIZED).body(e.getMessage());
        }
    }
}
