package com.example.mailserver.message.service;

import com.example.mailserver.config.properties.ExternalMailProperties;
import com.example.mailserver.message.builder.MessageBuilder;
import com.example.mailserver.message.converter.MessageToDraftMessageDTOConverter;
import com.example.mailserver.message.converter.MessageToMessageDTOConverter;
import com.example.mailserver.message.entity.Message;
import com.example.mailserver.message.model.DraftMessageDTO;
import com.example.mailserver.message.model.MessageDTO;
import com.example.mailserver.message.model.SaveMessageRequest;
import com.example.mailserver.message.repository.MessageRepository;
import com.example.mailserver.user.entity.User;
import com.example.mailserver.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    UserService userService;

    @Mock
    MessageBuilder messageBuilder;

    @Mock
    RestTemplate restTemplate;

    @Mock
    MessageRepository messageRepository;

    @Mock
    MessageToMessageDTOConverter messageToMessageDTOConverter;

    @Mock
    MessageToDraftMessageDTOConverter messageToDraftMessageDTOConverter;

    @Mock
    ExternalMailProperties externalMailProperties;

    @Spy
    @InjectMocks
    MessageService subject;

    @Test
    public void checkInbox() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .username("username")
                .build();

        Message message = Message.builder().build();
        List<Message> messageList = singletonList(message);

        MessageDTO messageDTO = MessageDTO.builder().build();
        List<MessageDTO> expected = singletonList(messageDTO);

        when(userService.findById(any(UUID.class))).thenReturn(user);
        when(messageRepository.findAllByRecipientOrderByCreatedDateDesc(anyString())).thenReturn(messageList);
        when(messageToMessageDTOConverter.convert(any(Message.class))).thenReturn(messageDTO);

        List<MessageDTO> actual = subject.checkInbox(userId);

        verify(userService).findById(userId);
        verify(messageRepository).findAllByRecipientOrderByCreatedDateDesc("username");
        verify(messageToMessageDTOConverter).convert(message);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void checkInbox_logsAndThrowsException() {
        UUID userId = UUID.randomUUID();
        Exception exception = new EntityNotFoundException();

        when(userService.findById(any(UUID.class))).thenThrow(exception);


        assertThatThrownBy(() -> subject.checkInbox(userId))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userService).findById(userId);
        verifyNoInteractions(messageRepository);
        verifyNoInteractions(messageToMessageDTOConverter);
    }

    @Test
    public void checkOutbox() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .username("username")
                .build();

        Message message = Message.builder().build();
        List<Message> messageList = singletonList(message);

        MessageDTO messageDTO = MessageDTO.builder().build();
        List<MessageDTO> expected = singletonList(messageDTO);

        when(userService.findById(any(UUID.class))).thenReturn(user);
        when(messageRepository.findAllBySenderAndSentOrderByCreatedDateDesc(anyString(), anyBoolean())).thenReturn(messageList);
        when(messageToMessageDTOConverter.convert(any(Message.class))).thenReturn(messageDTO);

        List<MessageDTO> actual = subject.checkOutbox(userId);

        verify(userService).findById(userId);
        verify(messageRepository).findAllBySenderAndSentOrderByCreatedDateDesc("username", true);
        verify(messageToMessageDTOConverter).convert(message);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void checkOutbox_logsAndThrowsException() {
        UUID userId = UUID.randomUUID();
        Exception exception = new EntityNotFoundException();

        when(userService.findById(any(UUID.class))).thenThrow(exception);


        assertThatThrownBy(() -> subject.checkOutbox(userId))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userService).findById(userId);
        verifyNoInteractions(messageRepository);
        verifyNoInteractions(messageToMessageDTOConverter);
    }

    @Test
    public void checkDrafts() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .username("username")
                .build();

        Message message = Message.builder().build();
        List<Message> messageList = singletonList(message);

        DraftMessageDTO draftMessageDTO = DraftMessageDTO.builder().build();
        List<DraftMessageDTO> expected = singletonList(draftMessageDTO);

        when(userService.findById(any(UUID.class))).thenReturn(user);
        when(messageRepository.findAllBySenderAndSentOrderByCreatedDateDesc(anyString(), anyBoolean())).thenReturn(messageList);
        when(messageToDraftMessageDTOConverter.convert(any(Message.class))).thenReturn(draftMessageDTO);

        List<DraftMessageDTO> actual = subject.checkDrafts(userId);

        verify(userService).findById(userId);
        verify(messageRepository).findAllBySenderAndSentOrderByCreatedDateDesc("username", false);
        verify(messageToDraftMessageDTOConverter).convert(message);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void checkDrafts_logsAndThrowsException() {
        UUID userId = UUID.randomUUID();
        Exception exception = new EntityNotFoundException();

        when(userService.findById(any(UUID.class))).thenThrow(exception);


        assertThatThrownBy(() -> subject.checkDrafts(userId))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userService).findById(userId);
        verifyNoInteractions(messageRepository);
        verifyNoInteractions(messageToMessageDTOConverter);
    }

    @Test
    public void saveMessage() {
        UUID messageId = UUID.randomUUID();
        SaveMessageRequest saveMessageRequest = SaveMessageRequest.builder()
                .recipient("recipient")
                .messageContent("messageContent")
                .build();

        Message message = Message.builder().build();
        Message messageToBeSaved = Message.builder()
                .recipient("recipient")
                .messageContent("messageContent")
                .sent(false)
                .build();
        Message savedMessage = Message.builder().build();

        when(messageRepository.findById(any(UUID.class))).thenReturn(Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        Message actual = subject.saveMessage(messageId.toString(), saveMessageRequest, false);

        verify(messageRepository).findById(messageId);
        verify(messageRepository).save(messageToBeSaved);
        assertThat(actual).isEqualTo(savedMessage);
    }

    @Test
    public void createMessage() {
        SaveMessageRequest saveMessageRequest = SaveMessageRequest.builder().build();

        Message message = Message.builder().build();
        UUID messageId = UUID.randomUUID();
        Message savedMessage = Message.builder()
                .id(messageId)
                .build();

        when(messageBuilder.build(any(SaveMessageRequest.class), anyBoolean())).thenReturn(message);
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        String actual = subject.createMessage(saveMessageRequest);

        verify(messageBuilder).build(saveMessageRequest, false);
        verify(messageRepository).save(message);
        assertThat(actual).isEqualTo(messageId.toString());
    }

    @Test
    public void sendMessage_recipientIsInternal() throws Exception {
        SaveMessageRequest saveMessageRequest = SaveMessageRequest.builder()
                .sender("senderId")
                .recipient("recipient")
                .messageContent("messageContent")
                .build();

        Message message = Message.builder().build();

        doReturn(message).when(subject).saveMessage(anyString(), any(SaveMessageRequest.class), anyBoolean());
        when(userService.userExists(anyString())).thenReturn(true);

        subject.sendMessage("messageId", saveMessageRequest);

        verify(subject).saveMessage("messageId", saveMessageRequest, true);
        verify(userService).userExists("recipient");
        verifyNoInteractions(externalMailProperties);
        verifyNoInteractions(restTemplate);
    }

    @Test
    public void sendMessage_recipientIsExternal() throws Exception {
        SaveMessageRequest saveMessageRequest = SaveMessageRequest.builder()
                .sender("senderId")
                .recipient("recipient")
                .messageContent("messageContent")
                .build();

        Message message = Message.builder().build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("API-KEY", "apiKey");
        HttpEntity<Message> httpEntity = new HttpEntity<>(message, headers);

        doReturn(message).when(subject).saveMessage(anyString(), any(SaveMessageRequest.class), anyBoolean());
        when(userService.userExists(anyString())).thenReturn(false);
        when(externalMailProperties.getIp()).thenReturn("ip");
        when(externalMailProperties.getApiKey()).thenReturn("apiKey");

        subject.sendMessage("messageId", saveMessageRequest);

        verify(subject).saveMessage("messageId", saveMessageRequest, true);
        verify(userService).userExists("recipient");
        verify(externalMailProperties).getIp();
        verify(externalMailProperties).getApiKey();
        verify(restTemplate).exchange("ip", HttpMethod.POST, httpEntity, Void.class);
    }

    @Test
    public void sendMessage_recipientIsEmpty_throwsException() {
        SaveMessageRequest saveMessageRequest = SaveMessageRequest.builder().build();

        assertThatThrownBy(() -> subject.sendMessage("messageId", saveMessageRequest))
                .hasMessage("Recipients cannot be null");

        verifyNoInteractions(userService);
        verify(subject, never()).saveMessage(anyString(), any(SaveMessageRequest.class), anyBoolean());
        verifyNoInteractions(userService);
    }

    @Test
    public void receiveEmail() {
        SaveMessageRequest saveMessageRequest = SaveMessageRequest.builder()
                .sender("sender")
                .recipient("recipient")
                .messageContent("messageContent")
                .build();

        Message message = Message.builder().build();

        when(userService.userExists(anyString())).thenReturn(true);
        when(messageBuilder.build(any(SaveMessageRequest.class), anyBoolean())).thenReturn(message);

        subject.receiveMessage(saveMessageRequest);

        verify(userService).userExists("recipient");
        verify(messageBuilder).build(saveMessageRequest, true);
        verify(messageRepository).save(message);
    }

    @Test
    public void receiveEmail_recipientDoesNotExist_throwsAndLogsException() {
        SaveMessageRequest saveMessageRequest = SaveMessageRequest.builder()
                .recipient("recipient")
                .build();

        when(userService.userExists(anyString())).thenReturn(false);

        assertThatThrownBy(() -> subject.receiveMessage(saveMessageRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("No user found to receive message for username: recipient");

        verify(userService).userExists("recipient");
        verifyNoInteractions(messageBuilder);
        verifyNoInteractions(messageRepository);
    }
}