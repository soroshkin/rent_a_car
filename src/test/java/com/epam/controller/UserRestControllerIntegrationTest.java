package com.epam.controller;

import com.epam.model.User;
import com.epam.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Mock
    private User user;

    @Test
    public void getUsersShouldReturnGivenUsers() throws Exception {
        String email = "user@mail.ru";
        LocalDate dateOfBirth = LocalDate.now();
        User user = new User(email, dateOfBirth);
        String emailSecondUser = "user@mail.ru";
        LocalDate dateOfBirthSecondUser = LocalDate.now();
        User secondUser = new User(emailSecondUser, dateOfBirthSecondUser);


        when(userService.findAll())
                .thenReturn(Arrays.asList(user, secondUser));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email", is(email)))
                .andExpect(jsonPath("$[1].email", is(emailSecondUser)))
                .andReturn();
    }

    @Test
    public void getByIdShouldReturnOkStatus() throws Exception {
        when(userService.findById(anyLong())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getByIdShouldReturnNotFound() throws Exception {
        when(userService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void updateUserShouldReturnNotFound() throws Exception {
        when(userService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    @Test
    public void updateUserIdNullShouldReturnBadRequest() throws Exception {
        when(userService.existsById(anyLong())).thenThrow(new IllegalArgumentException("id must not be null"));
        ObjectMapper objectMapper = converter.getObjectMapper();
        String userJSON = objectMapper.writeValueAsString(new User("some@email.com", LocalDate.now()));

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void deleteUserShouldReturn200() throws Exception {
        when(userService.existsById(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}