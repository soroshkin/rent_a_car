package com.epam.repository.datajpa;

import com.epam.model.Account;
import com.epam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrudAccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUser(User user);
}
