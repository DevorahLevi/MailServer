package com.example.mailserver.user.controller;

import com.example.mailserver.user.model.LoginRequest;
import com.example.mailserver.user.model.CreateUserRequest;
import com.example.mailserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/mail/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/userExists/{username}")
    public boolean userExists(@PathVariable String username) {
        return userService.userExists(username);
    }

    @PostMapping("/createNewUser")
    public String createNewUser(CreateUserRequest createUserRequest) {
        return userService.createNewUser(createUserRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
}
