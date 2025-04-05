CREATE SEQUENCE IF NOT EXISTS review_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE reviews
(
    id           BIGINT NOT NULL,
    place_id     BIGINT NOT NULL,
    user_id      BIGINT NOT NULL,
    rating       FLOAT NOT NULL,
    timestamp    BIGINT NOT NULL,
    comment_text VARCHAR(255),
    CONSTRAINT pk_reviews PRIMARY KEY (id)
);
