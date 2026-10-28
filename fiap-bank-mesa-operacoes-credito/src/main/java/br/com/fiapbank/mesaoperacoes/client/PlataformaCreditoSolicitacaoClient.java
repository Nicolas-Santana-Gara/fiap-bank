package br.com.fiapbank.mesaoperacoes.client;

import br.com.fiapbank.mesaoperacoes.config.FeignClientConfig;
import br.com.fiapbank.mesaoperacoes.dto.SolicitacaoView;
import br.com.fiapbank.mesaoperacoes.dto.StatusUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        name = "plataforma-credito-solicitacoes",
        url = "${fiapbank.plataforma-credito.base-url}",
        configuration = FeignClientConfig.class
)
public interface PlataformaCreditoSolicitacaoClient {

    @GetMapping("/api/v1/solicitacoes")
    List<SolicitacaoView> listarTodas();

    @PutMapping("/api/v1/solicitacoes/{id}/status")
    SolicitacaoView atualizarStatus(@PathVariable("id") Long id, @RequestBody StatusUpdateRequest request);
}
