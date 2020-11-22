package com.example.mailserver.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data
public class ExternalUsers
{
    public static final HashMap<String, UUID> externalUserDatabase = new HashMap<>();

    public ExternalUsers()
    {
        //Creating some fake entries for testing purposes
        externalUserDatabase.put("testExternal", UUID.randomUUID());
    }
}
