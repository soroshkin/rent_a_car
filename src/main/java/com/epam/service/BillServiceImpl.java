package com.epam.service;

import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import com.epam.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {
    private BillRepository repository;

    @Autowired
    public BillServiceImpl(BillRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Bill> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Bill> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Bill> findByUser(User user) {
        return repository.findAllByUser(user);
    }

    @Override
    public List<Bill> findByCar(Car car) {
        return repository.findByCar(car);
    }

    @Override
    public Bill save(Bill bill) {
        return repository.save(bill);
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
