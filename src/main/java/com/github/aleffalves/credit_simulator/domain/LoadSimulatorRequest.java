package com.github.aleffalves.credit_simulator.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados de entrada para a simulação de empréstimo")
public class LoadSimulatorRequest {
    @NotNull(message = "O valor do empréstimo não pode ser nulo.")
    @Positive(message = "O valor do empréstimo deve ser positivo.")
    @Schema(description = "Valor total solicitado para o empréstimo.", example = "15000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal loanAmount;

    @NotNull(message = "A data de nascimento não pode ser nula.")
    @Past(message = "A data de nascimento deve ser no passado.")
    @Schema(description = "Data de nascimento do cliente (formato YYYY-MM-DD). Usada para calcular a taxa de juros.", example = "1988-10-25", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dateOfBirth;

    @NotNull(message = "O prazo de pagamento não pode ser nulo.")
    @Positive(message = "O prazo de pagamento deve ser positivo.")
    @Schema(description = "Número de meses para pagar o empréstimo.", example = "24", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer paymentTermMonths;
}
