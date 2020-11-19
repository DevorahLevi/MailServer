package com.example.mailserver.controller;

import com.example.mailserver.config.ExternalMailConfiguration;
import com.example.mailserver.config.FeatureSwitchConfiguration;
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
    private final ExternalMailConfiguration externalMailConfiguration;
    private final FeatureSwitchConfiguration featureSwitchConfiguration;

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
}
