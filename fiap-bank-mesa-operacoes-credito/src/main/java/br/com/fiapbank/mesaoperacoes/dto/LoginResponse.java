package br.com.fiapbank.mesaoperacoes.dto;

public record LoginResponse(String accessToken, String tokenType, long expiresIn) {
}
