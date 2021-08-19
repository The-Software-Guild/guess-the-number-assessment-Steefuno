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
public class Round {
    final private Timestamp time;
    final private int[] answer;
    final private int[] guesses;
    
    /**
     * Constructs a new GuessResult given the time, results, and guesses
     * @param time the time of the guess
     * @param answer the correct answer of the guess
     * @param guesses the guesses made by the user
     */
    public Round(Timestamp time, int[] answer, int[] guesses) {
        this.time = time;
        this.answer = answer;
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
     * Gets the guesses
     * @return the guesses
     */
    public int[] getGuesses() {
        return guesses;
    }
    
    /**
     * Gets the results of the guess
     * @return the GuessResult
     */
    public GuessResult getGuessResult() {
        GuessResult result;
        char[] results = new char[guesses.length];
        
        // for each guess, generate the hint
        for (int guessNumber = 0; guessNumber < guesses.length; guessNumber++) {
            // initializes value to '0' in case no match is found
            results[guessNumber] = '0';
            
            // check each answer to match the value
            answerSlots: for (int answerNumber = 0; answerNumber < answer.length; answerNumber++) {
                if (answer[answerNumber] == guesses[guessNumber]) {
                    // exact match if the guess value matches the answer value at the same slot
                    if (answerNumber == guessNumber) {
                        results[guessNumber] = 'e';
                    } else { // partial match if the guess value matches the answer value at a different slot
                        results[guessNumber] = 'p';
                    }
                    break answerSlots;
                }
            }
        }
        
        result = new GuessResult(time, results, guesses);
        return result;
    }
}
