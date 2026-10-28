package br.com.fiapbank.mesaoperacoes.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * A Mesa de Operacoes e uma aplicacao web tradicional baseada em sessao: o navegador do
 * operador nao envia um cabecalho Authorization em cada requisicao. Este filtro recupera o
 * token JWT obtido no login (guardado na HttpSession) e o expoe como cabecalho Authorization
 * na requisicao recebida, para que o bearerTokenRequestInterceptor do Feign (que le o
 * cabecalho da requisicao atual) consiga repassa-lo para a Plataforma de Credito.
 */
@Component
public class TokenPropagationFilter extends OncePerRequestFilter {

    public static final String SESSION_TOKEN_ATTRIBUTE = "jwt_token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Object token = request.getSession(true).getAttribute(SESSION_TOKEN_ATTRIBUTE);
        if (token instanceof String tokenValue && !tokenValue.isBlank()) {
            chain.doFilter(new AuthorizationHeaderRequestWrapper(request, tokenValue), response);
            return;
        }
        chain.doFilter(request, response);
    }

    private static class AuthorizationHeaderRequestWrapper extends HttpServletRequestWrapper {

        private final String bearerToken;

        AuthorizationHeaderRequestWrapper(HttpServletRequest request, String token) {
            super(request);
            this.bearerToken = "Bearer " + token;
        }

        @Override
        public String getHeader(String name) {
            if ("Authorization".equalsIgnoreCase(name)) {
                return bearerToken;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if ("Authorization".equalsIgnoreCase(name)) {
                return Collections.enumeration(Collections.singletonList(bearerToken));
            }
            return super.getHeaders(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            java.util.List<String> names = Collections.list(super.getHeaderNames());
            if (!names.contains("Authorization")) {
                names.add("Authorization");
            }
            return Collections.enumeration(names);
        }
    }
}
