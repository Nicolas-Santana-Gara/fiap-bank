package br.com.fiapbank.credito.domain.solicitacao;

import java.util.List;
import java.util.Optional;

public interface SolicitacaoCreditoRepository {

    SolicitacaoCredito salvar(SolicitacaoCredito solicitacao);

    Optional<SolicitacaoCredito> buscarPorId(Long id);

    List<SolicitacaoCredito> listarTodas();

    List<SolicitacaoCredito> listarPorCpfCliente(String cpfCliente);
}
