package com.example.mailserver.oldFiles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExternalEmail {
    private String senderUsername;
    private String recipientUsername;
    private String messageContent;
}