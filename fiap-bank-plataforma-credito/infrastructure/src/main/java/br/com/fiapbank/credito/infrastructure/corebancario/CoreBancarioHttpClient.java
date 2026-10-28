package br.com.fiapbank.credito.infrastructure.corebancario;

import br.com.fiapbank.credito.application.dto.ContaInfo;
import br.com.fiapbank.credito.application.exception.ContaNaoEncontradaException;
import br.com.fiapbank.credito.application.gateway.CoreBancarioGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class CoreBancarioHttpClient implements CoreBancarioGateway {

    private final RestClient restClient;

    public CoreBancarioHttpClient(RestClient.Builder restClientBuilder,
                                   @Value("${fiapbank.core-bancario.base-url}") String baseUrl) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public ContaInfo buscarConta(String numeroConta) {
        try {
            ContaResponse resposta = restClient.get()
                    .uri("/api/contas/{numero}", numeroConta)
                    .retrieve()
                    .body(ContaResponse.class);

            if (resposta == null) {
                throw new ContaNaoEncontradaException(numeroConta);
            }

            return new ContaInfo(resposta.numero(), resposta.titularCpf(), resposta.saldo(),
                    resposta.tempoDeContaMeses());
        } catch (HttpClientErrorException.NotFound e) {
            throw new ContaNaoEncontradaException(numeroConta);
        }
    }

    private record ContaResponse(String numero, String titularCpf, java.math.BigDecimal saldo,
                                  int tempoDeContaMeses) {
    }
}
