package com.epam.repository.datajpa;

import com.epam.model.User;
import com.epam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.epam.config.Profiles.SPRING_DATA_PROFILE;

@Repository
@Profile(SPRING_DATA_PROFILE)
public class DataJpaUserRepositoryImpl implements UserRepository {
    private CrudUserRepository crudUserRepository;

    @Autowired
    public DataJpaUserRepositoryImpl(CrudUserRepository crudUserRepository) {
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return crudUserRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(crudUserRepository.getByEmail(email));
    }

    @Override
    public List<User> findAll() {
        return crudUserRepository.findAll();
    }

    @Override
    public User save(User user) {
        return crudUserRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        crudUserRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return crudUserRepository.existsById(id);
    }
}

