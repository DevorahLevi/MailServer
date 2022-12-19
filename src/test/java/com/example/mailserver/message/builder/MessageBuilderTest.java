package com.example.mailserver.message.builder;

import com.example.mailserver.message.entity.Message;
import com.example.mailserver.message.model.SaveMessageRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class MessageBuilderTest {

    @InjectMocks
    MessageBuilder subject;

    @Test
    public void build() {
        SaveMessageRequest saveMessageRequest = SaveMessageRequest.builder()
                .sender("sender")
                .recipient("recipient")
                .messageContent("messageContent")
                .build();

        Message expected = Message.builder()
                .sender("sender")
                .recipient("recipient")
                .messageContent("messageContent")
                .sent(true)
                .build();

        Message actual = subject.build(saveMessageRequest, true);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void build_emptyFields() {
        SaveMessageRequest saveMessageRequest = SaveMessageRequest.builder()
                .sender("sender")
                .build();

        Message expected = Message.builder()
                .sender("sender")
                .recipient("")
                .messageContent("")
                .sent(true)
                .build();

        Message actual = subject.build(saveMessageRequest, true);
        assertThat(actual).isEqualTo(expected);
    }
}