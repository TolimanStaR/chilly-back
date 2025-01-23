CREATE SEQUENCE IF NOT EXISTS place_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE places
(
    id         BIGINT NOT NULL,
    name       VARCHAR(255),
    address    VARCHAR(255),
    website    VARCHAR(255),
    y_page     VARCHAR(255),
    rating     DOUBLE PRECISION,
    images     VARCHAR(1023),
    phone      VARCHAR(255),
    social     VARCHAR(1023),
    open_hours VARCHAR(1023),
    CONSTRAINT pk_places PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS visit_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE visits
(
    id       BIGINT NOT NULL,
    user_id  BIGINT,
    place_id BIGINT,
    date     TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_visits PRIMARY KEY (id)
);

ALTER TABLE visits
    ADD CONSTRAINT FK_VISITS_ON_PLACE FOREIGN KEY (place_id) REFERENCES places (id);
