package com.epam.controller;

import com.epam.config.WebConfig;
import com.epam.model.Passport;
import com.epam.service.PassportService;
import com.epam.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.epam.config.Profiles.JPA_PROFILE;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles(JPA_PROFILE)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JsonTestControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    private PassportService passportService;

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();
        objectMapper = converter.getObjectMapper();
    }

    @Test
    void getTestPage() throws Exception {
        mockMvc.perform(get("/json"))
                .andExpect(status().isOk())
                .andExpect(view().name("jsontest"))
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
        String entitiesJSON = objectMapper.writeValueAsString(passportList);

        mockMvc.perform(get("/json/serialize"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", is(entitiesJSON)))
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