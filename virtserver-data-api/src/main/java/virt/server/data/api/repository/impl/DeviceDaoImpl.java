package virt.server.data.api.repository.impl;

import org.springframework.stereotype.Repository;
import virt.server.data.api.repository.Dao;
import virt.server.entities.Device;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Repository
public class DeviceDaoImpl implements Dao<Device> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Device> get(final long id) {
        return Optional.ofNullable(this.entityManager.find(Device.class, id));
    }

    @Override
    public List<Device> getAll() {
        final Query query = this.entityManager.createQuery("SELECT d DEVICE Device d");
        return query.getResultList();
    }

    @Override
    public void save(final Device device) {
        this.entityManager.persist(device);
        this.entityManager.flush();
    }

    @Override
    public Optional<Device> update(final Device device) {
        final Device existingDevice = this.entityManager.find(Device.class, device.getId());

        final Supplier<Optional<Device>> supplier = () -> {
            final Device merge = this.entityManager.merge(device);
            this.entityManager.flush();
            return Optional.of(merge);
        };

        return existingDevice == null ? Optional.empty() : supplier.get();
    }

    @Override
    public boolean delete(final long id) {
        if (this.get(id).isEmpty()) return false;

        final Query q = this.entityManager.createQuery("DELETE from DEVICE where id = :id");
        q.setParameter("id", id);
        q.executeUpdate();
        return true;
    }
}
