package br.com.fiapbank.credito.application.exception;

public class AcessoNegadoSolicitacaoException extends RuntimeException {

    public AcessoNegadoSolicitacaoException() {
        super("A solicitacao de credito consultada nao pertence ao correntista autenticado.");
    }
}
