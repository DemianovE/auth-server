package org.demianov.auth.infrastructure.crypto;
import org.demianov.auth.main.core.exceptions.ports.PasswordHasherPortException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BCryptPasswordHasherTest {
    private BCryptPasswordHasher hasher;

    @BeforeEach
    void setUp() {
        hasher = new BCryptPasswordHasher();
    }

    @Test
    @DisplayName("Should generate a non-empty hash that is different from the raw password")
    void hash_Success() {
        String raw = "raw";
        String hashed = hasher.hash(raw);

        assertThat(hashed).isNotNull();
        assertThat(hashed).isNotEqualTo(raw);
        assertThat(hashed).startsWith("$2a$");
    }

    @Test
    @DisplayName("Should generate different hashes for the same password (Salting check)")
    void hash_SaltingCheck() {
        String raw = "raw";
        String hashed1 = hasher.hash(raw);
        String hashed2 = hasher.hash(raw);

        assertThat(hashed1).isNotEqualTo(hashed2);
    }

    @Test
    @DisplayName("Should verify correct password against its hash")
    void verify_Success() {
        String raw = "raw";
        String hashed = hasher.hash(raw);

        assertThat(hasher.verify(raw, hashed)).isTrue();
    }

    @Test
    @DisplayName("Should always throw PasswordHasherPortException with cause.")
    void verify_NullInputs() {
        String hash = hasher.hash("raw");

        assertThatThrownBy(() -> hasher.verify(null, null)).isInstanceOf(PasswordHasherPortException.class);
        assertThatThrownBy(() -> hasher.verify(hash, null)).isInstanceOf(PasswordHasherPortException.class);
        assertThatThrownBy(() -> hasher.verify(null, hash)).isInstanceOf(PasswordHasherPortException.class);
    }
}
