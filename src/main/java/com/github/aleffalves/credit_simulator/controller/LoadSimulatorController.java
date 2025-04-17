package com.github.aleffalves.credit_simulator.controller;

import com.github.aleffalves.credit_simulator.domain.LoadSimulatorRequest;
import com.github.aleffalves.credit_simulator.domain.LoadSimulatorResponse;
import com.github.aleffalves.credit_simulator.service.LoadSimulatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/load-simulator")
@Tag(name = "Simulação de Crédito", description = "Endpoints para simular condições de empréstimo")
public class LoadSimulatorController {

    @Autowired
    private LoadSimulatorService loadSimulatorService;

    @Operation(summary = "Realiza uma simulação de empréstimo",
            description = "Calcula o valor total a pagar, as parcelas mensais e os juros totais com base no valor, prazo e data de nascimento do cliente (para definir a taxa).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Simulação calculada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoadSimulatorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "object", example = "{\"loanAmount\": \"O valor do empréstimo deve ser positivo.\", \"dateOfBirth\": \"A data de nascimento não pode ser nula.\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "object", example = "{\"error\": \"Ocorreu um erro inesperado no servidor.\"}")))
    })
    @PostMapping
    public ResponseEntity<LoadSimulatorResponse> simulate(
            @Valid @RequestBody LoadSimulatorRequest request) {
        return ResponseEntity.ok(loadSimulatorService.simulate(request));
    }

}
