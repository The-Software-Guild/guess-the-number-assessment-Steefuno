/*
 * @author Steven Nguyen
 * @email: steven.686295@gmail.com
 * @date: 
 */
package com.mthree.guess_the_number_assessment.services;

import com.mthree.guess_the_number_assessment.daos.GamesDao;
import com.mthree.guess_the_number_assessment.models.GuessResult;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Steven
 */
@SpringBootTest
public class DataServiceTest {
    @Autowired
    private DataService dataService;
    
    @Autowired
    private GamesDao gamesDao;
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        int[] gameIds;
        
        gameIds = gamesDao.getAllGames();
        
        for(int id: gameIds) {
            gamesDao.endGame(id);
        }
    }
    
    @AfterEach
    public void tearDown() {
        int[] gameIds;
        
        gameIds = gamesDao.getAllGames();
        
        for(int id: gameIds) {
            gamesDao.endGame(id);
        }
    }
    
    @Test
    public void createGame_Should_Create_Unique_Game() {
        int[] gameIds;
        int gameId;
        
        // check initialization of db
        gameIds = gamesDao.getAllGames();
        assertTrue(
            gameIds.length == 0,
            "there should not be any active games."
        );
        
        // check adding 1 game
        gameId = dataService.createGame();
        gameIds = gamesDao.getAllGames();
        assertTrue(
            gameIds.length == 1,
            "there should be one active game."
        );
        assertTrue(
            gameIds[0] == gameId,
            "the 1st active game should be the new game."
        );
        
        // check adding another game
        gameId = dataService.createGame();
        gameIds = gamesDao.getAllGames();
        assertTrue(
            gameIds.length == 2,
            "there should be one active game."
        );
        assertTrue(
            gameIds[1] == gameId,
            "the 2nd active game should be the new game."
        );
    }
    
    @Test
    public void addGuess_Should_Return_Results() {
        int gameId;
        GuessResult guessResult;
        char[] results;
        int[] guesses;
        
        // create the game and get the ID
        gameId = gamesDao.createGame(
            new int[]{0, 1, 2, 3}
        );
        
        // guess and get the GuessResult
        try {
            guessResult = dataService.addGuess(
                new int[]{0, 2, 4, 5},
                gameId
            );
        } catch (SQLIntegrityConstraintViolationException e) {
            fail("addGuess should not error.");
            return;
        }
        
        // assert that the results are valid
        assertTrue(
            guessResult != null,
            "guessResult should not be null."
        );
        assertTrue(
            guessResult.getTime() != null,
            "timestamp should not be null."
        );
        results = guessResult.getResults();
        assertTrue(
            (
                (results != null) &&
                (results[0] == 'e') && (results[1] == 'p') && (results[2] == '0') && (results[3] == '0')
            ),
            "results should be \"ep00\". got " + Arrays.toString(results)
        );
        results = guessResult.getResults();
        assertTrue(
            (
                (results != null) &&
                (results[0] == 'e') && (results[1] == 'p') && (results[2] == '0') && (results[3] == '0')
            ),
            "results should be \"ep00\". got " + Arrays.toString(results)
        );
        guesses = guessResult.getGuesses();
        assertTrue(
            (
                (guesses != null) &&
                (guesses[0] == 0) && (guesses[1] == 2) && (guesses[2] == 4) && (guesses[3] == 5)
            ),
            "guesses should be \"0245\". got " + Arrays.toString(guesses)
        );
    }
    
    @Test
    public void addGuess_Should_End_Solved_Game() {
        int gameId;
        
        // create the game and get the ID
        gameId = gamesDao.createGame(
            new int[]{0, 1, 2, 3}
        );
        
        // guess
        try {
            dataService.addGuess(
                new int[]{0, 1, 2, 3},
                gameId
            );
        } catch (SQLIntegrityConstraintViolationException e) {
            fail("addGuess should not error.");
            return;
        }
        
        // assert that the game has ended
        assertTrue(
            dataService.getAllGames().length == 0,
            "there should be 0 games."
        );
    }
    
    @Test
    public void addGuess_Should_Throw_Exception_On_Invalid_Game() {
        int gameId;
        
        // create the game and get the ID
        gameId = dataService.createGame();
        
        // guess
        try {
            dataService.addGuess(
                new int[]{0, 1, 2, 3},
                gameId + 1
            );
            fail("addGuess should error.");
        } catch (Exception e) {
            return;
        }
    }
    
    @Test
    public void getAllGames_Should_Get_All_Games() {
        int gameId;
        int[] gameIds;
        
        // create the game and get the ID
        gameId = dataService.createGame();
        
        gameIds = dataService.getAllGames();
        assertTrue(
            (gameIds != null) && (gameIds.length == 1) && (gameIds[0] == gameId),
            "getAllGames should get the newly created game."
        );
    }
    
    @Test
    public void getGame_Should_Get_The_Latest_Round() {
        int gameId;
        GuessResult guessResult;
        char[] results;
        int[] guesses;
        
        // create the game and get the ID
        gameId = gamesDao.createGame(
            new int[]{0, 1, 2, 3}
        );
        guessResult = dataService.getGame(gameId);
        
        // assert that the latest round is null
        assertTrue(
            guessResult == null,
            "guessResult should be null."
        );
        
        // guess
        try {
            dataService.addGuess(
                new int[]{6, 9, 4, 3},
                gameId
            );
        } catch (SQLIntegrityConstraintViolationException e) {
            fail("addGuess should not error.");
            return;
        }
        
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            fail("Sleep should not error.");
            return;
        }
        
        // guess and get the GuessResult
        try {
            dataService.addGuess(
                new int[]{0, 2, 4, 5},
                gameId
            );
        } catch (SQLIntegrityConstraintViolationException e) {
            fail("addGuess should not error.");
            return;
        }
        
        guessResult = dataService.getGame(gameId);
        
        // assert that the results are valid
        assertTrue(
            guessResult != null,
            "guessResult should not be null."
        );
        assertTrue(
            guessResult.getTime() != null,
            "timestamp should not be null."
        );
        results = guessResult.getResults();
        assertTrue(
            (
                (results != null) &&
                (results[0] == 'e') && (results[1] == 'p') && (results[2] == '0') && (results[3] == '0')
            ),
            "results should be \"ep00\". got " + Arrays.toString(results)
        );
        results = guessResult.getResults();
        assertTrue(
            (
                (results != null) &&
                (results[0] == 'e') && (results[1] == 'p') && (results[2] == '0') && (results[3] == '0')
            ),
            "results should be \"ep00\". got " + Arrays.toString(results)
        );
        guesses = guessResult.getGuesses();
        assertTrue(
            (
                (guesses != null) &&
                (guesses[0] == 0) && (guesses[1] == 2) && (guesses[2] == 4) && (guesses[3] == 5)
            ),
            "guesses should be \"0245\". got " + Arrays.toString(guesses)
        );
    }
    
    @Test
    public void getGame_Should_Get_null_On_DNE_Game() {
        GuessResult guessResult;
        
        guessResult = dataService.getGame(0);
        
        // assert that the latest round is null
        assertTrue(
            guessResult == null,
            "guessResult should be null."
        );
    }
    
    @Test
    public void getRounds_Should_Get_All_Rounds() {
        int gameId;
        GuessResult guessResult;
        GuessResult[] guessResults;
        char[] results;
        int[] guesses;
        
        // create the game and get the ID
        gameId = gamesDao.createGame(
            new int[]{0, 1, 2, 3}
        );
        
        // guess
        try {
            dataService.addGuess(
                new int[]{6, 9, 4, 3},
                gameId
            );
        } catch (SQLIntegrityConstraintViolationException e) {
            fail("addGuess should not error.");
            return;
        }
        
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            fail("Sleep should not error.");
            return;
        }
        
        // guess and get the GuessResult
        try {
            dataService.addGuess(
                new int[]{0, 2, 4, 5},
                gameId
            );
        } catch (SQLIntegrityConstraintViolationException e) {
            fail("addGuess should not error.");
            return;
        }
        
        guessResults = dataService.getRounds(gameId);
        assertTrue(
            guessResults.length == 2,
            "guessResults should have 2 guesses."
        );
        
        guessResult = guessResults[0];
        
        // assert that the results are valid
        assertTrue(
            guessResult != null,
            "guessResult should not be null."
        );
        assertTrue(
            guessResult.getTime() != null,
            "timestamp should not be null."
        );
        results = guessResult.getResults();
        assertTrue(
            (
                (results != null) &&
                (results[0] == 'e') && (results[1] == 'p') && (results[2] == '0') && (results[3] == '0')
            ),
            "results should be \"ep00\". got " + Arrays.toString(results)
        );
        guesses = guessResult.getGuesses();
        assertTrue(
            (
                (guesses != null) &&
                (guesses[0] == 0) && (guesses[1] == 2) && (guesses[2] == 4) && (guesses[3] == 5)
            ),
            "guesses should be \"0245\". got " + Arrays.toString(guesses)
        );
    }
    
    @Test
    public void getRounds_Should_Get_Empty_On_DNE_Game() {
        GuessResult[] guessResults;
        
        guessResults = dataService.getRounds(0);
        
        // assert that the latest round is null
        assertTrue(
            guessResults.length == 0,
            "guessResults should be empty."
        );
    }
}
