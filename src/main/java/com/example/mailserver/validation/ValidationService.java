package com.example.mailserver.validation;

import com.example.mailserver.config.exception.InvalidApiKeyException;
import com.example.mailserver.config.properties.ExternalMailProperties;
import com.example.mailserver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final ExternalMailProperties externalMailProperties;

    private final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{12,20}$";

    public boolean passwordMatch(User user, String password) {
        return password.equals(user.getPassword());
    }

    public boolean validPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    public void validateApiKey(String apiKey) throws InvalidApiKeyException {
        if (!apiKey.equals(externalMailProperties.getApiKey())) {
            throw new InvalidApiKeyException();
        }
    }
}