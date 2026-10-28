package br.com.fiapbank.credito.application.dto;

import java.math.BigDecimal;

public record ContaInfo(
        String numero,
        String titularCpf,
        BigDecimal saldo,
        int tempoDeContaMeses
) {
}
