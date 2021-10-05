CREATE DATABASE tacos;

CREATE USER taco_admin WITH PASSWORD 'Pa$$w0rd'

GRANT ALL privileges ON DATABASE tacos TO taco_admin

CREATE TABLE clients (id SERIAL, nickname VARCHAR(50), email VARCHAR(50), password VARCHAR(50), token INTEGER);

INSERT INTO clients (nickname, email, password, token)
VALUES ('TestUser', 'test@test.com', '12321', 1337);

