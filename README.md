# Task Management System (Mini Jira) - Spring Boot Base

## Mô tả
Dự án Spring Boot quản lý task/dự án (Mini Jira) với kiến trúc **Clean Architecture**, sử dụng JPA/Hibernate và MySQL. Điểm nổi bật là hệ thống **phân quyền 2 tầng** (Global Role & Project Role) bảo mật bằng JWT và cơ chế **Refresh Token**, tích hợp giám sát lỗi tập trung và xử lý tác vụ ngầm.

## Công nghệ & Phiên bản
- **Java:** 21
- **Spring Boot:** 4.0.5
- **Spring Data JPA** & **MySQL** 8.x
- **Spring Security:** Stateless, JWT (Access & Refresh Tokens), CORS, `@PreAuthorize`.
- **Swagger/OpenAPI 3:** Tài liệu API (`springdoc-openapi`).
- **Sentry:** Theo dõi lỗi, Exception tracking và hiệu năng.
- **Caffeine Cache:** Cơ chế cache local tối ưu hóa tốc độ truy xuất.
- **Spring Mail & @Async:** Gửi email thông báo bất đồng bộ.
- **Flyway:** Quản lý lịch sử và phiên bản Database Migration.
- **Scheduling (Cron Jobs):** Tự động quét và thông báo task quá hạn.
- **ELK Stack Support:** Logstash, Elasticsearch, Kibana integration.
- **Lombok** & **Jakarta Bean Validation**.
- **Spring Dotenv:** Tự động nạp biến môi trường từ file `.env`.

## Tính năng chính
- **Xác thực & Bảo mật:**
  - Login/Register với mật khẩu mã hóa BCrypt.
  - Cơ chế Refresh Token giúp tái cấp Access Token bảo mật.
  - Phân quyền (RBAC) 2 cấp độ:
    - **Global Role:** `ROLE_ADMIN`, `ROLE_USER`.
    - **Project Role:** `PROJECT_MANAGER`, `DEVELOPER`, `TESTER`, `VIEWER`.
- **Quản lý Dự án & Task:**
  - CRUD Dự án và quản lý thành viên linh hoạt.
  - Quản lý Task: Trạng thái (`TODO` $\rightarrow$ `DONE`), Assignee, Priority, Due Date.
  - Hệ thống Tags đa dạng giúp phân loại và quản lý công việc dễ dàng.
  - Tự động gán quyền `PROJECT_MANAGER` cho người tạo dự án.
- **Hệ thống Thông báo & Giám sát:**
  - Gửi email thông báo khi được gán task mới (chạy ngầm).
  - Cron Job tự động quét và log cảnh báo các task đã quá hạn.
  - **MDC Logging:** Tự động đính kèm `traceId` vào mọi dòng log giúp trace request trên ELK/Sentry.

## Cấu trúc thư mục (Clean Architecture)
```text
src/main/java/com/congpv/springboot_base_project/
├── application/      # Controllers (REST Endpoints)
├── core/             # Business Logic (Domain)
│   ├── entity/       # JPA Entities (Core Data Structures)
│   └── service/      # Business Services & Interfaces
├── infrastructure/   # Technical Implementations
│   ├── config/       # Framework Configuration (Security, Cache, MDC...)
│   ├── job/          # Background Jobs (Scheduled Tasks)
│   ├── repository/   # Data Persistence (JPA Repositories)
│   └── security/     # Auth Logic & JWT Provider
└── shared/           # Common components (DTOs, Enums, Exceptions, Utils)
```

## Quản lý Database (Flyway)
- **V1:** Khởi tạo cấu trúc cơ bản (Users, Projects, Tasks, Members, Tokens).
- **V2:** Bổ sung thực thể Comments và ghi nhận Logtimes.
- **V3:** Hỗ trợ cơ chế Soft Delete cho Comments và Logtimes.
- **V4:** Thêm thuộc tính Priority (Độ ưu tiên) cho Task.
- **V5:** Tích hợp hệ thống Tags và bảng liên kết Task-Tag.

## Cài đặt & Chạy

### 1. Biến môi trường (.env)
Tạo file `.env` ở thư mục gốc:
```env
DB_URL=jdbc:mysql://localhost:3306/springboot_base
DB_USERNAME=root
DB_PASSWORD=your_password
JWT_SECRET=your_super_secret_key_at_least_256_bits
JWT_EXPIRATION=900000 (15 mins)
REFRESH_TOKEN_EXPIRATION=604800000 (7 days)
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
SENTRY_DSN=your_sentry_dsn
```

### 2. Chạy ứng dụng
```bash
./mvnw clean spring-boot:run
```

## Danh sách API chính
- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Authentication:** `/api/v1/auth` (Login, Register, Refresh)
- **Users:** `/api/v1/users` (List all users - Admin only)
- **Projects:** `/api/v1/projects` (CRUD)
- **Members:** `/api/v1/projects/{projectId}/members` (Add/List members)
- **Tasks:** `/api/v1/projects/{projectId}/tasks` (Full CRUD)

## Giám sát & Logs
- **ELK Stack:** Cấu hình sẵn trong `docker-compose-elk.yml`.
- **Logging:** Sử dụng logback-spring config để đẩy logs sang Logstash (cổng 5000).
- **Sentry:** Tự động bắt Exception và đính kèm `traceId` để điều tra lỗi nhanh chóng.
