package atividade10.repository;

import java.util.List;
import java.util.Optional;

// André Vinícius Barros Macambira
public interface Repository<T, ID> {
    void save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void delete(ID id);
    boolean existsById(ID id);
}
