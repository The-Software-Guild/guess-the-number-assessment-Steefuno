/*
 * @author Steven Nguyen
 * @email: steven.686295@gmail.com
 * @date: 19 Aug 2021
 */

package com.mthree.guess_the_number_assessment.models.dao_db_implementation_models;

/**
 * 
 * @author Steven
 */
public class GameId {
    private int gameId;
    
    /**
     * Constructs a new GameId
     * @param gameId the game id
     */
    public GameId(int gameId) {
        this.gameId = gameId;
    }
    
    /**
     * Gets the game id
     * @return the game id
     */
    public int getGameId() {
        return gameId;
    }
}
