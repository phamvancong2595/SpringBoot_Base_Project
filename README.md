# Task Management System (Mini Jira) — Spring Boot Base Project

A production-ready Spring Boot base project built with **Clean Architecture**, providing a full-featured task/project management system. It includes two-tier RBAC, stateless JWT authentication with Refresh Token rotation, Flyway-managed schema migrations, and first-class observability via ELK Stack and Sentry.

---

## Tech Stack

| Category | Technology | Version |
|---|---|---|
| Language | Java | 21 (LTS) |
| Framework | Spring Boot | 4.0.5 |
| ORM | Spring Data JPA + Hibernate | (Boot managed) |
| Database | MySQL | 8.x |
| Security | Spring Security, JJWT | 0.13.0 |
| API Docs | SpringDoc OpenAPI (Swagger UI) | 3.0.2 |
| Caching | Caffeine Cache | (Boot managed) |
| Email | Spring Mail + `@Async` | (Boot managed) |
| DB Migration | Flyway (flyway-mysql) | (Boot managed) |
| Monitoring | Sentry for Spring Boot 4 | 8.27.0 |
| Logging | Logstash Logback Encoder | 7.4 |
| Utilities | Lombok, Jakarta Validation, Spring Dotenv | 1.18.32 / 4.0.0 |
| Test | JUnit 5, Spring Security Test, H2 (in-memory) | (Boot managed) |

---

## Key Features

### Authentication & Security
- Registration / Login with **BCrypt** password encoding.
- Issues short-lived **Access Tokens** and long-lived **Refresh Tokens** (JWT).
- Automatic Refresh Token **revocation** on suspicious reuse or explicit logout.
- `JwtAuthenticationFilter` validates every protected request stateless-ly.
- Custom `UsernamePwdAuthenticationProvider` for fine-grained authentication control.
- `JwtAuthenticationEntryPoint` returns structured 401 errors on unauthorized access.

### Two-Tier RBAC
| Scope | Roles |
|---|---|
| **Global Role** | `ROLE_ADMIN`, `ROLE_USER` |
| **Project Role** | `PROJECT_MANAGER`, `DEVELOPER`, `TESTER`, `VIEWER` |

- Method-level security enforced with `@PreAuthorize`.
- `ProjectSecurityService` bean provides SpEL helpers for project-scoped checks.

### Project Management
- Full CRUD for projects.
- Member management: add/remove members and assign project roles.
- Project creator is automatically assigned `PROJECT_MANAGER`.
- Paginated project listings.

### Task Management
- Full CRUD for tasks within a project.
- Task status workflow: `TODO` → `IN_PROGRESS` → `DONE` (managed via `TaskStatus` entity).
- Fields: assignee, priority (`TaskPriority` enum), start date, due date, estimated hours.
- Flexible **Tags** system for categorisation (`Tag` entity + join table).
- **Comments** and **Logtimes** for detailed progress tracking (with soft delete).

### Cross-cutting Concerns
- **MDC Logging** — a `traceId` is injected into every log statement via `MdcLoggingFilter` for end-to-end request tracing.
- **JPA Auditing** — `BaseEntity` auto-populates `createdAt`, `updatedAt`, `createdBy`, `updatedBy` on all main tables.
- **AOP** — `ExceptionAudit` aspect and `RegisterValidation` aspect for centralised cross-cutting validation logic.
- **Async email notifications** (`@Async` + `AsyncConfig`) sent on task assignment or important events.
- **Scheduled job** (`OverdueTaskJob`) periodically scans and logs overdue tasks.
- **Global exception handling** via `GlobalExceptionHandler` with consistent `ApiResponse` envelopes.

---

## Project Structure (Clean Architecture)

```text
src/main/java/com/congpv/springboot_base_project/
│
├── application/                     # Entry points — REST Controllers
│   └── controller/
│       ├── AuthController.java
│       ├── CommentController.java
│       ├── ProjectController.java
│       ├── ProjectMemberController.java
│       ├── TaskController.java
│       └── UserController.java
│
├── core/                            # Domain model — pure business logic
│   ├── entity/
│   │   ├── BaseEntity.java          # JPA Auditing base class
│   │   ├── User.java
│   │   ├── Role.java
│   │   ├── Token.java               # Refresh token store
│   │   ├── Project.java
│   │   ├── ProjectMember.java
│   │   ├── ProjectRole.java
│   │   ├── Task.java
│   │   ├── TaskStatus.java
│   │   ├── Tag.java
│   │   ├── Comment.java
│   │   └── Logtime.java
│   └── service/
│       ├── AuthService / impl
│       ├── UserService / impl
│       ├── ProjectService / impl
│       ├── ProjectMemberService / impl
│       ├── TaskService / impl
│       └── RoleService / impl
│
├── infrastructure/                  # Framework glue code
│   ├── aspect/
│   │   ├── ExceptionAudit.java      # AOP: exception auditing
│   │   └── RegisterValidation.java  # AOP: registration validation
│   ├── config/
│   │   ├── SecurityConfig.java      # Spring Security & CORS
│   │   ├── AsyncConfig.java         # Thread pool for @Async
│   │   ├── CacheConfig.java         # Caffeine cache definitions
│   │   ├── FlywayConfig.java        # Flyway customisation
│   │   ├── MdcLoggingFilter.java    # MDC traceId injection
│   │   ├── OpenApiConfig.java       # Swagger / OpenAPI 3 setup
│   │   ├── PathConfig.java          # Whitelisted paths
│   │   └── SchedulingConfig.java    # Enable scheduling
│   ├── job/
│   │   └── OverdueTaskJob.java      # Cron: overdue task scanner
│   ├── repository/                  # Spring Data JPA repositories
│   │   ├── UserRepository.java
│   │   ├── RoleRepository.java
│   │   ├── TokenRepository.java
│   │   ├── ProjectRepository.java
│   │   ├── ProjectMemberRepository.java
│   │   ├── ProjectRoleRepository.java
│   │   ├── TaskRepository.java
│   │   ├── CommnetRepository.java
│   │   └── (TagRepository, logtimeRepo…)
│   └── security/
│       ├── JwtTokenProvider.java
│       ├── JwtAuthenticationFilter.java
│       ├── JwtAuthenticationEntryPoint.java
│       ├── CustomUserDetails.java
│       ├── CustomUserDetailsService.java
│       ├── UsernamePwdAuthenticationProvider.java
│       └── ProjectSecurityService.java
│
└── shared/                          # Cross-layer shared utilities
    ├── constant/
    │   └── ApplicationConstants.java
    ├── dto/                         # Request / Response DTOs
    │   ├── ApiResponse.java
    │   ├── PageResponse.java
    │   ├── JwtAuthResponse.java
    │   ├── LoginRequestDto.java
    │   ├── RefreshTokenRequest.java
    │   ├── UserRequestDto.java / UserResponseDto.java
    │   ├── ProjectRequestDto.java / ProjectResponseDto.java
    │   ├── ProjectMemberRequestDto.java / ProjectMemberResponseDto.java
    │   └── TaskRequestDto.java / TaskResponseDto.java
    ├── enums/
    │   └── TaskPriority.java        # LOW, MEDIUM, HIGH, CRITICAL
    ├── exception/
    │   ├── GlobalExceptionHandler.java
    │   ├── BadRequestException.java
    │   ├── ResourceNotFoundException.java
    │   └── RegistrationValidationException.java
    ├── mapper/                      # Entity ↔ DTO mappers
    └── util/
        └── ApplicationUtil.java
```

---

## Database Migrations (Flyway)

Migrations are located in `src/main/resources/db/migration/`.

| Version | Description |
|---|---|
| **V1** | Initialize schema — `users`, `roles`, `projects`, `tasks`, `project_members`, `tokens` |
| **V2** | Add `comments` and `logtimes` tables |
| **V3** | Soft delete (`is_deleted`) on `comments` and `logtimes` |
| **V4** | Add `priority` column to `tasks` |
| **V5** | Create `tags` and `task_tags` join table |
| **V6** | Seed initial data |
| **V7** | JPA Auditing fields — `created_by`, `updated_by` on all main tables |
| **V8** | Additional seed data for development |
| **V9** | Refactor task status into a dedicated `task_status` table |
| **V10** | Refactor project roles into a dedicated `project_roles` table |
| **V11** | Refactor user roles to a Many-to-Many join table |

---

## Configuration

### Environment Variables (`.env`)

Create a `.env` file at the project root (see `.env` for reference):

```env
SERVER_PORT=8080
APP_NAME=mini-jira
APP_ENV=dev

# Database
DB_URL=jdbc:mysql://localhost:3307/springboot_base_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh
DB_PASSWORD=your_db_password

# JWT
JWT_SECRET=your_256bit_hex_secret
JWT_EXPIRATION=604800000        # 7 days in ms

# Sentry
SENTRY_DSN=https://<key>@o<org>.ingest.sentry.io/<project>
SENTRY_ENVIRONMENT=development

# Email (Gmail SMTP)
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587

# Logging / ELK
LOG_PATH=./logs
LOGSTASH_HOST=localhost
LOGSTASH_PORT=5000
```

### Application Profiles

| Profile | File | Purpose |
|---|---|---|
| `dev` | `application-dev.yml` | Local development, debug logging, `ddl-auto: none` |
| `prod` | `application-prod.yml` | Production settings |

Active profile is set in `application.yml`.

---

## Getting Started

### Prerequisites
- Java 21+
- MySQL 8.x (or use Docker — see below)
- Maven (or use the included `./mvnw` wrapper)

### 1. Start MySQL with Docker

```bash
docker compose -f docker-compose-db.yml up -d
```

This starts a MySQL 8.0 container (`mysql_jira`) on port **3307**, with database `springboot_base_db`.

### 2. Configure Environment

Copy the example variables into `.env` at the project root and fill in your credentials.

### 3. Run the Application

```bash
./mvnw clean spring-boot:run
```

Or with a specific profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## API Reference

| Module | Base Path | Description |
|---|---|---|
| Auth | `POST /api/v1/auth/login` | Login, receive access + refresh tokens |
| Auth | `POST /api/v1/auth/refresh` | Issue new access token from refresh token |
| Users | `GET /api/v1/users` | List users (Admin) |
| Users | `GET /api/v1/users/{id}` | Get user by ID |
| Users | `GET /api/v1/users/username/{username}` | Get user by username |
| Projects | `GET/POST /api/v1/projects` | List / create projects |
| Projects | `GET/PUT/DELETE /api/v1/projects/{projectId}` | Project CRUD |
| Members | `GET/POST /api/v1/projects/{projectId}/members` | Manage project members |
| Members | `DELETE /api/v1/projects/{projectId}/members/{id}` | Remove a member |
| Tasks | `GET/POST /api/v1/projects/{projectId}/tasks` | List / create tasks |
| Tasks | `GET/PUT/DELETE /api/v1/projects/{projectId}/tasks/{taskId}` | Task CRUD |
| Comments | Nested under tasks | Add / list / delete comments |

**Interactive docs:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Observability

### ELK Stack

```bash
docker compose -f docker-compose-elk.yml up -d
```

| Service | URL |
|---|---|
| Elasticsearch | http://localhost:9200 |
| Kibana | http://localhost:5601 |
| Logstash (TCP) | localhost:5000 |

Application logs are shipped to Logstash via `logstash-logback-encoder` (configured in `logback-spring.xml`). Set `LOGSTASH_HOST` and `LOGSTASH_PORT` in `.env`.

### Sentry

Runtime exceptions are captured automatically. Set `SENTRY_DSN` in `.env`. Each exception is correlated with the MDC `traceId` present in the logs for unified debugging.

---

## Running Tests

```bash
./mvnw test
```

Tests use an H2 in-memory database. Spring Security Test utilities are included for secured endpoint testing.
