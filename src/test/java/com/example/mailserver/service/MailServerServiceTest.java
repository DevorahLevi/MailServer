package com.example.mailserver.service;

import com.example.mailserver.helper.MailServerServiceHelper;
import com.example.mailserver.model.UserInfo;
import com.example.mailserver.utils.Users;
import io.swagger.models.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class MailServerServiceTest
{
    @Mock
    private MailServerServiceHelper mailServerServiceHelper;

    @Mock
    private RestTemplate restTemplate;

    private Users users;

    @InjectMocks
    private MailServerService mailServerService;

    @Test
    public void login_sendUserInfoVariablesToLoginFunction_whenCalledByControllerLoginEndpoint()
    {
//        UserInfo userInfo = new UserInfo("test", "password");
//        assertThat(userInfo.getUserName()).isEqualTo("test");
//        assertThat(userInfo.getPassword()).isEqualTo("password");

//        when(mailServerService.inboxLogin(userInfo)).thenReturn("Called second method.");
//        verify(mailServerService.inboxLogin(new UserInfo(eq("test"), eq("password"))));

        Users users = (Users)ReflectionTestUtils.getField(mailServerService, Users.class, "users");
        UUID uuid = (UUID) users.userDatabase.keySet().toArray()[0];

//        HashMap<UUID, UserInfo> hashmap = new HashMap<>();
//        hashmap.put(uuid, new UserInfo("DevorahLevi", "password1"));

//        when(users.getUserDatabase()).thenReturn(hashmap);
        when(mailServerServiceHelper.checkUserNameExists(any(String.class), any(HashMap.class))).thenReturn(uuid);

//        doReturn(hashmap).when(users).getUserDatabase();

        ResponseEntity actual = (ResponseEntity) mailServerService.inboxLogin("DevorahLevi", "password1");

        verify(mailServerServiceHelper).checkUserNameExists(eq("DevorahLevi"), any(HashMap.class));

        assertThat(actual.getBody()).isEqualTo(uuid.toString());
        assertThat(actual.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void login_loginUser_whenUserInfoObjectWithCorrectLoginInformationIsPassed()
    {
        String username = "test";
        String password = "password";


    }

}