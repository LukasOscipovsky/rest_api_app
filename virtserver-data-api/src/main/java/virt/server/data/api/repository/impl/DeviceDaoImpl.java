package virt.server.data.api.repository.impl;

import org.springframework.stereotype.Repository;
import virt.server.data.api.repository.Dao;
import virt.server.entities.Device;

import javax.persistence.Query;
import java.util.List;

@Repository
public class DeviceDaoImpl extends AbstractDao<Device> implements Dao<Device> {

    @Override
    public List<Device> getAll() {
        final Query query = this.entityManager.createQuery("select d from Device d");
        return query.getResultList();
    }

    @Override
    public boolean delete(final long id) {
        if (this.get(id).isEmpty()) return false;

        final Query q = this.entityManager.createQuery("delete from Device where id = :id");
        q.setParameter("id", id);
        q.executeUpdate();
        return true;
    }
}
