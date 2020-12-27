package com.example.mailserver.controller;

import com.example.mailserver.config.ExternalMailConfiguration;
import com.example.mailserver.config.FeatureSwitchReceiveExternalMailConfiguration;
import com.example.mailserver.model.GetUUID;
import com.example.mailserver.model.UserInfo;
import com.example.mailserver.service.MailServerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Key;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // or @RunWith(MockitoJUnitRunner.class) Test is written the same either way.
public class MailServerControllerTest
{
    @Mock
    private MailServerService mockMailServerService;

    @Mock
    private FeatureSwitchReceiveExternalMailConfiguration featureSwitchConfiguration;


    @InjectMocks
    private MailServerController mailServerController;

//     ** Notes: in Unit testing',
//            o use 'when' statements to do your setup
//            o use verify' statements to confirm that your code is actually being used
//            o use 'assert' statements to confirm that the results of your code are what you expect them to be.
//
//     ** Have to mock all of our dependencies (i.e. in this case, emailService, externalMailConfiguration and featureSwitchConfiguration, restTemplate).
//     ** Testing: have to test all possible outcomes of your code.
//          i.e. For login method:
//           1. positive situation: user gives email and it logs in
//           2. emailService.login throws a HttpClientErrorException
//           3. featureSwitch is false and a Service Unavailable is thrown




//    @Test
//    public void login_returnSuccessWhenEmailIsTurnedOn_andLoginIsSuccessful()
//    {
//        GetUUID key = getUUID.builder().
//                primaryKey(UUID.randomUUID())
//                .build();
//        when(featureSwitchConfiguration.isEmailUp().thenReturn(true));
//        when(mockEmailService.login(any(UserInfo.class))).thenReturn()
//    }

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