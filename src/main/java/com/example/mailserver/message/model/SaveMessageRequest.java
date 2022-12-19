package com.example.mailserver.message.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveMessageRequest {

    private String sender;
    private String recipient;
    private String messageContent;
}