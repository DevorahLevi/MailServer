package com.example.mailserver.message.service;

import com.example.mailserver.config.properties.ExternalMailProperties;
import com.example.mailserver.message.builder.MessageBuilder;
import com.example.mailserver.message.converter.MessageToMessageDTOConverter;
import com.example.mailserver.message.entity.Message;
import com.example.mailserver.message.model.MessageDTO;
import com.example.mailserver.message.model.SaveMessageRequest;
import com.example.mailserver.message.repository.MessageRepository;
import com.example.mailserver.user.entity.User;
import com.example.mailserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserService userService;
    private final RestTemplate restTemplate;
    private final MessageBuilder messageBuilder;
    private final MessageRepository messageRepository;
    private final ExternalMailProperties externalMailProperties;
    private final MessageToMessageDTOConverter messageToMessageDTOConverter;

    public List<MessageDTO> checkInbox(UUID userId) {
        try {
            User user = userService.findById(userId);
            List<Message> messages = messageRepository.findAllByRecipientOrderByCreatedDateDesc(user.getUsername());
            return messages.stream().map(messageToMessageDTOConverter::convert).collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            log.info(String.format("Failed to find inbox for userID: %s", userId));
            throw e;
        }
    }

    public List<MessageDTO> checkOutbox(UUID userId) {
        try {
            User user = userService.findById(userId);
            List<Message> messages = messageRepository.findAllBySenderAndSentOrderByCreatedDateDesc(user.getUsername(), true);
            return messages.stream().map(messageToMessageDTOConverter::convert).collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            log.info(String.format("Failed to find outbox for userID: %s", userId));
            throw e;
        }
    }

    public List<MessageDTO> checkDrafts(UUID userId) {
        try {
            User user = userService.findById(userId);
            List<Message> messages = messageRepository.findAllBySenderAndSentOrderByCreatedDateDesc(user.getUsername(), false);
            return messages.stream().map(messageToMessageDTOConverter::convert).collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            log.info(String.format("Failed to find drafts for userID: %s", userId));
            throw e;
        }
    }

    public Message saveMessage(String messageId, SaveMessageRequest saveMessageRequest, boolean sent) {
        Message message = messageRepository.findById(UUID.fromString(messageId)).orElseThrow(EntityNotFoundException::new);

        message.setRecipient(saveMessageRequest.getRecipient());
        message.setMessageContent(saveMessageRequest.getMessageContent());
        message.setSent(sent);

        return messageRepository.save(message);
    }

    public String createMessage(SaveMessageRequest saveMessageRequest) {
        Message message = messageRepository.save(messageBuilder.build(saveMessageRequest, false));
        return message.getId().toString();
    }

    public void sendMessage(String messageId, SaveMessageRequest request) throws Exception {
        if (isEmpty(request.getRecipient())) {
            throw new Exception("Recipients cannot be null");
        }

        Message savedMessage = saveMessage(messageId, request, true);

        if (!userService.userExists(request.getRecipient())) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("API-KEY", externalMailProperties.getApiKey());
            HttpEntity<Message> httpEntity = new HttpEntity<>(savedMessage, headers);

            restTemplate.exchange(externalMailProperties.getIp(), HttpMethod.POST, httpEntity, Void.class);
        }
    }

    public void receiveMessage(SaveMessageRequest request) {
        if (!userService.userExists(request.getRecipient())) {
            throw new EntityNotFoundException(String.format("No user found to receive message for username: %s", request.getRecipient()));
        }

        messageRepository.save(messageBuilder.build(request, true));
    }
}