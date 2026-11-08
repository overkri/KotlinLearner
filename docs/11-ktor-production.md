# 11 — Ktor для продакшена

## Цель
Добавить к Ktor-сервису то, что Spring Boot даёт «из коробки»: JWT-аутентификацию, health/readiness, метрики Prometheus, request id и graceful shutdown.

## Предусловия
Модуль 10. `TestJwt` генерирует локальные RSA-ключи; Keycloak в тестах не нужен.

## Шпаргалка
- **JWT**: `install(Authentication) { jwt("auth-jwt") { verifier(...); validate { ... } } }`; защита — `authenticate("auth-jwt") { }`.
- Проверяйте подпись **и** `issuer`, `audience`, `exp`; иначе токен другого сервиса пройдёт.
- **Liveness** (`/health/live`) — процесс жив; **readiness** (`/health/ready`) — можно принимать трафик. Liveness не должен зависеть от БД.
- **Метрики**: `MicrometerMetrics` + `PrometheusMeterRegistry`; JVM-биндеры; `/metrics` в текстовом формате.
- **Request ID**: заголовок `X-Request-Id`, атрибут вызова, ответный заголовок; в логах — MDC.
- **Graceful shutdown**: readiness -> false, затем дренаж in-flight запросов, затем остановка consumer'ов.
- Не логируйте токены, пароли, PAN/CVV; маскируйте секреты.
- Метрики с `accountId`/`userId` в labels — высокая кардинальность; используйте ограниченные наборы (status, error code, route).

## Java → Ktor / Spring
| Spring | Ktor |
|---|---|
| `spring-boot-starter-security` + Keycloak | `ktor-server-auth-jwt` |
| `@PreAuthorize` | `authenticate("auth-jwt") { }` + проверка claim |
| `SecurityFilterChain` | `install(Authentication) { }` |
| Actuator `/health/liveness` | свой `/health/live` |
| Actuator `/health/readiness` | свой `/health/ready` + `HealthCheck` |
| Actuator `/prometheus` | `MicrometerMetrics` + `/metrics` |
| `OncePerRequestFilter` + MDC | `createApplicationPlugin` + attributes |
| `server.shutdown=graceful` | `ApplicationStopping` + readiness |
| `@KafkaListener` (остановка) | отмена consumer-корутин по shutdown |

## JWT в тестах и проде
- В тестах `TestJwt` подписывает токен приватным ключом, а `JWTVerifier` проверяет публичным. Это быстрее и надёжнее, чем поднимать Keycloak.
- В проде verifier получает JWKS по URL (`JwkProvider`/Keycloak); ключи ротируются, поэтому кэшируйте их и обрабатывайте `kid`.
- `validate` должен проверять не только подпись, но и наличие subject/ролей, если они нужны бизнесу.

## Пять ловушек
1. **Проверять только подпись**. Без `issuer`/`audience` токен, выпущенный для другого сервиса тем же ключом, пройдёт.
2. **Liveness проверяет БД**. При недоступной БД Kubernetes начнёт перезапускать поды — каскадный сбой. Зависимости — в readiness.
3. **Секреты в логах**. `log.info("config=$config")` напечатает пароль. Используйте redaction и не логируйте токены.
4. **`/metrics` без auth**. Метрики могут раскрыть внутреннюю топологию; закройте их network policy/basic auth.
5. **Мгновенный `exit`**. In-flight запросы и Kafka-сообщения теряются. Сначала readiness=false, затем дренаж, потом остановка.
