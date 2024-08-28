CREATE SEQUENCE IF NOT EXISTS place_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE places
(
    id         BIGINT NOT NULL,
    name       VARCHAR(255),
    address    VARCHAR(255),
    website    VARCHAR(255),
    y_page     VARCHAR(255),
    rating     DOUBLE PRECISION,
    images     VARCHAR(255),
    phone      VARCHAR(255),
    social     VARCHAR(255),
    open_hours VARCHAR(255),
    CONSTRAINT pk_places PRIMARY KEY (id)
);