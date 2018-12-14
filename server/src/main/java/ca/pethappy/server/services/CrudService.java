package ca.pethappy.server.services;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, K> {
    List<T> findAll();
    Optional<T> findById(K id);
    T save(T t);
    void delete(K id);
}
