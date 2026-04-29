# Spring Boot Base Project

A robust and production-ready Spring Boot foundational project, pre-configured with industry-standard patterns and tools to accelerate backend development.

## 🚀 Key Features

- **Java 21 & Spring Boot 4.0.5**: Leveraging the latest LTS Java features and Spring Boot performance.
- **Database Management**: MySQL with **Flyway** for version-controlled database migrations.
- **Security**: Stateless authentication using **Spring Security & JWT**.
- **Caching & Locking**: **Redis** integration with **Redisson** for distributed locking and efficient caching.
- **Asynchronous Messaging**: **RabbitMQ** for decoupling services and handling background tasks.
- **Monitoring & Observability**:
    - **ELK Stack** (Elasticsearch, Logstash, Kibana) for centralized logging.
    - **Sentry** integration for real-time error tracking and performance monitoring.
- **API Documentation**: Interactive API docs using **Swagger/OpenAPI 3**.
- **Containerization**: Fully Dockerized environment with dedicated Compose files for DB and ELK.
- **Scheduled Tasks**: Built-in support for background jobs using Spring Scheduling.
- **AOP**: Aspect-Oriented Programming for request validation and audit logging.

## 🛠 Tech Stack

- **Backend**: Spring Boot, Spring Data JPA, Spring Security.
- **Database**: MySQL 8+, Redis.
- **Messaging**: RabbitMQ.
- **Logging**: Logback, Logstash, ELK.
- **Tools**: Lombok, MapStruct (for DTO mapping), Maven, Docker.

## 🏁 Getting Started

### Prerequisites

- [JDK 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Docker & Docker Compose](https://www.docker.com/get-started)
- [Maven](https://maven.apache.org/) (or use the provided `./mvnw`)

### Setup

1.  **Clone the Repository**
    ```bash
    git clone https://github.com/your-username/springboot-base-project.git
    cd springboot-base-project
    ```

2.  **Environment Configuration**
    Create a `.env` file in the root directory:
    ```dotenv
    # Application
    APP_NAME=SpringBootBaseProject
    APP_ENV=dev

    # JWT Security
    JWT_SECRET=your_very_long_and_secure_random_string_here
    JWT_EXPIRATION=86400000

    # Sentry
    SENTRY_DSN=your_sentry_dsn_here

    # Database
    DB_HOST=localhost
    DB_PORT=3306
    DB_DATABASE=springboot_base
    DB_USERNAME=root
    DB_PASSWORD=your_password

    # Redis
    REDIS_HOST=localhost
    REDIS_PORT=6379

    # RabbitMQ
    RABBITMQ_HOST=localhost
    RABBITMQ_PORT=5672
    RABBITMQ_USERNAME=guest
    RABBITMQ_PASSWORD=guest

    # Mail
    MAIL_HOST=smtp.gmail.com
    MAIL_PORT=587
    MAIL_USERNAME=your_email@gmail.com
    MAIL_PASSWORD=your_app_password
    ```

3.  **Spin up Infrastructure**
    ```bash
    docker-compose -f docker-compose-db.yml -f docker-compose-elk.yml up -d
    ```

4.  **Run the Application**
    ```bash
    ./mvnw spring-boot:run
    ```

## 📂 Project Structure

```text
src/main/java/com/congpv/springboot_base_project/
├── application/            # API Layer
│   └── controller/         # REST Controllers (Auth, Project, Task, etc.)
├── core/                   # Domain & Business Logic
│   ├── entity/             # JPA Entities (User, Project, Task, etc.)
│   ├── repository/         # Spring Data Repositories
│   └── service/            # Business Interfaces & Implementations (impl/)
├── infrastructure/         # External Systems & Configuration
│   ├── aspect/             # AOP for Validation and Auditing
│   ├── config/             # Spring Beans & System Config (Security, Redis, MQ)
│   ├── job/                # Scheduled Background Tasks
│   ├── messaging/          # RabbitMQ Producers & Consumers
│   └── security/           # JWT & Security Provider Logic
└── shared/                 # Common Utilities & DTOs
    ├── constant/           # App Constants
    ├── dto/                # Request/Response Data Transfer Objects
    ├── enums/              # Shared Enumerations (TaskPriority, etc.)
    ├── exception/          # Custom Exceptions & Global Handler
    ├── mapper/             # Entity-DTO Mappers (MapStruct/Custom)
    └── util/               # Helper Utility Classes
```

## 📖 API Documentation

Access the Swagger UI at: `http://localhost:8080/swagger-ui/index.html`

## 🧪 Testing

Run unit and integration tests:
```bash
./mvnw test
```

## 📝 Logging

Logs are stored in the `/logs` directory and forwarded to Logstash (if ELK is running) as per `logback-spring.xml` and `logstash.conf`.
