package br.com.fiapbank.credito.presentation.dto;

import jakarta.validation.constraints.NotNull;

import br.com.fiapbank.credito.domain.solicitacao.StatusSolicitacao;

public record StatusUpdateRequest(
        @NotNull(message = "O novo status e obrigatorio") StatusSolicitacao status
) {
}
