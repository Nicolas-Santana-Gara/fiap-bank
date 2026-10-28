package br.com.fiapbank.credito.domain.solicitacao;

import br.com.fiapbank.credito.domain.exception.TransicaoStatusInvalidaException;

import java.time.Instant;
import java.util.Objects;

public class SolicitacaoCredito {

    private final Long id;
    private final String cpfCliente;
    private final String numeroConta;
    private final ValorCredito valorSolicitado;
    private final int prazoMeses;
    private final String finalidade;
    private StatusSolicitacao status;
    private String operadorResponsavel;
    private final Instant dataCriacao;
    private Instant dataAtualizacao;

    private SolicitacaoCredito(Long id, String cpfCliente, String numeroConta, ValorCredito valorSolicitado,
                                int prazoMeses, String finalidade, StatusSolicitacao status,
                                String operadorResponsavel, Instant dataCriacao, Instant dataAtualizacao) {
        this.id = id;
        this.cpfCliente = Objects.requireNonNull(cpfCliente, "cpfCliente e obrigatorio");
        this.numeroConta = Objects.requireNonNull(numeroConta, "numeroConta e obrigatorio");
        this.valorSolicitado = Objects.requireNonNull(valorSolicitado, "valorSolicitado e obrigatorio");
        if (prazoMeses <= 0 || prazoMeses > 96) {
            throw new IllegalArgumentException("O prazo deve estar entre 1 e 96 meses.");
        }
        this.prazoMeses = prazoMeses;
        this.finalidade = Objects.requireNonNull(finalidade, "finalidade e obrigatoria");
        this.status = status;
        this.operadorResponsavel = operadorResponsavel;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public static SolicitacaoCredito nova(String cpfCliente, String numeroConta, ValorCredito valorSolicitado,
                                           int prazoMeses, String finalidade) {
        Instant agora = Instant.now();
        return new SolicitacaoCredito(null, cpfCliente, numeroConta, valorSolicitado, prazoMeses, finalidade,
                StatusSolicitacao.PENDENTE, null, agora, agora);
    }

    public static SolicitacaoCredito reconstruir(Long id, String cpfCliente, String numeroConta,
                                                   ValorCredito valorSolicitado, int prazoMeses, String finalidade,
                                                   StatusSolicitacao status, String operadorResponsavel,
                                                   Instant dataCriacao, Instant dataAtualizacao) {
        return new SolicitacaoCredito(id, cpfCliente, numeroConta, valorSolicitado, prazoMeses, finalidade, status,
                operadorResponsavel, dataCriacao, dataAtualizacao);
    }

    public void aprovar(String operador) {
        transicionar(StatusSolicitacao.APROVADA, operador);
    }

    public void reprovar(String operador) {
        transicionar(StatusSolicitacao.REPROVADA, operador);
    }

    private void transicionar(StatusSolicitacao destino, String operador) {
        if (this.status != StatusSolicitacao.PENDENTE) {
            throw new TransicaoStatusInvalidaException(this.status, destino);
        }
        this.status = destino;
        this.operadorResponsavel = Objects.requireNonNull(operador, "operadorResponsavel e obrigatorio");
        this.dataAtualizacao = Instant.now();
    }

    public boolean pertenceA(String cpf) {
        return this.cpfCliente.equals(cpf);
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

    public ValorCredito getValorSolicitado() {
        return valorSolicitado;
    }

    public int getPrazoMeses() {
        return prazoMeses;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public StatusSolicitacao getStatus() {
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
