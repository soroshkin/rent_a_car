package com.epam.repository.datajpa;

import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.repository.PassportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.epam.config.Profiles.SPRING_DATA_PROFILE;

@Profile(SPRING_DATA_PROFILE)
@Repository
public class DataJpaPassportRepositoryImpl implements PassportRepository {
    private CrudPassportRepository crudPassportRepository;

    @Autowired
    public DataJpaPassportRepositoryImpl(CrudPassportRepository crudPassportRepository) {
        this.crudPassportRepository = crudPassportRepository;
    }

    @Override
    public Optional<Passport> findById(Long id) {
        return crudPassportRepository.findById(id);
    }

    @Override
    public List<Passport> findAll() {
        return crudPassportRepository.findAll();
    }

    @Override
    public List<Passport> findByUser(User user) {
        return crudPassportRepository.findAllByUser(user);
    }

    @Override
    public Passport save(Passport passport) {
        return crudPassportRepository.save(passport);
    }

    @Override
    public void deleteById(Long id) {
        crudPassportRepository.deleteById(id);
    }

    @Override
    public List<Passport> findAllByUser(User user) {
        return crudPassportRepository.findAllByUser(user);
    }

    @Override
    public boolean existsById(Long id) {
        return crudPassportRepository.existsById(id);
    }
}
