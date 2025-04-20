package com.github.aleffalves.credit_simulator.service;

import com.github.aleffalves.credit_simulator.domain.LoadSimulatorRequest;
import com.github.aleffalves.credit_simulator.domain.LoadSimulatorResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

@Service
public class LoadSimulatorService {

    public LoadSimulatorResponse simulate(LoadSimulatorRequest request) {
        int age = calculateAge(request.getDateOfBirth());
        BigDecimal annualInterestRate = getAnnualInterestRateByAge(age);
        BigDecimal monthlyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_EVEN);

        BigDecimal installmentValue = calculateInstallment(
                                        request.getLoanAmount(),
                                        monthlyInterestRate,
                                        request.getPaymentTermMonths());

        BigDecimal totalValue = installmentValue.multiply(BigDecimal.valueOf(request.getPaymentTermMonths()));
        BigDecimal totalInterestPaid = totalValue.subtract(request.getLoanAmount());

        return LoadSimulatorResponse.builder()
                .monthlyInstallment(installmentValue)
                .totalAmountPayable(totalValue)
                .totalInterestPaid(totalInterestPaid)
                .build();
    }

    public int calculateAge(LocalDate dateOfBirth){
        if (dateOfBirth.isAfter(LocalDate.now())) throw new IllegalArgumentException("A data de nascimento não pode ser no futuro.");
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public BigDecimal getAnnualInterestRateByAge(int age){
        if (age < 0) throw new IllegalArgumentException("A idade não pode ser negativa.");

        if (age <= 25) return BigDecimal.valueOf(0.05);
        if (age <= 40) return BigDecimal.valueOf(0.03);
        if (age <= 60) return BigDecimal.valueOf(0.02);
        return BigDecimal.valueOf(0.04);
    }

    public BigDecimal calculateInstallment(BigDecimal pv, BigDecimal r, int n){
        if(pv.compareTo(BigDecimal.ZERO) <= 0 || r.compareTo(BigDecimal.ZERO) <= 0 || n <= 0) {
            throw new IllegalArgumentException("Argumentos inválidos para o calculo.");
        }

        // Tabela Price
        // Fórmula PMT: (pv * r) / (1 - (1 + r)^-n)

        double pow = Math.pow(1 + r.doubleValue(), -n);
        double v = pv.doubleValue() * r.doubleValue();
        double pmt = v / (1 - pow);

        return BigDecimal.valueOf(pmt).setScale(2, RoundingMode.HALF_EVEN);
    }
}
