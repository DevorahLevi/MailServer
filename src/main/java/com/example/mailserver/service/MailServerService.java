package com.example.mailserver.service;

import com.example.mailserver.model.Email;
import com.example.mailserver.model.UIEmail;
import com.example.mailserver.model.UserInfo;
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

    public String sendEmail(String sender, String recipient, String message)
    {
        String emailSent;
        UUID recipientUUID = checkUserNameExists(recipient);
        UUID senderUUID = UUID.fromString(sender);

        if (recipientUUID != null)
        {
            UserInfo recipientObject = userDatabase.get(recipientUUID);
            UserInfo senderObject = userDatabase.get(senderUUID);

            Email email = Email.builder().from(senderUUID).to(recipientUUID).message(message).build();
            recipientObject.updateEmails(email,"inbox");
            senderObject.updateEmails(email, "outbox");

            emailSent = "Hooray! Your email has been sent to " + recipient;
        } else {
            emailSent = "Sorry, you are trying to send a message to an email that does not exist. Please enter the correct recipient.";
        }
        return emailSent;
    }


    public ArrayList<Email> checkInbox(String primaryKey)
    {
        UserInfo userObject = userDatabase.get(UUID.fromString(primaryKey));
        return userObject.getEmailInbox();
    }


    /*
    public String checkOutbox(String primaryKey)
    {
        return null;
    }
 */

    public UUID checkUserNameExists(String userName)
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

}