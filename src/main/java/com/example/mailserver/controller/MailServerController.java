package com.example.mailserver.controller;

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

    @ResponseBody
    @PostMapping("/inbox")
    public ArrayList<DisplayInboxEmail> checkInbox(@RequestBody GetUUID primaryKey)
    {
        return mailServerService.checkInbox(primaryKey.getPrimaryKey());
    }
/*
    @PostMapping("/send")
    public String checkOutbox(@RequestBody String primaryKey)
    {
        return mailServerService.checkOutbox(primaryKey);
    } */
}
