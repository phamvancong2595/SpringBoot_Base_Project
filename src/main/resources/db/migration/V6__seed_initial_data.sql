INSERT INTO `users` (`active`, `created_at`, `username`, `email`, `full_name`, `password`, `role`)
VALUES (1, NOW(), 'admin', 'admin@jira.com', 'System Admin',
        '$2a$10$8vVM79Ot/tFh.F6W5C.vhuL7x7fE0.XzFkY2v9w3xY1z.6X7z2q.y', 'ROLE_ADMIN'),
       (1, NOW(), 'manager', 'pm@jira.com', 'Project Manager',
        '$2a$10$8vVM79Ot/tFh.F6W5C.vhuL7x7fE0.XzFkY2v9w3xY1z.6X7z2q.y', 'ROLE_USER'),
       (1, NOW(), 'developer', 'dev@jira.com', 'Senior Developer',
        '$2a$10$8vVM79Ot/tFh.F6W5C.vhuL7x7fE0.XzFkY2v9w3xY1z.6X7z2q.y', 'ROLE_USER');

INSERT INTO `projects` (`is_deleted`, `created_at`, `name`, `description`)
VALUES (0, NOW(), 'Mini Jira Project', 'Hệ thống quản lý công việc nội bộ'),
       (0, NOW(), 'E-Commerce App', 'Dự án phát triển ứng dụng bán hàng');

INSERT INTO `project_members` (`created_at`, `project_id`, `user_id`, `role`)
VALUES (NOW(), 1, 2, 'PROJECT_MANAGER'),
       (NOW(), 1, 3, 'DEVELOPER'),
       (NOW(), 2, 2, 'PROJECT_MANAGER');

INSERT INTO `tasks` (`estimate_hours`, `is_deleted`, `assignee_id`, `created_at`, `project_id`, `reporter_id`,
                     `start_date`, `due_date`, `title`, `description`, `status`, `priority`)
VALUES (8, 0, 3, NOW(), 1, 2, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 'Thiết kế Database',
        'Thiết kế các bảng cho Flyway migration', 'DONE', 'HIGH'),
       (16, 0, 3, NOW(), 1, 2, NOW(), DATE_ADD(NOW(), INTERVAL 14 DAY), 'Tích hợp Spring Security',
        'Cài đặt JWT và phân quyền User', 'IN_PROGRESS', 'MEDIUM'),
       (4, 0, NULL, NOW(), 1, 3, NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY), 'Fix bug Login',
        'Sửa lỗi không nhận @Value trong FlywayConfig', 'TODO', 'LOW');

INSERT INTO `tags` (`name`, `color`)
VALUES ('Backend', '#FF5733'),
       ('Bug', '#C70039'),
       ('Database', '#2ECC71');

INSERT INTO `task_tags` (`task_id`, `tag_id`)
VALUES (1, 1),
       (1, 3),
       (2, 1),
       (3, 2);

INSERT INTO `comments` (`content`, `created_at`, `task_id`, `user_id`, `is_deleted`)
VALUES ('Database đã chạy ổn định với Docker.', NOW(), 1, 3, 0),
       ('Cần kiểm tra lại checksum của Flyway.', NOW(), 1, 2, 0);

INSERT INTO `log_times` (`hours_spent`, `description`, `log_date`, `created_at`, `task_id`, `user_id`, `is_deleted`)
VALUES (2.5, 'Thiết kế sơ đồ ERD', CURDATE(), NOW(), 1, 3, 0),
       (4.0, 'Viết file SQL Migration V1-V5', CURDATE(), NOW(), 1, 3, 0);