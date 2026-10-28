package br.com.fiapbank.credito.application.usecase;

import br.com.fiapbank.credito.application.dto.SolicitacaoCreditoResult;
import br.com.fiapbank.credito.application.exception.AcessoNegadoSolicitacaoException;
import br.com.fiapbank.credito.domain.exception.SolicitacaoNaoEncontradaException;
import br.com.fiapbank.credito.domain.solicitacao.SolicitacaoCredito;
import br.com.fiapbank.credito.domain.solicitacao.SolicitacaoCreditoRepository;

import java.util.List;

public class ConsultarSolicitacoesUseCase {

    private final SolicitacaoCreditoRepository repository;

    public ConsultarSolicitacoesUseCase(SolicitacaoCreditoRepository repository) {
        this.repository = repository;
    }

    public List<SolicitacaoCreditoResult> listarTodas() {
        return repository.listarTodas().stream()
                .map(SolicitacaoCreditoResult::de)
                .toList();
    }

    public List<SolicitacaoCreditoResult> listarDoCliente(String cpfCliente) {
        return repository.listarPorCpfCliente(cpfCliente).stream()
                .map(SolicitacaoCreditoResult::de)
                .toList();
    }

    public SolicitacaoCreditoResult buscarComoOperador(Long id) {
        SolicitacaoCredito solicitacao = repository.buscarPorId(id)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException(id));
        return SolicitacaoCreditoResult.de(solicitacao);
    }

    public SolicitacaoCreditoResult buscarComoCliente(Long id, String cpfCliente) {
        SolicitacaoCredito solicitacao = repository.buscarPorId(id)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException(id));
        if (!solicitacao.pertenceA(cpfCliente)) {
            throw new AcessoNegadoSolicitacaoException();
        }
        return SolicitacaoCreditoResult.de(solicitacao);
    }
}
