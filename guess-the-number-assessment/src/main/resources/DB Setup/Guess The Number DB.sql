-- reconstruct the database
DROP DATABASE IF EXISTS GuessTheNumberDB;
CREATE DATABASE GuessTheNumberDB;

USE GuessTheNumberDB;

CREATE TABLE game(
	gameId INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
    PRIMARY KEY (gameId)
);

CREATE TABLE round(
	gameId INT UNSIGNED NOT NULL,
	roundId INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
    time TIMESTAMP NOT NULL,
    PRIMARY KEY (gameId, roundId),
    CONSTRAINT FOREIGN KEY fk_round_game(gameId)
		REFERENCES game(gameId)
);

CREATE TABLE guess(
	roundId INT UNSIGNED NOT NULL,
    guessNumber INT UNSIGNED NOT NULL,
    value INT UNSIGNED NOT NULL,
    PRIMARY KEY (roundId, guessNumber),
    CONSTRAINT FOREIGN KEY fk_guess_round(roundId)
		REFERENCES round(roundId)
);

CREATE TABLE solution(
	gameId INT UNSIGNED NOT NULL,
    solutionNumber INT UNSIGNED NOT NULL,
    value INT UNSIGNED NOT NULL,
    PRIMARY KEY (gameId, solutionNumber),
    CONSTRAINT FOREIGN KEY fk_solution_game(gameId)
		REFERENCES game(gameId)
);

INSERT INTO game() VALUES
	()
;
SELECT * FROM game;
INSERT INTO solution(gameId, solutionNumber, value) VALUES
	(1, 0, 1),
    (1, 1, 4),
    (1, 2, 8),
    (1, 3, 3)
;