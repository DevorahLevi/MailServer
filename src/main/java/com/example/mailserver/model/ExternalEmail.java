package com.example.mailserver.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExternalEmail
{
    private String from;    // sender user
    private String to;      // recipient user
    private String message;
}