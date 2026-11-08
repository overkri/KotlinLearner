# Модуль 04 — Коллекции и Sequence

## Цель
Строить отчёты по проводкам в функциональном стиле: `filter`, `map`, `groupBy`, `fold`, `associateBy`, `partition`, `chunked` — и понимать, когда `Sequence` экономит память, а когда проигрывает.

## Предусловия
Модуль 03: `Money`, `Account`, `LedgerEntry`, `Direction` готовы.

## Шпаргалка
- Kotlin-коллекции делятся на **read-only** (`List`, `Map`) и **mutable** (`MutableList`, `MutableMap`). Read-only — это интерфейс, а не глубокая неизменяемость.
- `filter`, `map`, `sortedBy`, `groupBy`, `associateBy`, `partition`, `chunked`, `take`, `sumOf`, `fold`.
- `Sequence` — ленивый конвейер: операции не выполняются, пока не вызвана терминальная (`toList`, `first`, `count`, `forEach`).
- `List`-операции создают промежуточные коллекции; `Sequence` — нет, но имеет накладные расходы на каждый элемент.
- `groupBy` возвращает `Map<K, List<V>>`; `groupingBy { }.eachCount()` — счётчики без материализации групп.
- `associateBy` при дубликате ключа оставляет последнее значение.
- `partition` возвращает `Pair<List<T>, List<T>>`; удобно деструктурировать: `val (active, other) = ...`.

## Eager vs lazy
```kotlin
// Eager: три промежуточных списка
list.filter { it.amount.minor > 100 }
    .map { it.accountId }
    .take(10)

// Lazy: один проход, останавливается после 10
list.asSequence()
    .filter { it.amount.minor > 100 }
    .map { it.accountId }
    .take(10)
    .toList()
```
Правило: `Sequence` выгодна на больших или бесконечных источниках и длинных цепочках; на маленьких списках обычные коллекции часто быстрее и читаемее.

## Java → Kotlin
| Java | Kotlin |
|---|---|
| `stream().filter(...).collect(toList())` | `filter { ... }` (List) / `.asSequence().filter { ... }.toList()` |
| `Collectors.groupingBy` | `groupBy { ... }` |
| `Collectors.counting` | `groupingBy { ... }.eachCount()` |
| `Collectors.toMap` | `associateBy { ... }` / `associate { ... }` |
| `Collectors.partitioningBy` | `partition { ... }` |
| `Lists.partition` | `chunked(size)` |
| `stream().sorted(comparing(...))` | `sortedWith(compareBy(...))` |
| `stream().limit(n)` | `take(n)` |
| `stream().mapToLong(...).sum()` | `sumOf { ... }` |
| `Optional<T>` из `findFirst` | `firstOrNull()` |
| `for (var e : map.entrySet())` | `for ((k, v) in map)` |
| `Collections.unmodifiableList` | тип `List` (но нужна дисциплина) |

## Пять ловушек
1. **`sortedBy` стабилен, но не определяет полный порядок**. При одинаковых `createdAt` порядок зависит от входного списка; для воспроизводимости добавляйте tie-breaker (`thenBy { it.id }`).
2. **`Sequence` можно пройти только один раз**. Повторный `forEach`/`toList` на одном и том же sequence может бросить `IllegalStateException` или дать неожиданный результат, если источник одноразовый.
3. **`groupBy` материализует все группы**. Для больших данных используйте `groupingBy` + `fold`/`eachCount` или БД-агрегацию.
4. **`List` read-only не значит immutable**. Если передать `MutableList` как `List`, другой код может изменить его через исходную ссылку. Для домена используйте `listOf`/`buildList` и не отдавайте mutable-ссылки.
5. **`sumOf` может переполниться**. `Long` в финансах — не бесконечность; для больших агрегатов используйте `BigInteger`/`BigDecimal` или проверяйте `Math.addExact`.
