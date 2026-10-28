package br.com.fiapbank.credito.presentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityFilterChainTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void endpointProtegidoDeveRejeitarRequisicaoSemToken() throws Exception {
        mockMvc.perform(get("/api/v1/solicitacoes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginComCredenciaisInvalidasDeveRetornarNaoAutorizado() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"login\":\"inexistente\",\"senha\":\"errada\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginComCredenciaisValidasDeveRetornarToken() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"login\":\"35211022087\",\"senha\":\"credito123\"}"))
                .andExpect(status().isOk());
    }
}
