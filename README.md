# Task Management System (Mini Jira) — Spring Boot Base Project

A production-ready Spring Boot base project built with **Clean Architecture**, providing a full-featured task/project management system. It includes two-tier RBAC, stateless JWT authentication with Refresh Token rotation, Flyway-managed schema migrations, and first-class observability via ELK Stack and Redis-based Distributed Caching.

---

## Tech Stack

| Category | Technology | Version |
|---|---|---|
| Language | Java | 21 (LTS) |
| Framework | Spring Boot | 4.0.5 |
| ORM | Spring Data JPA + Hibernate | (Boot managed) |
| Database | MySQL | 8.x |
| Caching | **Redis (Distributed Cache)** | (Boot managed) |
| Security | Spring Security, JJWT | 0.13.0 |
| API Docs | SpringDoc OpenAPI (Swagger UI) | 3.0.2 |
| DB Migration | Flyway (flyway-mysql) | (Boot managed) |
| Monitoring | Sentry for Spring Boot 4 | 8.27.0 |
| Logging | Logstash Logback Encoder | 7.4 |
| Utilities | Lombok, Jackson (JSON), Dotenv | 1.18.32 / 4.0.0 |

---

## Key Features

### Authentication & Security
- Registration / Login with **BCrypt** password encoding.
- Stateless JWT: **Access Tokens** & **Refresh Tokens** with rotation logic.
- `JwtAuthenticationFilter` for secure, per-request authorization.
- Custom `UsernamePwdAuthenticationProvider` for fine-grained auth control.
- Structured Error Handling: `JwtAuthenticationEntryPoint` for 401 Unauthorized responses.

### Two-Tier RBAC
- **Global Roles:** `ROLE_ADMIN`, `ROLE_USER`.
- **Project Roles:** `PROJECT_MANAGER`, `DEVELOPER`, `TESTER`, `VIEWER` (stored in `project_roles` table).
- Method-level security via `@PreAuthorize` and `ProjectSecurityService`.

### Distributed Caching (Redis)
- **Redis Cache Manager:** Configured for JSON serialization with `JavaTimeModule` support.
- **Granular TTL:** Custom TTL for different cache regions (e.g., 10m for projects, 24h for task statuses).
- **Pagination Caching:** Optimized caching for paginated results using composite keys.

### Task & Project Management
- **Projects:** CRUD operations, automatic manager assignment for creators, member management.
- **Tasks:** Rich attributes (Assignee, Reporter, Priority, Dates, Estimates).
- **Workflow:** Customizable `TaskStatus` entity (TODO, IN_PROGRESS, DONE, etc.).
- **Collaboration:** Comments and Logtimes for effort tracking (Soft-delete enabled).
- **Tags:** Many-to-Many relationship for flexible task categorization.

---

## Project Structure

```text
src/main/java/com/congpv/springboot_base_project/
│
├── application/                     # API Layer
│   └── controller/                  # Auth, User, Project, ProjectMember, Task, Comment, LogTime
│
├── core/                            # Domain & Business Layer
│   ├── entity/                      # JPA Entities (User, Task, Project, Role, Token, etc.)
│   ├── repository/                  # Spring Data JPA Repositories
│   └── service/                     # Service Interfaces
│       └── impl/                    # Business Logic Implementations
│
├── infrastructure/                  # Infrastructure Layer
│   ├── aspect/                      # AOP: ExceptionAudit, RegisterValidation
│   ├── config/                      # Config: Redis, Security, Async, Flyway, OpenAPI, MDC...
│   ├── job/                         # Scheduled Tasks (OverdueTaskJob)
│   └── security/                    # JWT, UserDetails, ProjectSecurityService
│
└── shared/                          # Cross-cutting Shared Layer
    ├── constant/                    # ApplicationConstants
    ├── dto/                         # Request/Response DTOs
    ├── enums/                       # TaskPriority
    ├── exception/                   # GlobalExceptionHandler & Custom Exceptions
    ├── mapper/                      # Entity ↔ DTO Mappers
    └── util/                        # ApplicationUtil
```

---

## Database Migrations (Flyway)

| Version | Description |
|---|---|
| **V1** | Initialize database schema (users, roles, projects, tasks, etc.) |
| **V2** | Add `comments` and `logtimes` tables |
| **V3** | Add `is_deleted` column to comments and logtimes |
| **V4** | Add `priority` column to tasks |
| **V5** | Create `tags` and `task_tags` join table |
| **V6** | Seed initial system data |
| **V7** | Add JPA Auditing fields (`created_by`, `updated_by`) |
| **V8** | Add development seed data |
| **V9** | Refactor task status into dedicated `task_status` table |
| **V10** | Refactor project roles into dedicated `project_roles` table |
| **V11** | Refactor user roles to Many-to-Many join table |

---

## Getting Started

### 1. Environment Configuration
Create a `.env` file at the project root:
```env
# Server
SERVER_PORT=8080

# Database
DB_URL=jdbc:mysql://localhost:3307/springboot_base_db
DB_PASSWORD=your_mysql_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT
JWT_SECRET=your_base64_secret_key
JWT_EXPIRATION=604800000

# Monitoring
SENTRY_DSN=...
LOGSTASH_HOST=localhost
LOGSTASH_PORT=5000
```

### 2. Infrastructure (Docker)
```bash
# Start MySQL and Redis
docker compose -f docker-compose-db.yml up -d

# (Optional) Start ELK Stack
docker compose -f docker-compose-elk.yml up -d
```

### 3. Run Application
```bash
./mvnw clean spring-boot:run
```

---

## API & Documentation
- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **Postman:** You can import the OpenAPI spec from `/v3/api-docs`.
- **Key Flow:** Login via `/api/v1/auth/login` to obtain the Bearer Token for subsequent requests.
