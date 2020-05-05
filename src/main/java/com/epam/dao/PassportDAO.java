package com.epam.dao;

import com.epam.model.Passport;

import java.util.List;
import java.util.Optional;

public interface PassportDAO {
    Optional<Passport> get(Long id);

    List<Passport> getAll();

    Passport save(Passport passport);

    boolean delete(Long id);
}
