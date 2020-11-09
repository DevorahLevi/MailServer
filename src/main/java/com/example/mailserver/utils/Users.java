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
        UUID uuid = UUID.randomUUID();
        userDatabase.put(UUID.randomUUID(), new UserInfo("DevorahLevi", "password1"));
        UUID uuid1 = UUID.randomUUID();
        userDatabase.put(UUID.randomUUID(), new UserInfo("NechamaLevi", "password2"));
        UUID uuid2 = UUID.randomUUID();
        userDatabase.put(UUID.randomUUID(), new UserInfo("YehudaLevi", "password3"));
        UUID uuid3 = UUID.randomUUID();
        userDatabase.put(UUID.randomUUID(), new UserInfo("MordechaiLevi", "password4"));
    }
}
