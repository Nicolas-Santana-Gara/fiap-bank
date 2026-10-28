package br.com.fiapbank.credito.presentation.dto;

public record LoginResponse(String accessToken, String tokenType, long expiresIn) {
}
