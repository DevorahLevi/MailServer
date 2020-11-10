package com.example.mailserver.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UIEmail
{
    private UUID from;          // UUID of sender
    private String to;          // userName of recipient
    private String message;     // message content
}
