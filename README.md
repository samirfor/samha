# SAMHA

Sistema web para apoio à alocação docente do IFES Colatina.

## Stack

- Frontend: Angular 13 em `samha-frontend/`
- Backend: Spring Boot 2.6 em `samha.backend/`
- Banco: MySQL 8
- Migrações: Liquibase
- Autenticação: Spring Security com JWT

## Pré-requisitos

- JDK 17
- Maven
- Node.js e npm compatíveis com Angular 13
- Docker e Docker Compose

## Como subir tudo

Use Docker Compose para subir a stack inteira:

```bash
docker compose up --build
```

Serviços expostos:

- Frontend: [http://localhost:80](http://localhost:80)
- Backend: [http://localhost:8080](http://localhost:8080)
- phpMyAdmin: [http://localhost:9001](http://localhost:9001)

O MySQL fica disponível apenas na rede interna do Compose e usa os dados de desenvolvimento definidos em `docker-compose.yml`.

## Usuário admin inicial

O `docker-compose.yml` já ativa o bootstrap do usuário admin com estas variáveis:

- `SAMHA_BOOTSTRAP_ADMIN_ENABLED=true`
- `SAMHA_BOOTSTRAP_ADMIN_LOGIN=admin`
- `SAMHA_BOOTSTRAP_ADMIN_PASSWORD=123`
- `SAMHA_BOOTSTRAP_ADMIN_PAPEL_NOME=COORDENADOR_ACADEMICO`

O papel `COORDENADOR_ACADEMICO` é criado pelas migrações do Liquibase. No primeiro start, o backend cria o usuário admin se ele ainda não existir; se já existir, ele ajusta senha e papel para ficar alinhado com as variáveis do Compose.

Esse bootstrap é só para desenvolvimento. Em produção, desative o bootstrap e use senha forte e segredo gerenciado fora do repositório.

## Testes

Use o Compose também para validar:

```bash
docker compose --profile test run --rm --build backend-tests
docker compose --profile test run --rm --build frontend-tests
```

## Contexto

O projeto nasceu da migração de uma aplicação Java Swing para uma aplicação web. Foi usado como trabalho final e publicado na Revista Contemporânea.
