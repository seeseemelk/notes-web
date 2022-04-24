CREATE TABLE TodoItem
(
    id BIGINT NOT NULL PRIMARY KEY,
    title VARCHAR(255),
    content VARCHAR(255),
    creationDate TIMESTAMP WITHOUT TIME ZONE
);

CREATE SEQUENCE hibernate_sequence START 1;
