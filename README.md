# Task Management System (Mini Jira) - Spring Boot Base

## Mô tả
Dự án Spring Boot quản lý task/dự án (Mini Jira) với kiến trúc **Clean Architecture**, sử dụng JPA/Hibernate và MySQL. Điểm nổi bật là hệ thống **phân quyền 2 tầng** (Global Role & Project Role) bảo mật bằng JWT và cơ chế **Refresh Token**.

## Công nghệ
- **Spring Boot** 3.x
- **Java** 17
- **Spring Data JPA** & **MySQL** 8.x
- **Spring Security** (Stateless, JWT, CORS, `@PreAuthorize`)
- **Swagger/OpenAPI 3** (API Documentation)
- **Sentry** (Theo dõi lỗi & Exception tracking)
- **Caffeine Cache** (Tăng tốc độ truy xuất đọc)
- **Spring Mail & @Async** (Gửi email thông báo ngầm)
- **Flyway** (Quản lý phiên bản Database Migration)
- **Docker & Docker Compose** (ELK Stack support)
- **Lombok** & **Bean Validation**

## Tính năng chính
- Xác thực người dùng bằng JWT Token (Access & Refresh Token).
- Phân quyền toàn cục (Global Role: `ADMIN`, `USER`).
- Quản lý Dự án (Projects) và phân quyền theo dự án (Project Role: `PROJECT_MANAGER`, `DEVELOPER`, v.v.).
- Quản lý Task: theo dõi tiến độ, gán người thực hiện, độ ưu tiên, ngày hết hạn.
- Hệ thống Tags, Comments và Logtime (theo dõi thời gian làm việc).
- Tự động gán người tạo dự án làm `PROJECT_MANAGER`.
- Gửi Email thông báo khi tạo task (chạy ngầm `@Async`).
- API Documentation (Swagger).
- Caching kết quả tự động bằng thư viện Caffeine.
- Quản lý Log tập trung với MDC Logging và ELK Stack integration.

## Cấu trúc thư mục (Clean Architecture)

Dự án được ứng dụng mô hình **Clean Architecture** để dễ dàng mở rộng và bảo trì:
```text
src/main/
├── java/com/congpv/springboot_base_project/
│   ├── application/      # Controllers, tiếp nhận yêu cầu từ user/client
│   ├── core/             # Lớp nghiệp vụ cốt lõi (Domain logic)
│   │   ├── entity/       # JPA Entities (User, Task, Project, Comment, Logtime, Tag...)
│   │   └── service/      # Business Services & Interfaces
│   ├── infrastructure/   # Tích hợp công nghệ bên ngoài
│   │   ├── config/       # Cấu hình Framework (Security, Cache, OpenAPI, Scheduling...)
│   │   ├── job/          # Background tasks (Overdue task tracking...)
│   │   ├── repository/   # Spring Data JPA Repositories
│   │   └── security/     # JWT, Auth Providers, Role-based access
│   └── shared/           # Thành phần dùng chung (DTOs, Enums, Exceptions, Mappers)
└── resources/
    ├── db/migration/     # SQL Migration scripts (Flyway)
    ├── application.yml   # Cấu hình hệ thống
    └── .env              # Biến môi trường (Local)
```

## Quản lý Database (Flyway)
Dự án sử dụng **Flyway** để quản lý migration:
- **V1:** Khởi tạo cấu trúc cơ bản (User, Project, Task, Member).
- **V2:** Bổ sung bảng Comments và Logtimes.
- **V3:** Thêm cơ chế Soft Delete cho Comments/Logtimes.
- **V4:** Bổ sung Priority cho Task.
- **V5:** Tích hợp hệ thống Tags.

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

### 1. Authentication (`/api/v1/auth`)
- `POST /login`: Đăng nhập nhận bộ đôi Token.
- `POST /refresh`: Sử dụng Refresh Token để lấy Access Token mới.

### 2. Users (`/api/v1/users`)
- `POST /`: Đăng ký tài khoản mới.
- `GET /`: Danh sách người dùng (Admin).
- `PUT /{id}`: Cập nhật thông tin (Admin).

### 3. Projects (`/api/v1/projects`)
- `GET /`: Danh sách dự án (Phân trang).
- `POST /`: Tạo dự án mới (Người tạo làm PM).
- `GET /{projectId}/members`: Danh sách thành viên trong dự án.
- `POST /{projectId}/members`: Thêm thành viên vào dự án (PM Only).

### 4. Tasks (`/api/v1/projects/{projectId}/tasks`)
- `GET /`: Danh sách task trong dự án.
- `POST /`: Tạo task mới.
- `PUT /{taskId}`: Cập nhật task.
- `DELETE /{taskId}`: Xóa task (PM Only).

## Giám sát & Logs
- **ELK Stack:** Hỗ trợ cấu hình qua `docker-compose-elk.yml`.
- **MDC Logging:** Tự động đính kèm `correlationId` vào logs để dễ dàng trace request.
- **Sentry:** Tự động bắt và báo cáo Exception lên dashboard.

