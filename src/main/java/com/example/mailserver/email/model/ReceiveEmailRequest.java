package com.example.mailserver.email.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveEmailRequest {
    private String sender;
    private String recipient;
    private String messageContent;
}