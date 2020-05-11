package com.epam.repository.datajpa;

import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import com.epam.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.epam.config.Profiles.SPRING_DATA_PROFILE;

@Profile(SPRING_DATA_PROFILE)
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
    public List<Bill> findAllByCar(Car car) {
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
