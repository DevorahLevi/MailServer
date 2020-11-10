package com.example.mailserver.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Email
{
    private UUID from;          // Primary Key ID of sender
    private UUID to;            // Primary Key ID of recipient
    private String message;     // Message content
}