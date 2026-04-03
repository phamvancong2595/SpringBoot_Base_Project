# Spring Boot Base Project

## Mô tả
Dự án Spring Boot cơ bản với cấu trúc layered architecture, sử dụng JPA/Hibernate và MySQL.

## Công nghệ
- **Spring Boot** 4.0.5
- **Java** 17
- **Spring Data JPA** (Hibernate ORM)
- **MySQL** 8.x
- **Spring Security** (Stateless, BCrypt)
- **Lombok**
- **Bean Validation**
- **Spring DevTools**

## Cấu trúc thư mục
```
src/main/java/com/congpv/springboot_base_project/
├── config/          # Cấu hình (Security, ...)
├── controller/      # REST Controller
├── dto/             # Data Transfer Objects
├── entity/          # JPA Entities
├── exception/       # Custom Exceptions & Global Handler
├── repository/      # JPA Repositories
├── service/         # Service interfaces
│   └── impl/        # Service implementations
└── SpringbootBaseProjectApplication.java
```

## Cài đặt & Chạy

### 1. Cấu hình MySQL
Cập nhật file `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/springboot_base_db
    username: root
    password: your_password
```

### 2. Build & Run
```bash
./mvnw spring-boot:run
```

### 3. API Endpoints
| Method | Endpoint                        | Mô tả              |
|--------|----------------------------------|---------------------|
| POST   | `/api/v1/users`                 | Tạo user mới        |
| GET    | `/api/v1/users`                 | Lấy tất cả users    |
| GET    | `/api/v1/users/{id}`            | Lấy user theo ID    |
| GET    | `/api/v1/users/username/{name}` | Lấy user theo username |
| PUT    | `/api/v1/users/{id}`            | Cập nhật user       |
| DELETE | `/api/v1/users/{id}`            | Xóa user            |

## Ví dụ Request
### Tạo User
```json
POST /api/v1/users
{
  "username": "congpv",
  "email": "congpv@example.com",
  "password": "password123",
  "fullName": "Pham Van Cong"
}
```
