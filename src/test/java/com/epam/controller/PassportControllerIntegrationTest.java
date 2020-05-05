package com.epam.controller;

import com.epam.config.WebConfig;
import com.epam.exception.RestResponseEntityExceptionHandler;
import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.service.PassportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public class PassportControllerIntegrationTest {
    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    private ObjectMapper objectMapper;
    private User user = new User("some@email.ru", LocalDate.now());
    private MockMvc mockMvc;


    @Mock
    private PassportService service;

    @InjectMocks
    private PassportRestController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        objectMapper = converter.getObjectMapper();
    }

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
    public void getByIdIfNotValidShouldThrowNotFoundExceptionAndReturn400() throws Exception {
        when(service.findAllByUser(Matchers.any(User.class))).thenThrow(new IllegalArgumentException("sdfs"));

        String userJSON = objectMapper.writeValueAsString(user);

        mockMvc.perform(get("/passports/byUser/1")
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
        when(service.save(Matchers.any(Passport.class))).thenReturn(passport);

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

        when(service.save(Matchers.any(Passport.class))).thenReturn(passport);

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

        when(service.save(Matchers.any(Passport.class))).thenReturn(passport);

        mockMvc.perform(post("/passports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(passportJSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void deletePassportShoulReturn200IfDeleteOk() throws Exception {
        when(service.existsById(anyLong())).thenReturn(true);

    mockMvc.perform(delete("/passports/1"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void deletePassportShoulReturn400IfNotFound() throws Exception {
        when(service.existsById(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/passports/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
