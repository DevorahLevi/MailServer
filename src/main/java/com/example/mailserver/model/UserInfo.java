package com.example.mailserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;

@ApiModel
@Data
public class UserInfo
{
    @ApiModelProperty
    private String userName;
    @ApiModelProperty
    private String password;
    @JsonIgnore // Makes this completely ignored when converted from code to JSON. Negative: cannot ever use this as a response body or request body.
    private ArrayList<Email> emailInbox;
    @JsonIgnore
    private ArrayList<Email> emailOutbox;

    public UserInfo(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
        emailInbox = new ArrayList<>();
        emailOutbox = new ArrayList<>();
    }

    public void updateEmails(Email email, String inboxOrOutbox)
    {
        if (inboxOrOutbox.equalsIgnoreCase("inbox")) {
            this.emailInbox.add(0, email);
        } else {
            this.emailOutbox.add(0, email);
        }
    }

    public void printEmails(String inboxOrOutbox) {
        if (inboxOrOutbox.equalsIgnoreCase("inbox")) {
            printEmails(this.emailInbox);
        } else {
            printEmails(this.emailOutbox);
        }
    }

    private void printEmails(ArrayList<Email> emailList)
    {
        if (emailList.isEmpty()) {
            System.out.println("No current emails.");
        } else {
            int count = 0;
            for (Email email : emailList) {
                System.out.println("Email" + count + " = " + email.toString());
                count += 1;
            }
            System.out.println("Total number of emails: " + count);
        }
    }
}
