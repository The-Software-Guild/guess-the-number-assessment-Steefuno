/*
 * @author Steven Nguyen
 * @email: steven.686295@gmail.com
 * @date: 18 Aug 2021
 */

package com.mthree.guess_the_number_assessment.controllers;

import com.mthree.guess_the_number_assessment.models.Guess;
import com.mthree.guess_the_number_assessment.models.GuessResult;
import com.mthree.guess_the_number_assessment.services.DataService;
import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Steven
 */
@RestController
@RequestMapping("/api/guess_the_number")
public class GuessTheNumberController {
    @Autowired
    private DataService dataService;
    
    /**
     * Create a new game
     * @return the id of the new game
     */
    @PostMapping("/begin")
    public int begin() {
        return dataService.createGame();
    }
    
    /**
     * Applys a user's guess returning either that they solved the puzzle, or hints for their next guess
     * @param userGuess the user's guess
     * @param gameId the id of the game
     * @return the results of the user's guess
     * @throws SQLIntegrityConstraintViolationException 
     */
    @PostMapping("/guess")
    public GuessResult guess(Guess userGuess, int gameId) throws SQLIntegrityConstraintViolationException {
        return dataService.addGuess(userGuess, gameId);
    }
    
    /**
     * Gets the ids of all active games
     * @return the ids of all active games
     */
    @GetMapping("/game")
    public int[] getAllGames() {
        return dataService.getAllGames();
    }
    
    /**
     * Gets the results of the most recent guess of a select game
     * @param gameId the id of the select game
     * @return the GuessResult
     */
    @GetMapping("/game/{gameId}")
    public GuessResult getGame(@PathVariable int gameId) {
        return dataService.getGame(gameId);
    }
    
    /**
     * Get the array of guesses and results of guesses for a select game
     * @param gameId the id of the select game
     * @return the array of GuessResults
     */
    @GetMapping("/rounds/{gameId}")
    public GuessResult[] getRounds(@PathVariable int gameId) {
        return dataService.getRounds(gameId);
    }
}
