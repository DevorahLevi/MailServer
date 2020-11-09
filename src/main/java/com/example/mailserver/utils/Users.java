package com.example.mailserver.utils;

import com.example.mailserver.model.UserInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data
public class Users
{
    public HashMap<String, UserInfo> userDatabase;

    public Users()
    {
        this.userDatabase = new HashMap<>();

        //Creating some fake entries for testing purposes
        userDatabase.put("DevorahLevi", UserInfo
                .builder()
                .userName("DevorahLevi")
                .password("password1")
                .primaryKey(UUID.randomUUID())
                .build());

        userDatabase.put("NechamaLevi", UserInfo
                .builder()
                .userName("NechamaLevi")
                .password("password2")
                .primaryKey(UUID.randomUUID())
                .build());

        userDatabase.put("YehudaLevi", UserInfo
                .builder().userName("YehudaLevi")
                .password("password3")
                .primaryKey(UUID.randomUUID())
                .build());

        userDatabase.put("MordechaiLevi", UserInfo
                .builder().userName("MordechaiLevi")
                .password("password4")
                .primaryKey(UUID.randomUUID())
                .build());
    }
}
