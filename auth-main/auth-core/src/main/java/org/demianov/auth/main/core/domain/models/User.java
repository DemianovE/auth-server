package org.demianov.auth.main.core.domain.models;

import org.demianov.auth.main.core.exceptions.models.user.UserAccountException;
import org.demianov.auth.main.kernel.application.models.UserSummary;
import org.demianov.auth.main.kernel.domain.models.Email;
import org.demianov.auth.main.kernel.domain.models.UserStatus;

import org.demianov.auth.main.core.application.ports.out.security.PasswordHasherPort;
import org.demianov.auth.main.core.exceptions.models.user.PasswordMismatchException;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;

/**
 * User domain model representation.
 * <p>
 *     The model is implemented in the Wither pattern to avoid data corruption.
 *     As such, the object is immutable.
 * </p>
 * <p>
 *  The model represents <b>only</b> the base user data structure.
 *  For further possible extensions, please use separate models.
 * </p>
 *
 * @since 0.1.0-alpha
 */
public final class User {
    /** User id. */
    private final UUID id;
    /** User email. */
    private final Email email;
    /** User password hash. */
    private final String passwordHash;
    /** User status. */
    private final UserStatus status;
    /** User roles. */
    private final Set<String> roles;

    /**
     * Private constructor. Use the builder.
     * @param builder user builder.
     * @throws NullPointerException if any of the fields is null.
     */
    private User(final Builder builder) {
        this.id = Objects.requireNonNull(builder.id,
                "User id is required");
        this.email = Objects.requireNonNull(builder.email,
                "User email is required");
        this.passwordHash = Objects.requireNonNull(builder.passwordHash,
                "User password hash is required");
        this.status = builder.status;
        this.roles = Collections.unmodifiableSet(builder.roles);
    }

    /**
     * perform user authentication.
     * @param rawPassword raw password to authenticate.
     * @param passwordHasher password hasher.
     * @throws PasswordMismatchException invalid credentials.
     * @throws UserAccountException user account is locked.
     */
    public void authenticate(
            final String rawPassword,
            final PasswordHasherPort passwordHasher) {
        if (this.status != UserStatus.ACTIVE) {
            throw new UserAccountException("User is not active");
        }

        if (!passwordHasher.verify(rawPassword, this.passwordHash)) {
            throw new PasswordMismatchException();
        }
    }

    /**
     * Create a user summary from self.
     * @return user summary.
     */
    public UserSummary toSummary() {
        return new UserSummary(
                this.id,
                this.email,
                this.status
        );
    }

    /**
     * Check if the user is active.
     * @return true if the user is active, false otherwise.
     */
    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    /**
     * Rehydrate a user from its components.
     * @param id user id
     * @param email user email
     * @param passwordHash user password hash
     * @param status user status
     * @param roles user roles
     * @return rehydrated user.
     * @throws NullPointerException if any of the fields is null.
     */
    public static User rehydrate(
            final UUID id,
            final Email email,
            final String passwordHash,
            final UserStatus status,
            final Set<String> roles) {
        return User.builder(id, email, passwordHash)
                .status(status)
                .roles(new HashSet<>(roles))
                .build();
    }

    // -- Getters --

    /**
     * Get the user id.
     * @return user id.
     */
    public UUID getId() {
        return this.id;
    }

    /**
     * Get the user email.
     * @return user email.
     */
    public Email getEmail() {
        return this.email;
    }

    /**
     * Get the user password hash.
     * @return user password hash.
     */
    public String getPasswordHash() {
        return this.passwordHash;
    }

    /**
     * Get the user roles.
     * @return user roles.
     */
    public Set<String> getRoles() {
        return this.roles;
    }

    /**
     * Get the user status.
     * @return user status.
     */
    public UserStatus getStatus() {
        return this.status;
    }

    // -- Setters --

    /**
     * Add a role to the user. Returns a new user object.
     * <p>
     *     The process returns the same object if the role already exists.
     * </p>
     * @param role user role
     * @return updated user.
     * @throws UserAccountException if the role is null or already exists.
     */
    public User addRole(final String role) {
        if (this.roles.contains(role)) {
            return this;
        }
        if (role == null) {
            throw new UserAccountException("User role cannot be null");
        }

        Set<String> newRoles = new HashSet<>(this.roles);
        newRoles.add(role);

        return newBuilderWithStatus(newRoles);
    }

    /**
     * Remove a role from the user. Returns a new user object.
     * @param role user role
     * @return updated user.
     */
    public User removeRole(final String role) {
        Set<String> newRoles = new HashSet<>(this.roles);
        newRoles.remove(role);

        return newBuilderWithStatus(newRoles);
    }

    /**
     * Create a new user builder with a copy of the user, except for the roles.
     * @param newRoles new roles
     * @return new user builder.
     */
    private User newBuilderWithStatus(final Set<String> newRoles) {
        return User.builder(this.id, this.email, this.passwordHash)
                .status(this.status)
                .roles(newRoles)
                .build();
    }

    /**
     * Block the user. Changing the status to BLOCKED.
     * @return - blocked user.
     * @throws UserAccountException - user account is already blocked.
     */
    public User block() {
        if (this.status == UserStatus.BLOCKED) {
            throw new UserAccountException("User is already blocked");
        }

        return copy()
                .status(UserStatus.BLOCKED)
                .build();
    }

    // -- Builder --

    /**
     * Create a new user builder.
     * @param id user id
     * @param email user email
     * @param passwordHash user password hash
     * @return user builder.
     * @throws NullPointerException if any of the fields is null.
     */
    public static Builder builder(
            final UUID id,
            final Email email,
            final String passwordHash) {
        return new Builder(id, email, passwordHash);
    }

    /**
     * A private copy for the set methods.
     * @return user builder.
     */
    private Builder copy() {
        return new Builder(this.id, this.email, this.passwordHash)
                .status(this.status)
                .roles(this.roles);
    }

    /**
     * Custom user builder.
     * <p>
     *     Created to use the builder pattern.
     *     But also adapt it to the user specifics.
     * </p>
     * @since 0.1.0-alpha
     */
    public static final class Builder {
        /** User id. */
        private final UUID id;
        /** User email. */
        private final Email email;
        /** User password hash. */
        private final String passwordHash;
        /** User status. */
        private UserStatus status = UserStatus.PENDING;
        /** User roles. */
        private Set<String> roles = new HashSet<>();

        /**
         * Canonical constructor. Perform mandatory field validation.
         * @param idParam user id
         * @param emailParam user email
         * @param passwordHashParam user password hash
         *
         * @throws NullPointerException if any of the fields is null.
         */
        private Builder(
                final UUID idParam,
                final Email emailParam,
                final String passwordHashParam) {
            this.id = Objects.requireNonNull(idParam,
                    "User id is required");
            this.email = Objects.requireNonNull(emailParam,
                    "User email is required");
            this.passwordHash = Objects.requireNonNull(passwordHashParam,
                    "User password hash is required");
        }

        /**
         * Set the user status.
         * @param statusNew user status
         * @return user builder.
         */
        public Builder status(final UserStatus statusNew) {
            this.status = statusNew == null ? UserStatus.PENDING : statusNew;
            return this;
        }

        /**
         * Set the user roles.
         * @param rolesNew user roles
         * @return user builder.
         */
        public Builder roles(final Set<String> rolesNew) {
            this.roles = rolesNew != null ? rolesNew : new HashSet<>();
            return this;
        }

        /**
         * Build the user.
         * @return user.
         */
        public User build() {
            return new User(this);
        }
    }
}
