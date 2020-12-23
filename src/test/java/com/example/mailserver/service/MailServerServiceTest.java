package com.example.mailserver.service;

import com.example.mailserver.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MailServerServiceTest
{
    @InjectMocks
    private MailServerService mailServerService;

    @Test
    public void login_sendUserInfoVariablesToLoginFunction_whenCalledByControllerLoginEndpoint()
    {
        UserInfo userInfo = new UserInfo("test", "password");
        assertThat(userInfo.getUserName()).isEqualTo("test");
        assertThat(userInfo.getPassword()).isEqualTo("password");

        when(mailServerService.inboxLogin(userInfo)).thenReturn("Called second method.");
        verify(mailServerService.inboxLogin(new UserInfo(eq("test"), eq("password"))));
    }

    @Test
    public void login_loginUser_whenUserInfoObjectWithCorrectLoginInformationIsPassed()
    {
        String username = "test";
        String password = "password";

        
    }

}