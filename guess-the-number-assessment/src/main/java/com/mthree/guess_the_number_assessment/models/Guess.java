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
    private int[] guesses;
    
    /**
     * Constructs a new Guess given the guesses
     * @param guesses 
     */
    public Guess(int[] guesses) {
        this.guesses = guesses;
    }
    
    /**
     * Gets the guesses
     * @return the array of guesses
     */
    public int[] getGuesses() {
        return guesses;
    }
}
