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

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final UserService userService;
    private final EmailBuilder emailBuilder;
    private final RestTemplate restTemplate;
    private final EmailRepository emailRepository;
    private final ExternalMailProperties externalMailProperties;
    private final EmailToEmailDTOConverter emailToEmailDTOConverter;

    public List<EmailDTO> checkInbox(UUID userId) {
        try {
            User user = userService.findById(userId);
            List<Email> emails = emailRepository.findAllByRecipientOrderByCreatedDateDesc(user.getUsername());
            return emails.stream().map(emailToEmailDTOConverter::convert).collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            log.info(String.format("Failed to find inbox for user associated with ID: %s", userId));
            throw e;
        }
    }

    public List<EmailDTO> checkOutbox(UUID userId) {
        try {
            User user = userService.findById(userId);
            List<Email> emails = emailRepository.findAllBySenderOrderByCreatedDateDesc(user.getUsername());
            return emails.stream().map(emailToEmailDTOConverter::convert).collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            log.info(String.format("Failed to find outbox for user associated with ID: %s", userId));
            throw e;
        }
    }

    public void sendEmail(SendEmailRequest request) {
        User sender = userService.findById(request.getSender());
        Email email = emailBuilder.build(sender.getUsername(), request.getRecipient(), request.getMessageContent());

        if (userService.userExists(request.getRecipient())) {
            emailRepository.save(email);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add("API-KEY", externalMailProperties.getApiKey());
            HttpEntity<Email> httpEntity = new HttpEntity<>(email, headers);

            restTemplate.exchange(externalMailProperties.getIp(), HttpMethod.POST, httpEntity, Void.class);
        }
    }

    public void receiveEmail(ReceiveEmailRequest request) {
        if (!userService.userExists(request.getRecipient())) {
            throw new EntityNotFoundException(String.format("No user found to receive email for username: %s", request.getRecipient()));
        }

        emailRepository.save(emailBuilder.build(request.getSender(), request.getRecipient(), request.getMessageContent()));
    }

    // TODO -- add Integration Tests for all endpoints
}
