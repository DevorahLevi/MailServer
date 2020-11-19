package com.example.mailserver.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties("feature-switch.send-external-mail")
public class FeatureSwitchSendExternalMailConfiguration
{
    /* Purpose of a feature switch is that you can turn parts of your code on or off without re-deploying/building the app.
        Just need to reboot it = much less time.
        Good for new features you want to try out or features you want to be able to turn on or off, or if you are not sure if it will work or not.
     */
//    private boolean printIp;
//    private boolean emailUp;

    private boolean sendExternalMailOn;
}
