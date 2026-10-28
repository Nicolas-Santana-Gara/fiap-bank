package br.com.fiapbank.credito.application.usecase;

import br.com.fiapbank.credito.application.dto.SolicitacaoCreditoResult;
import br.com.fiapbank.credito.domain.exception.SolicitacaoNaoEncontradaException;
import br.com.fiapbank.credito.domain.solicitacao.SolicitacaoCredito;
import br.com.fiapbank.credito.domain.solicitacao.SolicitacaoCreditoRepository;
import br.com.fiapbank.credito.domain.solicitacao.StatusSolicitacao;

public class AtualizarStatusSolicitacaoUseCase {

    private final SolicitacaoCreditoRepository repository;

    public AtualizarStatusSolicitacaoUseCase(SolicitacaoCreditoRepository repository) {
        this.repository = repository;
    }

    public SolicitacaoCreditoResult executar(Long id, StatusSolicitacao novoStatus, String operador) {
        SolicitacaoCredito solicitacao = repository.buscarPorId(id)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException(id));

        if (novoStatus == StatusSolicitacao.APROVADA) {
            solicitacao.aprovar(operador);
        } else {
            solicitacao.reprovar(operador);
        }

        SolicitacaoCredito atualizada = repository.salvar(solicitacao);
        return SolicitacaoCreditoResult.de(atualizada);
    }
}
