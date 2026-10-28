package br.com.fiapbank.mesaoperacoes.controller;

import br.com.fiapbank.mesaoperacoes.client.PlataformaCreditoAuthClient;
import br.com.fiapbank.mesaoperacoes.config.TokenPropagationFilter;
import br.com.fiapbank.mesaoperacoes.dto.LoginForm;
import br.com.fiapbank.mesaoperacoes.dto.LoginRequest;
import br.com.fiapbank.mesaoperacoes.dto.LoginResponse;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final PlataformaCreditoAuthClient authClient;

    public LoginController(PlataformaCreditoAuthClient authClient) {
        this.authClient = authClient;
    }

    @GetMapping("/login")
    public String formulario(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login";
    }

    @PostMapping("/login")
    public String autenticar(@ModelAttribute LoginForm loginForm, HttpServletRequest request, Model model) {
        try {
            LoginResponse resposta = authClient.login(new LoginRequest(loginForm.getLogin(), loginForm.getSenha()));
            request.getSession(true).setAttribute(TokenPropagationFilter.SESSION_TOKEN_ATTRIBUTE,
                    resposta.accessToken());
            request.getSession().setAttribute("operadorLogin", loginForm.getLogin());
            return "redirect:/solicitacoes";
        } catch (FeignException.Unauthorized e) {
            model.addAttribute("erro", "Login ou senha invalidos.");
            return "login";
        } catch (FeignException e) {
            model.addAttribute("erro", "Nao foi possivel contactar a Plataforma de Credito.");
            return "login";
        }
    }

    @PostMapping("/logout")
    public String sair(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
