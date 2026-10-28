package br.com.fiapbank.credito.domain.exception;

public class SolicitacaoNaoEncontradaException extends RuntimeException {

    public SolicitacaoNaoEncontradaException(Long id) {
        super("Solicitacao de credito nao encontrada: " + id);
    }
}
