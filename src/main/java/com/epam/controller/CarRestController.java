package com.epam.controller;

import com.epam.model.Car;
import com.epam.service.CarService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/cars")
public class CarRestController {
    private static final String CAR_NOT_FOUND = "car with id=%d not found";

    private CarService carService;

    @Autowired
    public CarRestController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCars() {
        return ResponseEntity.of(Optional.of(carService.findAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable(name = "id") Long id) throws NotFoundException {
        if (carService.existsById(id)) {
            return ResponseEntity.of(carService.findById(id));
        } else {
            throw new NotFoundException(String.format(CAR_NOT_FOUND, id));
        }
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Car> updateCar(@PathVariable(name = "id") Long id,
                                         @RequestBody Car car) throws NotFoundException {
        if (carService.existsById(id)) {
            return ResponseEntity.of(Optional.of(carService.save(car)));
        } else {
            throw new NotFoundException(String.format(CAR_NOT_FOUND, id));
        }
    }

    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        return ResponseEntity.of(Optional.of(carService.save(car)));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Long> deleteCarById(@PathVariable(name = "id") Long id) throws NotFoundException {
        if (carService.existsById(id)) {
            carService.deleteById(id);
            return ResponseEntity.of(Optional.of(id));
        } else {
            throw new NotFoundException(String.format(CAR_NOT_FOUND, id));
        }
    }
}
