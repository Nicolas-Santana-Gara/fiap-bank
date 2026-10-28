package br.com.fiapbank.credito.application.dto;

import br.com.fiapbank.credito.domain.solicitacao.SolicitacaoCredito;

import java.math.BigDecimal;
import java.time.Instant;

public record SolicitacaoCreditoResult(
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

    public static SolicitacaoCreditoResult de(SolicitacaoCredito solicitacao) {
        return new SolicitacaoCreditoResult(
                solicitacao.getId(),
                solicitacao.getCpfCliente(),
                solicitacao.getNumeroConta(),
                solicitacao.getValorSolicitado().valor(),
                solicitacao.getPrazoMeses(),
                solicitacao.getFinalidade(),
                solicitacao.getStatus().name(),
                solicitacao.getOperadorResponsavel(),
                solicitacao.getDataCriacao(),
                solicitacao.getDataAtualizacao()
        );
    }
}
