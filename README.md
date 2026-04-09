# Task Management System (Mini Jira) - Spring Boot Base

## Mô tả
Dự án Spring Boot quản lý task/dự án (Mini Jira) với kiến trúc Layered Architecture, sử dụng JPA/Hibernate và MySQL. Điểm nổi bật là hệ thống **phân quyền 2 tầng** (Global Role & Project Role) bảo mật bằng JWT.

## Công nghệ
- **Spring Boot** 3.x
- **Java** 17
- **Spring Data JPA** & **MySQL** 8.x
- **Spring Security** (Stateless, JWT, CORS, `@PreAuthorize`)
- **Swagger/OpenAPI** 3 (API Documentation)
- **Sentry** (Theo dõi lỗi & Exception tracking)
- **Caffeine Cache** (Tăng tốc độ truy xuất đọc)
- **Spring Mail & @Async** (Gửi email thông báo ngầm)
- **Dotenv** (Quản lý biến môi trường bảo mật)
- **Lombok** & **Bean Validation**

## Tính năng chính
- Xác thực người dùng bằng JWT Token.
- Phân quyền toàn cục (Global Role: `ADMIN`, `USER`).
- Quản lý Dự án (Projects) và phân quyền theo dự án (Project Role: `PROJECT_MANAGER`, `DEVELOPER`, v.v.).
- Tự động gán người tạo dự án làm `PROJECT_MANAGER`.
- API RESTful chuẩn, trả về format đồng nhất qua `ApiResponse` và `PageResponse` (Phân trang).
- Gửi Email thông báo khi tạo task (chạy ngầm `@Async`).
- API Documentation (Swagger).
- Caching kết quả tự động bằng thư viện Caffeine và theo dõi lỗi với Sentry.

## Cấu trúc thư mục (Clean Architecture)

Dự án được ứng dụng mô hình **Clean Architecture** để dễ dàng mở rộng và bảo trì, chia thành 4 lớp rõ ràng:
```text
src/main/java/com/congpv/springboot_base_project/
├── application/      # Controllers, tiếp nhận yêu cầu từ user/client
│   └── controller/   # REST API Controllers (User, Project, Task, Auth, v.v.)
├── core/             # Chứa Core Business (Domain logic xử lý nghiệp vụ)
│   ├── entity/       # Các Entity JPA (User, Project, ProjectMember, Task)
│   └── service/      # Cài đặt Interface / Business layer thao tác với logic lõi
├── infrastructure/   # Tích hợp công nghệ bên ngoài, Database, Bảo mật 
│   ├── config/       # Cấu hình Framework (CORS, Sentry, Cache, v.v.)
│   ├── repository/   # Kết nối DB sử dụng Spring Data JPA
│   └── security/     # Lớp cấu hình Spring Security (JWT, Provider, Permission)
└── shared/           # Mã nguồn dùng chung, tiện ích (Constants, Utils)
    ├── dto/          # Data Transfer Objects
    ├── enums/        # Các ENUM chung của toàn dự án
    ├── exception/    # Custom Exception & Global Controller Advice
    ├── mapper/       # Data Mappers (vd chuyển Entity <-> DTO)
    └── util/         # Logging, Date formatter, Utilities
```
Ngoài ra, cấu hình môi trường được quản lý tại `src/main/resources` gồm `application.yml`, `application-dev.yml`, và `application-prod.yml`.

## Cài đặt & Chạy

### 1. Biến môi trường (.env)
Tạo file `.env` ở thư mục gốc (ngang với `pom.xml`) để chứa thông tin bảo mật:
```env
DB_PASSWORD=mat_khau_mysql
JWT_SECRET=chuoi_ky_tu_bi_mat_jwt_khoang_hon_256_bit
JWT_EXPIRATION=604800000
MAIL_USERNAME=email_cua_ban@gmail.com
MAIL_PASSWORD=app_password_cua_email
SENTRY_DSN=link_sentry_dsn_neu_co
```

### 2. Cấu hình & Chạy
Hệ thống mặc định chạy profile `dev` nên sẽ đọc cấu hình từ `application-dev.yml`. Sau khi file `.env` được tạo xong, project sẽ tự động map credentials vào.

### 2. Build & Run
```bash
# Clean, compile & run project
./mvnw clean spring-boot:run
```

---

## Danh sách API Endpoints

### 1. Authentication (`/api/v1/auth`)
| Method | Endpoint                        | Quyền truy cập | Mô tả |
|--------|----------------------------------|---------------|---------|
| POST   | `/api/v1/auth/login`            | 🟢 Public      | Đăng nhập để lấy JWT |

### 2. Users (`/api/v1/users`)
| Method | Endpoint                        | Quyền truy cập | Mô tả |
|--------|----------------------------------|---------------|---------|
| POST   | `/api/v1/users`                 | 🟢 Public      | Tạo user mới (đăng ký) mặc định `ROLE_USER` |
| GET    | `/api/v1/users`                 | 🔒 Có JWT      | Lấy danh sách users |
| GET    | `/api/v1/users/{id}`            | 🔒 Có JWT      | Chi tiết user theo ID |
| GET    | `/api/v1/users/username/{name}` | 🔒 Có JWT      | Chi tiết user theo username |
| PUT    | `/api/v1/users/{id}`            | 🔴 ADMIN       | Cập nhật user |
| DELETE | `/api/v1/users/{id}`            | 🔴 ADMIN       | Xoá user |

### 3. Projects (`/api/v1/projects`)
| Method | Endpoint                        | Quyền truy cập | Mô tả |
|--------|----------------------------------|---------------|---------|
| GET    | `/api/v1/projects`              | 🔴 ADMIN       | Lấy DS toàn bộ dự án (Phân trang `?page=x&size=y`) |
| GET    | `/api/v1/projects/{id}`         | 🔒 Có JWT (Member) | Lấy thông tin dự án theo ID |
| POST   | `/api/v1/projects`              | 🔴 ADMIN       | Tạo dự án mới. User tạo tự động trở thành `PROJECT_MANAGER` |
| PUT    | `/api/v1/projects/{id}`         | 🟡 PROJECT_MANAGER | Cập nhật thông tin dự án |
| DELETE | `/api/v1/projects/{id}`         | 🔴 ADMIN hoặc PM  | Xóa dự án |

### 4. Project Members (`/api/v1/projects/{projectId}/members`)
| Method | Endpoint                        | Quyền truy cập | Mô tả |
|--------|----------------------------------|---------------|---------|
| GET    | `/.../members`                  | 🔒 Có JWT (Member) | Lấy danh sách thành viên dự án |
| POST   | `/.../members`                  | 🟡 PROJECT_MANAGER | Thêm một user vào dự án cụ thể |

### 5. Tasks (`/api/v1/projects/{projectId}/tasks`)
| Method | Endpoint                        | Quyền truy cập | Mô tả |
|--------|----------------------------------|---------------|---------|
| GET    | `/.../tasks`                    | 🔒 Có JWT (Member) | Lấy DS task trong dự án (Phân trang `?page=x&size=y`) |
| GET    | `/.../tasks/{taskId}`           | 🔒 Có JWT (Member) | Lấy chi tiết task |
| POST   | `/.../tasks`                    | 🔒 Có JWT (Member) | Tạo Task mới trong dự án |
| PUT    | `/.../tasks/{taskId}`           | 🔒 Có JWT (Member) | Cập nhật Task |
| DELETE | `/.../tasks/{taskId}`           | 🟡 PROJECT_MANAGER | Xóa Task |

---

## Luồng hoạt động (Workflow Example)

**A. Đăng ký & Đăng nhập (Authentication)**
1. **Đăng ký:** Gọi API `POST /api/v1/users` (không cần token). `role` mặc định là `ROLE_USER`. Để làm được luồng bên dưới, đổi cột `role` trong MySQL của user này thành `ROLE_ADMIN`.
2. **Đăng nhập:** Gọi API `POST /api/v1/auth/login` để chuyển `username/password` thành một token JWT. Token này phải gửi trong Header `Authorization: Bearer <token>` mỗi khi gọi các API khác.

**B. Quản lý dự án (Project & Members)**
1. **Tạo Project:** Kèm token *(Admin)* gọi `POST /api/v1/projects`. Khi đó hệ thống sẽ tự động gán Admin làm `PROJECT_MANAGER` qua bảng `project_members`.
2. **Gán thành viên:** Admin tiếp tục gọi `POST /api/v1/projects/{id}/members` để thêm "Alice" vào project với role `DEVELOPER`. Lớp `ProjectSecurityService` sẽ tự động xác minh Admin chính là PM của dự án đó thì mới cho phép.

## Cơ chế phân quyền nhiều lớp
- **Global `hasRole('ADMIN')`**: Kiểm tra bảng `users` cấu hình các API đặc quyền lớn (xoá user, config hệ thống, mở dự án mới).
- **Resource `@projectSecurity.isProjectManager(#projectId, authentication)`**: Kiểm tra chéo bảng `project_members`, đảm bảo Admin dù có token nhưng nếu **không thuộc** dự án đó, hoặc tham gia nhưng không phải role `PROJECT_MANAGER` thì cũng bị server báo **403 Access Denied**. 

## Tác giả / Nguồn
- Dự án Spring Boot Base / Mini Jira.
