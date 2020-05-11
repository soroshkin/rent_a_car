package com.epam.repository.jpacontainer;

import com.epam.model.Account;
import com.epam.model.User;
import com.epam.repository.AccountRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static com.epam.config.Profiles.JPA_PROFILE;

@Profile(JPA_PROFILE)
@Repository
@Transactional(readOnly = true)
public class JpaContainerAccountRepositoryImpl implements AccountRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Account.class, id));
    }

    @Override
    public List<Account> findAll() {
        return entityManager.createNamedQuery(Account.GET_ALL, Account.class)
                .getResultList();
    }

    @Override
    public Optional<Account> findByUser(User user) {
        return Optional.ofNullable(entityManager
                .createNamedQuery(Account.GET_BY_USER, Account.class)
                .setParameter("user", user)
                .getSingleResult());
    }

    @Override
    @Transactional
    public Account save(Account account) {
        if (account.isNew()) {
            entityManager.persist(account);
            return account;
        } else {
            return entityManager.merge(account);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        entityManager.createNamedQuery(Account.DELETE)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public boolean existsById(Long id) {
        return !entityManager.createNamedQuery(Account.EXISTS)
                .setParameter("id", id)
                .getResultList().isEmpty();
    }
}
