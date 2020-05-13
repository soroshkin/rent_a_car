package com.epam.repository;

import com.epam.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarRepository {
    Optional<Car> findById(Long id);

    List<Car> findAll();

    Car save(Car car);

    void deleteById(Long id);

    boolean existsById(Long id);
}
