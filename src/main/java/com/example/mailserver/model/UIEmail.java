package com.example.mailserver.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UIEmail
{
    private String from;        // UUID in string form of sender
    private String to;          // userName of recipient
    private String message;     // message content
}
