# 09 — Сериализация и конфигурация

## Цель
Описать JSON-контракты через `kotlinx.serialization`, сделать кастомный сериализатор для `Money` и собрать типизированный `AppConfig` из environment.

## Предусловия
Модули 03–06. `@Serializable` DTO не должны протекать в домен.

## Шпаргалка
- `@Serializable` — плагин компилятора генерирует сериализатор без рефлексии.
- `@SerialName` — имя поля/подтипа в JSON.
- `@Serializable(with = ...)` / `KSerializer` — кастомный формат.
- `Json { ignoreUnknownKeys = true; explicitNulls = false; encodeDefaults = true }`.
- `classDiscriminator = "type"` — поле-дискриминатор для sealed/polymorphic.
- `encodeDefaults` — писать поля со значениями по умолчанию.
- `explicitNulls = false` — не писать `null`-поля.
- Конфиг: fail-fast при старте, дефолты для необязательных, trim значений, секреты не логировать.
- DTO ↔ домен: маппинг на границе; домен не знает про JSON, DTO не знает про инварианты.

## Java → Kotlin
| Java | Kotlin |
|---|---|
| Jackson `@JsonProperty` | `@SerialName` |
| `ObjectMapper` | `Json { }` |
| `JsonSerializer<T>` | `KSerializer<T>` |
| `FAIL_ON_UNKNOWN_PROPERTIES=false` | `ignoreUnknownKeys = true` |
| `@JsonInclude(NON_NULL)` | `explicitNulls = false` |
| `@JsonTypeInfo` | `classDiscriminator` + sealed |
| `@ConfigurationProperties` | `data class AppConfig` + loader |
| `@Value("${DB_URL}")` | `env["DB_URL"]` + `require` |
| `@Validated` | `require` в загрузчике/домене |

## Деньги на проводе
Не передавайте `Double` для денег. Варианты:
- `amountMinor: Long` + `currency: String` — простой и явный контракт;
- строка `"1234 USD"` через кастомный сериализатор — компактно, но требует строгого парсера;
- `{"minor":1234,"currency":"USD"}` — расширяемо.

В курсе API использует `amountMinor` + `currency`, а `MoneyAsStringSerializer` показывает, как сделать кастомный формат для событий/CLI.

## Конфигурация
```kotlin
data class AppConfig(val http: HttpConfig, val db: DbConfig, val jwt: JwtConfig)

fun loadConfig(env: Map<String, String>): AppConfig {
    val url = env["DB_URL"]?.trim()?.takeIf { it.isNotEmpty() }
        ?: error("DB_URL is required")
    // ...
}
```
- Ошибка конфигурации должна падать при старте, а не при первом запросе.
- Дефолты — только для безопасных значений (host, port, pool size), не для паролей/URL БД.
- Секреты нельзя печатать в лог и отдавать в `/actuator/env` без маскировки.

## Пять ловушек
1. **`ignoreUnknownKeys = true` маскирует несовместимость**. Для публичного API лучше падать на неизвестном поле; для consumer'а событий — игнорировать, чтобы не падать при добавлении поля.
2. **`explicitNulls = false` не означает «поле отсутствует при decode»**. Missing-поле с дефолтом будет `null`/default; отсутствие и явный null — разные вещи.
3. **`@Serializable` и рефлексия**. Без плагина `kotlinx.serialization` не найден сериализатор; проверяйте, что плагин применён к модулю.
4. **Пароль в `toString()` data class**. `data class DbConfig(... password ...)` печатает пароль в логах. Переопределите `toString` или используйте отдельный redacted-тип.
5. **DTO с бизнес-валидацией**. Если `TransferRequestDto` начнёт проверять статусы счетов, домен и API переплетутся. DTO валидирует форму, домен — инварианты.
