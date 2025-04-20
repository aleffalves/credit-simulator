package com.github.aleffalves.credit_simulator.service;

import com.github.aleffalves.credit_simulator.domain.LoadSimulatorRequest;
import com.github.aleffalves.credit_simulator.domain.LoadSimulatorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LoadSimulatorServiceTest {

    @InjectMocks
    private LoadSimulatorService loadSimulatorService;

    @Test
    void calculateAge_ShouldReturn_CorrectAge() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30).minusMonths(6);
        int age = loadSimulatorService.calculateAge(dateOfBirth);
        assertEquals(30, age);
    }

    @Test
    void calculateAge_Should_ThrowExceptionForFutureDate() {
        LocalDate futureDate = LocalDate.now().plusYears(1);
        assertThrows(IllegalArgumentException.class, () -> {
            loadSimulatorService.calculateAge(futureDate);
        });
    }

    @Test
    void getAnnualInterestRateByAge_ShouldReturn_CorrectRateForEachAgeRange() {
        assertEquals(new BigDecimal("0.05"), loadSimulatorService.getAnnualInterestRateByAge(18));
        assertEquals(new BigDecimal("0.05"), loadSimulatorService.getAnnualInterestRateByAge(25));

        assertEquals(new BigDecimal("0.03"), loadSimulatorService.getAnnualInterestRateByAge(26));
        assertEquals(new BigDecimal("0.03"), loadSimulatorService.getAnnualInterestRateByAge(40));

        assertEquals(new BigDecimal("0.02"), loadSimulatorService.getAnnualInterestRateByAge(41));
        assertEquals(new BigDecimal("0.02"), loadSimulatorService.getAnnualInterestRateByAge(60));

        assertEquals(new BigDecimal("0.04"), loadSimulatorService.getAnnualInterestRateByAge(61));
        assertEquals(new BigDecimal("0.04"), loadSimulatorService.getAnnualInterestRateByAge(100));
    }

    @Test
    void getAnnualInterestRateByAge_Should_ThrowExceptionForNegativeAge() {
        assertThrows(IllegalArgumentException.class, () -> {
            loadSimulatorService.getAnnualInterestRateByAge(-1);
        });
    }

    @Test
    void calculateInstallment_Should_CalculateCorrectly() {
        BigDecimal pv = new BigDecimal("10000");
        BigDecimal r = new BigDecimal("0.01");
        int n = 12;

        BigDecimal installment = loadSimulatorService.calculateInstallment(pv, r, n);
        assertEquals(new BigDecimal("888.49"), installment);
    }

    @Test
    void calculateInstallment_Should_ThrowExceptionForInvalidArguments() {
        // Valor do empréstimo negativo
        assertThrows(IllegalArgumentException.class, () -> {
            loadSimulatorService.calculateInstallment(BigDecimal.valueOf(-1), BigDecimal.valueOf(0.01), 12);
        });

        // Taxa de juros negativa
        assertThrows(IllegalArgumentException.class, () -> {
            loadSimulatorService.calculateInstallment(BigDecimal.valueOf(10000), BigDecimal.valueOf(-0.01), 12);
        });

        // Número de parcelas zero
        assertThrows(IllegalArgumentException.class, () -> {
            loadSimulatorService.calculateInstallment(BigDecimal.valueOf(10000), BigDecimal.valueOf(0.01), 0);
        });

        // Todos argumentos inválidos
        assertThrows(IllegalArgumentException.class, () -> {
            loadSimulatorService.calculateInstallment(BigDecimal.valueOf(-1), BigDecimal.valueOf(-0.01), -1);
        });
    }

    @Test
    void simulate_ShouldReturn_CorrectValuesForYoungBorrower() {
        LocalDate dob = LocalDate.now().minusYears(20);
        LoadSimulatorRequest request = LoadSimulatorRequest.builder()
                .loanAmount(new BigDecimal(10000))
                .dateOfBirth(dob)
                .paymentTermMonths(12)
                .build();

        LoadSimulatorResponse response = loadSimulatorService.simulate(request);

        assertEquals(new BigDecimal("856.26"), response.getMonthlyInstallment());
        assertEquals(new BigDecimal("10275.12"), response.getTotalAmountPayable());
        assertEquals(new BigDecimal("275.12"), response.getTotalInterestPaid());
    }

}
