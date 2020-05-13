package com.epam.service;

import com.epam.model.User;
import com.epam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        List<User> users = repository.findAll();
        users.sort(Comparator.comparing(User::getEmail));
        return users;
    }

    @Override
    @Transactional
    public User save(User user) {
        Assert.notNull(user, "user must not be null");
        User userFromDB;
        if (repository.existsById(user.getId())) {
            userFromDB = repository.findById(user.getId()).get();
            userFromDB.setEmail(user.getEmail());
            userFromDB.setDateOfBirth(user.getDateOfBirth());
        } else {
            userFromDB = new User(user.getEmail(), user.getDateOfBirth());
        }
        return repository.save(userFromDB);
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
