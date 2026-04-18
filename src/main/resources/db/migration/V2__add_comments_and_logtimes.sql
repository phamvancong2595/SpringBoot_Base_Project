CREATE TABLE comments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    content    TEXT   NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    task_id    BIGINT NOT NULL, -- Khóa ngoại trỏ về bảng tasks
    user_id    BIGINT NOT NULL, -- Khóa ngoại trỏ về bảng users (người comment)
    CONSTRAINT fk_comments_task FOREIGN KEY (task_id) REFERENCES tasks (id),
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE log_times
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    hours_spent DOUBLE NOT NULL, -- Số giờ đã làm (vd: 2.5 giờ)
    description VARCHAR(255),    -- Làm cái gì trong 2.5 giờ đó
    log_date    DATE   NOT NULL, -- Ngày log
    created_at  DATETIME(6) NOT NULL,
    updated_at  DATETIME(6),
    task_id     BIGINT NOT NULL,
    user_id     BIGINT NOT NULL,
    CONSTRAINT fk_logtimes_task FOREIGN KEY (task_id) REFERENCES tasks (id),
    CONSTRAINT fk_logtimes_user FOREIGN KEY (user_id) REFERENCES users (id)
);