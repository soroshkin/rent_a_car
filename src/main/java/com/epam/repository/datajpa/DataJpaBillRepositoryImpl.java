package com.epam.repository.datajpa;

import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import com.epam.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaBillRepositoryImpl implements BillRepository {
    private CrudBillRepository crudBillRepository;

    @Autowired
    public DataJpaBillRepositoryImpl(CrudBillRepository crudBillRepository) {
        this.crudBillRepository = crudBillRepository;
    }

    @Override
    public Optional<Bill> findById(Long id) {
        return crudBillRepository.findById(id);
    }

    @Override
    public List<Bill> findAll() {
        return crudBillRepository.findAll();
    }

    @Override
    public List<Bill> findAllByUser(User user) {
        return crudBillRepository.findAllByUser(user);
    }

    @Override
    public List<Bill> findByCar(Car car) {
        return crudBillRepository.getByCar(car);
    }

    @Override
    public Bill save(Bill bill) {
        return crudBillRepository.save(bill);
    }

    @Override
    public void deleteById(Long id) {
        crudBillRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return crudBillRepository.existsById(id);
    }
}
