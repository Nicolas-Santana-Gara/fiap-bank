package br.com.fiapbank.mesaoperacoes.controller;

import br.com.fiapbank.mesaoperacoes.client.PlataformaCreditoSolicitacaoClient;
import br.com.fiapbank.mesaoperacoes.dto.StatusUpdateRequest;
import feign.FeignException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SolicitacaoOperacoesController {

    private final PlataformaCreditoSolicitacaoClient solicitacaoClient;

    public SolicitacaoOperacoesController(PlataformaCreditoSolicitacaoClient solicitacaoClient) {
        this.solicitacaoClient = solicitacaoClient;
    }

    @GetMapping("/solicitacoes")
    public String listar(Model model) {
        model.addAttribute("solicitacoes", solicitacaoClient.listarTodas());
        return "solicitacoes";
    }

    @PostMapping("/solicitacoes/{id}/aprovar")
    public String aprovar(@PathVariable Long id) {
        solicitacaoClient.atualizarStatus(id, new StatusUpdateRequest("APROVADA"));
        return "redirect:/solicitacoes";
    }

    @PostMapping("/solicitacoes/{id}/reprovar")
    public String reprovar(@PathVariable Long id) {
        solicitacaoClient.atualizarStatus(id, new StatusUpdateRequest("REPROVADA"));
        return "redirect:/solicitacoes";
    }
}
