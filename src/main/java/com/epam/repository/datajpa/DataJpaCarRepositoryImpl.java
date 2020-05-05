package com.epam.repository.datajpa;

import com.epam.model.Car;
import com.epam.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaCarRepositoryImpl implements CarRepository {
    private CrudCarRepository repository;

    @Autowired
    public DataJpaCarRepositoryImpl(CrudCarRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Car> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Car> findAll() {
        return repository.findAll();
    }

    @Override
    public Car save(Car car) {
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
