package com.example.mailserver.email.builder;

import com.example.mailserver.email.entity.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class EmailBuilderTest {

    @InjectMocks
    EmailBuilder subject;

    @Test
    public void build() {
        Email expected = Email.builder()
                .sender("sender")
                .recipient("recipient")
                .messageContent("messageContent")
                .build();

        Email actual = subject.build("sender", "recipient", "messageContent");
        assertThat(actual).isEqualTo(expected);
    }
}