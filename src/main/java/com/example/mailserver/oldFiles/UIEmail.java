package com.example.mailserver.oldFiles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UIEmail { // todo -- rename this to SendEmailRequest
    private UUID sender;
    private String recipient;
    private String messageContent;
}