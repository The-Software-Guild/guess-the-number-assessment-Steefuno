/*
 * @author Steven Nguyen
 * @email: steven.686295@gmail.com
 * @date: 19 Aug 2021
 */

package com.mthree.guess_the_number_assessment.daos;

import com.mthree.guess_the_number_assessment.models.Guess;
import com.mthree.guess_the_number_assessment.models.Round;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


/**
 * 
 * @author Steven
 */
@Repository
public class GamesDaoDBImplementation implements GamesDao {
    final private JdbcTemplate jdbcTemplate;
    
    final private static String CREATE_GAME_SQL = "INSERT INTO game() VALUES ();";
    final private static String INSERT_GAME_SOLUTION = "INSERT INTO solution(gameId, solutionNumber, value) VALUES (?, ?, ?)";
    
    @Autowired
    public GamesDaoDBImplementation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * Create a new game
     * @param answer the solution to the game
     * @return the id of the new game
     */
    @Override
    public int createGame(int[] answer) {
        KeyHolder keyHolder;
        int gameId;
        
        keyHolder = new GeneratedKeyHolder();
        
        // create a new game
        jdbcTemplate.update(
            (Connection connection) -> {
                PreparedStatement preparedStatement;

                preparedStatement = connection.prepareStatement(CREATE_GAME_SQL);
                return preparedStatement;
            },
            keyHolder
        );
        
        // get the generated gameId
        gameId = keyHolder.getKey().intValue();
        
        // insert the solution
        jdbcTemplate.batchUpdate(
            INSERT_GAME_SOLUTION,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
                    preparedStatement.setInt(1, gameId);
                    preparedStatement.setInt(2, index);
                    preparedStatement.setInt(3, answer[index]);
                }
                
                @Override
                public int getBatchSize() {
                    return answer.length;
                }
            }
        );
        
        return gameId;
    }
    
    @Override
    public void endGame(int gameId) {
        
    }

    /**
     * Applys a user's guess returning either that they solved the puzzle, or hints for their next guess
     * @param guess the user's guess
     * @param gameId the id of the game
     * @return the results of the user's guess
     * @throws SQLIntegrityConstraintViolationException 
     */
    @Override
    public Round addGuess(Guess guess, int gameId) throws SQLIntegrityConstraintViolationException {
        
    }
    
    /**
     * Gets the ids of all active games
     * @return the ids of all active games
     */
    @Override
    public int[] getAllGames() {
        
    }
    
    /**
     * Gets the data of the most recent round of a select game
     * @param gameId the id of the select game
     * @return the Round data
     */
    @Override
    public Round getGame(int gameId) {
        
    }
    
    /**
     * Get the array of rounds for a select game
     * @param gameId the id of the select game
     * @return the array of Rounds
     */
    @Override
    public Round[] getRounds(int gameId) {
        
    }
}
