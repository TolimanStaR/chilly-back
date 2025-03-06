ALTER TABLE security_users
    ADD COLUMN role VARCHAR(255) DEFAULT 'USER';

INSERT INTO security_users (id, email, password, role) VALUES
    (nextval('user_sequence'), '${DEF_ADMIN_USER}', '${DEF_ADMIN_PASSWORD}', 'ADMIN');