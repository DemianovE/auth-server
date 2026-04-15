package org.demianov.auth.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * The default audit entity.
 * <p>
 *     The entity is used to store audit data in the database.
 *     It is mapped to the {@code audits} table.
 * </p>
 * @since 0.1.0-alpha
 */
@MappedSuperclass
@Getter
public abstract class AbstractAuditEntity {

    /** Created at. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Updated at. */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Perform pre-persist operation. Updates the created at field.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Perform pre-update operation. Updates the updated at field.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
