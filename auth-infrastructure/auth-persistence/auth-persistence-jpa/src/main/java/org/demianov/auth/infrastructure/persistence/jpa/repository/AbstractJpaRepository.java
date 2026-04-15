package org.demianov.auth.infrastructure.persistence.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.demianov.auth.infrastructure.persistence.jpa.entities.AbstractAuditEntity;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

/**
 * The implementation of default JPA operations.
 * <p>
 *     This repository is meant to be used for the JPA operations.
 *     It controls the default behaviour of JPA operations.
 * </p>
 * <p>
 *     This class is generic and can be used for any entity.
 * </p>
 * @param <T> entity class
 * @param <ID> entity id class
 * @since 0.1.0-alpha
 */
public abstract class AbstractJpaRepository<T extends AbstractAuditEntity, ID> {
    /** Entity manager. */
    @PersistenceContext
    private EntityManager entityManager;

    /** Entity class. */
    private final Class<T> entityClass;

    /**
     * Constructor. Injects entity class.
     * @param entityClassParam  entity class
     */
    public AbstractJpaRepository(
            final Class<T> entityClassParam) {
        this.entityClass = entityClassParam;
    }

    /**
     * Perform search operation by id.
     * @param id entity id.
     * @return resulting entity.
     */
    @Transactional
    public Optional<T> findById(final ID id) {
        return Optional.ofNullable(
                this.entityManager.find(this.entityClass, id));
    }

    /**
     * Perform save/update entity operations.
     * <p>
     *     For the performance the {@code #persist()} is used
     *     for creation when the entity {@code createdAt} is null.
     * </p>
     * @param entity entity to be saved or updated
     */
    @Transactional
    public void save(final T entity) {
        if (entity.getCreatedAt() == null) {
            this.entityManager.persist(entity);
        } else {
            this.entityManager.merge(entity);
        }

    }

    /**
     * Perform the delete operation by id.
     * <p>
     *     The deletion is performed via the proxy.
     *     To save on SELECT queries, the proxy is used.
     * </p>
     * @param id entity id
     */
    @Transactional
    public void delete(final ID id) {
        try {
            T proxy = this.entityManager.getReference(this.entityClass, id);
            this.entityManager.remove(proxy);
        } catch (EntityNotFoundException | PersistenceException ignored) {
        }
    }

    /**
     * Getter for EntityManager.
     * @return entity manager.
     */
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Getter for the testing purposes.
     * @return the entity class.
     */
    protected Class<T> getEntityClass() {
        return this.entityClass;
    }

    /**
     * Perform flush of cash and memory.
     */
    public void flush() {
        this.entityManager.flush();
    }
}
