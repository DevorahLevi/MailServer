package com.example.mailserver.message.converter;

import com.example.mailserver.message.entity.Message;
import com.example.mailserver.message.model.MessageDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class MessageToMessageDTOConverterTest {

    @InjectMocks
    MessageToMessageDTOConverter subject;

    @Test
    public void convert() {
        Date updatedDate = new Date();
        MessageDTO expected = MessageDTO.builder()
                .recipient("recipient")
                .sender("sender")
                .messageContent("messageContent")
                .date(updatedDate)
                .build();

        Message message = Message.builder()
                .recipient("recipient")
                .sender("sender")
                .messageContent("messageContent")
                .updatedDate(updatedDate)
                .build();

        MessageDTO actual = subject.convert(message);
        assertThat(actual).isEqualTo(expected);
    }
}