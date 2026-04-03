package org.demianov.auth.main.kernel.domain.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserStatusTest {

    @Test
    @DisplayName("Should contain all required user statuses")
    void containsAllStatuses() {
        List<String> names = Arrays.stream(UserStatus.values())
                .map(UserStatus::name)
                .toList();

        assertThat(names).containsExactlyInAnyOrder("ACTIVE", "BLOCKED", "PENDING");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ACTIVE", "BLOCKED", "PENDING"})
    @DisplayName("Should resolve from code")
    void shouldResolveFromCode(String code) {
        UserStatus status = UserStatus.valueOf(code);
        assertThat(status).isNotNull();
        assertThat(status.name()).isEqualTo(code);
    }
}
