package virt.server.data.api.repository.impl;

import org.springframework.stereotype.Repository;
import virt.server.data.api.repository.Dao;
import virt.server.entities.Zone;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Repository
public class ZoneDaoImpl implements Dao<Zone> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Zone> get(final long id) {
        return Optional.ofNullable(this.entityManager.find(Zone.class, id));
    }

    @Override
    public List<Zone> getAll() {
        final Query query = this.entityManager.createQuery("select z from Zone z");
        return query.getResultList();
    }

    @Override
    public void save(final Zone zone) {
        this.entityManager.persist(zone);
        this.entityManager.flush();
    }

    @Override
    public Optional<Zone> update(final Zone zone) {
        final Zone existingZone = this.entityManager.find(Zone.class, zone.getId());

        final Supplier<Optional<Zone>> supplier = () -> {
            final Zone merge = this.entityManager.merge(zone);
            this.entityManager.flush();
            return Optional.of(merge);
        };

        return existingZone == null ? Optional.empty() : supplier.get();
    }

    @Override
    public boolean delete(final long id) {
        if (this.get(id).isEmpty()) return false;

        final Query q = this.entityManager.createQuery("delete from Zone where id = :id");
        q.setParameter("id", id);
        q.executeUpdate();
        return true;
    }
}
