//package com.example.mailserver.oldFiles;
//
//import com.example.mailserver.config.ExternalMailConfiguration;
//import com.example.mailserver.config.FeatureSwitchReceiveExternalMailConfiguration;
//import com.example.mailserver.config.FeatureSwitchSendExternalMailConfiguration;
//import com.example.mailserver.oldFiles.model.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.*;
//
//@Service
//@RequiredArgsConstructor
//public class MailServerService
//{
//    private HashMap<UUID, User> userDatabase = new Users().getUserDatabase(); // Mock Database
//
//    private final RestTemplate restTemplate;
//    private final MailServerServiceHelper mailServerServiceHelper;
//    private final ExternalMailConfiguration externalMailConfiguration;
//    private final FeatureSwitchSendExternalMailConfiguration sendExternalMailConfiguration;
//    private final FeatureSwitchReceiveExternalMailConfiguration receiveExternalMailConfiguration;
//
//    public ResponseEntity<String> sendEmail(UIEmail tempEmail) {
//        return sendEmail(tempEmail.getSender(), tempEmail.getRecipient(), tempEmail.getMessageContent());
//    }
//
//    public ResponseEntity<String> sendEmail(UUID sender, String recipient, String message) // TODO == refactor to be void and throw specific exceptions
//    {
//        ResponseEntity<String> emailSent;
//
//        UUID recipientUUID = mailServerServiceHelper.checkUserNameExists(recipient, userDatabase);
//        if (recipientUUID != null && userDatabase.containsKey(sender)) {
//            User recipientObject = userDatabase.get(recipientUUID);
//            User senderObject = userDatabase.get(sender);
//
//            EmailDTO emailDTO = EmailDTO.builder().sender(sender).recipient(recipientUUID).messageContent(message).build();
//            recipientObject.updateEmails(emailDTO,"inbox");
//            senderObject.updateEmails(emailDTO, "outbox");
//
//            emailSent = new ResponseEntity<>(("Hooray! Your email has been sent to " + recipient), HttpStatus.OK);
//        } else {
//            if(!sendExternalMailConfiguration.isSendExternalMailOn()) {
//                emailSent = new ResponseEntity<>(("Sorry, you are trying to send a message to an email that does not exist. " +
//                        "Please enter the correct recipient."), HttpStatus.SERVICE_UNAVAILABLE);
//            } else {
//                    ExternalEmail body = ExternalEmail.builder().senderUsername(userDatabase.get(sender).getUserName()).recipientUsername(recipient).messageContent(message).build();
//                    String headerValue = new String (Base64.getEncoder().encode(externalMailConfiguration.getApiKey().getBytes()));
//                    HttpHeaders headers = new HttpHeaders();
//                    headers.add("api-key", headerValue);
//                    HttpEntity<ExternalEmail> httpEntity = new HttpEntity<>(body, headers); // Http entity is equal to the body of the request
//
//                try {
//                    ResponseEntity<Void> response = restTemplate.exchange(externalMailConfiguration.getIp(), HttpMethod.POST, httpEntity, Void.class); //use Void.class if not expecting anything back
//                    emailSent = new ResponseEntity<>("Hooray! Your email has been sent to an external server!", HttpStatus.OK);
//                } catch (RestClientException e) {
//                    emailSent = new ResponseEntity<>(("Sorry, you are trying to send a message to an email that does not exist. " +
//                            "Please enter the correct recipient."), HttpStatus.BAD_REQUEST);
//                }
//            }
//        }
//        return emailSent;
//    }
//
//    public List<DisplayInboxEmail> checkInbox(UUID primaryKey)
//    {
//        User userObject = userDatabase.get(primaryKey);
//        ArrayList<EmailDTO> emailDTOInboxes = userObject.getEmailDTOInboxes();
//
//        ArrayList<DisplayInboxEmail> emailDisplay = new ArrayList<>();
//
//        Iterator iterator = emailDTOInboxes.iterator();
//        while (iterator.hasNext()) {
//            EmailDTO tempEmailDTO = (EmailDTO)iterator.next();
//
//            if (ExternalUsers.externalUserDatabase.containsValue(tempEmailDTO.getSender())) {
//                String externalUserUUID = getExternalUserName(tempEmailDTO.getSender());
//                emailDisplay.add(DisplayInboxEmail.builder()
//                        .senderUsername(externalUserUUID)
//                        .messageContent(tempEmailDTO.getMessageContent())
//                        .build());
//            } else {
//                emailDisplay.add(DisplayInboxEmail.builder()
//                        .senderUsername(userDatabase.get(tempEmailDTO.getSender()).getUserName())
//                        .messageContent(tempEmailDTO.getMessageContent())
//                        .build());
//            }
//        }
//
//        return emailDisplay;
//    }
//
//    private String getExternalUserName(UUID externalUserUUID)
//    {
//        String userName = null;
//        Iterator externalUserDatabaseIterator = ExternalUsers.externalUserDatabase.entrySet().iterator();
//        while (externalUserDatabaseIterator.hasNext()) {
//            Map.Entry mapElement = (Map.Entry)externalUserDatabaseIterator.next();
//            UUID tempUUID = (UUID) mapElement.getValue();
//            if (tempUUID.equals(externalUserUUID)) {
//                userName = (String) mapElement.getKey();;
//            }
//        }
//        return userName;
//    }
//
//    public List<DisplayOutboxEmail> checkOutbox(UUID primaryKey)
//    {
//        User userObject = userDatabase.get(primaryKey);
//        ArrayList<EmailDTO> emailDTOOutboxes = userObject.getEmailDTOOutboxes();
//
//        ArrayList<DisplayOutboxEmail> emailDisplay = new ArrayList<>();
//
//        Iterator iterator = emailDTOOutboxes.iterator();
//        while (iterator.hasNext()) {
//            EmailDTO tempEmailDTO = (EmailDTO)iterator.next();
//            emailDisplay.add(DisplayOutboxEmail.builder()
//                                            .recipientUsername(userDatabase.get(tempEmailDTO.getRecipient()).getUserName())
//                                            .messageContent(tempEmailDTO.getMessageContent())
//                                            .build());
//        }
//        return emailDisplay;
//    }
//
//    public ResponseEntity<String> receiveExternalMail(ExternalEmail externalEmail, String keyValue) //check if header is equal to "letMeIn". call second method if yes, if not, throw error
//    {   // use header to check if it is equals to letMeIn in base code 64
//        String decodedHeader = new String (Base64.getDecoder().decode(keyValue));
//        if (decodedHeader.equals(externalMailConfiguration.getApiKey()))
//        {
//            return receiveExternalMail(externalEmail.getSenderUsername(), externalEmail.getRecipientUsername(), externalEmail.getMessageContent());
//        } else {
//            return new ResponseEntity<>("Sorry you have the wrong key and cannot be let in.", HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//    public ResponseEntity<String> receiveExternalMail(String sender, String recipient, String message)
//    {
//        ResponseEntity<String> responseEntity;
//
//        // ** note that for booleans with @Data, to retrieve, use: .is and not .get
//        if (!receiveExternalMailConfiguration.isReceiveExternalMailOn()) {
//            responseEntity = new ResponseEntity<>("Sorry, this service is not available right now. Please try again later.",
//                                                            HttpStatus.SERVICE_UNAVAILABLE);
//        } else {
//            UUID recipientUUID = mailServerServiceHelper.checkUserNameExists(recipient, userDatabase); // check if recipient exists on our server
//
//            if (recipientUUID != null) { //recipient exists, then
//                if (!ExternalUsers.externalUserDatabase.containsKey(sender)) {
//                    ExternalUsers.externalUserDatabase.put(sender, UUID.randomUUID());
//                }
//                UUID senderUUID = ExternalUsers.externalUserDatabase.get(sender);
//                User recipientObject = userDatabase.get(recipientUUID);
//                EmailDTO emailDTO = EmailDTO.builder().sender(senderUUID).recipient(recipientUUID).messageContent(message).build();
//                recipientObject.updateEmails(emailDTO,"inbox");
//
//                responseEntity = new ResponseEntity<>("Hooray! Your email has been received from an external server!", HttpStatus.OK);
//            } else {
//                responseEntity = new ResponseEntity<>("Sorry, the user that you are trying to send an email to does not exist.",
//                        HttpStatus.BAD_REQUEST);
//            }
//        }
//        return responseEntity;
//    }
//}