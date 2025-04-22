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

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class LoadSimulatorControllerTests {

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

    @Test
    void shouldProcess_1000Simulations_InLessThan_TwoSeconds() throws Exception {
        int numSimulations = 1000;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numSimulations; i++) {
            LocalDate dob = LocalDate.now().minusYears(20 + (i % 60));
            BigDecimal loanAmount = BigDecimal.valueOf(10000 + (i * 100));
            int term = 12 + (i % 48);

            LoadSimulatorRequest request = LoadSimulatorRequest.builder()
                    .loanAmount(loanAmount)
                    .dateOfBirth(dob)
                    .paymentTermMonths(term)
                    .build();

            mockMvc.perform(post("/load-simulator")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)));
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        assertTrue(duration < 2000, "O processamento de 1.000 simulações levou mais de 2 segundos: " + duration + "ms");
    }

    @Test
    void shouldProcess_10000Simulations_InLessThan_FiveSeconds() throws Exception {
        int numSimulations = 10000;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numSimulations; i++) {
            LocalDate dob = LocalDate.now().minusYears(20 + (i % 60));
            BigDecimal loanAmount = BigDecimal.valueOf(10000 + (i * 100));
            int term = 12 + (i % 48);

            LoadSimulatorRequest request = LoadSimulatorRequest.builder()
                    .loanAmount(loanAmount)
                    .dateOfBirth(dob)
                    .paymentTermMonths(term)
                    .build();

            mockMvc.perform(post("/load-simulator")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        assertTrue(duration < 5000, "O processamento de 10.000 simulações levou mais de 5 segundos: " + duration + "ms");
    }

    @Test
    void shouldProcess_100000Simulations_InLessThan_TenSeconds() throws Exception {
        int numSimulations = 100000;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numSimulations; i++) {
            LocalDate dob = LocalDate.now().minusYears(20 + (i % 60));
            BigDecimal loanAmount = BigDecimal.valueOf(10000 + (i * 100));
            int term = 12 + (i % 48);

            LoadSimulatorRequest request = LoadSimulatorRequest.builder()
                    .loanAmount(loanAmount)
                    .dateOfBirth(dob)
                    .paymentTermMonths(term)
                    .build();

            mockMvc.perform(post("/load-simulator")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        assertTrue(duration < 10000, "O processamento de 100.000 simulações levou mais de 10 segundos: " + duration + "ms");
    }

}
