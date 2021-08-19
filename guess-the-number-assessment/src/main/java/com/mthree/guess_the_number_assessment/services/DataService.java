/*
 * @author Steven Nguyen
 * @email: steven.686295@gmail.com
 * @date: 19 Aug 2021
 */

package com.mthree.guess_the_number_assessment.services;

import com.mthree.guess_the_number_assessment.daos.GamesDao;
import com.mthree.guess_the_number_assessment.models.Guess;
import com.mthree.guess_the_number_assessment.models.GuessResult;
import com.mthree.guess_the_number_assessment.models.Round;
import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Steven
 */
public class DataService {
    @Autowired
    private GamesDao gamesDao;
    
    /**
     * Create a new game
     * @return the id of the new game
     */
    public int createGame() {
        return gamesDao.createGame();
    }
    
    /**
     * Applys a user's guess returning either that they solved the puzzle, or hints for their next guess
     * @param guess the user's guess
     * @param gameId the id of the game
     * @return the results of the user's guess
     * @throws SQLIntegrityConstraintViolationException 
     */
    public GuessResult addGuess(Guess guess, int gameId) throws SQLIntegrityConstraintViolationException {
        Round round;
        
        round = gamesDao.addGuess(guess, gameId);
        return round.getGuessResult();
    }
    
    /**
     * Gets the ids of all active games
     * @return the ids of all active games
     */
    public int[] getAllGames() {
        return gamesDao.getAllGames();
    }
    
    /**
     * Gets the results of the most recent guess of a select game
     * @param gameId the id of the select game
     * @return the GuessResult
     */
    public GuessResult getGame(int gameId) {
        Round latestRound;
        
        latestRound = gamesDao.getGame(gameId);
        return latestRound.getGuessResult();
    }
    
    /**
     * Get the array of guesses and results of guesses for a select game
     * @param gameId the id of the select game
     * @return the array of GuessResults
     */
    public GuessResult[] getRounds(int gameId) {
        GuessResult[] results;
        Round[] rounds;
        
        rounds = gamesDao.getRounds(gameId);
        results = new GuessResult[rounds.length];
        
        for (int i = 0; i < rounds.length; i++) {
            results[i] = rounds[i].getGuessResult();
        }
        
        return results;
    }
}
