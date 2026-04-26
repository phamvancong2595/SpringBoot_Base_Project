CREATE TABLE task_status (
         id BIGINT AUTO_INCREMENT PRIMARY KEY,
         code VARCHAR(20) NOT NULL UNIQUE,
         description VARCHAR(500),
         is_deleted BIT(1) DEFAULT 0,
         created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
         updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
         created_by VARCHAR(50) DEFAULT 'SYSTEM',
         updated_by VARCHAR(50) DEFAULT 'SYSTEM'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO task_status (code, description) VALUES
        ('NEW', 'new task created'),
        ('TODO', 'task need to done'),
        ('IN_PROGRESS', 'in progress task'),
        ('REVIEW', 'waiting to review task'),
        ('PENDING', 'pending task'),
        ('DONE', 'done task');

ALTER TABLE tasks ADD COLUMN status_id BIGINT;
UPDATE tasks t, task_status ts
SET t.status_id = ts.id
WHERE t.status = ts.code;

ALTER TABLE tasks DROP COLUMN status;
ALTER TABLE tasks MODIFY COLUMN status_id BIGINT NOT NULL;

ALTER TABLE tasks
ADD CONSTRAINT fk_tasks_status_id
FOREIGN KEY (status_id) REFERENCES task_status (id);