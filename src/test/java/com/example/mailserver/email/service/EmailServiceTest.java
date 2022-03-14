package com.example.mailserver.email.service;

import com.example.mailserver.email.converter.EmailToEmailDTOConverter;
import com.example.mailserver.email.entity.Email;
import com.example.mailserver.email.repository.EmailRepository;
import com.example.mailserver.email.model.EmailDTO;
import com.example.mailserver.user.entity.User;
import com.example.mailserver.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    EmailRepository emailRepository;

    @Mock
    EmailToEmailDTOConverter emailToEmailDTOConverter;

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

        when(userService.findUserById(any(UUID.class))).thenReturn(user);
        when(emailRepository.findAllByRecipientOrderByCreatedDateDesc(anyString())).thenReturn(emailList);
        when(emailToEmailDTOConverter.convert(any(Email.class))).thenReturn(emailDTO);

        List<EmailDTO> actual = subject.checkInbox(userId);

        verify(userService).findUserById(userId);
        verify(emailRepository).findAllByRecipientOrderByCreatedDateDesc("username");
        verify(emailToEmailDTOConverter).convert(email);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void checkInbox_logsAndThrowsException() {
        UUID userId = UUID.randomUUID();
        Exception exception = new EntityNotFoundException();
        when(userService.findUserById(any(UUID.class))).thenThrow(exception);

        try {
            subject.checkInbox(userId);
            fail("Expected exception not thrown");
        } catch (EntityNotFoundException e) {
            // slf4j logging
            verify(userService).findUserById(userId);
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

        when(userService.findUserById(any(UUID.class))).thenReturn(user);
        when(emailRepository.findAllBySenderOrderByCreatedDateDesc(anyString())).thenReturn(emailList);
        when(emailToEmailDTOConverter.convert(any(Email.class))).thenReturn(emailDTO);

        List<EmailDTO> actual = subject.checkOutbox(userId);

        verify(userService).findUserById(userId);
        verify(emailRepository).findAllBySenderOrderByCreatedDateDesc("username");
        verify(emailToEmailDTOConverter).convert(email);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void checkOutbox_logsAndThrowsException() {
        UUID userId = UUID.randomUUID();
        Exception exception = new EntityNotFoundException();
        when(userService.findUserById(any(UUID.class))).thenThrow(exception);

        try {
            subject.checkOutbox(userId);
            fail("Expected exception not thrown");
        } catch (EntityNotFoundException e) {
            // slf4j logging
            verify(userService).findUserById(userId);
            verifyNoInteractions(emailRepository);
            verifyNoInteractions(emailToEmailDTOConverter);
        }
    }
}