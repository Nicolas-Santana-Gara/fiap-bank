package br.com.fiapbank.credito.presentation.controller;

import br.com.fiapbank.credito.presentation.dto.LoginRequest;
import br.com.fiapbank.credito.presentation.dto.LoginResponse;
import br.com.fiapbank.credito.presentation.security.AutenticacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AutenticacaoService autenticacaoService;

    @Value("${jwt.expiration-seconds}")
    private Long expirationSeconds;

    public AuthController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = autenticacaoService.autenticar(request.login(), request.senha());
        return ResponseEntity.ok(new LoginResponse(token, "Bearer", expirationSeconds));
    }
}
