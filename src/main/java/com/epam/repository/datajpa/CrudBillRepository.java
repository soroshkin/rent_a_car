package com.epam.repository.datajpa;

import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrudBillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllByUser(User user);

    List<Bill> getByCar(Car car);
}
