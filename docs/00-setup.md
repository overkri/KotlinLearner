# 00 — Kotlin, Gradle и Java interop

## Как устроена сборка
- **Gradle Kotlin DSL**: `build.gradle.kts` — это Kotlin-код, а не XML. Плагины подключаются через `plugins { }`, зависимости — через `dependencies { }`.
- **Version catalog** `gradle/libs.versions.toml`: единое место для версий. В build-скриптах используются типизированные аксессоры `libs.ktor.server.core`.
- **Toolchain**: `kotlin { jvmToolchain(21) }` фиксирует JDK 21 независимо от того, какой JVM запустила Gradle. Это важно: Gradle может работать на JDK 25, а компилировать и тестировать проект на JDK 21.
- **Source sets**: `src/main/kotlin` и `src/main/java` компилируются вместе. Kotlin-компилятор первым видит Java-классы, поэтому Kotlin может вызывать Java без прослойки.
- **K2** — компилятор Kotlin 2.x. Он быстрее и строже старого K1; часть ошибок, которые K1 пропускал, теперь видна сразу.

## Как Kotlin попадает на JVM
1. `.kt` → Kotlin-компилятор (`kotlinc`) → `.class` с JVM-байткодом.
2. Top-level-функция `environmentSummary()` в файле `Environment.kt` превращается в статический метод класса `EnvironmentKt`.
3. `object` — singleton с полем `INSTANCE`; `companion object` — вложенный singleton.
4. Default-аргументы компилируются в synthetic-метод `foo$default(...)`, а не в настоящую Java-перегрузку.
5. `suspend`-функция получает дополнительный параметр `Continuation`; корутины — это не потоки.

## Java ↔ Kotlin: минимум для старта
| Java | Kotlin |
|---|---|
| `final class Foo` | `class Foo` (final по умолчанию) |
| `String` может быть `null` | `String` не может; `String?` может |
| `String!` в Kotlin | platform type: Java-тип без null-аннотации |
| `foo(null)` + перегрузки | default-аргументы и именованные аргументы |
| `Objects.requireNonNull(x)` | `requireNotNull(x)`, `checkNotNull(x)` |
| `IllegalArgumentException` | `require(...)`, `require(...) { "message" }` |
| `static` | top-level-функция, `object`, `companion object` |
| try-with-resources | `use { }` |
| checked exceptions | checked exceptions в Kotlin нет; `@Throws` нужен для Java-вызывающих |

## MockK в двух словах
```kotlin
val clock = mockk<Clock>()
every { clock.now() } returns Instant.parse("2026-11-08T12:00:00Z")

printer.print(1_234, "USD")

verify(exactly = 1) { clock.now() }
```
MockK — Kotlin-first аналог Mockito. Он умеет мокать `final`-классы, `object` и `companion object`, но для этого использует inline-агент. В тестах этого курса моки применяются точечно; для репозиториев чаще пишем hand-written fake.

## Пять ловушек
1. **Platform type**. `LegacyFeeCalculator.describe()` возвращает `String!`. Компилятор разрешает `val s: String = describe(...)`, но в рантайме может прилететь `NullPointerException`. Если Java-метод может вернуть null, сразу ставьте `?` и обрабатывайте.
2. **`!!` — это не «я уверен», а «уронить сервис»**. В эталонных решениях курса `!!` запрещён. Исключение — специально помеченные «плохие» примеры.
3. **Default-аргументы не создают Java-перегрузку**. Java-код увидит только полную сигнатуру и `foo$default`; если метод должен быть удобен из Java, добавьте `@JvmOverloads`.
4. **`build -x test` не равен «тесты компилируются»**. Флаг `-x test` исключает выполнение, но не обязательно компиляцию тестов. Для гейта отдельно запускайте `test -Pmodule=NN`.
5. **Тег и фильтр — разные вещи**. `@Tag("m00")` помечает тест, а `-Pmodule=00` превращается в `includeTags("m00")`. Если тег забыт, тест молча не запустится в модульном прогоне.
