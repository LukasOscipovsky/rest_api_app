package virt.server.data.api.repository;

import virt.server.entities.AbstractEntity;

import java.util.List;
import java.util.Optional;

/**
 * DAO performing DB operations on given Entity
 *
 * @author Lukas.Oscipovsky
 */
public interface Dao<T extends AbstractEntity> {

    /**
     * Return entity based on id
     */
    Optional<T> get(long id);

    /**
     * Return all entities from DB
     */
    List<T> getAll();

    /**
     * Save entity to DB
     */
    void save(T t);

    /**
     * Update entity in DB based on given type
     */
    Optional<T> update(T t);

    /**
     * Delete entity based on id
     */
    boolean delete(long id);
}
