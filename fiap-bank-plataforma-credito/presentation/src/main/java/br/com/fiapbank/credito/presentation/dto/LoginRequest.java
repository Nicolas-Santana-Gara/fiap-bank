package br.com.fiapbank.credito.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "O login e obrigatorio") String login,
        @NotBlank(message = "A senha e obrigatoria") String senha
) {
}
