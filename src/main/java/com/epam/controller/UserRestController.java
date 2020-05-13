package com.epam.controller;

import com.epam.model.User;
import com.epam.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserRestController {
    private static final String USER_NOT_FOUND = "user with id=%d not found";

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.of(Optional.ofNullable(userService.findAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Long id) throws NotFoundException {
        if (userService.existsById(id)) {
            return ResponseEntity.of(userService.findById(id));
        } else {
            throw new NotFoundException(String.format(USER_NOT_FOUND, id));
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody User user) throws NotFoundException {
        if (userService.existsById(id)) {
            return ResponseEntity.of(Optional.ofNullable(userService.save(user)));
        } else {
            throw new NotFoundException(String.format(USER_NOT_FOUND, id));
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody User user) {
        return ResponseEntity.of(Optional.ofNullable(
                userService.save(user)));
    }

    @DeleteMapping(path = "/{id}")
    public void deleteUser(
            @PathVariable(name = "id") Long id) throws NotFoundException {
        if (userService.existsById(id)) {
            userService.deleteById(id);
        } else {
            throw new NotFoundException(String.format(USER_NOT_FOUND, id));
        }
    }
}
