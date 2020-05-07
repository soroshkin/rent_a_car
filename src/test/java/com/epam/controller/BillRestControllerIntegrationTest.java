package com.epam.controller;

import com.epam.config.WebConfig;
import com.epam.exception.RestResponseEntityExceptionHandler;
import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import com.epam.service.BillService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class BillRestControllerIntegrationTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Bill bill;
    private User user;
    private Car car;

    @Mock
    private BillService billService;

    @InjectMocks
    private BillRestController billRestController;

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(billRestController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        objectMapper = converter.getObjectMapper();
        user = new User("some@email.ru", LocalDate.now());
        car = new Car("mazda", "1234", LocalDate.now(), 0);
        bill = new Bill(LocalDate.now(),
                BigDecimal.ZERO,
                user,
                car);
    }

    @Test
    public void getBillsByUserShouldReturn200AndCorrectJSONIfValid() throws Exception {
        List<Bill> billList = Arrays.asList(bill, bill);

        when(billService.findByUser(any(User.class))).thenReturn(billList);
        String userJSON = objectMapper.writeValueAsString(user);


        mockMvc.perform(get("/bills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].user.email", is(user.getEmail())))
                .andExpect(jsonPath("$[1].car.model", is(car.getModel())))
                .andReturn();
    }

    @Test
    public void getBillsByUserShouldReturn400IfUserNotFound() throws Exception {
        List<Bill> billList = Arrays.asList(bill, bill);

        when(billService.findByUser(any(User.class))).thenReturn(billList);
        String userJSON = objectMapper.writeValueAsString(null);

        mockMvc.perform(get("/bills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getBillByIdIfNullShouldThrowBadRequestAndReturn400() throws Exception {
        when(billService.existsById(anyLong())).thenReturn(true);
        when(billService.findById(anyLong())).thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/bills/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void getBillByIdIfNotExistsShouldThrowNotFoundExceptionAndReturn404() throws Exception {
        when(billService.existsById(anyLong())).thenReturn(false);

        mockMvc.perform(get("/bills/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    public void createBillShouldReturn200IfValid() throws Exception {
        when(billService.save(bill)).thenReturn(bill);
        String billJSON = objectMapper.writeValueAsString(bill);

        mockMvc.perform(post("/bills")
                .content(billJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.car.model", is(bill.getCar().getModel())))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void createBillShouldReturn400IfNotValid() throws Exception {
        when(billService.save(bill)).thenThrow(new IllegalArgumentException());
        String billJSON = objectMapper.writeValueAsString(bill);

        mockMvc.perform(post("/bills")
                .content(billJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void updateBillShouldReturn400IfNotValid() throws Exception {
        when(billService.existsById(anyLong())).thenReturn(true);
        when(billService.save(bill)).thenThrow(new IllegalArgumentException());
        String billJSON = objectMapper.writeValueAsString(bill);

        mockMvc.perform(put("/bills/1")
                .content(billJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void updateBillShouldReturn404IfNotFound() throws Exception {
        when(billService.existsById(anyLong())).thenReturn(false);
        String billJSON = objectMapper.writeValueAsString(bill);

        mockMvc.perform(put("/bills/1")
                .content(billJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void updateBillShouldReturn200IfValid() throws Exception {
        when(billService.existsById(anyLong())).thenReturn(true);
        when(billService.save(bill)).thenReturn(bill);
        String billJSON = objectMapper.writeValueAsString(bill);

        mockMvc.perform(put("/bills/1")
                .content(billJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email", is(user.getEmail())))
                .andReturn();
    }

    @Test
    public void deleteBillShouldReturn200IfDeleteOk() throws Exception {
        when(billService.existsById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/bills/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deleteBillShouldReturn400IfNotFound() throws Exception {
        when(billService.existsById(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/bills/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}
