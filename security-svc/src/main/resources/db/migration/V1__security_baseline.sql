CREATE SEQUENCE IF NOT EXISTS refresh_token_sequence START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS user_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE refresh_tokens
(
    id         BIGINT NOT NULL,
    token      VARCHAR(255),
    expiration TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id)
);

CREATE TABLE security_users
(
    id               BIGINT NOT NULL,
    email            VARCHAR(255),
    phone_number     VARCHAR(255),
    password         VARCHAR(255),
    refresh_token_id BIGINT,
    CONSTRAINT pk_security_users PRIMARY KEY (id)
);

ALTER TABLE security_users
    ADD CONSTRAINT uc_security_users_email UNIQUE (email);

ALTER TABLE security_users
    ADD CONSTRAINT uc_security_users_phone_number UNIQUE (phone_number);

ALTER TABLE security_users
    ADD CONSTRAINT FK_SECURITY_USERS_ON_REFRESH_TOKEN FOREIGN KEY (refresh_token_id) REFERENCES refresh_tokens (id);