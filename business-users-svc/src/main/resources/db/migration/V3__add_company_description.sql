ALTER TABLE business_users
    ADD COLUMN company_description VARCHAR(1023) NOT NULL DEFAULT '',
    ADD COLUMN images VARCHAR(1023);