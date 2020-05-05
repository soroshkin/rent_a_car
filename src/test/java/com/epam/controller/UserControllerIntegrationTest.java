package com.epam.controller;

import com.epam.config.WebConfig;
import com.epam.exception.RestResponseEntityExceptionHandler;
import com.epam.model.User;
import com.epam.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public class UserControllerIntegrationTest {

    //    @Autowired
//    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestController userRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        mockMvc = MockMvcBuilders
                .standaloneSetup(userRestController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

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

    @Mock
    private User user;

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
}
