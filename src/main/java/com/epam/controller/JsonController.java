package com.epam.controller;

import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.service.PassportService;
import com.epam.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(path = "/json")
public class JsonController {
    private UserService userService;
    private PassportService passportService;
    private ObjectMapper objectMapper;

    @Autowired
    public JsonController(UserService userService,
                          PassportService passportService,
                          MappingJackson2HttpMessageConverter converter) {
        this.userService = userService;
        this.passportService = passportService;
        this.objectMapper = converter.getObjectMapper();
    }

    @GetMapping
    public String getTestPage() {
        return "jsontest";
    }

    @GetMapping(path = "/serialize")
    public @ResponseBody
    String getJSONFromDB() throws JsonProcessingException, NotFoundException {
        List<Passport> passports = passportService.findAllByUser(getUser());
        return objectMapper.writeValueAsString(passports);
    }

    private final String filePath = Objects.requireNonNull(Thread.currentThread()
            .getContextClassLoader()
            .getResource("."))
            .getPath()
            + "json.txt";

    @GetMapping(path = "/deserialize")
    public @ResponseBody
    ResponseEntity<List<Passport>> deserializeFromFile() throws IOException, NotFoundException {
        User user = getUser();
        List<Passport> passports = passportService.findAllByUser(user);
        objectMapper.writeValue(new File(filePath), passports);

        passports.forEach(passport -> passportService.deleteById(passport.getId()));

        List<Passport> passportsFromFile = objectMapper
                .readValue(new File(filePath), new TypeReference<List<Passport>>() {
                });
        passportsFromFile.forEach(passport -> passportService.save(passport));

        return ResponseEntity.of(Optional.ofNullable(passportService.findAllByUser(user)));
    }

    private User getUser() throws NotFoundException {
        String email = "email1@mail.ru";
        return userService.findByEmail(email).orElseThrow(() ->
                new NotFoundException(String.format("user with email=%s not found", email)));
    }
}
