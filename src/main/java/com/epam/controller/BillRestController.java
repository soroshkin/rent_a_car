package com.epam.controller;

import com.epam.model.Bill;
import com.epam.model.User;
import com.epam.service.BillService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/bills")
public class BillRestController {
    private static final String BILL_NOT_FOUND = "bill with id=%d not found";
    private BillService billService;

    @Autowired
    public BillRestController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getBillsByUser(@RequestBody User user) {
        return ResponseEntity.of(Optional.of(billService.findAllByUser(user)));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable(name = "id") Long id) throws NotFoundException {
        if (billService.existsById(id)) {
            return ResponseEntity.of(billService.findById(id));
        } else {
            throw new NotFoundException(String.format(BILL_NOT_FOUND, id));
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable(name = "id") Long id,
                                           @RequestBody Bill bill) throws NotFoundException {
        if (billService.existsById(id)) {
            return ResponseEntity.of(Optional.of(billService.save(bill)));
        } else {
            throw new NotFoundException(String.format(BILL_NOT_FOUND, id));
        }
    }

    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        return ResponseEntity.of(Optional.of(billService.save(bill)));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Long> deleteBill(@PathVariable(name = "id") Long id) throws NotFoundException {
        if (billService.existsById(id)) {
            billService.deleteById(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } else {
            throw new NotFoundException(String.format(BILL_NOT_FOUND, id));
        }
    }
}
