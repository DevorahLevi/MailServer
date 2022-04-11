package com.example.mailserver.user.controller;

import com.example.mailserver.user.model.ChangePasswordRequest;
import com.example.mailserver.user.model.CreateUserRequest;
import com.example.mailserver.user.model.LoginRequest;
import com.example.mailserver.user.orchestrator.UserOrchestrator;
import com.example.mailserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/mail/user")
public class UserController {

    private final UserService userService;
    private final UserOrchestrator userOrchestrator;

    @GetMapping("/userExists/{username}")
    public boolean userExists(@PathVariable String username) {
        return userService.userExists(username);
    }

    @PostMapping("/createNewUser")
    public ResponseEntity<String> createNewUser(CreateUserRequest createUserRequest) {
        return userOrchestrator.createNewUser(createUserRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return userOrchestrator.login(loginRequest);
    }

    @PostMapping("/changePassword")
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        userOrchestrator.changePassword(changePasswordRequest);
    }
}
