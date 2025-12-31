CREATE DATABASE homework2;

USE homework2;

CREATE TABLE IF NOT EXISTS users
(
    id       CHAR(32) PRIMARY KEY,
    user_name     VARCHAR(50) NOT NULL,
    password CHAR(64)    NOT NULL,
    personality_type VARCHAR(20) DEFAULT 'UNDEFINED'
);

# admin 12345678a
INSERT INTO users (id, users.user_name, password)
VALUES ('47991310a13a4a67a5547d1b2e8d9641', 'admin', 'f969248d621bcded4a3582a1c3b17a71eedfefa9120c36ee3bd1957438cd55b9');
