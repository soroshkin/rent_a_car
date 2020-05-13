package com.epam.service;

import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.repository.PassportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Comparator;
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

    private final Comparator<Passport> passportComparator = Comparator
            .comparing(Passport::getSurname)
            .thenComparing(Passport::getName)
            .thenComparing(Passport::getPassportNumber);

    @Override
    public List<Passport> findAll() {
        List<Passport> passports = passportRepository.findAll();
        passports.sort(passportComparator);
        return passports;
    }

    @Override
    public List<Passport> findAllByUser(User user) {
        List<Passport> passports = passportRepository.findAllByUser(user);
        passports.sort(passportComparator);
        return passports;
    }

    @Override
    public Passport save(Passport passport) {
        Assert.notNull(passport, "passport must not be null");
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
