package com.example.mailserver.controller;

import com.example.mailserver.config.ExternalMailConfiguration;
import com.example.mailserver.config.FeatureSwitchReceiveExternalMailConfiguration;
import com.example.mailserver.config.FeatureSwitchSendExternalMailConfiguration;
import com.example.mailserver.model.*;
import com.example.mailserver.service.MailServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/email")
public class MailServerController
{
    private final MailServerService mailServerService;

    // Testing Config files and environment variables
//    private final ExternalMailConfiguration externalMailConfiguration;

    @PostMapping("/login")
    public Object login(@RequestBody UserInfo userInfo)
    {
        /*
        // Testing out the feature switch and the external mail config ** note that for booleans with @Data, it is .is and not .get
        if (featureSwitchConfiguration.isEmailUp()) {
            System.out.println(externalMailConfiguration.getIp());
        } */

        return mailServerService.inboxLogin(userInfo);
    }

    @PostMapping("/send")
    public String sendEmail(@RequestBody UIEmail email)
    {
        return mailServerService.sendEmail(email);
    }

    @ResponseBody
    @PostMapping("/inbox")
    public ArrayList<DisplayInboxEmail> checkInbox(@RequestBody GetUUID primaryKey)
    {
        return mailServerService.checkInbox(primaryKey.getPrimaryKey());
    }

    @ResponseBody
    @PostMapping("/outbox")
    public ArrayList<DisplayOutboxEmail> checkOutbox(@RequestBody GetUUID primaryKey)
    {
        return mailServerService.checkOutbox(primaryKey.getPrimaryKey());
    }

    @PostMapping("/receiveExternalMail")
    public Object receiveExternalMail(@RequestBody ExternalEmail externalEmail)
    {
        return mailServerService.receiveExternalMail(externalEmail);
    }
}

/*
Homework Assignment:
API endpoint that can receive external mail and puts it into the correct person's inbox
Send function (might not need a new endpoint), just needs to have awareness that if the 'to' is not in your local users, you have to send it externally
(IP) Address of external server must be in the configurations
Ability to send externally must have a feature switch around it to turn it on or off
 */