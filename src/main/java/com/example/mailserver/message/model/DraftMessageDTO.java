package com.example.mailserver.message.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DraftMessageDTO {

    private String draftMessageId;
    private String sender;
    private String recipient;
    private String messageContent;
    private Date date;
}