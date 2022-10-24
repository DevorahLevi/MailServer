package com.example.mailserver.email.service;

import com.example.mailserver.config.properties.ExternalMailProperties;
import com.example.mailserver.email.builder.EmailBuilder;
import com.example.mailserver.email.converter.EmailToEmailDTOConverter;
import com.example.mailserver.email.entity.Email;
import com.example.mailserver.email.model.EmailDTO;
import com.example.mailserver.email.model.ReceiveEmailRequest;
import com.example.mailserver.email.model.SaveDraftRequest;
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

import static org.springframework.util.ObjectUtils.isEmpty;

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
            List<Email> emails = emailRepository.findAllByRecipientAndDraftOrderByCreatedDateDesc(user.getUsername(), false);
            return emails.stream().map(emailToEmailDTOConverter::convert).collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            log.info(String.format("Failed to find inbox for user associated with ID: %s", userId));
            throw e;
        }
    }

    public List<EmailDTO> checkOutbox(UUID userId) {
        try {
            User user = userService.findById(userId);
            List<Email> emails = emailRepository.findAllBySenderAndDraftOrderByCreatedDateDesc(user.getUsername(), false);
            return emails.stream().map(emailToEmailDTOConverter::convert).collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            log.info(String.format("Failed to find outbox for user associated with ID: %s", userId));
            throw e;
        }
    }

    // todo -- test
    public void saveDraft(SaveDraftRequest saveDraftRequest) {
        User sender = userService.findById(saveDraftRequest.getSender());

        if (isEmpty(saveDraftRequest.getDraftId())) {
            // save new draft, return the new draft's ID
        } else {

        }

        // find user, get ID for sender
        // check if already a draft started - maybe we need sep methods for save and update
        // will need to take in all other optional fields, we will need a builder with null checks
        // save draft
    }

    public void sendEmail(SaveDraftRequest request) {
//        User sender = userService.findById(request.getSender());
        // todo -- this builder will no longer be used here
//        Email email = emailBuilder.build(sender.getUsername(), request.getRecipient(), request.getMessageContent());
//
//        if (userService.userExists(request.getRecipient())) {
//            emailRepository.save(email);
//        } else {
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("API-KEY", externalMailProperties.getApiKey());
//            HttpEntity<Email> httpEntity = new HttpEntity<>(email, headers);
//
//            restTemplate.exchange(externalMailProperties.getIp(), HttpMethod.POST, httpEntity, Void.class);
//        }
    }

    public void receiveEmail(ReceiveEmailRequest request) {
        if (!userService.userExists(request.getRecipient())) {
            throw new EntityNotFoundException(String.format("No user found to receive email for username: %s", request.getRecipient()));
        }

        emailRepository.save(emailBuilder.build(request.getSender(), request.getRecipient(), request.getMessageContent(), false));
    }
}
