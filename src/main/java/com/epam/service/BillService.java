package com.epam.service;

import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;

import java.util.List;
import java.util.Optional;

public interface BillService {
    Optional<Bill> findById(Long id);

    List<Bill> findAll();

    List<Bill> findByUser(User user);

    List<Bill> findByCar(Car car);

    Bill save(Bill bill);

    void deleteById(Long id);

    boolean existsById(Long id);
}
