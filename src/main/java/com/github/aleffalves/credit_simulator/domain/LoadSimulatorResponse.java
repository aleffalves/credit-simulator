package com.github.aleffalves.credit_simulator.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resultado da simulação de empréstimo")
public class LoadSimulatorResponse {
    @Schema(description = "Valor total que será pago ao final do empréstimo (principal + juros).", example = "15473.28")
    private BigDecimal totalAmountPayable;

    @Schema(description = "Valor fixo de cada parcela mensal.", example = "644.72")
    private BigDecimal monthlyInstallment;

    @Schema(description = "Valor total pago apenas em juros.", example = "473.28")
    private BigDecimal totalInterestPaid;
}
