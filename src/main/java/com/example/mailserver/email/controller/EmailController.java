package com.example.mailserver.email.controller;

import com.example.mailserver.email.model.EmailDTO;
import com.example.mailserver.email.model.ReceiveEmailRequest;
import com.example.mailserver.email.model.SendEmailRequest;
import com.example.mailserver.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/inbox/{userId}")
    public List<EmailDTO> checkInbox(@PathVariable UUID userId) {
        return emailService.checkInbox(userId);
    }

    @GetMapping("/outbox/{userId}")
    public List<EmailDTO> checkOutbox(@PathVariable UUID userId) {
        return emailService.checkOutbox(userId);
    }

    @PostMapping("/send")
    public void sendEmail(@RequestBody SendEmailRequest sendEmailRequest) {
        emailService.sendEmail(sendEmailRequest);
    }

    @PostMapping("/receive")
    public void receiveEmail(@RequestBody ReceiveEmailRequest receiveEmailRequest) {
        emailService.receiveEmail(receiveEmailRequest);
    }
}
