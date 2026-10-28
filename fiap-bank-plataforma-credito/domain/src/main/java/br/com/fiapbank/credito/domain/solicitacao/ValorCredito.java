package br.com.fiapbank.credito.domain.solicitacao;

import br.com.fiapbank.credito.domain.exception.ValorCreditoInvalidoException;

import java.math.BigDecimal;
import java.util.Objects;

public final class ValorCredito {

    private static final BigDecimal LIMITE_MAXIMO = new BigDecimal("100000.00");

    private final BigDecimal valor;

    private ValorCredito(BigDecimal valor) {
        this.valor = valor;
    }

    public static ValorCredito de(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorCreditoInvalidoException("O valor solicitado deve ser maior que zero.");
        }
        if (valor.compareTo(LIMITE_MAXIMO) > 0) {
            throw new ValorCreditoInvalidoException(
                    "O valor solicitado excede o limite maximo de " + LIMITE_MAXIMO + " por proposta.");
        }
        return new ValorCredito(valor);
    }

    public BigDecimal valor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValorCredito that)) return false;
        return valor.compareTo(that.valor) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor.stripTrailingZeros());
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
