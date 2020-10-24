package virt.server.data.api.repository.impl;

import org.springframework.stereotype.Repository;
import virt.server.data.api.repository.Dao;
import virt.server.entities.Zone;

import javax.persistence.Query;
import java.util.List;

@Repository
public class ZoneDaoImpl extends AbstractDao<Zone> implements Dao<Zone> {

    @Override
    public List<Zone> getAll() {
        final Query query = this.entityManager.createQuery("select z from Zone z");
        return query.getResultList();
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
