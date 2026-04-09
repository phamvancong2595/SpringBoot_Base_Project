ALTER TABLE tokens 
CHANGE COLUMN token access_token VARCHAR(500);

ALTER TABLE tokens 
ADD COLUMN refresh_token VARCHAR(500) UNIQUE AFTER access_token;