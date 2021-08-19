/*
 * @author Steven Nguyen
 * @email: steven.686295@gmail.com
 * @date: 19 Aug 2021
 */

package com.mthree.guess_the_number_assessment.models;

import java.sql.Timestamp;

/**
 * 
 * @author Steven
 */
public class GuessResult {
    final private Timestamp time;
    final private char[] results;
    final private int[] guesses;
    
    /**
     * Constructs a new GuessResult given the time, results, and guesses
     * @param time the time of the guess
     * @param results the results of the guess
     * @param guesses the guesses made by the user
     */
    public GuessResult(Timestamp time, char[] results, int[] guesses) {
        this.time = time;
        this.results = results;
        this.guesses = guesses;
    }
    
    /**
     * Gets the time the guess was made
     * @return the time
     */
    public Timestamp getTime() {
        return time;
    }
    
    /**
     * Gets the results
     * @return the results of the guess
     */
    public char[] getResults() {
        return results;
    }
    
    /**
     * Gets the guesses
     * @return the guesses
     */
    public int[] getGuesses() {
        return guesses;
    }
}
