package com.example.mailserver.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisplayOutboxEmail
{
    private String to;          // Username of recipient
    private String message;     // Message content
}
