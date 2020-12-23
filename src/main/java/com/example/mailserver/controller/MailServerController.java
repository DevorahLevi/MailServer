package com.example.mailserver.controller;

import com.example.mailserver.model.*;
import com.example.mailserver.service.MailServerService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ExampleProperty;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/email")
public class MailServerController
{
    private final MailServerService mailServerService;

    // To see generated documentation, go to: http://localhost:8080/swagger-ui.html

    // Adds a note to your API endpoint method documentation
    @ApiOperation(value = "Login to your awesome Mail Server App!")
    @ApiResponses(value = {@ApiResponse(code = 200,
                                        message = "OK",
                                        response = String.class,
                                        examples = @Example(value = @ExampleProperty(value = "1234-1234-12345678"))),
                            @ApiResponse(code = 401,
                                        message = "Unauthorized",
                                        response = String.class,
                                        examples = @Example(value = @ExampleProperty(value = "Sorry, you have entered invalid credentials. Please try again.")))
    }) // Sets the responses for the API endpoint method documentation
    @PostMapping("/login")
    public Object login(@RequestBody UserInfo userInfo)
    {
        return mailServerService.inboxLogin(userInfo);
    }



    @ApiOperation(value = "Send an email through your Mail App.")
    @ApiResponses(value = {@ApiResponse(code = 200,
                                        message = "OK",
                                        response = String.class,
                                        examples = @Example(value = @ExampleProperty(value = "Hooray! Your email has been sent to recipient!"))),
                            @ApiResponse(code = 401,
                                        message = "Unauthorized",
                                        response = String.class,
                                        examples = @Example(value = @ExampleProperty(value = "Sorry, you are trying to send a message to an email " +
                                                "that does not exist. Please enter the correct recipient."))),
                            @ApiResponse(code = 503,
                                        message = "Service Unavailable",
                                        response = String.class,
                                        examples = @Example(value = @ExampleProperty(value = "Sorry, you are trying to send a message to an email " +
                                                "that does not exist. Please enter the correct recipient.")))})
    @ResponseBody
    @PostMapping("/send")
    public Object sendEmail(@RequestBody UIEmail email)
    {
        return mailServerService.sendEmail(email);
    }



    @ApiOperation(value = "Check your Mail App Inbox.")
    @ApiResponses(value = {@ApiResponse(code = 200,
                                        message = "OK",
                                        response = ArrayList.class)})
    @ResponseBody
    @PostMapping("/inbox")
    public ArrayList<DisplayInboxEmail> checkInbox(@RequestBody GetUUID primaryKey)
    {
        return mailServerService.checkInbox(primaryKey.getPrimaryKey());
    }



    @ApiOperation(value = "Check your Mail App Outbox.")
    @ApiResponses(value = {@ApiResponse(code = 200,
                                        message = "OK",
                                        response = ArrayList.class)})
    @ResponseBody
    @PostMapping("/outbox")
    public ArrayList<DisplayOutboxEmail> checkOutbox(@RequestBody GetUUID primaryKey)
    {
        return mailServerService.checkOutbox(primaryKey.getPrimaryKey());
    }



    @ApiOperation(value = "Receive mail from friends and acquaintances on other Mail Apps.")
    @ApiResponses(value = {@ApiResponse(code = 200,
                                        message = "OK",
                                        response = String.class,
                                        examples = @Example(value = @ExampleProperty(value = "Hooray! Your email has been received from an external server!"))),
                            @ApiResponse(code = 401,
                                        message = "Unauthorized",
                                        response = String.class,
                                        examples = @Example(value = @ExampleProperty(value = "Sorry, the user that you are trying to send an email to does not exist."))),
                            @ApiResponse(code = 503,
                                    message = "Service Unavailable",
                                    response = String.class,
                                    examples = @Example(value = @ExampleProperty(value = "Sorry, this service is not available right now. Please try again later.")))})
    @PostMapping("/receiveExternalMail")
    public Object receiveExternalMail(@RequestBody ExternalEmail externalEmail,
                                        @RequestHeader ("api-key") String keyValue) // use header to check if it is equals to letMeIn in base code 64
    {
        return mailServerService.receiveExternalMail(externalEmail, keyValue);
    }
}