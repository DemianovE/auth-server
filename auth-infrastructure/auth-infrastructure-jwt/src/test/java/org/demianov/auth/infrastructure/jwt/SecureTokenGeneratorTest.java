package org.demianov.auth.infrastructure.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class SecureTokenGeneratorTest {

    @Test
    @DisplayName("Should generate a string using default constructor (32 bytes)")
    void generate_Success() {
        SecureTokenGenerator generator = new SecureTokenGenerator();
        String token = generator.generate();

        assertThat(token).isNotBlank();
        assertThat(token.length()).isEqualTo(43);
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 24, 64})
    @DisplayName("Should generate a string using custom constructor")
    void generate_Success_CustomByteLength(int byteLength) {
        SecureTokenGenerator generator = new SecureTokenGenerator(byteLength);
        String token = generator.generate();

        Base64.getUrlDecoder().decode(token);

        int expectedLength = (int) Math.ceil(byteLength * 8 / 6.0);
        assertThat(token.length()).isEqualTo(expectedLength);
    }

    @Test
    @DisplayName("Should generate unique strings on consecutive calls")
    void generate_Unique() {
        SecureTokenGenerator generator = new SecureTokenGenerator();
        Set<String> tokens = new HashSet<>();
        int iterations = 100;

        for (int i = 0; i < iterations; i++) {
            String token = generator.generate();
            tokens.add(token);
        }

        assertThat(tokens.size()).isEqualTo(iterations);
    }

    @Test
    @DisplayName("Should not contain non-URL safe characters")
    void generate_NonUrlSafe() {
        SecureTokenGenerator generator = new SecureTokenGenerator();
        String token = generator.generate();

        assertThat(token)
                .doesNotContain("+")
                .doesNotContain("/")
                .doesNotContain("=");
    }

}
