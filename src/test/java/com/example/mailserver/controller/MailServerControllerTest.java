//package com.example.mailserver.controller;
//
//import com.example.mailserver.oldFiles.model.*;
//import com.example.mailserver.oldFiles.MailServerController;
//import com.example.mailserver.oldFiles.MailServerService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//import java.util.UUID;
//
//import static java.util.Collections.singletonList;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class MailServerControllerTest {
//
//    @Mock
//    private MailServerService mailServerService;
//
//    @InjectMocks
//    private MailServerController subject;
//
//    @Test
//    public void sendEmail() {
//        UIEmail uiEmail = UIEmail.builder().build();
//        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.OK).body("body");
//
//        when(mailServerService.sendEmail(any(UIEmail.class))).thenReturn(expected);
//
//        ResponseEntity<String> actual = subject.sendEmail(uiEmail);
//
//        verify(mailServerService).sendEmail(uiEmail);
//        assertThat(actual).isEqualTo(expected);
//    }
//
//    @Test
//    public void checkInbox() {
//        UUID userId = UUID.randomUUID();
//        DisplayInboxEmail displayInboxEmail = DisplayInboxEmail.builder().build();
//        List<DisplayInboxEmail> expected = singletonList(displayInboxEmail);
//
//        when(mailServerService.checkInbox(any(UUID.class))).thenReturn(expected);
//
//        List<DisplayInboxEmail> actual = subject.checkInbox(userId);
//
//        verify(mailServerService).checkInbox(userId);
//        assertThat(actual).isEqualTo(expected);
//    }
//
//    @Test
//    public void checkOutbox() {
//        UUID userId = UUID.randomUUID();
//        DisplayOutboxEmail displayOutboxEmail = DisplayOutboxEmail.builder().build();
//        List<DisplayOutboxEmail> expected = singletonList(displayOutboxEmail);
//
//        when(mailServerService.checkOutbox(any(UUID.class))).thenReturn(expected);
//
//        List<DisplayOutboxEmail> actual = subject.checkOutbox(userId);
//
//        verify(mailServerService).checkOutbox(userId);
//        assertThat(actual).isEqualTo(expected);
//    }
//
//    @Test
//    public void receiveExternalMail() {
//        ExternalEmail externalEmail = ExternalEmail.builder().build();
//        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.OK).body("body");
//
//        when(mailServerService.receiveExternalMail(any(ExternalEmail.class), anyString())).thenReturn(expected);
//
//        ResponseEntity<String> actual = subject.receiveExternalMail(externalEmail, "apiKey");
//
//        verify(mailServerService).receiveExternalMail(externalEmail, "apiKey");
//        assertThat(actual).isEqualTo(expected);
//    }
//}