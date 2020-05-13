package com.epam.controller;

import com.epam.model.Passport;
import com.epam.service.PassportService;
import com.epam.service.UserService;
import javassist.NotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JsonTestController {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PassportService passportService;

    @Test
    void getTestPage() throws Exception {
        mockMvc.perform(get("/json"))
                .andExpect(status().isOk())
                .andExpect(view().name("jsontest.html"))
                .andReturn();
    }

    @Autowired
    private UserService userService;

    @Test
    void getJSONFromDB() throws Exception {
        final String email = "email1@mail.ru";

        List<Passport> passportList = passportService.findAllByUser(
                userService.findByEmail(email).orElseThrow(
                        () -> new NotFoundException(String.format("user with email %s not found", email))));

        mockMvc.perform(get("/json/serialize")
        )
                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].passportNumber", Matchers.is(passportList.get(0).getPassportNumber())))
                .andExpect(jsonPath("$[1].passportNumber", Matchers.is(passportList.get(1).getPassportNumber())))
                .andExpect(jsonPath("$[0].address", Matchers.is(passportList.get(0).getAddress())))
                .andExpect(jsonPath("$[1].address", Matchers.is(passportList.get(1).getAddress())))
                .andExpect(jsonPath("$[0].surname", Matchers.is(passportList.get(0).getSurname())))
                .andExpect(jsonPath("$[1].surname", Matchers.is(passportList.get(1).getSurname())))
                .andReturn();
    }

    @Test
    void deserializeFromFile() throws Exception {
        final String email = "email1@mail.ru";
        List<Passport> passportList = passportService.findAllByUser(
                userService.findByEmail(email).orElseThrow(
                        () -> new NotFoundException(String.format("user with email %s not found", email))));

        mockMvc.perform(get("/json/deserialize"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(passportList.size())))
                .andExpect(jsonPath("$[0].passportNumber", Matchers.is(passportList.get(0).getPassportNumber())))
                .andExpect(jsonPath("$[1].passportNumber", Matchers.is(passportList.get(1).getPassportNumber())))
                .andExpect(jsonPath("$[0].address", Matchers.is(passportList.get(0).getAddress())))
                .andExpect(jsonPath("$[1].address", Matchers.is(passportList.get(1).getAddress())))
                .andExpect(jsonPath("$[0].surname", Matchers.is(passportList.get(0).getSurname())))
                .andExpect(jsonPath("$[1].surname", Matchers.is(passportList.get(1).getSurname())))
                .andReturn();
    }
}