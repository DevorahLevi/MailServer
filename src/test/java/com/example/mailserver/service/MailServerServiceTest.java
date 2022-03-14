//package com.example.mailserver.service;
//
//import com.example.mailserver.config.ExternalMailConfiguration;
//import com.example.mailserver.config.FeatureSwitchReceiveExternalMailConfiguration;
//import com.example.mailserver.config.FeatureSwitchSendExternalMailConfiguration;
//import com.example.mailserver.oldFiles.MailServerService;
//import com.example.mailserver.oldFiles.ValidationService;
//import com.example.mailserver.oldFiles.Users;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.UUID;
//
//@ExtendWith(MockitoExtension.class)
//public class MailServerServiceTest {
//
//    @Mock
//    RestTemplate restTemplate;
//
//    @Mock
//    ValidationService validationService;
//
//    @Mock
//    ExternalMailConfiguration externalMailConfiguration;
//
//    @Mock
//    FeatureSwitchSendExternalMailConfiguration featureSwitchSendExternalMailConfiguration;
//
//    @Mock
//    FeatureSwitchReceiveExternalMailConfiguration featureSwitchReceiveExternalMailConfiguration;
//
//    @InjectMocks
//    private MailServerService subject;
//
////    @Test
////    public void inboxLogin_returnsUserId() {
////        UUID userId = UUID.randomUUID();
////        User user = User.builder().build();
////
////        LoginRequest loginRequest = LoginRequest.builder()
////                .userName("username")
////                .password("password")
////                .build();
////
////        ResponseEntity<String> expected = ResponseEntity
////                .status(HttpStatus.OK)
////                .body(userId.toString());
////
////        when(validationService.findUserByUsername(anyString())).thenReturn(userId);
////        when(validationService.findUserById(any(UUID.class))).thenReturn(user);
////        when(validationService.validatePassword(any(User.class), anyString())).thenReturn(true);
////
////        ResponseEntity<String> actual = subject.inboxLogin(loginRequest);
////
////        verify(validationService).findUserByUsername("username");
////        verify(validationService).findUserById(userId);
////        verify(validationService).validatePassword(user, "password");
////
////        assertThat(actual).isEqualTo(expected);
////    }
////
////    @Test
////    public void inboxLogin_credentialsAreNotRegistered() {
////        LoginRequest loginRequest = LoginRequest.builder()
////                .userName("username")
////                .password("password")
////                .build();
////
////        ResponseEntity<String> expected = ResponseEntity
////                .status(HttpStatus.UNAUTHORIZED)
////                .body("Credentials are not registered. Please create an account before logging in.");
////
////        when(validationService.findUserByUsername(anyString())).thenReturn(null);
////
////        ResponseEntity<String> actual = subject.inboxLogin(loginRequest);
////
////        verify(validationService).findUserByUsername("username");
////        verifyNoMoreInteractions(validationService);
////        assertThat(actual).isEqualTo(expected);
////    }
////
////    @Test
////    public void inboxLogin_credentialsAreInvalid() {
////        UUID userId = UUID.randomUUID();
////        User user = User.builder().build();
////
////        LoginRequest loginRequest = LoginRequest.builder()
////                .userName("username")
////                .password("password")
////                .build();
////
////        ResponseEntity<String> expected = ResponseEntity
////                .status(HttpStatus.UNAUTHORIZED)
////                .body("Invalid credentials. Please try again.");
////
////        when(validationService.findUserByUsername(anyString())).thenReturn(userId);
////        when(validationService.findUserById(any(UUID.class))).thenReturn(user);
////        when(validationService.validatePassword(any(User.class), anyString())).thenReturn(false);
////
////        ResponseEntity<String> actual = subject.inboxLogin(loginRequest);
////
////        verify(validationService).findUserByUsername("username");
////        verify(validationService).findUserById(userId);
////        verify(validationService).validatePassword(user, "password");
////
////        assertThat(actual).isEqualTo(expected);
////    }
//
//    // todo -- delete under here
//
//    @Test
//    public void login_sendUserInfoVariablesToLoginFunction_whenCalledByControllerLoginEndpoint()
//    {
////        UserInfo userInfo = new UserInfo("test", "password");
////        assertThat(userInfo.getUserName()).isEqualTo("test");
////        assertThat(userInfo.getPassword()).isEqualTo("password");
//
////        when(mailServerService.inboxLogin(userInfo)).thenReturn("Called second method.");
////        verify(mailServerService.inboxLogin(new UserInfo(eq("test"), eq("password"))));
//
//        Users users = (Users)ReflectionTestUtils.getField(subject, Users.class, "users");
//        UUID uuid = (UUID) users.userDatabase.keySet().toArray()[0];
//
////        HashMap<UUID, UserInfo> hashmap = new HashMap<>();
////        hashmap.put(uuid, new UserInfo("DevorahLevi", "password1"));
//
////        when(users.getUserDatabase()).thenReturn(hashmap);
////        when(mailServerServiceHelper.checkUserNameExists(any(String.class), any(HashMap.class))).thenReturn(uuid);
//
////        doReturn(hashmap).when(users).getUserDatabase();
//
////        ResponseEntity actual = (ResponseEntity) mailServerService.inboxLogin("DevorahLevi", "password1");
//
////        verify(mailServerServiceHelper).checkUserNameExists(eq("DevorahLevi"), any(HashMap.class));
//
////        assertThat(actual.getBody()).isEqualTo(uuid.toString());
////        assertThat(actual.getStatusCode()).isEqualTo(OK);
//    }
//
//    @Test
//    public void login_loginUser_whenUserInfoObjectWithCorrectLoginInformationIsPassed()
//    {
//        String username = "test";
//        String password = "password";
//
//
//    }
//
//}