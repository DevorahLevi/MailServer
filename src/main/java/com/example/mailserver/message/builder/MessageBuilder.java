package com.example.mailserver.message.builder;

import com.example.mailserver.message.entity.Message;
import com.example.mailserver.message.model.SaveMessageRequest;
import org.springframework.stereotype.Component;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class MessageBuilder {

    public Message build(SaveMessageRequest request, boolean sent) {

        return Message.builder()
                .sender(request.getSender())
                .recipient(isEmpty(request.getRecipient()) ? "" : request.getRecipient())
                .messageContent(isEmpty(request.getMessageContent()) ? "" : request.getMessageContent())
                .sent(sent)
                .build();
    }
}