package br.com.fiapbank.mesaoperacoes.client;

import br.com.fiapbank.mesaoperacoes.dto.LoginRequest;
import br.com.fiapbank.mesaoperacoes.dto.LoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "plataforma-credito-auth", url = "${fiapbank.plataforma-credito.base-url}")
public interface PlataformaCreditoAuthClient {

    @PostMapping("/api/auth/login")
    LoginResponse login(@RequestBody LoginRequest request);
}
