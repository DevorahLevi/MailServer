package com.example.mailserver.utils;

import com.example.mailserver.model.UserInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data
public class Users
{
    public final HashMap<UUID, UserInfo> userDatabase = new HashMap<>();

    public Users()
    {
        //Creating some fake entries for testing purposes
        userDatabase.put(UUID.randomUUID(), new UserInfo("DevorahLevi", "password1"));
        userDatabase.put(UUID.randomUUID(), new UserInfo("NechamaLevi", "password2"));
        userDatabase.put(UUID.randomUUID(), new UserInfo("YehudaLevi", "password3"));
        userDatabase.put(UUID.randomUUID(), new UserInfo("MordechaiLevi", "password4"));
    }
}
