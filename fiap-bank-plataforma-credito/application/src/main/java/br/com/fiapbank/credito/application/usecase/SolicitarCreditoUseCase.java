package br.com.fiapbank.credito.application.usecase;

import br.com.fiapbank.credito.application.dto.ContaInfo;
import br.com.fiapbank.credito.application.dto.SolicitacaoCreditoResult;
import br.com.fiapbank.credito.application.dto.SolicitarCreditoCommand;
import br.com.fiapbank.credito.application.exception.ContaNaoEncontradaException;
import br.com.fiapbank.credito.application.gateway.CoreBancarioGateway;
import br.com.fiapbank.credito.domain.solicitacao.SolicitacaoCredito;
import br.com.fiapbank.credito.domain.solicitacao.SolicitacaoCreditoRepository;
import br.com.fiapbank.credito.domain.solicitacao.ValorCredito;

public class SolicitarCreditoUseCase {

    private final SolicitacaoCreditoRepository repository;
    private final CoreBancarioGateway coreBancarioGateway;

    public SolicitarCreditoUseCase(SolicitacaoCreditoRepository repository, CoreBancarioGateway coreBancarioGateway) {
        this.repository = repository;
        this.coreBancarioGateway = coreBancarioGateway;
    }

    public SolicitacaoCreditoResult executar(SolicitarCreditoCommand command) {
        ContaInfo conta = coreBancarioGateway.buscarConta(command.numeroConta());
        if (conta == null || !conta.titularCpf().equals(command.cpfCliente())) {
            throw new ContaNaoEncontradaException(command.numeroConta());
        }

        SolicitacaoCredito solicitacao = SolicitacaoCredito.nova(
                command.cpfCliente(),
                command.numeroConta(),
                ValorCredito.de(command.valor()),
                command.prazoMeses(),
                command.finalidade()
        );

        SolicitacaoCredito salva = repository.salvar(solicitacao);
        return SolicitacaoCreditoResult.de(salva);
    }
}
