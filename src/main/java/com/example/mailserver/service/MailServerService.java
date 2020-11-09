package com.example.mailserver.service;

import com.example.mailserver.model.Email;
import com.example.mailserver.model.UserInfo;
import com.example.mailserver.utils.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MailServerService
{
    public Users users = new Users();
    public HashMap<String, UserInfo> userDatabase = users.getUserDatabase();


    public ResponseEntity<String> inboxLogin(UserInfo userInfo)
    {
        return inboxLogin(userInfo.getUserName(), userInfo.getPassword());
    }

    public ResponseEntity<String> inboxLogin(String userName, String password)
    {
        ResponseEntity<String> responseEntity;

        if (userDatabase.containsKey(userName)) {
            if(password.equals(userDatabase.get(userName).getPassword())) {
                responseEntity = new ResponseEntity<>(userDatabase.get(userName).getPrimaryKey().toString(), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>("Sorry, you have entered invalid credentials. Please try again.", HttpStatus.UNAUTHORIZED);
            }

        } else {
            responseEntity = new ResponseEntity<>("Sorry, these credentials are not registered. Please make an account and try again.", HttpStatus.UNAUTHORIZED);
        }

        return responseEntity;
    }




    public String sendEmail(Email email)
    {
        return sendEmail(email, email.getTo());
    }

    public String sendEmail(Email email, String recipient)
    {
        String emailSent;
        if (userDatabase.containsKey(recipient))
        {
            UserInfo recipientObject = userDatabase.get(recipient);
            recipientObject.updateEmails(email); //**Throwing null pointer exception error
            emailSent = "Hooray! Your email has been sent to " + recipient;

        } else {
            emailSent = "Sorry, you are trying to send a message to an email that does not exist. Please enter the correct recipient.";
        }
        return emailSent;
    }

}
