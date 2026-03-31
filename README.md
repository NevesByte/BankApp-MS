# BankApp - Microservices Banking Platform

BankApp e uma plataforma bancaria orientada a microsservicos, com foco em autenticacao segura, operacoes financeiras e comunicacao assincrona por mensageria.

O objetivo do projeto e demonstrar engenharia backend moderna com Java/Spring, cobrindo boas praticas de organizacao, validacao, tratamento de erros, testes unitarios com Mockito/JUnit, documentacao OpenAPI e containerizacao profissional.

## Visao Geral

A solucao e composta por tres servicos independentes:

- `login-ms`: cadastro de usuario, autenticacao e emissao de JWT.
- `painel-ms`: consultas de conta, emprestimos e transferencias.
- `email-sender-ms`: envio de notificacoes por email consumidas de fila.

## Arquitetura

### Stack

- Java 17
- Spring Boot
- Spring Security + OAuth2 Resource Server (JWT)
- Spring Data JPA
- RabbitMQ
- PostgreSQL (`login-ms`)
- MySQL (`painel-ms`)
- Mockito + JUnit 5
- Swagger/OpenAPI (`springdoc`)
- Docker + Docker Compose

### Fluxo de negocio principal

1. Usuario realiza cadastro no `login-ms`.
2. `login-ms` persiste credenciais e dados da conta.
3. `login-ms` publica eventos em filas (`email-queue` e `user-data`).
4. `painel-ms` consome `user-data` e replica dados essenciais da conta.
5. `email-sender-ms` consome `email-queue` e envia notificacoes.
6. Usuario autenticado usa JWT para acessar endpoints financeiros no `painel-ms`.

## Melhorias Implementadas

### Engenharia de codigo

- refatoracao de controladores e servicos para responsabilidades mais claras;
- adocao de injecao por construtor nas classes principais;
- ajustes de DTOs para contratos mais seguros;
- padronizacao de rotas REST com prefixo ` /api `.

### Tratamento de erro profissional

- criacao de excecoes de dominio (`ResourceNotFoundException`, `ConflictException`, `InsufficientBalanceException`, etc.);
- implementacao de `@RestControllerAdvice` com payload padrao de erro (`status`, `error`, `message`, `path`, `timestamp`);
- respostas HTTP semanticamente corretas (`400`, `401`, `404`, `409`, `500`).

### Validacao

- validacoes de payload com Bean Validation (`@NotBlank`, `@Email`, `@DecimalMin`, `@Min`, `@NotNull`).

### Swagger/OpenAPI

- OpenAPI configurado em:
  - `login-ms`
  - `painel-ms`
- endpoints documentados com tags e operacoes.

### Testes unitarios (Mockito + JUnit)

Foram adicionados testes de unidade para cenarios criticos:

- `login-ms`
  - `UserServiceTest`
- `painel-ms`
  - `TransferenciaServiceTest`
  - `EmprestimoServiceTest`
- `email-sender-ms`
  - `EmailServiceTest`

Cobrem fluxos de sucesso e erro (conflito, credenciais invalidas, saldo insuficiente, usuario inexistente e falha de envio de email).

### Seguranca e configuracao

- remocao de credenciais sensiveis hardcoded dos `application.properties`;
- externalizacao de configuracoes para variaveis de ambiente;
- liberacao de rotas do Swagger e protecao das demais rotas por JWT.

### Containerizacao

- `Dockerfile` multi-stage para cada microsservico;
- `docker-compose.yml` com stack completa:
  - RabbitMQ (com management UI)
  - PostgreSQL
  - MySQL
  - login-ms
  - painel-ms
  - email-sender-ms

## Endpoints Principais

### login-ms

Base URL: `http://localhost:1010`

- `POST /api/auth/signup`
- `POST /api/auth/login`

Swagger:
- `http://localhost:1010/swagger-ui.html`

### painel-ms

Base URL: `http://localhost:1013`

- `GET /api/account/saldo`
- `POST /api/transferencias`
- `GET /api/transferencias`
- `POST /api/emprestimos`
- `GET /api/emprestimos`
- `DELETE /api/emprestimos/parcelas/{id}`
- `GET /api/auth/validar-token`

Swagger:
- `http://localhost:1013/swagger-ui.html`

## Como Executar

### 1) Com Docker Compose

Na raiz do projeto:

```bash
docker compose up --build
```

### 2) Execucao local por servico

Em cada pasta de microsservico:

```bash
./mvnw spring-boot:run
```

## Testes

Executar testes em cada servico:

```bash
./mvnw test
```

## Estrutura de Pastas

```text
BankApp/
  login-ms/
  painel-ms/
  email-sender-ms/
  docker-compose.yml
```
