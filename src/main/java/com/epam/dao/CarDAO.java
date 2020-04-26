package com.epam.dao;

import com.epam.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarDAO {
    Optional<Car> get(Long id);

    List<Car> getAll();

    Car save(Car car);

    boolean delete(Long id);
}
