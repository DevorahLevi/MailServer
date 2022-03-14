package com.example.mailserver.email.converter;

import com.example.mailserver.email.entity.Email;
import com.example.mailserver.email.model.EmailDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EmailToEmailDTOConverter implements Converter<Email, EmailDTO> {

    public EmailDTO convert(Email email) {
        return EmailDTO.builder()
                .recipient(email.getRecipient())
                .sender(email.getSender())
                .messageContent(email.getMessageContent())
                .createdDate(email.getCreatedDate())
                .build();
    }
}
