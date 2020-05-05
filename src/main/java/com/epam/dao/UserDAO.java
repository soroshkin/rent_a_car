package com.epam.dao;

import com.epam.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> get(Long id);

    Optional<User> getByEmail(String email);

    List<User> getAll();

    User save(User user);

    boolean delete(Long id);
}
