package com.example.mailserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MailServerApplication
{
	@Bean
	public RestTemplate restTemplate() { return new RestTemplate(); }

	public static void main(String[] args) {
		SpringApplication.run(MailServerApplication.class, args);
	}
}

/*
Homework Assignment:
API endpoint that can receive external mail and puts it into the correct person's inbox
Send function (might not need a new endpoint), just needs to have awareness that if the 'to' is not in your local users, you have to send it externally
(IP) Address of external server must be in the configurations
Ability to send externally must have a feature switch around it to turn it on or off
 */