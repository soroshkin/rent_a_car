package com.epam.repository.datajpa;

import com.epam.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface CrudUserRepository extends JpaRepository<User, Long> {
    User getByEmail(@Param("email") String email);

    @EntityGraph(attributePaths = {"passports"})
    List<User> findAll();
}
