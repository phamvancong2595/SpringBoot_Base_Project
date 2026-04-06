# Task Management System (Mini Jira) - Spring Boot Base

## Mô tả
Dự án Spring Boot quản lý task/dự án (Mini Jira) với kiến trúc Layered Architecture, sử dụng JPA/Hibernate và MySQL. Điểm nổi bật là hệ thống **phân quyền 2 tầng** (Global Role & Project Role) bảo mật bằng JWT.

## Công nghệ
- **Spring Boot** 3.x
- **Java** 17
- **Spring Data JPA** (Hibernate ORM)
- **MySQL** 8.x
- **Spring Security** (Stateless, BCrypt, `@PreAuthorize`)
- **JSON Web Token (JWT)** (jjwt)
- **Lombok**
- **Bean Validation**
- **Spring DevTools**

## Tính năng chính
- Xác thực người dùng bằng JWT Token.
- Phân quyền toàn cục (Global Role: `ADMIN`, `USER`).
- Quản lý Dự án (Projects) và phân quyền theo dự án (Project Role: `PROJECT_MANAGER`, `DEVELOPER`, v.v.).
- Tự động gán người tạo dự án làm `PROJECT_MANAGER`.
- API RESTful chuẩn, trả về format đồng nhất qua `ApiResponse`.

## Cấu trúc thư mục chuẩn
```
src/main/java/com/congpv/springboot_base_project/
 ├── config/          # Cấu hình Spring Security, CORS, etc.
 ├── controller/      # REST API Controllers (User, Project, Task, Auth)
 ├── service/         # Các Interface xử lý nghiệp vụ
 │   └── impl/        # Cài đặt chi tiết của các Service (VD: tự động gán Project Manager)
 ├── repository/      # Spring Data JPA Repositories truy cập DB
 ├── entity/          # Các thực thể JPA (User, Project, ProjectMember, Task)
 ├── enums/           # Enum chứa quyền dự án (ProjectRole)
 ├── dto/             # Data Transfer Objects (Request/Response format)
 ├── security/        # Xử lý JWT, Custom User Details, isProjectManager Bean
 └── exception/       # Custom Exceptions & Global Exception Handler (dùng @ControllerAdvice)
```

## Cài đặt & Chạy

### 1. Cấu hình MySQL
Cập nhật file `src/main/resources/application.properties` (hoặc `.yml` nếu có đổi format):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/springboot_base_db
spring.datasource.username=root
spring.datasource.password=123456
```
*(Hoặc đổi thông tin cho phù hợp với môi trường của bạn)*

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
| POST   | `/api/v1/projects`              | 🔴 ADMIN       | Tạo dự án mới. User tạo tự động trở thành `PROJECT_MANAGER`. |
| POST   | `/api/v1/projects/{id}/members` | 🟡 PROJECT_MANAGER | Thêm một user vào dự án cụ thể. |
| POST   | `/api/v1/projects/{id}/tasks`   | 🔒 Có JWT (*)  | Tạo Task trong dự án. |
> (*) Ghi chú: TaskCreation hiện tại mở tạm, bạn có thể uncomment `@PreAuthorize` tại `TaskController` để chặn.

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
