CREATE TABLE project_roles
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(500),
    is_deleted  BIT(1)      DEFAULT 0,
    created_at  DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    created_by  VARCHAR(50) DEFAULT 'SYSTEM',
    updated_by  VARCHAR(50) DEFAULT 'SYSTEM'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO project_roles (code, description)
VALUES ('PROJECT_MANAGER', 'Manager of the project'),
       ('DEVELOPER', 'role developer in project'),
       ('TESTER', 'role tester in project'),
       ('BRIDGE_ENGINEER', 'communication between client and team'),
       ('PQA', 'role pqa in project'),
       ('LEADER', 'role leader in project');

ALTER TABLE project_members
    ADD COLUMN role_id BIGINT;
UPDATE project_members p, project_roles pr
SET p.role_id = pr.id
WHERE p.role = pr.code;

ALTER TABLE project_members
DROP
COLUMN role,
    MODIFY COLUMN role_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_project_role_id FOREIGN KEY (role_id) REFERENCES project_roles (id);