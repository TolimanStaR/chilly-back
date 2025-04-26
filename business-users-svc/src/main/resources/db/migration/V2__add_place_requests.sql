CREATE SEQUENCE IF NOT EXISTS place_request_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE place_requests
(
    id         BIGINT NOT NULL,
    timestamp  BIGINT NOT NULL,
    status     VARCHAR(255) NOT NULL,
    reason     VARCHAR(255),

    owner_id   BIGINT,

    name       VARCHAR(255) NOT NULL,
    address    VARCHAR(255) NOT NULL,
    website    VARCHAR(255),
    y_page     VARCHAR(255),
    rating     DOUBLE PRECISION,
    images     VARCHAR(1023),
    phone      VARCHAR(255),
    social     VARCHAR(1023),
    open_hours VARCHAR(1023),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,

    CONSTRAINT pk_requests PRIMARY KEY (id)
);

ALTER TABLE place_requests
    ADD CONSTRAINT FK_REQUESTS_ON_USERS FOREIGN KEY (owner_id) REFERENCES place_requests (id);