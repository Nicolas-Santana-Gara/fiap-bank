package br.com.fiapbank.credito.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "solicitacao_credito")
public class SolicitacaoCreditoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf_cliente", nullable = false, length = 11)
    private String cpfCliente;

    @Column(name = "numero_conta", nullable = false, length = 20)
    private String numeroConta;

    @Column(name = "valor_solicitado", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorSolicitado;

    @Column(name = "prazo_meses", nullable = false)
    private int prazoMeses;

    @Column(name = "finalidade", nullable = false, length = 255)
    private String finalidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private br.com.fiapbank.credito.domain.solicitacao.StatusSolicitacao status;

    @Column(name = "operador_responsavel", length = 60)
    private String operadorResponsavel;

    @Column(name = "data_criacao", nullable = false)
    private Instant dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private Instant dataAtualizacao;

    protected SolicitacaoCreditoJpaEntity() {
    }

    public SolicitacaoCreditoJpaEntity(Long id, String cpfCliente, String numeroConta, BigDecimal valorSolicitado,
                                        int prazoMeses, String finalidade,
                                        br.com.fiapbank.credito.domain.solicitacao.StatusSolicitacao status,
                                        String operadorResponsavel, Instant dataCriacao, Instant dataAtualizacao) {
        this.id = id;
        this.cpfCliente = cpfCliente;
        this.numeroConta = numeroConta;
        this.valorSolicitado = valorSolicitado;
        this.prazoMeses = prazoMeses;
        this.finalidade = finalidade;
        this.status = status;
        this.operadorResponsavel = operadorResponsavel;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getId() {
        return id;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }

    public int getPrazoMeses() {
        return prazoMeses;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public br.com.fiapbank.credito.domain.solicitacao.StatusSolicitacao getStatus() {
        return status;
    }

    public String getOperadorResponsavel() {
        return operadorResponsavel;
    }

    public Instant getDataCriacao() {
        return dataCriacao;
    }

    public Instant getDataAtualizacao() {
        return dataAtualizacao;
    }
}
