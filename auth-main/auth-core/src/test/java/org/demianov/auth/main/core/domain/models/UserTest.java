package org.demianov.auth.main.core.domain.models;

import org.demianov.auth.main.core.application.ports.out.security.PasswordHasherPort;
import org.demianov.auth.main.core.exceptions.PasswordMismatchException;
import org.demianov.auth.main.core.exceptions.UserAccountLockedException;
import org.demianov.auth.main.core.exceptions.UserRoleInvalidException;

import org.demianov.auth.main.kernel.application.models.UserSummary;
import org.demianov.auth.main.kernel.domain.models.Email;
import org.demianov.auth.main.kernel.domain.models.UserStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    private PasswordHasherPort passwordHasher;

    private Clock fixedClock;
    private final UUID id = UUID.randomUUID();
    private final Email email = new Email("max.mustermann@demianov.org");
    private final String hashedPass = "hashedPassword";

    @BeforeEach
    void setUp(){
        this.fixedClock = Clock.fixed(Instant.parse("2026-03-26T10:00:00Z"), ZoneId.of("UTC"));
    }

    @Test
    @DisplayName("Should authenticate successfully and record login time")
    void authenticate_Success() {
        User user = createUser();
        when(this.passwordHasher.verify("rawPassword", this.hashedPass)).thenReturn(true);

        user.authenticate("rawPassword", this.passwordHasher, this.fixedClock);

        verify(this.passwordHasher).verify("rawPassword", this.hashedPass);
    }

    @Test
    @DisplayName("Should throw PasswordMismatchException when password is incorrect")
    void authenticate_PasswordMismatch() {
        User user = createUser();
        when(this.passwordHasher.verify("rawPassword", this.hashedPass)).thenReturn(false);

        assertThatThrownBy(() -> user.authenticate("rawPassword", this.passwordHasher, this.fixedClock))
                .isInstanceOf(PasswordMismatchException.class);
    }

    @Test
    @DisplayName("Should throw UserAccountLockedException when user is not active")
    void authenticate_UserAccountLocked() {
        User user = createUser();
        User blockedUser = user.block();

        assertThatThrownBy(() -> blockedUser.authenticate("rawPassword", this.passwordHasher, this.fixedClock))
                .isInstanceOf(UserAccountLockedException.class);
    }

    @Test
    @DisplayName("Should throw when blocking blocked user")
    void block_UserAccountLocked() {
        User user = createUser();
        User blockedUser = user.block();

        assertThatThrownBy(blockedUser::block)
                .isInstanceOf(UserAccountLockedException.class);
    }


    @Test
    @DisplayName("Should block user")
    void block_Success() {
        User user = createUser();
        User blockedUser = user.block();

        assertThat(blockedUser.getStatus()).isEqualTo(UserStatus.BLOCKED);
    }

    @Test
    @DisplayName("Should add and remove roles correctly")
    void addAndRemoveRoles_Success() {
        User user = createUser();
        String newRole = "newRole";

        User addedRoleUser = user.addRole(newRole);
        assertThat(addedRoleUser.getRoles()).contains(newRole);

        User removedRoleUser = user.removeRole(newRole);
        assertThat(removedRoleUser.getRoles()).doesNotContain(newRole);
    }

    @Test
    @DisplayName("Should not allow change roles")
    void mutateRoles_Forbidden() {
        User user = createUser();

        assertThatThrownBy(() -> user.getRoles().add("newRole"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Should throw UserRoleInvalidException for null role")
    void assignNullRole_UserRoleInvalidException() {
        User user = createUser();
        assertThatThrownBy(() -> user.addRole(null))
                .isInstanceOf(UserRoleInvalidException.class);
    }

    @Test
    @DisplayName("Should correctly rehydrate user from component")
    void rehydrate_Success() {
        Set<String> roles = Set.of("role1", "role2");
        LocalDateTime lastLogin = LocalDateTime.now().minusDays(1);

        User user = User.rehydrate(this.id, this.email, this.hashedPass, UserStatus.ACTIVE, roles, lastLogin);

        assertThat(user.getId()).isEqualTo(this.id);
        assertThat(user.getEmail()).isEqualTo(this.email);
        assertThat(user.getPasswordHash()).isEqualTo(this.hashedPass);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getRoles()).isEqualTo(roles);
    }

    @Test
    @DisplayName("Should preserve data integrity in Summary")
    void toSummary_Success() {
        User user = createUser();

        UserSummary summary = user.toSummary();

        assertThat(summary.id()).isEqualTo(this.id);
        assertThat(summary.email()).isEqualTo(this.email);
        assertThat(summary.status()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should be active for UserStatus.ACTIVE")
    void isActive_Active_True() {
        User user = createUser();

        assertThat(user.isActive()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"PENDING", "BLOCKED"})
    @DisplayName("Should not be active for UserStatus.PENDING or UserStatus.BLOCKED")
    void isActive_Inactive_False(UserStatus status) {
        User user = User.builder(this.id, this.email, this.hashedPass).status(status).build();

        assertThat(user.isActive()).isFalse();
    }

    @Test
    @DisplayName("Should correctly create user from builder")
    void builder_Success() {
        User user = createUser();

        assertThat(user.getId()).isEqualTo(this.id);
        assertThat(user.getEmail()).isEqualTo(this.email);
        assertThat(user.getPasswordHash()).isEqualTo(this.hashedPass);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getRoles()).isNotNull();
    }

    @Test
    @DisplayName("Should return error for null values")
    void builder_NullValues() {
        assertThatThrownBy(() -> User.builder(null, this.email, this.hashedPass))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User id is required");
        assertThatThrownBy(() -> User.builder(this.id, null, this.hashedPass))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User email is required");
        assertThatThrownBy(() -> User.builder(this.id, this.email, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User password hash is required");
    }

    @Test
    @DisplayName("Should have UserStatus.PENDING by default")
    void builder_DefaultStatus() {
        User user = User.builder(this.id, this.email, this.hashedPass).build();
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    @DisplayName("Should preserve UserStatus when set in builder")
    void builder_SetStatus() {
        User user = User.builder(this.id, this.email, this.hashedPass).status(UserStatus.BLOCKED).build();
        assertThat(user.getStatus()).isEqualTo(UserStatus.BLOCKED);
    }

    @Test
    @DisplayName("Should set status to PENDING when null is passed")
    void builder_NullStatus() {
        User user = User.builder(this.id, this.email, this.hashedPass).status(null).build();
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    @DisplayName("Should set status to PENDING when not set")
    void builder_PendingStatus() {
        User user = User.builder(this.id, this.email, this.hashedPass).build();
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    @DisplayName("Should set roles to empty set when not set")
    void builder_EmptyRoles() {
        User user = User.builder(this.id, this.email, this.hashedPass).build();
        assertThat(user.getRoles()).isNotNull();
        assertThat(user.getRoles()).isEmpty();
    }

    @Test
    @DisplayName("Should set roles to empty set when null")
    void builder_NullRoles() {
        User user = User.builder(this.id, this.email, this.hashedPass).roles(null).build();
        assertThat(user.getRoles()).isNotNull();
        assertThat(user.getRoles()).isEmpty();
    }

    @Test
    @DisplayName("Should return same roles when removing non-existent role")
    void removeRole_NonExistentRole() {
        User user = User.builder(this.id, this.email, this.hashedPass).roles(Set.of("ADMIN")).build();
        User result = user.removeRole("USER");
        assertThat(result.getRoles()).isEqualTo(user.getRoles());
    }

    @Test
    @DisplayName("Should return same object if role already exist")
    void addRole_ExistingRole() {
        User user = User.builder(this.id, this.email, this.hashedPass).roles(Set.of("ADMIN")).build();
        User result = user.addRole("ADMIN");
        assertThat(result).isSameAs(user);
    }

    @Test
    @DisplayName("Should not fail when removing from empty list")
    void addRole_NonExistentRole() {
        User user = createUser();

        user.removeRole("USER");
    }

    private User createUser() {
        return User.builder(this.id, this.email, this.hashedPass)
                .status(UserStatus.ACTIVE)
                .roles(new HashSet<>())
                .build();
    }
}
