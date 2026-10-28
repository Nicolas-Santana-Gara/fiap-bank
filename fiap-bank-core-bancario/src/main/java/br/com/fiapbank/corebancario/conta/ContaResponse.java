package br.com.fiapbank.corebancario.conta;

import java.math.BigDecimal;

public record ContaResponse(String numero, String titularCpf, BigDecimal saldo, int tempoDeContaMeses) {

    public static ContaResponse de(Conta conta) {
        return new ContaResponse(conta.getNumero(), conta.getTitularCpf(), conta.getSaldo(),
                conta.tempoDeContaMeses());
    }
}
