package com.example.mailserver.email.service;

import com.example.mailserver.config.properties.ExternalMailProperties;
import com.example.mailserver.email.builder.EmailBuilder;
import com.example.mailserver.email.converter.EmailToEmailDTOConverter;
import com.example.mailserver.email.entity.Email;
import com.example.mailserver.email.model.EmailDTO;
import com.example.mailserver.email.model.ReceiveEmailRequest;
import com.example.mailserver.email.model.SendEmailRequest;
import com.example.mailserver.email.repository.EmailRepository;
import com.example.mailserver.user.entity.User;
import com.example.mailserver.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.fail;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    UserService userService;

    @Mock
    EmailBuilder emailBuilder;

    @Mock
    RestTemplate restTemplate;

    @Mock
    EmailRepository emailRepository;

    @Mock
    EmailToEmailDTOConverter emailToEmailDTOConverter;

    @Mock
    ExternalMailProperties externalMailProperties;

    @InjectMocks
    EmailService subject;

    @Test
    public void checkInbox() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .username("username")
                .build();

        Email email = Email.builder().build();
        List<Email> emailList = singletonList(email);

        EmailDTO emailDTO = EmailDTO.builder().build();
        List<EmailDTO> expected = singletonList(emailDTO);

        when(userService.findById(any(UUID.class))).thenReturn(user);
        when(emailRepository.findAllByRecipientOrderByCreatedDateDesc(anyString())).thenReturn(emailList);
        when(emailToEmailDTOConverter.convert(any(Email.class))).thenReturn(emailDTO);

        List<EmailDTO> actual = subject.checkInbox(userId);

        verify(userService).findById(userId);
        verify(emailRepository).findAllByRecipientOrderByCreatedDateDesc("username");
        verify(emailToEmailDTOConverter).convert(email);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void checkInbox_logsAndThrowsException() {
        UUID userId = UUID.randomUUID();
        Exception exception = new EntityNotFoundException();
        when(userService.findById(any(UUID.class))).thenThrow(exception);

        try {
            subject.checkInbox(userId);
            fail("Expected exception not thrown");
        } catch (EntityNotFoundException e) {
            verify(userService).findById(userId);
            verifyNoInteractions(emailRepository);
            verifyNoInteractions(emailToEmailDTOConverter);
        }
    }

    @Test
    public void checkOutbox() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .username("username")
                .build();

        Email email = Email.builder().build();
        List<Email> emailList = singletonList(email);

        EmailDTO emailDTO = EmailDTO.builder().build();
        List<EmailDTO> expected = singletonList(emailDTO);

        when(userService.findById(any(UUID.class))).thenReturn(user);
        when(emailRepository.findAllBySenderOrderByCreatedDateDesc(anyString())).thenReturn(emailList);
        when(emailToEmailDTOConverter.convert(any(Email.class))).thenReturn(emailDTO);

        List<EmailDTO> actual = subject.checkOutbox(userId);

        verify(userService).findById(userId);
        verify(emailRepository).findAllBySenderOrderByCreatedDateDesc("username");
        verify(emailToEmailDTOConverter).convert(email);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void checkOutbox_logsAndThrowsException() {
        UUID userId = UUID.randomUUID();
        Exception exception = new EntityNotFoundException();
        when(userService.findById(any(UUID.class))).thenThrow(exception);

        try {
            subject.checkOutbox(userId);
            fail("Expected exception not thrown");
        } catch (EntityNotFoundException e) {
            verify(userService).findById(userId);
            verifyNoInteractions(emailRepository);
            verifyNoInteractions(emailToEmailDTOConverter);
        }
    }

    @Test
    public void sendEmail_recipientIsLocalUser() {
        UUID senderId = UUID.randomUUID();
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .sender(senderId)
                .recipient("recipient")
                .messageContent("messageContent")
                .build();

        User user = User.builder().username("username").build();
        Email email = Email.builder().build();

        when(userService.findById(any(UUID.class))).thenReturn(user);
        when(emailBuilder.build(anyString(), anyString(), anyString())).thenReturn(email);
        when(userService.userExists(anyString())).thenReturn(true);

        subject.sendEmail(sendEmailRequest);

        verify(userService).findById(senderId);
        verify(emailBuilder).build("username", "recipient", "messageContent");
        verify(userService).userExists("recipient");
        verify(emailRepository).save(email);
    }

    @Test
    public void sendEmail_recipientIsExternal() {
        UUID senderId = UUID.randomUUID();
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .sender(senderId)
                .recipient("recipient")
                .messageContent("messageContent")
                .build();

        User user = User.builder().username("username").build();
        Email email = Email.builder().build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("API-KEY", "apiKey");
        HttpEntity<Email> httpEntity = new HttpEntity<>(email, headers);

        when(userService.findById(any(UUID.class))).thenReturn(user);
        when(emailBuilder.build(anyString(), anyString(), anyString())).thenReturn(email);
        when(userService.userExists(anyString())).thenReturn(false);
        when(externalMailProperties.getIp()).thenReturn("ip");
        when(externalMailProperties.getApiKey()).thenReturn("apiKey");

        subject.sendEmail(sendEmailRequest);

        verify(userService).findById(senderId);
        verify(emailBuilder).build("username", "recipient", "messageContent");
        verify(userService).userExists("recipient");
        verify(externalMailProperties).getIp();
        verify(externalMailProperties).getApiKey();
        verify(restTemplate).exchange("ip", HttpMethod.POST, httpEntity, Void.class);
    }

    @Test
    public void receiveEmail() {
        ReceiveEmailRequest receiveEmailRequest = ReceiveEmailRequest.builder()
                .sender("sender")
                .recipient("recipient")
                .messageContent("messageContent")
                .build();

        Email email = Email.builder().build();

        when(userService.userExists(anyString())).thenReturn(true);
        when(emailBuilder.build(anyString(), anyString(), anyString())).thenReturn(email);

        subject.receiveEmail(receiveEmailRequest);

        verify(userService).userExists("recipient");
        verify(emailBuilder).build("sender", "recipient", "messageContent");
        verify(emailRepository).save(email);
    }

    @Test
    public void receiveEmail_recipientDoesNotExist_throwsAndLogsException() {
        ReceiveEmailRequest receiveEmailRequest = ReceiveEmailRequest.builder()
                .recipient("recipient")
                .build();

        when(userService.userExists(anyString())).thenReturn(false);

        try {
            subject.receiveEmail(receiveEmailRequest);
            fail("Expected exception not thrown");
        } catch (EntityNotFoundException e) {
            verify(userService).userExists("recipient");
            verifyNoInteractions(emailBuilder);
            verifyNoInteractions(emailRepository);
        }
    }
}