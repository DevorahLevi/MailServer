package com.example.mailserver.email.controller;

import com.example.mailserver.config.exception.InvalidApiKeyException;
import com.example.mailserver.email.model.EmailDTO;
import com.example.mailserver.email.model.ReceiveEmailRequest;
import com.example.mailserver.email.model.SaveDraftRequest;
import com.example.mailserver.email.service.EmailService;
import com.example.mailserver.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class EmailController {

    // todo -- add Integration Tests for all endpoints
    private final EmailService emailService;
    private final ValidationService validationService;

    @GetMapping("/inbox/{userId}")
    public List<EmailDTO> checkInbox(@PathVariable UUID userId) {
        return emailService.checkInbox(userId);
    }

    @GetMapping("/outbox/{userId}")
    public List<EmailDTO> checkOutbox(@PathVariable UUID userId) {
        return emailService.checkOutbox(userId);
    }

    @PostMapping("/send")
    public void sendEmail(@RequestBody SaveDraftRequest saveDraftRequest) { // todo -- SaveDraftRequest
        emailService.sendEmail(saveDraftRequest);
    }

    @PostMapping("/receive")
    public void receiveEmail(@RequestBody ReceiveEmailRequest receiveEmailRequest, @RequestHeader("API-KEY") String apiKey) throws InvalidApiKeyException {
        validationService.validateApiKey(apiKey);
        emailService.receiveEmail(receiveEmailRequest);
    }

    // todo -- how to implement drafts?
    // Potential New Endpoints: saveDraft, accessDraft, getAllDrafts

    // Add field to the email object that indicates whether the email is a draft (draft = true/false) -- DONE
    // When getting inbox or outbox, update query to exclude all emails that draft = false -- DONE
    // Update receiveEmail endpoint: email object built and saved has draft = false -- DONE
    // Save Draft: Create and save a new email object, with draft = true, all fields are able to be null, except the sender/user writing draft
    //      ** note - we want this to be reusable so that the front end can call the backend every 5 seconds to update drafts automatically
    // todo -- what is the unique identifier of this draft going to be? how does the front end identify which draft to access
    // Update sendEmail: we find a current draft once email is sent, we update it to sent = true

    // Steps:
    // 1. User hits create draft button
    // 2. Every five seconds, backend should be called to save new updates to the draft. While the draft remains blank,
    //     backend should not be called. Upon first update, backend will be called to save new draft.
    //          ** NOTE: backend should respond with unique identifier (ID or maybe we add a draftId column)
    //                  to the front end so that upon update, backend can update correct draft


    // Scenario A: User finishes draft and hits send - sendEmail endpoint is called with emailID: final draft information is updated,
    //              draft field is set to false, and email is sent if external or saved if internal
    // Scenario B: User hits x on draft, which will prompt one final save to the backend for that draft. User comes back later to send
    //      Step 1: User will go to all drafts section, which will trigger getAllDrafts endpoint, which will return a list of all drafts
    //      Step 2: User will select the draft they would like to open, which will trigger accessDraft for particular draft
    //      Step 3: User either makes changes, or just hits send, which will replicate process in Scenario A

    // todo -- update email to allow for multiple recipients, cc recipients, bcc recipients and an email subject
    // todo -- look into implementing javax mail sender - can we actually utilize that to physically send/receive emails
    //          but still stick to our implementation via DB and saving emails?
}
