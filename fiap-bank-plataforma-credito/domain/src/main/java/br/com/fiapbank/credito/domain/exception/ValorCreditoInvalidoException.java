package br.com.fiapbank.credito.domain.exception;

public class ValorCreditoInvalidoException extends RuntimeException {

    public ValorCreditoInvalidoException(String message) {
        super(message);
    }
}
