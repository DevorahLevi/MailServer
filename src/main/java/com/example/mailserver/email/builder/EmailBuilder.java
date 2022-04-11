package com.example.mailserver.email.builder;

import com.example.mailserver.email.entity.Email;
import org.springframework.stereotype.Component;

@Component
public class EmailBuilder {

    public Email build(String sender, String recipient, String messageContent) {

        return Email.builder()
                .sender(sender)
                .recipient(recipient)
                .messageContent(messageContent)
                .build();
    }
}
