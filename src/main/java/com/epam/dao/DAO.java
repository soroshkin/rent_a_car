package com.epam.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> get(Long id);

    List<T> getAll();

    T save(T t);

    boolean delete(Long id);
}
