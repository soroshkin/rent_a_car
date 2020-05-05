package com.epam.repository.datajpa;

import com.epam.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrudCarRepository extends JpaRepository<Car, Long> {
}
