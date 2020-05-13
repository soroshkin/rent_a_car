package com.epam.repository.datajpa;

import com.epam.model.Passport;
import com.epam.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrudPassportRepository extends JpaRepository<Passport, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<Passport> findAll();

    @EntityGraph(attributePaths = {"user"})
    List<Passport> findAllByUser(User user);
}