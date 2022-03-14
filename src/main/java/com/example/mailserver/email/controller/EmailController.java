package com.example.mailserver.email.controller;

import com.example.mailserver.email.service.EmailService;
import com.example.mailserver.email.model.EmailDTO;
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
    public void sendEmail() {

    }

    @PostMapping("/receive")
    public void receiveEmail() {

    }

    /*
Homework Assignment:
API endpoint that can receive external mail and puts it into the correct person's inbox
Send function (might not need a new endpoint), just needs to have awareness that if the 'to' is not in your local users, you have to send it externally
(IP) Address of external server must be in the configurations
Ability to send externally must have a feature switch around it to turn it on or off
 */
}
