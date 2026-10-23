# Task API (Ktor)
Учебный REST API на Kotlin и Ktor для управления задачами. Демонстрирует GET, POST, DELETE, JSON через ContentNegotiation + kotlinx.serialization, query/path параметры и HTTP-статусы.

## Требования
- JDK 17+
- IntelliJ IDEA (рекомендуется)
- Gradle (или запуск через Gradle tool window в IntelliJ)

## Запуск
Откройте папку проекта в IntelliJ IDEA, дождитесь синхронизации Gradle и запустите `src/main/kotlin/com/example/Application.kt` (main). Сервер: `http://localhost:8080`.
Если Gradle установлен: `gradle test` и `gradle run`.

## Маршруты
- `GET /` — JSON-описание API.
- `GET /tasks` — все задачи.
- `GET /tasks?completed=true` — фильтр по query-параметру `completed` (`true`/`false`).
- `GET /tasks/{id}` — задача по path-параметру ID.
- `POST /tasks` — создать задачу, JSON-тело: `{"title":"Study Ktor","completed":false}`.
- `DELETE /tasks/{id}` — удалить задачу по ID.

## HTTP-статусы
- `200 OK` — успешный GET.
- `201 Created` — задача создана.
- `204 No Content` — задача удалена.
- `400 Bad Request` — некорректный ID, query или JSON.
- `404 Not Found` — задача не найдена.

Ошибки возвращаются JSON, например `{"error":"Task with id 999 was not found"}`.

## Тесты
В IntelliJ откройте Gradle tool window и запустите `Tasks > verification > test`. Тесты проверяют JSON, создание задачи и ошибки 400/404.

## Структура
```text
task-api-ktor/
├── build.gradle.kts
├── settings.gradle.kts
├── README.md
└── src/
    ├── main/kotlin/com/example/
    │   ├── Application.kt
    │   ├── Routing.kt
    │   ├── Task.kt
    │   └── TaskStore.kt
    └── test/kotlin/com/example/TaskApiTest.kt
```

## Архитектура
`Application.kt` запускает сервер и подключает JSON-сериализацию; `Routing.kt` отвечает за HTTP-маршруты и валидацию; `Task.kt` описывает сериализуемые модели; `TaskStore.kt` хранит и изменяет задачи; `TaskApiTest.kt` проверяет API. Данные хранятся в памяти и сбрасываются при перезапуске — для учебного проекта это позволяет сосредоточиться на HTTP и JSON без настройки базы данных.
