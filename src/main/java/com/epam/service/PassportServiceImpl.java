package com.epam.service;

import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.repository.PassportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassportServiceImpl implements PassportService {
    private PassportRepository passportRepository;

    @Autowired
    public PassportServiceImpl(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    @Override
    public Optional<Passport> findById(Long id) {
        return passportRepository.findById(id);
    }

    @Override
    public List<Passport> findAll() {
        return passportRepository.findAll();
    }

    @Override
    public List<Passport> findAllByUser(User user) {
        return passportRepository.findAllByUser(user);
    }

    @Override
    public Passport save(Passport passport) {
        return passportRepository.save(passport);
    }

    @Override
    public void deleteById(Long id) {
        passportRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return passportRepository.existsById(id);
    }
}
