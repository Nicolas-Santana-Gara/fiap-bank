package br.com.fiapbank.credito.application.dto;

import java.math.BigDecimal;

public record SolicitarCreditoCommand(
        String cpfCliente,
        String numeroConta,
        BigDecimal valor,
        int prazoMeses,
        String finalidade
) {
}
