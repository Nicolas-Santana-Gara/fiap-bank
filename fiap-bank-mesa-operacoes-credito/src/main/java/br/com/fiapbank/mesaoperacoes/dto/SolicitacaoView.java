package br.com.fiapbank.mesaoperacoes.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record SolicitacaoView(
        Long id,
        String cpfCliente,
        String numeroConta,
        BigDecimal valor,
        int prazoMeses,
        String finalidade,
        String status,
        String operadorResponsavel,
        Instant dataCriacao,
        Instant dataAtualizacao
) {
}
