package com.example.mailserver.utils;

import com.example.mailserver.model.UserInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data
public class Users
{
    public final HashMap<String, UserInfo> userDatabase = new HashMap<>();

    public Users()
    {
        //Creating some fake entries for testing purposes
        userDatabase.put("DevorahLevi", new UserInfo("DevorahLevi", "password1", UUID.randomUUID()));
        userDatabase.put("NechamaLevi", new UserInfo("NechamaLevi", "password2", UUID.randomUUID()));
        userDatabase.put("YehudaLevi", new UserInfo("YehudaLevi", "password3", UUID.randomUUID()));
        userDatabase.put("MordechaiLevi", new UserInfo("MordechaiLevi", "password4", UUID.randomUUID()));
    }
}
