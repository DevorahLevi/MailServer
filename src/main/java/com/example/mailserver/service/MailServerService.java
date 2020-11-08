package com.example.mailserver.service;

import com.example.mailserver.model.UserInfo;
import com.example.mailserver.utils.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailServerService
{
    public ResponseEntity<String> inboxLogin(UserInfo userInfo)
    {
        Users users = new Users();
        HashMap<UserInfo, UUID> usersDatabase = users.getUserDatabase();
        ResponseEntity<String> responseEntity;

        if (usersDatabase.containsKey(userInfo)) {
            responseEntity = new ResponseEntity<>(users.getUserDatabase().get(userInfo).toString(), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Sorry, you have entered invalid credentials. Please try again.", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }
}
