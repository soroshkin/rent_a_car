package com.epam.repository.datajpa;

import com.epam.model.Account;
import com.epam.model.User;
import com.epam.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaAccountRepositoryImpl implements AccountRepository {
    private CrudAccountRepository repository;

    @Autowired
    public DataJpaAccountRepositoryImpl(CrudAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Account> findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public List<Account> findAll() {
        return repository.findAll();
    }

    @Override
    public Account save(Account account) {
        return repository.save(account);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
