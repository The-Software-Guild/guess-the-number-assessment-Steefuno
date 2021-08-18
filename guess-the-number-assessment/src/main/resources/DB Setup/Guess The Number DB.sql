-- reconstruct the database
DROP DATABASE IF EXISTS GuessTheNumberDB;
CREATE DATABASE GuessTheNumberDB;

USE GuessTheNumberDB;

CREATE TABLE game(
	gameId INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
    slot1 INT UNSIGNED NOT NULL,
    slot2 INT UNSIGNED NOT NULL,
    slot3 INT UNSIGNED NOT NULL,
    slot4 INT UNSIGNED NOT NULL,
    PRIMARY KEY (gameId)
);

CREATE TABLE round(
	roundId INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
    time TIMESTAMP NOT NULL,
    slot1 INT UNSIGNED NOT NULL,
    slot2 INT UNSIGNED NOT NULL,
    slot3 INT UNSIGNED NOT NULL,
    slot4 INT UNSIGNED NOT NULL,
    PRIMARY KEY (roundId)
);

CREATE TABLE game_has_round(
	gameId INT UNSIGNED NOT NULL,
	roundId INT UNSIGNED NOT NULL
);