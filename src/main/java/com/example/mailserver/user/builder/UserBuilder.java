package com.example.mailserver.user.builder;

import com.example.mailserver.user.entity.User;
import com.example.mailserver.user.model.CreateUserRequest;
import org.springframework.stereotype.Component;

import static com.example.mailserver.config.constants.UserStatusConstants.USER_CREATED;

@Component
public class UserBuilder {

    public User build(CreateUserRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getEmail())
                .password(request.getPassword())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .status(USER_CREATED)
                .build();
    }
}
