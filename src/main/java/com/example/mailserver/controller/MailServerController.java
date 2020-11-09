package com.example.mailserver.controller;

import com.example.mailserver.model.Email;
import com.example.mailserver.model.UIEmail;
import com.example.mailserver.model.UserInfo;
import com.example.mailserver.service.MailServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class MailServerController
{
    private final MailServerService mailServerService;

    @PostMapping("/email/login")
    public Object login(@RequestBody UserInfo userInfo)
    {
        return mailServerService.inboxLogin(userInfo);
    }

    @PostMapping("/email/send")
    public String sendEmail(@RequestBody UIEmail email)
    {
        return mailServerService.sendEmail(email);
    }
}
