# Mini Jira - Spring Boot Base Project

A high-performance, enterprise-grade backend template based on **Spring Boot 4.0.6** and **Java 21**. This project serves as a robust foundation for building scalable applications with features like distributed caching, asynchronous messaging, and multi-layered architecture.

## 🚀 Key Features

- **Cutting-Edge Stack**: Built with **Java 21 (LTS)** and **Spring Boot 4.0.6**.
- **Database & Migrations**: MySQL integration with **Flyway** for reliable schema versioning.
- **Advanced Caching**: **Redis** with **Redisson** for distributed locking and optimized data access.
- **Dual Messaging Systems**: 
    - **RabbitMQ** for reliable background task processing.
    - **Apache Kafka** for high-throughput event streaming and notifications.
- **Security**: Robust stateless authentication using **Spring Security** and **JWT**.
- **Observability**: 
    - **ELK Stack** (Elasticsearch, Logstash, Kibana) for centralized log management.
    - **Sentry** (Optional) for real-time error tracking and performance monitoring.
- **API Standards**: Fully documented REST APIs with **Swagger/OpenAPI 3**.
- **Performance & AOP**: Aspect-Oriented Programming for audit logging, performance tracking, and request validation.
- **Scheduled Jobs**: Integrated support for automated background tasks.

## 🛠 Tech Stack

- **Framework**: Spring Boot 4.0.6
- **Database**: MySQL 8.0, Redis 7.2
- **Messaging**: Apache Kafka, RabbitMQ
- **Migration**: Flyway
- **Documentation**: SpringDoc OpenAPI
- **Build Tool**: Maven

## 🏁 Getting Started

### Prerequisites

- **JDK 21**
- **Docker & Docker Compose**
- **Maven** (optional, wrapper included)

### Setup

1. **Environment Configuration**
   Create a `.env` file in the root directory based on the following template:
   \`\`\`dotenv
   SERVER_PORT=8080
   APP_NAME=mini-jira
   APP_ENV=dev

   # Database (Ensure correct JDBC format)
   DB_URL=jdbc:mysql://localhost:3307/springboot_base_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh
   DB_PASSWORD=your_password

   # Security
   JWT_SECRET=your_base64_encoded_secret_key
   JWT_EXPIRATION=604800000

   # Infrastructure
   REDIS_HOST=localhost
   REDIS_PORT=6379
   RABBITMQ_HOST=localhost
   RABBITMQ_PORT=5672
   KAFKA_BROKERS=localhost:9092

   # Email (SMTP)
   MAIL_HOST=smtp.gmail.com
   MAIL_PORT=587
   MAIL_USERNAME=your_email@gmail.com
   MAIL_PASSWORD=your_app_password
   \`\`\`

2. **Launch Infrastructure Services**
   \`\`\`bash
   docker-compose -f docker-compose-db.yml up -d
   \`\`\`

3. **Build and Run**
   \`\`\`bash
   ./mvnw clean spring-boot:run
   \`\`\`

## 📂 Project Structure

\`\`\`text
com.congpv.springboot_base_project/
├── application/            # Controllers & Entry Points
│   └── controller/         # REST endpoints for Auth, Projects, Tasks, etc.
├── core/                   # Domain & Business Logic
│   ├── entity/             # JPA Entities
│   ├── repository/         # Data Access Layer
│   └── service/            # Business Logic Interfaces & Implementations
├── infrastructure/         # System Integrations
│   ├── aspect/             # AOP: Logging, Performance, Validation
│   ├── config/             # Configuration: Security, Kafka, Redis, RabbitMQ
│   ├── messaging/          # Producers & Consumers (Kafka & Rabbit)
│   └── job/                # Scheduled Tasks
└── shared/                 # Shared Components
    ├── dto/                # Data Transfer Objects
    ├── exception/          # Global Exception Handling
    ├── mapper/             # Object Mapping logic
    └── util/               # Common Utility classes
\`\`\`

## 📖 Documentation & Monitoring

- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Centralized Logs**: Forwarded to Logstash via port \`5000\` (requires ELK stack).

## 🧪 Development Notes

- **Cache Consistency**: Always use the correct cache name (e.g., \`projects_details\`) when using \`@CacheEvict\`.
- **Null Safety**: When creating tasks, ensure the \`assignee\` object is checked for null before accessing properties to avoid \`NullPointerException\`.
- **Kafka Topics**: The default topic \`task-notification-topic\` is automatically created upon the first message.
