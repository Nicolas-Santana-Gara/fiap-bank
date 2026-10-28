package br.com.fiapbank.corebancario.conta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaRepository contaRepository;

    public ContaController(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    @GetMapping("/{numero}")
    public ResponseEntity<ContaResponse> buscarPorNumero(@PathVariable String numero) {
        return contaRepository.findByNumero(numero)
                .map(ContaResponse::de)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
