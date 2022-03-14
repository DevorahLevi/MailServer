//package com.example.mailserver.oldFiles;
//
//import com.example.mailserver.oldFiles.model.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/mailServer/email")
//public class MailServerController
//{
//    private final MailServerService mailServerService;
//
//    @PostMapping("/send")
//    public ResponseEntity<String> sendEmail(@RequestBody UIEmail email) {
//        return mailServerService.sendEmail(email);
//    }
//
//    @GetMapping("/inbox/{userId}")
//    public List<DisplayInboxEmail> checkInbox(@PathVariable UUID userId) {
//        return mailServerService.checkInbox(userId);
//    }
//
//    @GetMapping("/outbox/{userId}")
//    public List<DisplayOutboxEmail> checkOutbox(@PathVariable UUID userId) {
//        return mailServerService.checkOutbox(userId);
//    }
//
//    @PostMapping("/receiveExternalMail")
//    public ResponseEntity<String> receiveExternalMail(@RequestBody ExternalEmail externalEmail,
//                                                      @RequestHeader ("api-key") String apiKey) {
//        return mailServerService.receiveExternalMail(externalEmail, apiKey);
//    }
//}