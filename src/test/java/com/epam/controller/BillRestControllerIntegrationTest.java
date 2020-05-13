package com.epam.controller;

import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import com.epam.service.BillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
public class BillRestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Bill bill;
    private User user;
    private Car car;

    @MockBean
    private BillService billService;

    @BeforeEach
    void setUp() {
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

        when(billService.findAllByUser(any(User.class))).thenReturn(billList);
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

        when(billService.findAllByUser(any(User.class))).thenReturn(billList);
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
