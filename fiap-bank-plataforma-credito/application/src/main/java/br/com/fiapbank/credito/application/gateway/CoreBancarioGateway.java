package br.com.fiapbank.credito.application.gateway;

import br.com.fiapbank.credito.application.dto.ContaInfo;

public interface CoreBancarioGateway {

    ContaInfo buscarConta(String numeroConta);
}
