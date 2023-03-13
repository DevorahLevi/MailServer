package com.example.mailserver.message.controller;

import com.example.mailserver.config.exception.InvalidApiKeyException;
import com.example.mailserver.message.model.DraftMessageDTO;
import com.example.mailserver.message.model.MessageDTO;
import com.example.mailserver.message.model.SaveMessageRequest;
import com.example.mailserver.message.service.MessageService;
import com.example.mailserver.validation.ValidationService;
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
public class MessageControllerTest {

    @Mock
    MessageService messageService;

    @Mock
    ValidationService validationService;

    @InjectMocks
    MessageController subject;

    @Test
    public void checkInbox() {
        UUID userId = UUID.randomUUID();

        MessageDTO messageDTO = MessageDTO.builder().build();
        List<MessageDTO> expected = singletonList(messageDTO);

        when(messageService.checkInbox(any(UUID.class))).thenReturn(expected);

        List<MessageDTO> actual = subject.checkInbox(userId);

        verify(messageService).checkInbox(userId);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void checkOutbox() {
        UUID userID = UUID.randomUUID();

        MessageDTO messageDTO = MessageDTO.builder().build();
        List<MessageDTO> expected = singletonList(messageDTO);

        when(messageService.checkOutbox(any(UUID.class))).thenReturn(expected);

        List<MessageDTO> actual = subject.checkOutbox(userID);

        verify(messageService).checkOutbox(userID);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void checkDrafts() {
        UUID userID = UUID.randomUUID();

        DraftMessageDTO draftMessageDTO = DraftMessageDTO.builder().build();
        List<DraftMessageDTO> expected = singletonList(draftMessageDTO);

        when(messageService.checkDrafts(any(UUID.class))).thenReturn(expected);

        List<DraftMessageDTO> actual = subject.checkDrafts(userID);

        verify(messageService).checkDrafts(userID);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void createDraft() {
        SaveMessageRequest saveMessageRequest = SaveMessageRequest.builder().build();

        subject.saveMessage(saveMessageRequest);

        verify(messageService).createMessage(saveMessageRequest);
    }

    @Test
    public void saveMessage() {
        SaveMessageRequest saveMessageRequest = SaveMessageRequest.builder().build();

        subject.saveMessage("messageId", saveMessageRequest);

        verify(messageService).saveMessage("messageId", saveMessageRequest, false);
    }

    @Test
    public void sendMessage() throws Exception {
        SaveMessageRequest saveMessageRequest = SaveMessageRequest.builder().build();

        subject.sendMessage("messageId", saveMessageRequest);

        verify(messageService).sendMessage("messageId", saveMessageRequest);
    }

    @Test
    public void receiveMessage() throws InvalidApiKeyException {
        SaveMessageRequest saveMessageRequest = SaveMessageRequest.builder().build();

        subject.receiveMessage(saveMessageRequest, "apiKey");

        verify(validationService).validateApiKey("apiKey");
        verify(messageService).receiveMessage(saveMessageRequest);
    }
}