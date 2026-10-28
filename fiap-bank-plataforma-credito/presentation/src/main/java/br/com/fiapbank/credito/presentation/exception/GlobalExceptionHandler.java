package br.com.fiapbank.credito.presentation.exception;

import br.com.fiapbank.credito.application.exception.AcessoNegadoSolicitacaoException;
import br.com.fiapbank.credito.application.exception.ContaNaoEncontradaException;
import br.com.fiapbank.credito.domain.exception.SolicitacaoNaoEncontradaException;
import br.com.fiapbank.credito.domain.exception.TransicaoStatusInvalidaException;
import br.com.fiapbank.credito.domain.exception.ValorCreditoInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SolicitacaoNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> handleNaoEncontrada(SolicitacaoNaoEncontradaException ex) {
        return corpo(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> handleContaNaoEncontrada(ContaNaoEncontradaException ex) {
        return corpo(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler({TransicaoStatusInvalidaException.class, ValorCreditoInvalidoException.class})
    public ResponseEntity<Map<String, Object>> handleRegraDominio(RuntimeException ex) {
        return corpo(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(AcessoNegadoSolicitacaoException.class)
    public ResponseEntity<Map<String, Object>> handleAcessoNegado(AcessoNegadoSolicitacaoException ex) {
        return corpo(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleCredenciaisInvalidas(BadCredentialsException ex) {
        return corpo(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .orElse("Dados invalidos.");
        return corpo(HttpStatus.BAD_REQUEST, mensagem);
    }

    private ResponseEntity<Map<String, Object>> corpo(HttpStatus status, String mensagem) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("erro", status.getReasonPhrase());
        body.put("mensagem", mensagem);
        return ResponseEntity.status(status).body(body);
    }
}
