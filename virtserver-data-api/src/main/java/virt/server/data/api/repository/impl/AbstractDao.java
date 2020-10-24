package virt.server.data.api.repository.impl;

import virt.server.entities.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractDao<T extends AbstractEntity> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> entityBeanType;

    public AbstractDao() {
        this.entityBeanType = ((Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public Optional<T> get(final long id) {
        return Optional.ofNullable(this.entityManager.find(this.entityBeanType, id));
    }

    public void save(final T t) {
        this.entityManager.persist(t);
        this.entityManager.flush();
    }

    public Optional<T> update(final T t) {
        final T existingEntity = this.entityManager.find(this.entityBeanType, t.getId());

        final Supplier<Optional<T>> supplier = () -> {
            final T merge = this.entityManager.merge(t);
            this.entityManager.flush();
            return Optional.of(merge);
        };

        return existingEntity == null ? Optional.empty() : supplier.get();
    }

}
