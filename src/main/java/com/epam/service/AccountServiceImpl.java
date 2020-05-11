package com.epam.service;

import com.epam.model.Account;
import com.epam.model.User;
import com.epam.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> findByUser(User user) {
        return accountRepository.findByUser(user);
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = accountRepository.findAll();
        accounts.sort(Comparator.comparing(Account::getDepositUSD)
                .thenComparing(Account::getDepositEUR));
        return accounts;
    }

    @Override
    public Account save(Account account) {
        Assert.notNull(account, "Account must not be null");
        return accountRepository.save(account);
    }

    @Override
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return accountRepository.existsById(id);
    }
}
