package com.example.mailserver.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class UserInfo
{
    private String userName;
    private String password;
    private UUID primaryKey;
    private ArrayList<Email> emails;

    public UserInfo(String userName, String password, UUID primaryKey)
    {
        this.userName = userName;
        this.password = password;
        this.primaryKey = primaryKey;
        emails = new ArrayList<>();
    }

    public void updateEmails(Email email)
    {
        this.emails.add(0, email);
    }

    public void printEmails()
    {
        if (this.emails.isEmpty())
        {
            System.out.println("No current emails.");
        }
        else
        {
            int count = 0;
            for (Email email : this.emails) {
                System.out.println("Email" + count + " = " + email.toString());
                count += 1;
            }
            System.out.println("Total number of emails: " + count);
        }
    }
}
