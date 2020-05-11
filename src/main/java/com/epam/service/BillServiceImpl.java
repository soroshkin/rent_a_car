package com.epam.service;

import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import com.epam.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Comparator;
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

    private final Comparator<Bill> billsComparator = Comparator
            .comparing(Bill::getDate)
            .thenComparing(bill -> bill.getUser().getEmail())
            .thenComparing(bill -> bill.getCar().getModel());

    @Override
    public List<Bill> findAll() {
        List<Bill> bills = repository.findAll();
        bills.sort(billsComparator);
        return bills;
    }

    @Override
    public List<Bill> findAllByUser(User user) {
        List<Bill> bills = repository.findAllByUser(user);
        bills.sort(billsComparator);
        return bills;
    }

    @Override
    public List<Bill> findAllByCar(Car car) {
        List<Bill> bills = repository.findAllByCar(car);
        bills.sort(billsComparator);
        return bills;
    }

    @Override
    public Bill save(Bill bill) {
        Assert.notNull(bill, "Bill must not be null");
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
