#!/bin/bash
#set -e
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE skeleton;

    CREATE USER docker PASSWORD 'password';
    GRANT ALL PRIVILEGES ON DATABASE skeleton TO docker;

    \connect skeleton;

    CREATE TABLE users(id BIGINT PRIMARY KEY NOT NULL, status CHARACTER VARYING(255));

    INSERT INTO users(id, status) VALUES (1, 'unregister');
    INSERT INTO users(id, status) VALUES (2, 'registered');
    INSERT INTO users(id, status) VALUES (3, 'unknown');

EOSQL
echo