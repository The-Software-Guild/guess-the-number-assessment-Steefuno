/*
 * @author Steven Nguyen
 * @email: steven.686295@gmail.com
 * @date: 
 */

package com.mthree.guess_the_number_assessment.models;

/**
 * 
 * @author Steven
 */
public class Guess {
    final private int[] guess;
    final private int gameId;
    
    /**
     * Constructs a new Guess that stores the user's request body
     * @param guess the guess in the game
     * @param gameId the game's id
     */
    public Guess(int[] guess, int gameId) {
        this.guess = guess;
        this.gameId = gameId;
    }
    
    /**
     * Gets the guesses
     * @return the guesses
     */
    public int[] getGuesses() {
        return guess;
    }
    
    /**
     * Gets the game ID
     * @return the game ID
     */
    public int getGameId() {
        return gameId;
    }
}
