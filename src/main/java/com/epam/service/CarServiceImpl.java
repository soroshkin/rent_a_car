package com.epam.service;

import com.epam.model.Car;
import com.epam.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {
    private CarRepository repository;

    @Autowired
    public CarServiceImpl(CarRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Car> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Car> findAll() {
        List<Car> cars = repository.findAll();
        cars.sort(Comparator.comparing(Car::getModel)
                .thenComparing(Car::getProductionDate)
                .thenComparing(Car::getRegistrationNumber));
        return cars;
    }

    @Override
    public Car save(Car car) {
        Assert.notNull(car, "Car must not be null");
        return repository.save(car);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
