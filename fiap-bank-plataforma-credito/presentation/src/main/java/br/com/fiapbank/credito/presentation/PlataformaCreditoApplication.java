package br.com.fiapbank.credito.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "br.com.fiapbank.credito")
@EnableJpaRepositories(basePackages = "br.com.fiapbank.credito.infrastructure.persistence")
@EntityScan(basePackages = "br.com.fiapbank.credito.infrastructure.persistence")
public class PlataformaCreditoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlataformaCreditoApplication.class, args);
    }
}
