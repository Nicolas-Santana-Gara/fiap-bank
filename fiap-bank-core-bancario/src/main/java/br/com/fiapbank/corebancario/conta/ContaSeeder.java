package br.com.fiapbank.corebancario.conta;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ContaSeeder implements CommandLineRunner {

    private final ContaRepository contaRepository;

    public ContaSeeder(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    @Override
    public void run(String... args) {
        if (contaRepository.count() > 0) {
            return;
        }

        contaRepository.save(new Conta("00112233-4", "35211022087", new BigDecimal("18500.00"),
                LocalDate.now().minusMonths(30)));
        contaRepository.save(new Conta("00998877-1", "22233344455", new BigDecimal("3200.00"),
                LocalDate.now().minusMonths(6)));
    }
}
