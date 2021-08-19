/*
 * @author Steven Nguyen
 * @email: steven.686295@gmail.com
 * @date: 19 Aug 2021
 */
package com.mthree.guess_the_number_assessment.daos;

import com.mthree.guess_the_number_assessment.models.Guess;
import com.mthree.guess_the_number_assessment.models.Round;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 *
 * @author Steven
 */
public interface GamesDao {
    /**
     * Create a new game
     * @param answer the solution to the game
     * @return the id of the new game
     */
    public int createGame(int[] answer);
    
    /**
     * 
     * @param gameId 
     */
    public void endGame(int gameId);

    /**
     * Applys a user's guess returning either that they solved the puzzle, or hints for their next guess
     * @param guess the user's guess
     * @param gameId the id of the game
     * @return the results of the user's guess
     * @throws SQLIntegrityConstraintViolationException 
     */
    public Round addGuess(Guess guess, int gameId) throws SQLIntegrityConstraintViolationException;
    
    /**
     * Gets the ids of all active games
     * @return the ids of all active games
     */
    public int[] getAllGames();
    
    /**
     * Gets the data of the most recent round of a select game
     * @param gameId the id of the select game
     * @return the Round data
     */
    public Round getGame(int gameId);
    
    /**
     * Get the array of rounds for a select game
     * @param gameId the id of the select game
     * @return the array of Rounds
     */
    public Round[] getRounds(int gameId);
}
