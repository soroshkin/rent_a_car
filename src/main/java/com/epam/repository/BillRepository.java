package com.epam.repository;

import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;

import java.util.List;
import java.util.Optional;

public interface BillRepository {
    Optional<Bill> findById(Long id);

    List<Bill> findAll();

    List<Bill> findAllByUser(User user);

    List<Bill> findAllByCar(Car car);

    Bill save(Bill bill);

    void deleteById(Long id);

    boolean existsById(Long id);
}
