package br.com.fiapbank.credito.presentation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SolicitarCreditoRequest(
        @NotBlank(message = "O numero da conta e obrigatorio") String numeroConta,
        @NotNull(message = "O valor e obrigatorio") @Positive(message = "O valor deve ser positivo") BigDecimal valor,
        @Min(value = 1, message = "O prazo minimo e de 1 mes") @Max(value = 96, message = "O prazo maximo e de 96 meses") int prazoMeses,
        @NotBlank(message = "A finalidade e obrigatoria") String finalidade
) {
}
