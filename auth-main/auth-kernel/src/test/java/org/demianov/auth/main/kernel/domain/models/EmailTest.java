package org.demianov.auth.main.kernel.domain.models;

import org.demianov.auth.main.kernel.exceptions.base.WrongParametersException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link Email} ensuring data integrity and validation rules.
 */
public class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "user@domain.com",
            "first.last@sub.domain.org",
            "user_name@domain.co.at",
            "1234@number.net"
    })
    @DisplayName("Should create Email for valid forms")
    void constructor_Success(String email) {
        assertThatCode(() -> new Email(email)).doesNotThrowAnyException();
        assertThat(new Email(email).value()).isEqualTo(email);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "",
            "   ",
            "just_text",
            "@domain.just",
            "user@.com",
            "user@domain.",
            "user@domain@domain.com"
    })
    @DisplayName("Should throw WrongParametersException for invalid forms")
    void constructor_WrongParametersException(String email) {
        assertThatThrownBy(() -> new Email(email)).isInstanceOf(WrongParametersException.class).hasMessage("Invalid email format: " + email);
    }

    @Test
    @DisplayName("Should obey Value Object equality rules")
    void equals_Success() {
        // Given
        Email email1 = Email.of("test@example.com");
        Email email2 = Email.of("test@example.com");
        Email email3 = Email.of("other@example.com");

        // Then
        assertThat(email1).isEqualTo(email2);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
        assertThat(email1).isNotEqualTo(email3);
    }

    @Test
    @DisplayName("Should obey Value Object between factory and new")
    void staticFactory_of() {
        String val = "test@example.com";
        assertThat(Email.of(val)).isEqualTo(new Email(val));
    }
}
