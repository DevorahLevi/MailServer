package com.example.mailserver.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisplayInboxEmail
{
    private String from;        // Username of sender
    private String message;     // Message content
}
