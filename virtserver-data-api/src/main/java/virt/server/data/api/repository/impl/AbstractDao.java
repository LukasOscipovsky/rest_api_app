package virt.server.data.api.repository.impl;

import virt.server.entities.AbstractEntity;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractDao<T extends AbstractEntity> {

    private final Class<T> entityBeanType;

    public AbstractDao() {
        this.entityBeanType = ((Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public Optional<T> get(final long id) {
        return Optional.ofNullable(this.getEntityManager().find(this.entityBeanType, id));
    }

    public void save(final T t) {
        this.getEntityManager().persist(t);
        this.getEntityManager().flush();
    }

    public Optional<T> update(final T t) {
        final T existingEntity = this.getEntityManager().find(this.entityBeanType, t.getId());

        final Supplier<Optional<T>> supplier = () -> {
            final T merge = this.getEntityManager().merge(t);
            this.getEntityManager().flush();
            return Optional.of(merge);
        };

        return existingEntity == null ? Optional.empty() : supplier.get();
    }

    protected abstract EntityManager getEntityManager();
}
