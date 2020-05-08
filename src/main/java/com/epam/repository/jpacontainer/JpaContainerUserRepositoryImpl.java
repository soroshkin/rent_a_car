package com.epam.repository.jpacontainer;

import com.epam.model.User;
import com.epam.repository.UserRepository;
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
public class JpaContainerUserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(entityManager
                .createNamedQuery(User.GET_BY_EMAIL, User.class)
                .setParameter("email", email)
                .getSingleResult());
    }

    @Override
    public List<User> findAll() {
        EntityGraph<?> graph = entityManager.getEntityGraph(User.GRAPH_USER_PASSPORTS);

        return entityManager.createNamedQuery(User.GET_ALL, User.class)
                .setHint("javax.persistence.fetchgraph", graph)
                .getResultList();
    }

    @Override
    @Transactional
    public User save(User user) {
        if (user.isNew()) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);

    }

    @Override
    public boolean existsById(Long id) {
        return !entityManager.createNamedQuery(User.EXISTS)
                .setParameter("id", id)
                .getResultList().isEmpty();
    }
}

