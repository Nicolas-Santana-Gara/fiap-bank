package br.com.fiapbank.corebancario.conta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true, length = 20)
    private String numero;

    @Column(name = "titular_cpf", nullable = false, length = 11)
    private String titularCpf;

    @Column(name = "saldo", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    protected Conta() {
    }

    public Conta(String numero, String titularCpf, BigDecimal saldo, LocalDate dataAbertura) {
        this.numero = numero;
        this.titularCpf = titularCpf;
        this.saldo = saldo;
        this.dataAbertura = dataAbertura;
    }

    public int tempoDeContaMeses() {
        return (int) java.time.temporal.ChronoUnit.MONTHS.between(dataAbertura, LocalDate.now());
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public String getTitularCpf() {
        return titularCpf;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }
}
