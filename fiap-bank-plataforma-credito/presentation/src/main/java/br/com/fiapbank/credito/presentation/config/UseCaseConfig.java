package br.com.fiapbank.credito.presentation.config;

import br.com.fiapbank.credito.application.gateway.CoreBancarioGateway;
import br.com.fiapbank.credito.application.usecase.AtualizarStatusSolicitacaoUseCase;
import br.com.fiapbank.credito.application.usecase.ConsultarSolicitacoesUseCase;
import br.com.fiapbank.credito.application.usecase.SolicitarCreditoUseCase;
import br.com.fiapbank.credito.domain.solicitacao.SolicitacaoCreditoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public SolicitarCreditoUseCase solicitarCreditoUseCase(SolicitacaoCreditoRepository repository,
                                                             CoreBancarioGateway coreBancarioGateway) {
        return new SolicitarCreditoUseCase(repository, coreBancarioGateway);
    }

    @Bean
    public AtualizarStatusSolicitacaoUseCase atualizarStatusSolicitacaoUseCase(SolicitacaoCreditoRepository repository) {
        return new AtualizarStatusSolicitacaoUseCase(repository);
    }

    @Bean
    public ConsultarSolicitacoesUseCase consultarSolicitacoesUseCase(SolicitacaoCreditoRepository repository) {
        return new ConsultarSolicitacoesUseCase(repository);
    }
}
