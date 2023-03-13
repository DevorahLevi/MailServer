package com.example.mailserver.message.controller;

import com.example.mailserver.config.exception.InvalidApiKeyException;
import com.example.mailserver.message.model.DraftMessageDTO;
import com.example.mailserver.message.model.MessageDTO;
import com.example.mailserver.message.model.SaveMessageRequest;
import com.example.mailserver.message.service.MessageService;
import com.example.mailserver.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class MessageController {

    // todo -- add Integration Tests for all endpoints
    // todo -- implement a log table in DB
    // todo -- turn MessageService into an Orchestrator level service, then abstract repo calls to a new MessageService, including saveMessage method

    // todo -- update email to allow for multiple recipients, cc recipients, bcc recipients and an email subject
    // todo -- look into implementing javax mail sender - can we utilize that to physically send/receive emails
    //          but still stick to our implementation via DB and saving emails?
    // todo -- implement reply's to emails, probably will have to save the ID of the 'next' email? and reply emails will all have the parent ID (maybe will also have an order number rather than doing a linked list approach)

    private final MessageService messageService;
    private final ValidationService validationService;

    @GetMapping("/inbox/{userId}")
    public List<MessageDTO> checkInbox(@PathVariable UUID userId) {
        return messageService.checkInbox(userId);
    }

    @GetMapping("/outbox/{userId}")
    public List<MessageDTO> checkOutbox(@PathVariable UUID userId) {
        return messageService.checkOutbox(userId);
    }

    @GetMapping("/drafts/{userId}")
    public List<DraftMessageDTO> checkDrafts(@PathVariable UUID userId) {
        return messageService.checkDrafts(userId);
    }

    @PostMapping("/saveMessage")
    public String saveMessage(@RequestBody SaveMessageRequest saveMessageRequest) {
        return messageService.createMessage(saveMessageRequest);
    }

    @PostMapping("/saveMessage/{messageId}")
    public void saveMessage(@PathVariable String messageId, @RequestBody SaveMessageRequest saveMessageRequest) {
        messageService.saveMessage(messageId, saveMessageRequest, false);
    }

    @PostMapping("/sendMessage/{messageId}")
    public void sendMessage(@PathVariable String messageId, @RequestBody SaveMessageRequest saveMessageRequest) throws Exception {
        messageService.sendMessage(messageId, saveMessageRequest);
    }

    @PostMapping("/receiveMessage")
    public void receiveMessage(@RequestBody SaveMessageRequest saveMessageRequest, @RequestHeader("API-KEY") String apiKey) throws InvalidApiKeyException {
        validationService.validateApiKey(apiKey);
        messageService.receiveMessage(saveMessageRequest);
    }
}