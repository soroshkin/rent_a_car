package com.epam.service;

import com.epam.model.Passport;
import com.epam.model.User;

import java.util.List;
import java.util.Optional;

public interface PassportService {
    Optional<Passport> findById(Long id);

    List<Passport> findAll();

    Passport save(Passport passport);

    void deleteById(Long id);

    List<Passport> findAllByUser(User user);

    boolean existsById(Long id);
}
