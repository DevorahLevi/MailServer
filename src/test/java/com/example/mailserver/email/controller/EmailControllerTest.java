package com.example.mailserver.email.controller;

import com.example.mailserver.email.service.EmailService;
import com.example.mailserver.email.model.EmailDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {

    @Mock
    EmailService emailService;

    @InjectMocks
    EmailController subject;

    @Test
    public void checkInbox() {
        UUID userId = UUID.randomUUID();

        EmailDTO emailDTO = EmailDTO.builder().build();
        List<EmailDTO> expected = singletonList(emailDTO);

        when(emailService.checkInbox(any(UUID.class))).thenReturn(expected);

        List<EmailDTO> actual = subject.checkInbox(userId);

        verify(emailService).checkInbox(userId);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void checkOutbox() {
        UUID userID = UUID.randomUUID();

        EmailDTO emailDTO = EmailDTO.builder().build();
        List<EmailDTO> expected = singletonList(emailDTO);

        when(emailService.checkOutbox(any(UUID.class))).thenReturn(expected);

        List<EmailDTO> actual = subject.checkOutbox(userID);

        verify(emailService).checkOutbox(userID);
        assertThat(actual).isEqualTo(expected);
    }
}