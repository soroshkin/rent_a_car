package com.epam.dao;

import com.epam.model.Bill;

import java.util.List;
import java.util.Optional;

public interface BillDAO {
    Optional<Bill> get(Long id);

    List<Bill> getAll();

    Bill save(Bill bill);

    boolean delete(Long id);
}
