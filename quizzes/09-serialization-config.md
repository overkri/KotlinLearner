# Квиз 09 — Сериализация и конфигурация

## 1. Зачем `@Serializable`, если есть рефлексия?
<details><summary>Ответ</summary>Плагин генерирует сериализатор на этапе компиляции: нет рефлексии в рантайме, меньше сюрпризов, работает на Kotlin/JS/Native. Рефлексия в Jackson требует `kotlin-reflect` и иногда не видит default-аргументы.</details>

## 2. Что делает `explicitNulls = false`?
<details><summary>Ответ</summary>При сериализации не пишет поля со значением `null`. При десериализации отсутствующее поле получает default/null. Это не то же самое, что «удалить поле из контракта».</details>

## 3. Что выведет?
```kotlin
@Serializable data class A(val x: Int = 1, val y: String? = null)
Json { encodeDefaults = true; explicitNulls = false }.encodeToString(A())
```
<details><summary>Ответ</summary>`{"x":1}`. Default-значение записано, null-поле опущено.</details>

## 4. Зачем `classDiscriminator = "type"`?
<details><summary>Ответ</summary>Для полиморфной сериализации sealed-иерархии JSON содержит поле `type` с именем подтипа, чтобы десериализатор знал, какой класс создать. Имя можно переопределить через `@SerialName`.</details>

## 5. Почему `Money` лучше не сериализовать как `Double`?
<details><summary>Ответ</summary>`Double` теряет точность на десятичных дробях (0.1 + 0.2 != 0.3). В финансах используют минорные единицы `Long`/`BigDecimal`/строку. Наш формат — `amountMinor: Long` + `currency`.</details>

## 6. Что не так с этим data class?
```kotlin
data class DbConfig(val url: String, val password: String)
```
<details><summary>Ответ</summary>`toString()` сгенерируется и напечатает пароль в логах/ошибках. Нужно переопределить `toString`, использовать redacted-обёртку или не логировать конфиг целиком.</details>

## 7. Где валидировать `amountMinor > 0` — в DTO или домене?
<details><summary>Ответ</summary>В домене (в `Money`/transfer engine), потому что инвариант должен соблюдаться независимо от источника данных. DTO может дать более дружелюбное сообщение, но не должен быть единственной защитой.</details>

## 8. Почему `ignoreUnknownKeys = true` может быть опасен?
<details><summary>Ответ</summary>Опечатка в имени поля не вызовет ошибку, и значение молча потеряется. Для публичного API лучше `false`, для consumer'ов с обратной совместимостью — `true`.</details>
