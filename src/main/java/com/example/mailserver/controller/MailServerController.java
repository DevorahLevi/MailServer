package com.example.mailserver.controller;

import com.example.mailserver.model.*;
import com.example.mailserver.service.MailServerService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/email")
public class MailServerController
{
    private final MailServerService mailServerService;

    // To see generated documentation, go to: http://localhost:8080/swagger-ui.html
    @ApiOperation(notes = "Test notes.", value = "This is a test note.") // Adds a note to your API endpoint method documentation
    @ApiResponses(value = {@ApiResponse(code = 200,
                                        message = "OK",
                                        response = String.class,
                                        examples = @Example (value = @ExampleProperty(mediaType = "UUID", value = "1234-1234-12345678"))),
                            @ApiResponse(code = 401,
                                        message = "Unauthorized",
                                        response = String.class)
    }) // Sets the responses for the API endpoint method documentation
    @PostMapping("/login")
    public Object login(@RequestBody UserInfo userInfo)
    {
        return mailServerService.inboxLogin(userInfo);
    }

    @ResponseBody
    @PostMapping("/send")
    public Object sendEmail(@RequestBody UIEmail email)
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
    public Object receiveExternalMail(@RequestBody ExternalEmail externalEmail,
                                        @RequestHeader ("api-key") String keyValue) // use header to check if it is equals to letMeIn in base code 64
    {
        return mailServerService.receiveExternalMail(externalEmail, keyValue);
    }
}

/*
Homework Assignment:
API endpoint that can receive external mail and puts it into the correct person's inbox
Send function (might not need a new endpoint), just needs to have awareness that if the 'to' is not in your local users, you have to send it externally
(IP) Address of external server must be in the configurations
Ability to send externally must have a feature switch around it to turn it on or off
 */