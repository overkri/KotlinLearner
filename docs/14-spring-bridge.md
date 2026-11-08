# 14 — Spring Boot 4 + Kotlin, ревью и миграция

## Цель
Перенести срез LedgerKt на Spring Boot 4 + Kotlin, увидеть, что Spring скрывает, и научиться ревьюить Kotlin-код коллег. Дополнительно — переписать Java-код на идиоматичный Kotlin.

## Предусловия
Модули 03–09, 12. `spring-bridge` и `migration-kata` настроены; H2 и JPA используются для среза.

## Шпаргалка
- **kotlin-spring** (`allopen`): `@Component`, `@Service`, `@RestController` становятся `open` — Spring может создать proxy (CGLIB) для `@Transactional`, `@Async`, `@Cacheable`.
- **kotlin-jpa** (`noarg`): Hibernate требует no-arg конструктор у `@Entity`.
- **Spring Boot 4**: Jakarta EE 11, Spring Framework 7, Hibernate 7, Jackson 3 (`tools.jackson`), новые test-стартеры (`spring-boot-starter-webmvc-test`).
- `@Transactional` работает через proxy: приватные методы и self-invocation не проксируются.
- `@RestControllerAdvice` + `@ExceptionHandler` — единый формат ошибок.
- `@ConfigurationProperties` + `@ConfigurationPropertiesScan` — типизированный конфиг; `kotlin-reflect` нужен для binding.
- DTO на границе API; Entity не отдавать наружу.
- `@JvmOverloads` — Java-перегрузки для default-аргументов; `@Throws` — checked-исключения для Java.

## Java → Kotlin/Spring
| Java/Spring | Kotlin |
|---|---|
| `@Entity` class + no-arg ctor | `class Entity(...)` + `kotlin-jpa` noarg |
| `@Service class final` | `@Service class` + `kotlin-spring` allopen |
| `@Autowired` field injection | constructor injection (Kotlin-стиль) |
| `@Transactional` | тот же, но помните про proxy |
| `@ConfigurationProperties` JavaBean | `data class` + `@ConfigurationProperties` |
| `@ControllerAdvice` | `@RestControllerAdvice` |
| `record` DTO | `data class` DTO |
| `Optional<T>` | `T?` |
| `.stream().filter()` | `filter`/`Sequence` |
| Lombok `@Data` | `data class` |
| `@JvmOverloads` (Java) | default-аргументы + `@JvmOverloads` для Java |
| `throws IOException` | `@Throws(IOException::class)` для Java-вызывающих |

## Ревью Kotlin-кода: чек-лист
1. **Null-safety**: нет `!!`; platform types из Java обработаны.
2. **Мутабельность**: доменные значения — `val`/`data class`; JPA-сущности — исключение, но не отдаются наружу.
3. **Транзакции**: `@Transactional` на public-методе бина, не на private/self-invocation.
4. **N+1**: связи JPA проверены (`@EntityGraph`, JOIN FETCH, batch size).
5. **Dual write**: БД + Kafka без outbox — потенциальная потеря события.
6. **Идемпотентность**: повтор запроса не создаёт второй перевод.
7. **Ошибки**: ожидаемые бизнес-отказы — значения; 4xx/5xx не смешаны.
8. **Секреты**: токены/пароли/ PAN не в логах и не в ответах.
9. **Java-измы**: `getX()/setX()`, `Objects.requireNonNull`, `String.format`, лишние `var`, `for (int i...)`.
10. **Тесты**: есть тест на границу (MockMvc) и на бизнес-правило (сервис).

## Плохой PR (мысленно отревьюйте)
```kotlin
@Service
class BadTransferService(
    private val accounts: AccountRepository,
    private val kafka: KafkaTemplate<String, String>,
) {
    fun transfer(id: Long, amount: BigDecimal) {
        val account = accounts.findById(id)!!
        account.balance = account.balance.subtract(amount)
        accounts.save(account)
        kafka.send("transfers", "completed:$id")
    }
}
```
Проблемы: `!!`, mutable entity без optimistic locking, нет `@Transactional`, dual write, нет idempotency, `BigDecimal` без scale/currency, отсутствие ошибок/HTTP-кодов, нет проверки статуса счёта, нет тестов.

## Пять ловушек
1. **`@Transactional` на private/self-invocation**. Proxy не перехватит вызов; транзакция не начнётся. Вынесите в отдельный бин/public-метод.
2. **Entity наружу**. Ленивые поля → `LazyInitializationException`/утечка схемы. Используйте DTO.
3. **`open` и `final`**. Без `kotlin-spring` `@Transactional`/`@Async` на final-классе не проксируются (CGLIB).
4. **Jackson 3**. Импорты `com.fasterxml.jackson` из старых примеров не совпадут; Spring Boot 4 использует `tools.jackson`.
5. **H2 ≠ Postgres**. H2 удобен для среза, но SQL-диалект, блокировки и JSON-функции отличаются; интеграционные тесты — на Testcontainers Postgres.
