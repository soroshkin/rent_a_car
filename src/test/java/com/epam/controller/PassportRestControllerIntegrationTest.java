package com.epam.controller;

import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.service.PassportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PassportRestControllerIntegrationTest {
    private User user = new User("some@email.ru", LocalDate.now());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassportService service;

    @Test
    public void getAllShouldReturn200AndCorrectJSONIfValid() throws Exception {
        Passport firstPassport = new Passport("1", "address1", "Semen", "Fedorov", user);
        Passport secondPassport = new Passport("2", "address2", "Semen", "Fedorov", user);

        when(service.findAll()).thenReturn(Arrays.asList(firstPassport, secondPassport));
        mockMvc.perform(get("/passports"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].address", is("address1")))
                .andExpect(jsonPath("$[1].address", is("address2")))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }


    @Test
    public void getByUserIfNotValidShouldThrowNotFoundExceptionAndReturn400() throws Exception {
        when(service.findAllByUser(any(User.class))).thenThrow(new IllegalArgumentException("message"));

        String userJSON = objectMapper.writeValueAsString(user);

        mockMvc.perform(get("/passports/byUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void updatePassportShouldReturn200IfValid() throws Exception {
        Passport passport = new Passport("1", "some address", "Semen", "Fedorov", user);
        String passportJSON = objectMapper.writeValueAsString(passport);

        when(service.existsById(anyLong())).thenReturn(true);
        when(service.save(any(Passport.class))).thenReturn(passport);

        mockMvc.perform(put("/passports/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(passportJSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("address", is("some address")))
                .andReturn();
    }

    @Test
    public void createPassportShouldReturn200IfValid() throws Exception {
        Passport passport = new Passport("1", "some address", "Semen", "Fedorov", user);
        String passportJSON = objectMapper.writeValueAsString(passport);

        when(service.save(any(Passport.class))).thenReturn(passport);

        mockMvc.perform(post("/passports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(passportJSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("address", is("some address")))
                .andReturn();
    }

    @Test
    public void createPassportShouldReturn400IfPassportNull() throws Exception {
        Passport passport = null;
        String passportJSON = objectMapper.writeValueAsString(passport);

        when(service.save(any(Passport.class))).thenReturn(passport);

        mockMvc.perform(post("/passports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(passportJSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void deletePassportShouldReturn200IfDeleteOk() throws Exception {
        when(service.existsById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/passports/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deletePassportShouldReturn400IfNotFound() throws Exception {
        when(service.existsById(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/passports/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
