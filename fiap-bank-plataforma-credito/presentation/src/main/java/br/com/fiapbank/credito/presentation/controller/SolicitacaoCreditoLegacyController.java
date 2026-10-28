package br.com.fiapbank.credito.presentation.controller;

import br.com.fiapbank.credito.application.dto.SolicitacaoCreditoResult;
import br.com.fiapbank.credito.application.dto.SolicitarCreditoCommand;
import br.com.fiapbank.credito.application.usecase.ConsultarSolicitacoesUseCase;
import br.com.fiapbank.credito.application.usecase.SolicitarCreditoUseCase;
import br.com.fiapbank.credito.presentation.dto.SolicitacaoResponse;
import br.com.fiapbank.credito.presentation.dto.SolicitarCreditoRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * Mantido para compatibilidade com integracoes anteriores ao Checkpoint 5 que ainda
 * apontam para /api/credito/*. Delega para os mesmos casos de uso do controller v1.
 */
@RestController
@RequestMapping("/api/credito")
public class SolicitacaoCreditoLegacyController {

    private final SolicitarCreditoUseCase solicitarCreditoUseCase;
    private final ConsultarSolicitacoesUseCase consultarSolicitacoesUseCase;

    public SolicitacaoCreditoLegacyController(SolicitarCreditoUseCase solicitarCreditoUseCase,
                                               ConsultarSolicitacoesUseCase consultarSolicitacoesUseCase) {
        this.solicitarCreditoUseCase = solicitarCreditoUseCase;
        this.consultarSolicitacoesUseCase = consultarSolicitacoesUseCase;
    }

    @PostMapping("/solicitar")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_CLIENTE')")
    public ResponseEntity<SolicitacaoResponse> solicitar(@Valid @RequestBody SolicitarCreditoRequest request,
                                                           @AuthenticationPrincipal Jwt jwt) {
        String cpfCliente = jwt.getClaimAsString("cpf");
        SolicitacaoCreditoResult resultado = solicitarCreditoUseCase.executar(new SolicitarCreditoCommand(
                cpfCliente, request.numeroConta(), request.valor(), request.prazoMeses(), request.finalidade()));
        return ResponseEntity.ok(SolicitacaoResponse.de(resultado));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_CLIENTE') or hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<SolicitacaoResponse> consultar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        boolean ehOperador = jwt.getClaimAsStringList("roles").stream()
                .anyMatch(role -> role.equals("ADMIN") || role.equals("OPERADOR"));
        SolicitacaoCreditoResult resultado = ehOperador
                ? consultarSolicitacoesUseCase.buscarComoOperador(id)
                : consultarSolicitacoesUseCase.buscarComoCliente(id, jwt.getClaimAsString("cpf"));
        return ResponseEntity.ok(SolicitacaoResponse.de(resultado));
    }
}
