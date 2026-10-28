package br.com.fiapbank.credito.presentation.dto;

import br.com.fiapbank.credito.application.dto.SolicitacaoCreditoResult;

import java.math.BigDecimal;
import java.time.Instant;

public record SolicitacaoResponse(
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

    public static SolicitacaoResponse de(SolicitacaoCreditoResult result) {
        return new SolicitacaoResponse(result.id(), result.cpfCliente(), result.numeroConta(), result.valor(),
                result.prazoMeses(), result.finalidade(), result.status(), result.operadorResponsavel(),
                result.dataCriacao(), result.dataAtualizacao());
    }
}
