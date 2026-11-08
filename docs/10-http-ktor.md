# 10 — От голого HTTP к Ktor

## Цель
Понять, что Spring MVC/WebFlux скрывает за аннотациями: маршрутизацию, path-параметры, content negotiation, обработку ошибок и тестирование HTTP без запуска реального порта.

## Предусловия
Модули 03–09. `TransferRequestDto`, `TransferResponseDto`, `ErrorDto`, `ledgerJson`, `toDomain/toDto` готовы.

## Шпаргалка
- Ktor-приложение — это `Application` с явной установкой плагинов: `install(ContentNegotiation)`, `install(StatusPages)`.
- `routing { get("/path") { ... } }`; path-параметры: `call.parameters["id"]`.
- `call.receive<T>()` — аналог `@RequestBody`; `call.respond(status, body)` — аналог `ResponseEntity`.
- `StatusPages` — аналог `@ControllerAdvice`: централизованно превращает исключения в ответы.
- `testApplication { application { module() } }` — тест без реального порта, аналог MockMvc.
- `MiniRouter` показывает, что routing — это обычное сопоставление сегментов и методов.
- HTTP-коды — часть контракта: 400 (форма), 404 (нет ресурса), 409 (конфликт состояния), 422 (семантически неверно), 500 (баг/инфраструктура).

## Java → Ktor
| Spring | Ktor |
|---|---|
| `@RestController` | `routing { }` + функции-обработчики |
| `@GetMapping("/x")` | `get("/x") { }` |
| `@PathVariable` | `call.parameters["id"]` |
| `@RequestBody` | `call.receive<T>()` |
| `ResponseEntity` | `call.respond(status, body)` |
| `@ControllerAdvice` | `install(StatusPages) { exception<...> { } }` |
| `@Valid` | `toDomain()` + `require`/валидаторы |
| `MockMvc` | `testApplication { }` |
| `@SpringBootTest(webEnvironment = RANDOM_PORT)` | `testApplication` / `embeddedServer` |
| `HttpMessageConverter` | `ContentNegotiation` + `json(...)` |

## Порядок обработки запроса
1. Ktor парсит HTTP и выбирает маршрут по методу/сегментам.
2. Плагины (логирование, auth, metrics) выполняют `intercept`.
3. `call.receive<T>()` десериализует тело; ошибка -> исключение -> `StatusPages`.
4. Доменный вызов возвращает `TransferResult`.
5. `call.respond` сериализует DTO через ContentNegotiation.
6. `StatusPages` перехватывает необработанные исключения и формирует `ErrorDto`.

## Пять ловушек
1. **Отдавать доменные объекты напрямую**. `Account` не `@Serializable` и содержит бизнес-инварианты; наружу должен уходить `AccountDto`.
2. **Ловить все исключения и возвращать 200**. Ошибка должна иметь код; иначе клиент не отличит успех от сбоя.
3. **Возвращать 500 на невалидный JSON**. Это ошибка клиента (400), а не сервера. `StatusPages` должен различать `BadRequestException`/`ContentTransformationException`.
4. **Забыть `contentType(ContentType.Application.Json)` в тесте**. Без него ContentNegotiation может не выбрать JSON-конвертер, и тест упадёт не по той причине.
5. **Проверять только статус**. Контракт включает тело `ErrorDto`; тестируйте `code`, чтобы клиент мог обрабатывать ошибки программно.
