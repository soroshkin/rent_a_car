package com.epam.controller;

import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.service.PassportService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/passports")
public class PassportRestController {
    private static final String PASSPORT_NOT_FOUND = "passport with id=%d not found";
    private PassportService passportService;

    @Autowired
    public PassportRestController(PassportService passportService) {
        this.passportService = passportService;
    }

    @GetMapping
    public ResponseEntity<List<Passport>> getPassports() {
        return ResponseEntity.of(Optional.of(passportService.findAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Passport> getById(@PathVariable(name = "id") Long id) throws NotFoundException {
        if (passportService.existsById(id)) {
            return ResponseEntity.of(passportService.findById(id));
        } else {
            throw new NotFoundException(String.format(PASSPORT_NOT_FOUND, id));
        }
    }

    @GetMapping(path = "/byUser")
    public ResponseEntity<List<Passport>> getByUser(@RequestBody User user) {
        return new ResponseEntity<>(passportService.findAllByUser(user), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Passport> updatePassport(
            @PathVariable(name = "id") Long id,
            @RequestBody Passport passport) throws NotFoundException {
        if (passportService.existsById(id)) {
            return ResponseEntity.of(Optional.ofNullable(passportService.save(passport)));
        } else {
            throw new NotFoundException(String.format(PASSPORT_NOT_FOUND, id));
        }
    }

    @PostMapping
    public ResponseEntity<Passport> createPassport(@RequestBody Passport passport) {
        return ResponseEntity.of(Optional.ofNullable(passportService.save(passport)));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Long> deletePassport(@PathVariable(name = "id") Long id) throws NotFoundException {
        if (passportService.existsById(id)) {
            passportService.deleteById(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } else {
            throw new NotFoundException(String.format(PASSPORT_NOT_FOUND, id));
        }
    }
}
