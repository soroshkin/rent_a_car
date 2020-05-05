package com.epam.repository;

import com.epam.model.Account;
import com.epam.model.User;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findById(Long id);

    Optional<Account> findByUser(User user);

    List<Account> findAll();

    Account save(Account account);

    void deleteById(Long id);

    boolean existsById(Long id);
}
