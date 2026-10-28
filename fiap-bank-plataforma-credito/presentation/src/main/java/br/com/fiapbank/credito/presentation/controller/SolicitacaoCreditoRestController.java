package br.com.fiapbank.credito.presentation.controller;

import br.com.fiapbank.credito.application.dto.SolicitacaoCreditoResult;
import br.com.fiapbank.credito.application.dto.SolicitarCreditoCommand;
import br.com.fiapbank.credito.application.usecase.AtualizarStatusSolicitacaoUseCase;
import br.com.fiapbank.credito.application.usecase.ConsultarSolicitacoesUseCase;
import br.com.fiapbank.credito.application.usecase.SolicitarCreditoUseCase;
import br.com.fiapbank.credito.presentation.dto.SolicitacaoResponse;
import br.com.fiapbank.credito.presentation.dto.SolicitarCreditoRequest;
import br.com.fiapbank.credito.presentation.dto.StatusUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitacoes")
public class SolicitacaoCreditoRestController {

    private final SolicitarCreditoUseCase solicitarCreditoUseCase;
    private final AtualizarStatusSolicitacaoUseCase atualizarStatusSolicitacaoUseCase;
    private final ConsultarSolicitacoesUseCase consultarSolicitacoesUseCase;

    public SolicitacaoCreditoRestController(SolicitarCreditoUseCase solicitarCreditoUseCase,
                                             AtualizarStatusSolicitacaoUseCase atualizarStatusSolicitacaoUseCase,
                                             ConsultarSolicitacoesUseCase consultarSolicitacoesUseCase) {
        this.solicitarCreditoUseCase = solicitarCreditoUseCase;
        this.atualizarStatusSolicitacaoUseCase = atualizarStatusSolicitacaoUseCase;
        this.consultarSolicitacoesUseCase = consultarSolicitacoesUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_CLIENTE')")
    public ResponseEntity<SolicitacaoResponse> criarProposta(@Valid @RequestBody SolicitarCreditoRequest request,
                                                               @AuthenticationPrincipal Jwt jwt) {
        String cpfCliente = jwt.getClaimAsString("cpf");
        SolicitacaoCreditoResult resultado = solicitarCreditoUseCase.executar(new SolicitarCreditoCommand(
                cpfCliente, request.numeroConta(), request.valor(), request.prazoMeses(), request.finalidade()));
        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitacaoResponse.de(resultado));
    }

    @GetMapping("/minhas")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_CLIENTE')")
    public ResponseEntity<List<SolicitacaoResponse>> minhasSolicitacoes(@AuthenticationPrincipal Jwt jwt) {
        String cpfCliente = jwt.getClaimAsString("cpf");
        List<SolicitacaoResponse> resposta = consultarSolicitacoesUseCase.listarDoCliente(cpfCliente).stream()
                .map(SolicitacaoResponse::de)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<List<SolicitacaoResponse>> listarTodas() {
        List<SolicitacaoResponse> resposta = consultarSolicitacoesUseCase.listarTodas().stream()
                .map(SolicitacaoResponse::de)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERADOR')")
    public ResponseEntity<SolicitacaoResponse> atualizarStatus(@PathVariable Long id,
                                                                 @Valid @RequestBody StatusUpdateRequest request,
                                                                 @AuthenticationPrincipal Jwt jwt) {
        String operador = jwt.getSubject();
        SolicitacaoCreditoResult resultado = atualizarStatusSolicitacaoUseCase.executar(id, request.status(), operador);
        return ResponseEntity.ok(SolicitacaoResponse.de(resultado));
    }
}
