CREATE SEQUENCE IF NOT EXISTS answer_sequence START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS question_sequence START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS quiz_sequence START WITH 1 INCREMENT BY 1;


CREATE TABLE answers
(
    id          BIGINT NOT NULL,
    body        VARCHAR(255),
    question_id BIGINT,
    CONSTRAINT pk_answers PRIMARY KEY (id)
);

CREATE TABLE questions
(
    id        BIGINT   NOT NULL,
    quiz_type SMALLINT NOT NULL,
    body      VARCHAR(255),
    CONSTRAINT pk_questions PRIMARY KEY (id)
);

CREATE TABLE quiz_answers
(
    id          BIGINT NOT NULL,
    user_id     BIGINT,
    question_id BIGINT,
    answer_id   BIGINT,
    CONSTRAINT pk_quiz_answers PRIMARY KEY (id)
);

CREATE TABLE main_users
(
    id           BIGINT NOT NULL,
    phone_number VARCHAR(255),
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    email        VARCHAR(255),
    CONSTRAINT pk_main_users PRIMARY KEY (id)
);

ALTER TABLE answers
    ADD CONSTRAINT FK_ANSWERS_ON_QUESTION FOREIGN KEY (question_id) REFERENCES questions (id);

ALTER TABLE quiz_answers
    ADD CONSTRAINT FK_QUIZ_ANSWERS_ON_ANSWER FOREIGN KEY (answer_id) REFERENCES answers (id);

ALTER TABLE quiz_answers
    ADD CONSTRAINT FK_QUIZ_ANSWERS_ON_QUESTION FOREIGN KEY (question_id) REFERENCES questions (id);

ALTER TABLE quiz_answers
    ADD CONSTRAINT FK_QUIZ_ANSWERS_ON_USER FOREIGN KEY (user_id) REFERENCES main_users (id);