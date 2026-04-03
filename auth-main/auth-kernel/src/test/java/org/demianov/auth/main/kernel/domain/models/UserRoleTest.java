package org.demianov.auth.main.kernel.domain.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRoleTest {

    @Test
    @DisplayName("Should contain all required user roles")
    void containsAllRoles() {
        List<String> names = Arrays.stream(UserRole.values())
                .map(UserRole::name)
                .toList();

        assertThat(names).containsExactlyInAnyOrder("USER", "ADMIN");
    }

    @ParameterizedTest
    @ValueSource(strings = {"USER", "ADMIN"})
    @DisplayName("Should resolve from code")
    void shouldResolveFromCode(String code) {
        UserRole role = UserRole.valueOf(code);
        assertThat(role).isNotNull();
        assertThat(role.name()).isEqualTo(code);
    }
}
