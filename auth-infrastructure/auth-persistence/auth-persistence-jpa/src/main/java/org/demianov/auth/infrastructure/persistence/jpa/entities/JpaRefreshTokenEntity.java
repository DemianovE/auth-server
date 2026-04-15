package org.demianov.auth.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * JPA entity for the refresh token.
 * @since 0.1.0-alpha
 */
@Entity
@Table
@Getter
@Setter
public final class JpaRefreshTokenEntity extends AbstractAuditEntity {
    /** Refresh token id. */
    @Id
    private UUID id;

    /** Refresh token. */
    @Column(unique = true, nullable = false)
    private String token;

    /** User id. Pure id, to be user impl agnostic. */
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /** Expiry date. */
    private Instant expiry;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof JpaRefreshTokenEntity that)) {
            return false;
        }

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
