# Квиз 14 — Spring Boot + Kotlin, ревью, миграция

## 1. Зачем нужен плагин `kotlin-spring`?
<details><summary>Ответ</summary>Он делает классы/методы с Spring-аннотациями (`@Component`, `@Service`, `@RestController`, `@Transactional` и др.) `open`, чтобы CGLIB мог создать proxy. Без него final-класс нельзя проксировать, и `@Transactional` может не сработать.</details>

## 2. Зачем `kotlin-jpa` (noarg)?
<details><summary>Ответ</summary>Hibernate требует no-arg конструктор у `@Entity`. Плагин генерирует его для классов с `@Entity` (и других JPA-аннотаций).</details>

## 3. Почему `@Transactional` на приватном методе не работает?
<details><summary>Ответ</summary>Spring создаёт proxy вокруг бина; приватные методы не переопределяются, а self-invocation идёт мимо proxy. Транзакция не начинается. Нужен public-метод и вызов через бин.</details>

## 4. Что не так с кодом?
```kotlin
@Service
class S(private val repo: Repo) {
    fun transfer(id: Long) {
        val a = repo.findById(id)!!
        a.balance -= 100
        repo.save(a)
    }
}
```
<details><summary>Ответ</summary>`!!` может упасть; мутабельная entity без транзакции/версии; нет проверки статуса/валюты/средств; нет DTO/ошибок; lost update. Нужны `findById(id) ?: throw ...`, `@Transactional`, optimistic locking и бизнес-проверки.</details>

## 5. Почему Entity нельзя отдавать из контроллера?
<details><summary>Ответ</summary>Ленивые поля могут не загрузиться, наружу утечёт схема БД, изменятся контракты API, появятся лишние/чувствительные поля. Нужен DTO.</details>

## 6. Что генерирует `@JvmOverloads`?
<details><summary>Ответ</summary>Java-перегрузки для функции с default-аргументами. Без неё Java видит только полную сигнатуру и synthetic `$default`.</details>

## 7. Что делает `@Throws`?
<details><summary>Ответ</summary>Добавляет в байткод declaration of checked exceptions, чтобы Java-вызывающие и IDE знали об исключении. Kotlin сам checked exceptions не требует.</details>

## 8. Чем Spring Boot 4 отличается от 3 для Kotlin-разработчика?
<details><summary>Ответ</summary>Spring Framework 7, Jakarta EE 11, Hibernate 7, Jackson 3 (`tools.jackson`), новые webmvc-test стартеры и пакеты тестовых аннотаций (`org.springframework.boot.webmvc.test.autoconfigure`). Часть старых импортов не скомпилируется.</details>
