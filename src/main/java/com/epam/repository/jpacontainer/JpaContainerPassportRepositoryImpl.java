package com.epam.repository.jpacontainer;

import com.epam.model.Bill;
import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.repository.PassportRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static com.epam.config.Profiles.JPA_PROFILE;

@Profile(JPA_PROFILE)
@Repository
@Transactional(readOnly = true)
public class JpaContainerPassportRepositoryImpl implements PassportRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Passport> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Passport.class, id));
    }

    @Override
    public List<Passport> findAll() {
        return entityManager
                .createNamedQuery(Passport.FIND_ALL, Passport.class)
                .getResultList();
    }

    @Override
    public List<Passport> findAllByUser(User user) {
        EntityGraph<?> graph = entityManager.getEntityGraph(Passport.GRAPH_PASSPORT_USER);

        return entityManager
                .createNamedQuery(Passport.FIND_BY_USER, Passport.class)
                .setHint("javax.persistence.fetchgraph", graph)
                .setParameter("userId", user.getId())
                .getResultList();
    }

    @Override
    public List<Passport> findByUser(User user) {
        EntityGraph<?> graph = entityManager.getEntityGraph(Passport.GRAPH_PASSPORT_USER);

        return entityManager.createNamedQuery(Bill.GET_BY_USER, Passport.class)
                .setHint("javax.persistence.fetchgraph", graph)
                .setParameter("userId", user.getId())
                .getResultList();
    }

    @Override
    @Transactional
    public Passport save(Passport passport) {
        if (passport.isNew()) {
            entityManager.persist(passport);
            return passport;
        } else {
            return entityManager.merge(passport);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        entityManager
                .createNamedQuery(Passport.DELETE)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public boolean existsById(Long id) {
        return !entityManager.createQuery(Passport.EXISTS)
                .setParameter("id", id)
                .getResultList().isEmpty();
    }
}
