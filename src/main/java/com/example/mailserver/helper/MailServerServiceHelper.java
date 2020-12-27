package com.example.mailserver.helper;

import com.example.mailserver.model.UserInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Component
public class MailServerServiceHelper
{
    public UUID checkUserNameExists(String userName, HashMap<UUID, UserInfo> userDatabase)
    {
        UUID userPrimaryKey = null;
        Iterator userDatabaseIterator = userDatabase.entrySet().iterator();

        while (userDatabaseIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)userDatabaseIterator.next();
            UserInfo tempUserInfo = (UserInfo) mapElement.getValue();
            if (userName.equals(tempUserInfo.getUserName())) {
                userPrimaryKey = (UUID)mapElement.getKey();
            }
        }
        return userPrimaryKey;
    }
}
