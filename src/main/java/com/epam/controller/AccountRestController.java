package com.epam.controller;

import com.epam.model.Account;
import com.epam.service.AccountService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/accounts")
public class AccountRestController {
    private static final String ACCOUNT_NOT_FOUND = "account with id=%d not found";

    private AccountService accountService;

    @Autowired
    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.of(Optional.of(accountService.findAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable(name = "id") Long id) throws NotFoundException {
        if (accountService.existsById(id)) {
            return ResponseEntity.of(accountService.findById(id));
        } else {
            throw new NotFoundException(String.format(ACCOUNT_NOT_FOUND, id));
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable(name = "id") Long id,
            @RequestBody Account account) throws NotFoundException {
        if (accountService.existsById(id)) {
            return ResponseEntity.of(Optional.of(accountService.save(account)));
        } else {
            throw new NotFoundException(String.format(ACCOUNT_NOT_FOUND, id));
        }
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.of(Optional.of(accountService.save(account)));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Long> deleteAccount(@PathVariable(name = "id") Long id) throws NotFoundException {
        if (accountService.existsById(id)) {
            accountService.deleteById(id);
            return ResponseEntity.of(Optional.of(id));
        } else {
            throw new NotFoundException(String.format(ACCOUNT_NOT_FOUND, id));
        }
    }
}
