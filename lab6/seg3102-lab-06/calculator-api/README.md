<!-- Haojian Wang, 300411829 -->
# Lab 6 Exercise – REST Calculator Web Service

Spring Boot (Kotlin) REST API with OpenAPI documentation.

| Operation | Request |
|---|---|
| Addition | `GET /calculator/add/{number1}/{number2}` |
| Subtraction | `GET /calculator/subtract/{number1}/{number2}` |
| Multiplication | `GET /calculator/multiply/{number1}/{number2}` |
| Division | `GET /calculator/divide/{number1}/{number2}` |

Invalid numbers and division by zero return `400 Bad Request`.

Run: `./gradlew bootRun`, then open http://localhost:8080/calculator/add/2/3

OpenAPI docs: http://localhost:8080/v3/api-docs (JSON), http://localhost:8080/v3/api-docs.yaml (YAML),
http://localhost:8080/swagger-ui.html (Swagger UI)

Tests: `./gradlew test`
