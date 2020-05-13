package com.epam.controller;

import com.epam.model.User;
import com.epam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping(path = "/")
public class RootController {
    private UserService userService;

    @Autowired
    public RootController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getRootPage() {
        return "index";
    }

    @GetMapping(path = "/createTestUsers")
    public String createTestUsers() {
        for (int i = 0; i < 50; i++) {
            userService.save(new User(String.format("newemail%s@domain.com", i),
                    LocalDate.now().minus(5, ChronoUnit.YEARS)));
        }
        return "index";
    }
}
