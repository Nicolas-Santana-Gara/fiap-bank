package br.com.fiapbank.credito.infrastructure.persistence;

import br.com.fiapbank.credito.domain.solicitacao.SolicitacaoCredito;
import br.com.fiapbank.credito.domain.solicitacao.SolicitacaoCreditoRepository;
import br.com.fiapbank.credito.domain.solicitacao.ValorCredito;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SolicitacaoCreditoRepositoryImpl implements SolicitacaoCreditoRepository {

    private final SolicitacaoCreditoJpaRepository jpaRepository;

    public SolicitacaoCreditoRepositoryImpl(SolicitacaoCreditoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SolicitacaoCredito salvar(SolicitacaoCredito solicitacao) {
        SolicitacaoCreditoJpaEntity entity = new SolicitacaoCreditoJpaEntity(
                solicitacao.getId(),
                solicitacao.getCpfCliente(),
                solicitacao.getNumeroConta(),
                solicitacao.getValorSolicitado().valor(),
                solicitacao.getPrazoMeses(),
                solicitacao.getFinalidade(),
                solicitacao.getStatus(),
                solicitacao.getOperadorResponsavel(),
                solicitacao.getDataCriacao(),
                solicitacao.getDataAtualizacao()
        );
        SolicitacaoCreditoJpaEntity salva = jpaRepository.save(entity);
        return paraDominio(salva);
    }

    @Override
    public Optional<SolicitacaoCredito> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(this::paraDominio);
    }

    @Override
    public List<SolicitacaoCredito> listarTodas() {
        return jpaRepository.findAll().stream().map(this::paraDominio).toList();
    }

    @Override
    public List<SolicitacaoCredito> listarPorCpfCliente(String cpfCliente) {
        return jpaRepository.findByCpfCliente(cpfCliente).stream().map(this::paraDominio).toList();
    }

    private SolicitacaoCredito paraDominio(SolicitacaoCreditoJpaEntity entity) {
        return SolicitacaoCredito.reconstruir(
                entity.getId(),
                entity.getCpfCliente(),
                entity.getNumeroConta(),
                ValorCredito.de(entity.getValorSolicitado()),
                entity.getPrazoMeses(),
                entity.getFinalidade(),
                entity.getStatus(),
                entity.getOperadorResponsavel(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao()
        );
    }
}
