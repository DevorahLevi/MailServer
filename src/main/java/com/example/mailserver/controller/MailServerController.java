package com.example.mailserver.controller;

import com.example.mailserver.model.UIEmail;
import com.example.mailserver.model.UserInfo;
import com.example.mailserver.service.MailServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/email")
@RequiredArgsConstructor
public class MailServerController
{
    private final MailServerService mailServerService;

    @PostMapping("/login")
    public Object login(@RequestBody UserInfo userInfo)
    {
        return mailServerService.inboxLogin(userInfo);
    }

    @PostMapping("/send")
    public String sendEmail(@RequestBody UIEmail email)
    {
        return mailServerService.sendEmail(email);
    }

    /*
    @PostMapping("/inbox")
    public String checkInbox(@RequestBody String primaryKey)
    {
        return mailServerService.checkInbox(primaryKey);
    }

    @PostMapping("/send")
    public String checkOutbox(@RequestBody String primaryKey)
    {
        return mailServerService.checkOutbox(primaryKey);
    } */
}
