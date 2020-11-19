package com.example.mailserver.service;

import com.example.mailserver.config.FeatureSwitchReceiveExternalMailConfiguration;
import com.example.mailserver.config.FeatureSwitchSendExternalMailConfiguration;
import com.example.mailserver.model.*;
import com.example.mailserver.utils.ExternalUsers;
import com.example.mailserver.utils.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MailServerService
{
    private Users users = new Users();
    private HashMap<UUID, UserInfo> userDatabase = users.getUserDatabase();

    private final FeatureSwitchSendExternalMailConfiguration featureSwitchSendExternalMailConfiguration;
    private final FeatureSwitchReceiveExternalMailConfiguration receiveExternalMailConfiguration;



    public Object inboxLogin(UserInfo userInfo)
    {
        return inboxLogin(userInfo.getUserName(), userInfo.getPassword());
    }

    public Object inboxLogin(String userName, String password)
    {
        ResponseEntity<String> responseEntity;
        UUID userUUID = checkUserNameExists(userName);

        if (userUUID != null) {
            if(password.equals(userDatabase.get(userUUID).getPassword())) {
                responseEntity = new ResponseEntity<>(userUUID.toString(), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>("Sorry, you have entered invalid credentials. Please try again.", HttpStatus.UNAUTHORIZED);
            }

        } else {
            responseEntity = new ResponseEntity<>("Sorry, these credentials are not registered. Please make an account and try again.", HttpStatus.UNAUTHORIZED);
        }

        return responseEntity;
    }



    public String sendEmail(UIEmail tempEmail)
    {
        return sendEmail(tempEmail.getFrom(), tempEmail.getTo(), tempEmail.getMessage());
    }

    public String sendEmail(UUID sender, String recipient, String message)
    {
        String emailSent;
        UUID recipientUUID = checkUserNameExists(recipient);

        if (recipientUUID != null)
        {
            UserInfo recipientObject = userDatabase.get(recipientUUID);
            UserInfo senderObject = userDatabase.get(sender);

            Email email = Email.builder().from(sender).to(recipientUUID).message(message).build();
            recipientObject.updateEmails(email,"inbox");
            senderObject.updateEmails(email, "outbox");

            emailSent = "Hooray! Your email has been sent to " + recipient;
        } else { // ***** add a check to see if it can be sent externally. if not, than display emailSent invalid email error message
            emailSent = "Sorry, you are trying to send a message to an email that does not exist. Please enter the correct recipient.";
        }
        return emailSent;
    }



    private UUID checkUserNameExists(String userName)
    {
        UUID userPrimaryKey = null;
        Iterator userDatabaseIterator = userDatabase.entrySet().iterator();

        while (userDatabaseIterator.hasNext())
        {
            Map.Entry mapElement = (Map.Entry)userDatabaseIterator.next();
            UserInfo tempUserInfo = (UserInfo) mapElement.getValue();
            if (userName.equals(tempUserInfo.getUserName()))
            {
                userPrimaryKey = (UUID)mapElement.getKey();
            }
        }
        return userPrimaryKey;
    }



    public ArrayList<DisplayInboxEmail> checkInbox(UUID primaryKey)
    {
        UserInfo userObject = userDatabase.get(primaryKey);
        ArrayList<Email> emailInbox = userObject.getEmailInbox();

        ArrayList<DisplayInboxEmail> emailDisplay = new ArrayList<>();

        Iterator iterator = emailInbox.iterator();
        while (iterator.hasNext())
        {
            Email tempEmail = (Email)iterator.next();

            if (ExternalUsers.externalUserDatabase.containsValue(tempEmail.getFrom()))
            {
                String externalUserUUID = getExternalUserName(tempEmail.getFrom());
                emailDisplay.add(DisplayInboxEmail.builder()
                        .from(externalUserUUID)
                        .message(tempEmail.getMessage())
                        .build());
            }
            else
            {
                emailDisplay.add(DisplayInboxEmail.builder()
                        .from(userDatabase.get(tempEmail.getFrom()).getUserName())
                        .message(tempEmail.getMessage())
                        .build());
            }
        }

        return emailDisplay;
    }

    private String getExternalUserName(UUID externalUserUUID)
    {
        String userName = null;
        Iterator externalUserDatabaseIterator = ExternalUsers.externalUserDatabase.entrySet().iterator();
        while (externalUserDatabaseIterator.hasNext())
        {
            Map.Entry mapElement = (Map.Entry)externalUserDatabaseIterator.next();
            UUID tempUUID = (UUID) mapElement.getValue();
            if (tempUUID.equals(externalUserUUID))
            {
                userName = (String) mapElement.getKey();;
            }
        }
        return userName;
    }



    public ArrayList<DisplayOutboxEmail> checkOutbox(UUID primaryKey)
    {
        UserInfo userObject = userDatabase.get(primaryKey);
        ArrayList<Email> emailInbox = userObject.getEmailOutbox();

        ArrayList<DisplayOutboxEmail> emailDisplay = new ArrayList<>();

        Iterator iterator = emailInbox.iterator();
        while (iterator.hasNext())
        {
            Email tempEmail = (Email)iterator.next();
            emailDisplay.add(DisplayOutboxEmail.builder()
                                            .to(userDatabase.get(tempEmail.getTo()).getUserName())
                                            .message(tempEmail.getMessage())
                                            .build());
        }

        return emailDisplay;
    }



    public Object receiveExternalMail(ExternalEmail externalEmail)
    {

        return receiveExternalMail(externalEmail.getFrom(), externalEmail.getTo(), externalEmail.getMessage());
    }

    public Object receiveExternalMail(String sender, String recipient, String message)
    {
        ResponseEntity<String> responseEntity;

        if (!receiveExternalMailConfiguration.isReceiveExternalMailOn())
        {
            responseEntity = new ResponseEntity<>("Sorry, this service is not available right now. Please try again later.",
                                                            HttpStatus.SERVICE_UNAVAILABLE);
        }
        else
        {
            UUID recipientUUID = checkUserNameExists(recipient); // check if recipient exists on our server

            if (recipientUUID != null) //recipient exists, then
            {
                if (!ExternalUsers.externalUserDatabase.containsKey(sender)) {
                    ExternalUsers.externalUserDatabase.put(sender, UUID.randomUUID());
                }
                UUID senderUUID = ExternalUsers.externalUserDatabase.get(sender);
                UserInfo recipientObject = userDatabase.get(recipientUUID);
                Email email = Email.builder().from(senderUUID).to(recipientUUID).message(message).build();
                recipientObject.updateEmails(email,"inbox");

                responseEntity = new ResponseEntity<>("Hooray! Your email has been received from an external server!", HttpStatus.OK);
            }
            else
            {
                responseEntity = new ResponseEntity<>("Sorry, the user that you are trying to send an email to does not exist.",
                        HttpStatus.BAD_REQUEST);
            }
        }

        return responseEntity;
    }

}