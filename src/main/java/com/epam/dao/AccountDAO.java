package com.epam.dao;

import com.epam.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDAO {
    Optional<Account> get(Long id);

    List<Account> getAll();

    Account save(Account account);

    boolean delete(Long id);
}
