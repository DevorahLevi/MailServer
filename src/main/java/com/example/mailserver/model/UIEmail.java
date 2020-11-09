package com.example.mailserver.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UIEmail
{
    private String from;
    private String to;
    private String message;
}
