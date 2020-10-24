package virt.server.data.api.repository;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(long id);

    List<T> getAll();

    void save(T t);

    Optional<T> update(T t);

    boolean delete(long id);
}
