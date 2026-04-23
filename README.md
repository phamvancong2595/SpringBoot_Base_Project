# Task Management System (Mini Jira) - Spring Boot Base

## Mô tả
Dự án Spring Boot quản lý task/dự án (Mini Jira) được xây dựng với kiến trúc **Clean Architecture**, tập trung vào tính mở rộng, bảo mật và khả năng giám sát. Hệ thống cung cấp cơ chế **phân quyền 2 tầng** (Global Role & Project Role), bảo mật bằng JWT với **Refresh Token**, và tích hợp đầy đủ các công cụ hiện đại như ELK Stack, Sentry, và Flyway.

## Công nghệ & Phiên bản
- **Java:** 21 (LTS)
- **Spring Boot:** 4.0.5
- **Spring Data JPA** & **MySQL** 8.x: Quản lý dữ liệu và ORM.
- **Spring Security:** Stateless architecture, JWT (Access & Refresh Tokens), CORS, RBAC với `@PreAuthorize`.
- **Swagger/OpenAPI 3:** Tài liệu API tương tác (`springdoc-openapi`).
- **Sentry:** Giám sát lỗi tập trung, Exception tracking và phân tích hiệu năng.
- **Caffeine Cache:** Cache local hiệu năng cao cho các dữ liệu ít thay đổi.
- **Spring Mail & @Async:** Hệ thống gửi email thông báo bất đồng bộ.
- **Flyway:** Quản lý phiên bản Database Migration chuyên nghiệp.
- **JPA Auditing:** Tự động ghi nhận `createdAt`, `updatedAt`, `createdBy`, `updatedBy`.
- **Scheduling (Cron Jobs):** Thực hiện các tác vụ định kỳ (quét task quá hạn, dọn dẹp token).
- **ELK Stack Support:** Tích hợp Logstash để đẩy log tập trung về Elasticsearch & Kibana.
- **Lombok** & **Jakarta Bean Validation**: Giảm thiểu mã lặp và kiểm tra dữ liệu đầu vào.
- **Spring Dotenv:** Quản lý cấu hình linh hoạt qua file `.env`.

## Tính năng chính
- **Hệ thống xác thực mạnh mẽ:**
  - Đăng ký/Đăng nhập với mật khẩu BCrypt.
  - Cấp phát Access Token (ngắn hạn) và Refresh Token (dài hạn).
  - Tự động thu hồi Refresh Token khi phát hiện sử dụng trái phép hoặc logout.
- **Phân quyền đa cấp độ (RBAC):**
  - **Global Role:** Quyền trên toàn hệ thống (`ROLE_ADMIN`, `ROLE_USER`).
  - **Project Role:** Quyền hạn cụ thể trong từng dự án (`PROJECT_MANAGER`, `DEVELOPER`, `TESTER`, `VIEWER`).
- **Quản lý Dự án (Project Management):**
  - CRUD Dự án.
  - Quản lý thành viên dự án và gán vai trò tương ứng.
  - Người tạo dự án mặc định trở thành `PROJECT_MANAGER`.
- **Quản lý Công việc (Task Management):**
  - Quy trình xử lý task: `TODO` -> `IN_PROGRESS` -> `DONE`.
  - Gán người thực hiện, độ ưu tiên (Priority), ngày hết hạn (Due Date).
  - Hệ thống Tags linh hoạt để phân loại task.
  - Hỗ trợ Comments và Logtimes để theo dõi tiến độ chi tiết.
- **Giao tiếp & Giám sát:**
  - Email thông báo tự động khi được phân công task hoặc có thay đổi quan trọng.
  - Kiểm tra task quá hạn định kỳ và log cảnh báo.
  - **MDC Logging:** Gắn `traceId` vào mọi log statement giúp truy vết request xuyên suốt hệ thống.

## Cấu trúc thư mục (Clean Architecture)
```text
src/main/java/com/congpv/springboot_base_project/
├── application/      # Giao diện người dùng (Controllers, REST API Endpoints)
├── core/             # Logic nghiệp vụ cốt lõi (Domain Model)
│   ├── entity/       # Các thực thể JPA & BaseEntity (Audit)
│   └── service/      # Định nghĩa Service & Business Logic chuyên sâu
├── infrastructure/   # Triển khai kỹ thuật (Framework glue code)
│   ├── config/       # Cấu hình Framework (Security, Cache, Audit, Swagger...)
│   ├── job/          # Các background tasks & Scheduled Jobs
│   ├── repository/   # Giao tiếp với Database (Spring Data JPA Repositories)
│   └── security/     # Xử lý JWT, Authentication & Authorization Providers
└── shared/           # Thành phần dùng chung (DTOs, Enums, Exceptions, Utils)
```

## Lịch sử Database (Flyway Migrations)
- **V1:** Khởi tạo cấu trúc cơ bản (Users, Projects, Tasks, Members, Tokens).
- **V2:** Thêm thực thể `Comments` và `Logtimes`.
- **V3:** Hỗ trợ cơ chế Soft Delete cho `Comments` và `Logtimes`.
- **V4:** Bổ sung trường `Priority` (Độ ưu tiên) cho Task.
- **V5:** Tích hợp hệ thống Tags và bảng liên kết `Task-Tag`.
- **V6:** Seed dữ liệu ban đầu cho hệ thống.
- **V7:** Triển khai **JPA Auditing** (Thêm `created_by`, `updated_by` cho tất cả các bảng chính).
- **V8:** Bổ sung thêm dữ liệu mẫu (Seed data) cho môi trường dev.

## Cài đặt & Chạy ứng dụng

### 1. File biến môi trường (.env)
Tạo file `.env` tại thư mục gốc của dự án:
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

### 2. Khởi chạy
Sử dụng Maven Wrapper để chạy ứng dụng:
```bash
./mvnw clean spring-boot:run
```

## Tài liệu API & Giám sát
- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Auth Endpoints:** `/api/v1/auth/login`, `/api/v1/auth/register`, `/api/v1/auth/refresh-token`.
- **Project Endpoints:** `/api/v1/projects` (Quản lý dự án, thành viên, và phân quyền project).
- **Task Endpoints:** `/api/v1/projects/{projectId}/tasks` (Full CRUD cho task của từng dự án).
- **ELK Stack:** Khởi chạy ELK qua `docker-compose-elk.yml`. Log được đẩy qua Logstash (Port 5000).
- **Sentry Dashboard:** Giám sát các runtime exception và traceid liên kết với log.
