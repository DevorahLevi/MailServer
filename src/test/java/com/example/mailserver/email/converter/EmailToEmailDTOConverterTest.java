package com.example.mailserver.email.converter;

import com.example.mailserver.email.entity.Email;
import com.example.mailserver.email.model.EmailDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class EmailToEmailDTOConverterTest {

    @InjectMocks
    EmailToEmailDTOConverter subject;

    @Test
    public void convert() {
        Date createdDate = new Date();
        EmailDTO expected = EmailDTO.builder()
                .recipient("recipient")
                .sender("sender")
                .messageContent("messageContent")
                .createdDate(createdDate)
                .build();

        Email email = Email.builder()
                .recipient("recipient")
                .sender("sender")
                .messageContent("messageContent")
                .createdDate(createdDate)
                .build();

        EmailDTO actual = subject.convert(email);
        assertThat(actual).isEqualTo(expected);
    }
}