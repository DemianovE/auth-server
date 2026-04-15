package org.demianov.auth.infrastructure.persistence.jpa.entities;

import org.demianov.auth.main.kernel.domain.models.UserStatus;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;


/**
 * JPA entity for the user.
 * <p>
 *     The entity is used to store user data in the database.
 *     It is mapped to the {@code users} table.
 * </p>
 * @since 0.1.0-alpha
 */
@Entity
@Table
@Getter
@Setter
public final class JpaUserEntity extends AbstractAuditEntity {
    /** User id. */
    @Id
    private UUID id;

    /** User email. */
    @Column(unique = true, nullable = false)
    private String email;

    /** User password hash. */
    @Column(nullable = false)
    private String passwordHash;

    /** User status. */
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    /** Last login date. */
    private LocalDateTime lastLogin;

    /** User roles. Foreign key to the {@code roles} table. */
    @CollectionTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    private Set<String> roles = new HashSet<>();;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof JpaUserEntity that)) {
            return false;
        }

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
