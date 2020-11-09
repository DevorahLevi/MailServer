package com.example.mailserver.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
@Builder
public class UserInfo
{
    private String userName;
    private String password;
    private UUID primaryKey;
    private ArrayList<Email> emails;


    public void updateEmails(Email email)
    {
        if (this.emails.size() == 0)
        {
            emails = new ArrayList<>();
            this.emails.add(email);
        }
        else
        {
            this.emails.add(0, email);
        }
    }

    public void printEmails() // *** This method x work. Also throws a null pointer exception
    {
        if (this.emails.isEmpty())
        {
            System.out.println("No current emails.");
        }
        else
        {
            int count = 0;
            for (Email email : this.emails) {
                System.out.println("Email" + count + " = " + email);
            }
            System.out.println("Number of emails: " + count);
        }
    }
}
