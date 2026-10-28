# FIAP Bank - Plataforma de Concessão de Crédito e Segurança

Checkpoint 6 - Engenharia de Software / Domain Driven Design - Java (FIAP, turma 2ESPH, 2026)

## Autor

Trabalho individual.

Nicolas Santana Gará - RM561461

## Visão geral

Este Checkpoint evolui a Plataforma de Crédito do FIAP Bank (base do Checkpoint 5, em DDD e
contratos RESTful) para um **OAuth2 Resource Server** completo, stateless, com validação
criptográfica de tokens JWT e controle de acesso baseado em papéis (RBAC).

A `fiap-bank-plataforma-credito` deixou de confiar apenas em validações internas de domínio: toda
requisição a um endpoint protegido agora precisa chegar com `Authorization: Bearer <token>`, emitido
e assinado por ela mesma em `/api/auth/login` e validado de forma autônoma (sem sessão, sem
`HttpSession`, sem consulta a um servidor externo) a cada chamada seguinte.

O monorepo reúne os quatro sistemas do ecossistema do FIAP Bank usados neste Checkpoint:

```
fiap-bank/
  fiap-bank-plataforma-credito/      Resource Server (Spring Boot + Spring Security + JWT)
  fiap-bank-core-bancario/           Consulta de saldo/conta (chamado de forma síncrona pela Plataforma)
  fiap-bank-mesa-operacoes-credito/  Spring Boot + Thymeleaf + OpenFeign (operadores aprovam/reprovam)
  fiap-bank-internet-banking-mobile/ Angular (correntistas solicitam crédito e acompanham propostas)
```

## `fiap-bank-plataforma-credito`

Módulos Maven, no padrão DDD já usado desde o Checkpoint 5:

```
presentation -> infrastructure -> application -> domain
```

- **domain**: `SolicitacaoCredito` (aggregate root), `ValorCredito` (value object), `StatusSolicitacao`
  e as exceções de regra de negócio. Não depende de nenhum framework - nem de Spring, nem de Spring
  Security. É só Java puro.
- **application**: casos de uso (`SolicitarCreditoUseCase`, `AtualizarStatusSolicitacaoUseCase`,
  `ConsultarSolicitacoesUseCase`) e o gateway `CoreBancarioGateway`, implementado depois na
  infraestrutura.
- **infrastructure**: adapters JPA/H2 para `SolicitacaoCredito` e `Usuario`, e o cliente HTTP
  (`CoreBancarioHttpClient`) que consulta o Core Bancário.
- **presentation**: único módulo com `spring-boot-starter-security` e
  `spring-boot-starter-oauth2-resource-server` no classpath (regra do enunciado). É aqui que vivem o
  `SecurityConfig`, o `TokenService`, os controllers REST e o `AuthController`.

### Segurança (o que efetivamente mudou no Checkpoint 6)

- `SecurityFilterChain` com CSRF desabilitado, `SessionCreationPolicy.STATELESS` e `/api/auth/**` +
  `/h2-console/**` liberados via `permitAll()` - todo o resto exige token válido.
- `frameOptions().disable()` para o H2 Console continuar acessível em `<frame>` (gotcha do Spring
  Security 6+ citado no enunciado).
- `JwtDecoder`/`JwtEncoder` usando a mesma chave HMAC (`HmacSHA256`), lida de `jwt.secret` no
  `application.yml` - nenhuma chave hardcoded no código Java.
- `TokenService` monta o token com as *registered claims* da RFC 7519 (`iss`, `sub`, `iat`, `exp`) e
  as claims privadas do domínio (`cpf`, `conta`, `roles`, `scope`).
- `JwtAuthenticationConverter` customizado: além das authorities padrão derivadas de `scope`
  (`SCOPE_ROLE_CLIENTE`), ele também lê a claim `roles` (array) e gera `ROLE_ADMIN`/`ROLE_OPERADOR`,
  permitindo usar `hasRole(...)` sem divergência de nomenclatura.
- `@EnableMethodSecurity` + `@PreAuthorize` em todos os endpoints de negócio:
  - `POST /api/v1/solicitacoes` → `hasAuthority('SCOPE_ROLE_CLIENTE')`
  - `GET /api/v1/solicitacoes/minhas` → `hasAuthority('SCOPE_ROLE_CLIENTE')`
  - `GET /api/v1/solicitacoes` e `PUT /api/v1/solicitacoes/{id}/status` → `hasRole('ADMIN') or
    hasRole('OPERADOR')`
  - `POST /api/credito/solicitar` (controller adaptador legado) → mesma regra do endpoint v1
- `AuthController` (`/api/auth/login`, público) autentica contra a tabela `usuario` (senha com
  BCrypt) e devolve o JWT assinado.
- CORS liberado para `http://localhost:*` (necessário para o app Angular, servido em outra porta,
  conseguir chamar a API - sem isso o navegador bloqueia a resposta do preflight).

### Rodando localmente

```bash
# a partir de fiap-bank-plataforma-credito/
mvn clean install
mvn spring-boot:run -pl presentation
```

A aplicação sobe em `http://localhost:8081`. H2 Console em `/h2-console` (JDBC URL
`jdbc:h2:mem:plataformacredito`, usuário `sa`, sem senha).

Usuários de teste (populados automaticamente no start):

| Login         | Senha       | Papel     |
|---------------|-------------|-----------|
| `35211022087` | `credito123`| CLIENTE   |
| `operador.mesa`| `mesa123`  | OPERADOR  |
| `admin.credito`| `admin123` | ADMIN     |

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"35211022087","senha":"credito123"}'
```

## `fiap-bank-core-bancario`

Serviço interno simples (sem autenticação - a confiança entre serviços internos não faz parte do
escopo deste Checkpoint), expõe `GET /api/contas/{numero}` com saldo e tempo de conta. Sobe em
`http://localhost:8083` e já vem com duas contas de exemplo, uma delas associada ao cliente de teste
acima (conta `00112233-4`).

## `fiap-bank-mesa-operacoes-credito`

Spring Boot + Thymeleaf + Spring Cloud OpenFeign, porta `8082`. Fluxo:

1. O operador faz login em `/login` (formulário HTML comum, sem token no navegador).
2. O `LoginController` chama `/api/auth/login` da Plataforma de Crédito via Feign e guarda o JWT
   retornado na `HttpSession` do operador - só o token fica na sessão, nenhuma credencial de domínio
   bancário.
3. Como o navegador não envia `Authorization: Bearer` sozinho numa aplicação de sessão tradicional,
   o `TokenPropagationFilter` recupera o token da sessão e o expõe como cabeçalho `Authorization` na
   requisição recebida pelo Spring MVC.
4. O `bearerTokenRequestInterceptor` do `FeignClientConfig` (exatamente como pedido no enunciado) lê
   esse cabeçalho da requisição atual e o repassa para toda chamada Feign à Plataforma de Crédito.
5. Em `/solicitacoes`, o operador vê a fila de propostas (`GET /api/v1/solicitacoes`) e aprova ou
   reprova (`PUT /api/v1/solicitacoes/{id}/status`), sempre autenticado como `ROLE_OPERADOR`/`ROLE_ADMIN`.

```bash
# a partir de fiap-bank-mesa-operacoes-credito/, com a Plataforma de Crédito já rodando
mvn spring-boot:run
```

## `fiap-bank-internet-banking-mobile`

Angular standalone (sem NgModules), porta `4200`. O `AuthInterceptor` (`HttpInterceptor` clássico,
registrado via `HTTP_INTERCEPTORS`) injeta `Authorization: Bearer <token>` em toda chamada HTTP a
partir do token salvo no `localStorage` no login - a tela de login, o fluxo de solicitar crédito e a
listagem de propostas do cliente não mudaram de layout nem de navegação, só passaram a autenticar.

```bash
# a partir de fiap-bank-internet-banking-mobile/
npm install
npm start
```

## Decisões de implementação

- O cliente de login do mobile usa o CPF como `login` (mesmo valor usado como `subject`/`cpf` no
  token), mantendo o contrato que a Mesa de Operações e o mobile já usavam antes do Checkpoint 6.
- `TokenService.gerarToken` monta a claim `scope` como `ROLE_<primeiro papel>` - para um cliente vira
  `ROLE_CLIENTE` (→ `SCOPE_ROLE_CLIENTE`, igual ao exemplo do enunciado); para operador/admin vira
  `ROLE_OPERADOR`/`ROLE_ADMIN`, mantendo o mesmo método para os três perfis sem hardcode por caso de uso.
- O controller legado `/api/credito/*` foi mantido só como adapter de compatibilidade (delega para os
  mesmos casos de uso do controller `/api/v1/solicitacoes`), conforme pedido no enunciado - não é um
  novo fluxo de negócio.
- `CoreBancarioGateway` é uma interface do módulo `application`; a implementação HTTP
  (`CoreBancarioHttpClient`) mora em `infrastructure`, então o domínio nunca sabe que existe uma
  chamada de rede por trás da consulta de conta.
