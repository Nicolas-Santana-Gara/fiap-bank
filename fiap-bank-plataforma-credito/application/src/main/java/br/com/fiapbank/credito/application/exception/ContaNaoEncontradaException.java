package br.com.fiapbank.credito.application.exception;

public class ContaNaoEncontradaException extends RuntimeException {

    public ContaNaoEncontradaException(String numeroConta) {
        super("O Core Bancario nao encontrou a conta " + numeroConta
                + " ou ela nao pertence ao cliente autenticado.");
    }
}
