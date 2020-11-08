package com.example.mailserver.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo
{
    private String userName;
    private String password;
}
