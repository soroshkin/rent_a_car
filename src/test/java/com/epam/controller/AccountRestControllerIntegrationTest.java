package com.epam.controller;

import com.epam.config.WebConfig;
import com.epam.exception.RestResponseEntityExceptionHandler;
import com.epam.model.Account;
import com.epam.model.User;
import com.epam.service.AccountService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = WebConfig.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class AccountRestControllerIntegrationTest {
    private User user;
    private Account account;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountRestController accountRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(accountRestController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        objectMapper = converter.getObjectMapper();
        user = new User("some@email.com", LocalDate.now());
        account = user.getAccount();
    }

    @Test
    public void getAccountsShouldReturn200() throws Exception {
        account.setDepositUSD(BigDecimal.valueOf(10.5));
        List<Account> accounts = Arrays.asList(account, account);
        when(accountService.findAll()).thenReturn(accounts);
        String d = objectMapper.writeValueAsString(accounts);

        mockMvc.perform(get("/accounts"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].depositUSD", is(account.getDepositUSD().doubleValue())))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getAccountByIdShouldReturn200IfFound() throws Exception {
        when(accountService.existsById(anyLong())).thenReturn(true);
        when(accountService.findById(anyLong())).thenReturn(Optional.of(account));

        mockMvc.perform(get("/accounts/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.depositUSD", notNullValue()))
                .andExpect(jsonPath("$.depositEUR", notNullValue()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getAccountByIdShouldReturn400IfNotFound() throws Exception {
        when(accountService.existsById(anyLong())).thenReturn(false);

        mockMvc.perform(get("/accounts/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void updateAccountShouldReturn200IfValid() throws Exception {
        String accountJSON = objectMapper.writeValueAsString(account);

        when(accountService.existsById(anyLong())).thenReturn(true);
        when(accountService.save(org.mockito.Matchers.any(Account.class))).thenReturn(account);

        mockMvc.perform(put("/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.depositUSD", notNullValue()))
                .andExpect(jsonPath("$.depositEUR", notNullValue()))
                .andReturn();
    }

    @Test
    public void createAccountShouldReturn200IfValid() throws Exception {
        String accountJSON = objectMapper.writeValueAsString(account);

        when(accountService.save(Matchers.any(Account.class))).thenReturn(account);

        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();
    }

    @Test
    public void createPassportShouldReturn400IfPassportNull() throws Exception {
        String accountJSON = objectMapper.writeValueAsString(null);

        when(accountService.save(Matchers.any(Account.class))).thenReturn(account);

        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void deletePassportShouldReturn200IfDeleteOk() throws Exception {
        when(accountService.existsById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/accounts/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deletePassportShouldReturn400IfNotFound() throws Exception {
        when(accountService.existsById(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/accounts/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
