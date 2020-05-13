package com.epam.service;

import com.epam.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {
    Optional<Car> findById(Long id);

    List<Car> findAll();

    Car save(Car car);

    void deleteById(Long id);

    boolean existsById(Long id);
}
