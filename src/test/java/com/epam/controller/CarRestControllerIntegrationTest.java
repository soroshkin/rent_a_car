package com.epam.controller;

import com.epam.model.Car;
import com.epam.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CarRestControllerIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;
    private Car car;

    @MockBean
    private CarService carService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        car = new Car("Tatra", "W21", LocalDate.now(), 2000);
    }

    @Test
    public void getCarsShouldReturn200() throws Exception {
        List<Car> cars = Arrays.asList(car, car);
        when(carService.findAll()).thenReturn(cars);

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].model", is(car.getModel())))
                .andExpect(jsonPath("$[1].mileage", is(car.getMileage())))
                .andReturn();
    }

    @Test
    public void getCarsIfNoCarsFoundShouldReturnEmptyListAndStatus200() throws Exception {
        when(carService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getCarByIdShouldReturn200IfCarFound() throws Exception {
        when(carService.existsById(anyLong())).thenReturn(true);
        when(carService.findById(anyLong())).thenReturn(Optional.of(car));

        mockMvc.perform(get("/cars/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.model", is(car.getModel())))
                .andReturn();
    }

    @Test
    public void getCarByIdShouldReturn404IfCarNotFound() throws Exception {
        when(carService.existsById(anyLong())).thenReturn(false);

        mockMvc.perform(get("/cars/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void updateCarShouldReturn200IfValid() throws Exception {
        String carJSON = objectMapper.writeValueAsString(car);

        when(carService.existsById(anyLong())).thenReturn(true);
        when(carService.save(ArgumentMatchers.any(Car.class))).thenReturn(car);

        mockMvc.perform(put("/cars/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(carJSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("model", is(car.getModel())))
                .andReturn();
    }

    @Test
    public void createCarShouldReturn200IfValid() throws Exception {
        String carJSON = objectMapper.writeValueAsString(car);

        when(carService.save(ArgumentMatchers.any(Car.class))).thenReturn(car);

        mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(carJSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("model", is(car.getModel())))
                .andReturn();
    }

    @Test
    public void createCarShouldReturn400IfNotValid() throws Exception {
        String carJSON = objectMapper.writeValueAsString(null);

        when(carService.save(ArgumentMatchers.any(Car.class))).thenReturn(car);

        mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(carJSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void deleteCarShouldReturn200IfDeleteOk() throws Exception {
        when(carService.existsById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/cars/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deleteCarShouldReturn400IfNotFound() throws Exception {
        when(carService.existsById(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/cars/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}
