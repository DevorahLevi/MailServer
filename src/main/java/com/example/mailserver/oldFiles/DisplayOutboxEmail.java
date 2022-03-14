package com.example.mailserver.oldFiles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisplayOutboxEmail {
    private String recipientUsername;
    private String messageContent;
}
