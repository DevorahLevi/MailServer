package com.example.mailserver.email.service;

import com.example.mailserver.email.converter.EmailToEmailDTOConverter;
import com.example.mailserver.email.entity.Email;
import com.example.mailserver.email.model.ReceiveEmailRequest;
import com.example.mailserver.email.model.SendEmailRequest;
import com.example.mailserver.email.repository.EmailRepository;
import com.example.mailserver.email.model.EmailDTO;
import com.example.mailserver.user.entity.User;
import com.example.mailserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final UserService userService;
    private final EmailRepository emailRepository;
    private final EmailToEmailDTOConverter emailToEmailDTOConverter;

    public List<EmailDTO> checkInbox(UUID userId) {
        try {
            User user = userService.findUserById(userId);
            List<Email> emails = emailRepository.findAllByRecipientOrderByCreatedDateDesc(user.getUsername());
            return emails.stream().map(emailToEmailDTOConverter::convert).collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            log.info(String.format("Cannot check Inbox; failed to find user associated with ID: %s", userId));
            throw e;
        }
    }

    public List<EmailDTO> checkOutbox(UUID userId) {
        try {
            User user = userService.findUserById(userId);
            List<Email> emails = emailRepository.findAllBySenderOrderByCreatedDateDesc(user.getUsername());
            return emails.stream().map(emailToEmailDTOConverter::convert).collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            log.info(String.format("Cannot check Outbox; failed to find user associated with ID: %s", userId));
            throw e;
        }
    }

    public void sendEmail(SendEmailRequest sendEmailRequest) {

    }

    public void receiveEmail(ReceiveEmailRequest receiveEmailRequest) {

    }

    // When receiving an email, we need to check if the recipient on the email exists in our system
    // When sending an email, we check if the recipient is a local user. If so, save directly. If not, send it externally to configured IP address of external server
}
