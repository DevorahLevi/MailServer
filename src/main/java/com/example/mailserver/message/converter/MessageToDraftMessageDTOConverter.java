package com.example.mailserver.message.converter;

import com.example.mailserver.message.entity.Message;
import com.example.mailserver.message.model.DraftMessageDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MessageToDraftMessageDTOConverter implements Converter<Message, DraftMessageDTO> {

    public DraftMessageDTO convert(Message message) {
        return DraftMessageDTO.builder()
                .draftMessageId(message.getId().toString())
                .recipient(message.getRecipient())
                .sender(message.getSender())
                .messageContent(message.getMessageContent())
                .date(message.getUpdatedDate())
                .build();
    }
}