# Java → Kotlin: одностраничная шпаргалка

| Java | Kotlin |
|---|---|
| `final class User { }` | `class User` (final по умолчанию) |
| `class User` (наследуемая) | `open class User` |
| `String name = null;` | `var name: String? = null` |
| `@Nullable String` | `String?` |
| `@NonNull String` | `String` |
| `String!` (platform type) | тип из Java без аннотаций; требует ручной проверки |
| `name.length()` | `name.length` (свойство) |
| `user.getName()` | `user.name` |
| `new User("a", 1)` | `User("a", 1)` |
| `record User(String name, int age) {}` | `data class User(val name: String, val age: Int)` |
| `enum class` | `enum class` |
| `interface` + `default`-методы | `interface` с телами методов |
| `static` | top-level-функция / `object` / `companion object` |
| `static final int MAX = 10;` | `const val MAX = 10` (top-level/companion) |
| `switch` | `when` (выражение) |
| `if` / `try` как statement | `if` / `try` как выражение |
| `for (int i = 0; i < n; i++)` | `for (i in 0 until n)` |
| `for (String s : list)` | `for (s in list)` |
| `list.forEach(x -> ...)` | `list.forEach { ... }` |
| `list.stream().filter(...).collect(...)` | `list.filter { ... }` (eager) / `.asSequence()` (lazy) |
| `Optional<T>` | `T?` + `?.` / `?:` / `let` |
| `Objects.requireNonNull(x)` | `requireNotNull(x)` |
| `x != null ? x : y` | `x ?: y` |
| `if (x instanceof Foo f)` | `if (x is Foo)` + smart cast |
| `(Foo) x` | `x as Foo` / `x as? Foo` |
| `try (var r = open()) { }` | `open().use { }` |
| `throws IOException` | нет checked exceptions; `@Throws(IOException::class)` для Java |
| `CompletableFuture<T>` | `suspend fun` / `Deferred<T>` |
| `Thread.sleep(100)` | `delay(100)` (в корутине) |
| `ExecutorService` | `Dispatchers.IO` / `Dispatchers.Default` |
| `synchronized` | `Mutex` / `Atomic*` / `StateFlow` |
| `Stream<T>` | `Flow<T>` / `Sequence<T>` |
| `List<T>` (mutable) | `List<T>` read-only, `MutableList<T>` изменяемый |
| `Map<K,V>` | `Map<K,V>` / `MutableMap<K,V>` |
| `String.format("%s", x)` | `"$x"` / `"%s".format(x)` |
| `StringBuilder` | `buildString { append(...) }` |
| getter/setter | свойство с `get()` / `set(value)` |
| `equals` / `hashCode` / `toString` | `data class` генерирует |
| `clone()` | `copy()` у `data class` |
| `BigDecimal`/`long` центы | `value class Money(val minor: Long)` |
| `@Autowired` конструктор | конструктор + ручной composition root |
| `@Component` | класс в composition root |
| `@ConfigurationProperties` | `data class AppConfig` + загрузчик |
| `@Transactional` | `suspendTransaction { }` / `TxRunner` |
| `@KafkaListener` | цикл `poll` в корутине |
| `RedisTemplate` | Lettuce coroutines API |
| Jackson | kotlinx.serialization |
| Lombok | `data class`, свойства, `value class` |
| `@Valid` | `RequestValidation` / явные валидаторы |

## Полезные идиомы
```kotlin
// read-only по умолчанию
val ids = accounts.map { it.id }

// безопасная цепочка
val domain = customer.email?.substringAfter('@') ?: "unknown"

// when как выражение
val label = when (status) {
    ACTIVE -> "active"
    FROZEN -> "frozen"
    CLOSED -> "closed"
}

// scope-функции
val request = TransferRequestBuilder().apply {
    from = source
    to = target
}.build()

// корутины
suspend fun load(id: AccountId): Account =
    withContext(Dispatchers.IO) { repository.find(id) ?: error("not found") }
```
