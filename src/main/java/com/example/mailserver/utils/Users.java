package com.example.mailserver.utils;

import com.example.mailserver.model.UserInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data
public class Users
{
    public HashMap<UserInfo, UUID> userDatabase;

    public Users()
    {
        this.userDatabase = new HashMap<>();

        //Creating some fake entries for testing purposes
        userDatabase.put(UserInfo.builder().userName("DevorahLevi").password("password1").build(), UUID.randomUUID());
        userDatabase.put(UserInfo.builder().userName("NechamaLevi").password("password2").build(), UUID.randomUUID());
        userDatabase.put(UserInfo.builder().userName("YehudaLevi").password("password3").build(), UUID.randomUUID());
        userDatabase.put(UserInfo.builder().userName("MordechaiLevi").password("password4").build(), UUID.randomUUID());

        /*
        //Printing out all values and keys for test:
        userDatabase.forEach((key, value) -> System.out.println(key + " : " + value)); */
    }
}
