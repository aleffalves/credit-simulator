package com.github.aleffalves.credit_simulator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aleffalves.credit_simulator.domain.LoadSimulatorRequest;
import com.github.aleffalves.credit_simulator.service.LoadSimulatorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class LoadSimularControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoadSimulatorService loadSimulatorService;

    @InjectMocks
    private LoadSimulatorService loadSimulatorServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void simulate_ShouldReturnSuccessResponse_WhenInputIsValid() throws Exception {

        LocalDate dob = LocalDate.now().minusYears(30);
        LoadSimulatorRequest request = LoadSimulatorRequest.builder()
                .loanAmount(new BigDecimal(10000))
                .dateOfBirth(dob)
                .paymentTermMonths(12)
                .build();

        mockMvc.perform(post("/load-simulator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.monthlyInstallment").value(846.94))
                .andExpect(jsonPath("$.totalAmountPayable").value(10163.28))
                .andExpect(jsonPath("$.totalInterestPaid").value(163.28));
    }

    @Test
    void simulate_ShouldReturnBadRequest_WhenDateOfBirthIsInFuture() throws Exception {
        LocalDate futureDob = LocalDate.now().plusYears(1);
        LoadSimulatorRequest request = LoadSimulatorRequest.builder()
                .loanAmount(new BigDecimal(10000))
                .dateOfBirth(futureDob)
                .paymentTermMonths(12)
                .build();

        mockMvc.perform(post("/load-simulator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void simulate_ShouldReturnBadRequest_WhenLoanAmountIsNegative() throws Exception {
        LocalDate dob = LocalDate.now().minusYears(30);
        LoadSimulatorRequest request = LoadSimulatorRequest.builder()
                .loanAmount(new BigDecimal(-10000))
                .dateOfBirth(dob)
                .paymentTermMonths(12)
                .build();

        mockMvc.perform(post("/load-simulator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void simulate_ShouldReturnBadRequest_WhenPaymentTermIsZero() throws Exception {
        LocalDate dob = LocalDate.now().minusYears(30);
        LoadSimulatorRequest request = LoadSimulatorRequest.builder()
                .loanAmount(new BigDecimal(-10000))
                .dateOfBirth(dob)
                .paymentTermMonths(0)
                .build();

        mockMvc.perform(post("/load-simulator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
