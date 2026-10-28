package br.com.fiapbank.credito.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoCreditoJpaRepository extends JpaRepository<SolicitacaoCreditoJpaEntity, Long> {

    List<SolicitacaoCreditoJpaEntity> findByCpfCliente(String cpfCliente);
}
