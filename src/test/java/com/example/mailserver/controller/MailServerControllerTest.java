package com.example.mailserver.controller;

import com.example.mailserver.model.UserInfo;
import com.example.mailserver.service.MailServerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MailServerControllerTest
{
    @Mock
    private MailServerService mockMailServerService;

    @InjectMocks
    private MailServerController mailServerController;

    @Test // one standard for naming tests: functionName_whatItDoes_whenWhatIsPassedIntoIt()
    public void login_shouldCallServiceAndReturnItsResults()
    {
        when(mockMailServerService.inboxLogin(any(UserInfo.class))).thenReturn("Logged in.");
        Object actual = mailServerController.login(new UserInfo("DevorahLevi", "password1"));
        verify(mockMailServerService).inboxLogin(eq(new UserInfo("DevorahLevi", "password1")));
        String expected = "Logged in.";
        assertThat(actual).isEqualTo(expected);
    }
}