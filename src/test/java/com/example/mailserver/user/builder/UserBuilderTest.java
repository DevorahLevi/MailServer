package com.example.mailserver.user.builder;

import com.example.mailserver.user.entity.User;
import com.example.mailserver.user.model.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.mailserver.constants.UserStatusConstants.USER_CREATED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserBuilderTest {

    @InjectMocks
    UserBuilder subject;

    @Test
    public void build() {
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .password("password")
                .phoneNumber("phoneNumber")
                .address("address")
                .city("city")
                .state("state")
                .zipCode("zipCode")
                .build();

        User expected = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .username("email")
                .password("password")
                .email("email")
                .phoneNumber("phoneNumber")
                .address("address")
                .city("city")
                .state("state")
                .zipCode("zipCode")
                .status(USER_CREATED)
                .build();

        User actual = subject.build(createUserRequest);

        assertThat(actual).isEqualTo(expected);
    }
}