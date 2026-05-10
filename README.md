# Auth Backend ( Spring Boot )

Skeleton Spring Boot project providing JWT auth endpoints and a simple data save API backed by Postgres.

Endpoints:
- POST /api/auth/register { email, password }
- POST /api/auth/login { email, password } -> { token }
- POST /api/data/save (protected, Bearer token) -> saves JSON body as text

Setup:
1. Ensure PostgreSQL is running and create database `loan_db` (or change application.properties).
2. Build: ./gradlew bootJar
3. Run: java -jar build\libs\auth-backend-1.0-SNAPSHOT.jar

Notes:
- Update jwt.secret in application.properties to a secure random value.
- This is a minimal skeleton; adjust entities and validation when actual frontend payload is available.
