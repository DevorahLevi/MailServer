package com.example.mailserver.validation;

import com.example.mailserver.config.exception.InvalidApiKeyException;
import com.example.mailserver.config.properties.ExternalMailProperties;
import com.example.mailserver.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidationServiceTest {

    @Mock
    ExternalMailProperties externalMailProperties;

    @InjectMocks
    ValidationService subject;

    @Test
    public void passwordMatch_returnsTrue_whenPasswordMatches() {
        User user = User.builder()
                .password("password")
                .build();

        boolean actual = subject.passwordMatch(user, "password");
        assertThat(actual).isTrue();
    }

    @Test
    public void passwordMatch_returnsFalse_whenPasswordDoesNotMatch() {
        User user = User.builder()
                .password("password")
                .build();

        boolean actual = subject.passwordMatch(user, "wrongPassword");
        assertThat(actual).isFalse();
    }

    @Test
    public void validPassword_returnsTrueWhenPasswordMatchesValidRegex() {
        boolean actual = subject.validPassword("ValidPassword#1");
        assertThat(actual).isTrue();
    }

    @Test
    public void validPassword_returnsFalseWhenPassword_hasTooFewCharacters() {
        boolean actual = subject.validPassword("ShortPass#1");
        assertThat(actual).isFalse();
    }

    @Test
    public void validPassword_returnsFalseWhenPassword_hasTooManyCharacters() {
        boolean actual = subject.validPassword("SuperLongInvalidPassword#1");
        assertThat(actual).isFalse();
    }

    @Test
    public void validPassword_returnsFalseWhenPassword_doesNotHaveCapitalLetter() {
        boolean actual = subject.validPassword("password-no-caps#1");
        assertThat(actual).isFalse();
    }

    @Test
    public void validPassword_returnsFalseWhenPassword_doesNotHaveNumericCharacter() {
        boolean actual = subject.validPassword("PasswordNoNumerics*");
        assertThat(actual).isFalse();
    }

    @Test
    public void validPassword_returnsFalseWhenPassword_doesNotHaveSpecialCharacter() {
        boolean actual = subject.validPassword("NoSpecialCharacters1");
        assertThat(actual).isFalse();
    }

    @Test
    public void validPassword_returnsFalseWhenPassword_hasWhiteSpace() {
        boolean actual = subject.validPassword("White Space #1");
        assertThat(actual).isFalse();
    }

    @Test
    public void validateApiKey() throws InvalidApiKeyException {
        when(externalMailProperties.getApiKey()).thenReturn("apiKey");

        subject.validateApiKey("apiKey");

        verify(externalMailProperties).getApiKey();
    }

    @Test
    public void validateApiKey_throwsInvalidApiException() {
        when(externalMailProperties.getApiKey()).thenReturn("apiKey");

        try {
            subject.validateApiKey("invalidApiKey");
            fail("Expected exception not thrown");
        } catch (InvalidApiKeyException e) {
            verify(externalMailProperties).getApiKey();
        }
    }
}