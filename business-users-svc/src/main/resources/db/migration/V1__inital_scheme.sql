CREATE TABLE business_users
(
    id            BIGINT NOT NULL,
    company_name  VARCHAR(255) NOT NULL,
    legal_address VARCHAR(255) NOT NULL,
    inn           VARCHAR(255) NOT NULL,
    okved         VARCHAR(1023) NOT NULL,
    kpp           VARCHAR(255),

    CONSTRAINT pk_main_users PRIMARY KEY (id)
);
