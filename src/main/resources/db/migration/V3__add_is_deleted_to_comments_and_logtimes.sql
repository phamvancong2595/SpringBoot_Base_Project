ALTER TABLE comments 
ADD COLUMN is_deleted BIT(1) DEFAULT 0;

ALTER TABLE log_times 
ADD COLUMN is_deleted BIT(1) DEFAULT 0;

CREATE INDEX idx_comments_is_deleted ON comments(is_deleted);
CREATE INDEX idx_logtimes_is_deleted ON log_times(is_deleted);