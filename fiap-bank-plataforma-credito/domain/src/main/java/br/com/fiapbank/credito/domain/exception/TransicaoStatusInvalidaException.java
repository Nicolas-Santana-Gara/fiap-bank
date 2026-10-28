package br.com.fiapbank.credito.domain.exception;

import br.com.fiapbank.credito.domain.solicitacao.StatusSolicitacao;

public class TransicaoStatusInvalidaException extends RuntimeException {

    public TransicaoStatusInvalidaException(StatusSolicitacao atual, StatusSolicitacao destino) {
        super("Nao e possivel mover a solicitacao de " + atual + " para " + destino
                + ". Somente solicitacoes PENDENTE podem ser aprovadas ou reprovadas.");
    }
}
