# nimbusdesk-identity-service

Authentication and JWT issuance for the [NimbusDesk](https://github.com/eliangilsierra/nimbusdesk-infra) system.

Built with a classic layered architecture (`controller` → `service` → `persistence`) rather than hexagonal — intentionally, not by oversight. See [ADR-0003](https://github.com/eliangilsierra/nimbusdesk-infra/blob/develop/adr/0003-identity-service-arquitectura-por-capas.md) for why, and the note there on how this gets revisited in v2.

This service issues tokens; it does not enforce authorization itself. The API Gateway validates every token before routing a request (see [ADR-0002](https://github.com/eliangilsierra/nimbusdesk-infra/blob/develop/adr/0002-validacion-jwt-en-el-gateway.md)), so `/auth/**` stays open here and Spring Security's own filter chain is reduced to just supplying the `PasswordEncoder`.

## Stack

- Java 17 · Spring Boot 3.2.4 · Spring Cloud 2023.0.1 (Eureka client)
- Spring Data JPA + MySQL
- `jjwt` for token issuance, BCrypt for password hashing
- MapStruct for entity/DTO mapping

## Endpoints

| Method | Path | Description |
|---|---|---|
| `POST` | `/auth/register` | Creates a user, returns a signed JWT |
| `POST` | `/auth/login` | Validates credentials, returns a signed JWT |

## Configuration

| Env var | Default | Purpose |
|---|---|---|
| `DB_URL` / `DB_USERNAME` / `DB_PASSWORD` | local MySQL, `nimbusdesk_dev` | Database connection |
| `EUREKA_URI` | `http://localhost:8761/eureka/` | Discovery server |
| `JWT_SECRET` | dev placeholder in `application.yml` | Must match `nimbusdesk-api-gateway`'s secret |

## Run locally

Requires MySQL and `nimbusdesk-discovery-server` running (see `nimbusdesk-infra`'s `docker-compose.yml`).

```bash
mvn spring-boot:run
```

Or with Docker:

```bash
docker build -t nimbusdesk-identity-service .
docker run -p 8081:8081 \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/nimbusdesk \
  -e EUREKA_URI=http://host.docker.internal:8761/eureka/ \
  nimbusdesk-identity-service
```

## How it fits in

Part of the NimbusDesk v1 system. See [`nimbusdesk-infra`](https://github.com/eliangilsierra/nimbusdesk-infra) for the full architecture and roadmap.
