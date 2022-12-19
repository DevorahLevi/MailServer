package com.example.mailserver.message.converter;

import com.example.mailserver.message.entity.Message;
import com.example.mailserver.message.model.MessageDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MessageToMessageDTOConverter implements Converter<Message, MessageDTO> {

    public MessageDTO convert(Message message) {
        return MessageDTO.builder()
                .recipient(message.getRecipient())
                .sender(message.getSender())
                .messageContent(message.getMessageContent())
                .date(message.getUpdatedDate())
                .build();
    }
}