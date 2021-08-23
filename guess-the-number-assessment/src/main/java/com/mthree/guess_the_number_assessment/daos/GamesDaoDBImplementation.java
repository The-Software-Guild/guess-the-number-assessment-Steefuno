/*
 * @author Steven Nguyen
 * @email: steven.686295@gmail.com
 * @date: 19 Aug 2021
 */

package com.mthree.guess_the_number_assessment.daos;

import com.mthree.guess_the_number_assessment.models.Round;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author Steven
 */
@Repository
public class GamesDaoDBImplementation implements GamesDao {
    final private JdbcTemplate jdbcTemplate;
    
    final private static String INSERT_GAME_SQL = 
        "INSERT INTO game() " +
        "VALUES () "
    ;
    final private static String INSERT_GAME_SOLUTION_SQL =
        "INSERT INTO solution(gameId, solutionNumber, value) " +
        "VALUES (?, ?, ?) "
    ;
    final private static String DELETE_GUESSES_SQL = 
        "DELETE guess " +
        "FROM round " +
        "INNER JOIN guess ON guess.roundId = round.roundId " +
        "WHERE round.gameId = ? "
    ;
    final private static String DELETE_ROUNDS_SQL = 
        "DELETE round " +
        "FROM round " +
        "WHERE round.gameId = ? "
    ;
    final private static String DELETE_SOLUTIONS_SQL = 
        "DELETE solution " +
        "FROM solution " +
        "WHERE solution.gameId = ? "
    ;
    final private static String DELETE_GAME_SQL = 
        "DELETE game " +
        "FROM game " +
        "WHERE game.gameId = ? "
    ;
    final private static String INSERT_ROUND_SQL = 
        "INSERT INTO round(gameId, time) " +
        "VALUES (?, ?) "
    ;
    final private static String INSERT_GUESS_SQL = 
        "INSERT INTO guess(roundId, guessNumber, value) " +
        "VALUES (?, ?, ?) "
    ;
    final private static String SELECT_SOLUTION_SQL =
        "SELECT " +
        "    solution.solutionNumber as solutionNumber, " +
        "    solution.value as value " +
        "FROM game " +
        "INNER JOIN solution ON solution.gameId = game.gameId " +
        "WHERE game.gameId = ? " +
        "ORDER BY solutionNumber ASC "
    ;
    final private static String SELECT_GUESSES_SQL =
        "SELECT " +
        "    round.time as time, " +
        "    guess.guessNumber as guessNumber, " +
        "    guess.value as value " +
        "FROM round " +
        "INNER JOIN guess ON guess.roundId = round.roundId " +
        "WHERE round.roundId = ? " +
        "ORDER BY guess.guessNumber ASC "
    ;
    final private static String SELECT_GAMEIDS_SQL = 
        "SELECT gameId " +
        "FROM game " +
        "ORDER BY gameId ASC "
    ;
    final private static String COUNT_ROUNDS_SQL = 
        "SELECT COUNT(*) " +
        "FROM round " +
        "WHERE round.gameId = ? "
    ;
    final private static String SELECT_ALL_ROUNDS_SQL =
        "SELECT " +
        "   round.roundId as roundId, " +
        "   round.time as time, " +
        "   guess.guessNumber as guessNumber, " +
        "   guess.value as guessValue " +
        "FROM round " +
        "INNER JOIN guess ON guess.roundId = round.roundId " +
        "WHERE round.gameId = ? " +
        "ORDER BY round.time DESC "
    ;
    
    @Autowired
    public GamesDaoDBImplementation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * Create a new game
     * @param answer the solution to the game
     * @return the id of the new game
     */
    @Transactional
    @Override
    public int createGame(int[] answer) {
        KeyHolder keyHolder;
        int gameId;
        
        keyHolder = new GeneratedKeyHolder();
        
        // create a new game
        jdbcTemplate.update(
            (Connection connection) -> {
                PreparedStatement preparedStatement;
                
                preparedStatement = connection.prepareStatement(INSERT_GAME_SQL, Statement.RETURN_GENERATED_KEYS);
                return preparedStatement;
            },
            keyHolder
        );
        
        // get the generated gameId
        gameId = keyHolder.getKey().intValue();
        
        // insert the solution
        jdbcTemplate.batchUpdate(
            INSERT_GAME_SOLUTION_SQL,
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
    
    @Transactional
    @Override
    public void endGame(int gameId) {
        // delete the guesses
        jdbcTemplate.update(
            (Connection connection) -> {
                PreparedStatement preparedStatement;
                
                preparedStatement = connection.prepareStatement(DELETE_GUESSES_SQL);
                preparedStatement.setInt(1, gameId);
                return preparedStatement;
            }
        );
        
        // delete the rounds
        jdbcTemplate.update(
            (Connection connection) -> {
                PreparedStatement preparedStatement;
                
                preparedStatement = connection.prepareStatement(DELETE_ROUNDS_SQL);
                preparedStatement.setInt(1, gameId);
                return preparedStatement;
            }
        );
        
        // delete the solutions
        jdbcTemplate.update(
            (Connection connection) -> {
                PreparedStatement preparedStatement;
                
                preparedStatement = connection.prepareStatement(DELETE_SOLUTIONS_SQL);
                preparedStatement.setInt(1, gameId);
                return preparedStatement;
            }
        );
        
        // delete the game
        jdbcTemplate.update(
            (Connection connection) -> {
                PreparedStatement preparedStatement;
                
                preparedStatement = connection.prepareStatement(DELETE_GAME_SQL);
                preparedStatement.setInt(1, gameId);
                return preparedStatement;
            }
        );
        
        return;
    }

    /**
     * Applys a user's guess returning either that they solved the puzzle, or hints for their next guess
     * @param guesses the user's guess
     * @param gameId the id of the game
     * @return the results of the user's guess
     * @throws SQLIntegrityConstraintViolationException 
     */
    @Transactional
    @Override
    public Round addGuess(int[] guesses, int gameId) throws SQLIntegrityConstraintViolationException {
        KeyHolder keyHolder;
        int roundId;
        List<GuessValue> guessValuesList;
        int[] solutionValues;
        int[] guessValues;
        
        keyHolder = new GeneratedKeyHolder();
        
        // insert the new round
        jdbcTemplate.update(
            (Connection connection) -> {
                PreparedStatement preparedStatement;
                
                preparedStatement = connection.prepareStatement(INSERT_ROUND_SQL, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, gameId);
                preparedStatement.setTimestamp(
                    2,
                    Timestamp.from(
                        Instant.now()
                    )
                );
                
                return preparedStatement;
            },
            keyHolder
        );
        
        // get the round id
        roundId = keyHolder.getKey().intValue();
        
        // insert the guess
        jdbcTemplate.batchUpdate(
            INSERT_GUESS_SQL,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
                    preparedStatement.setInt(1, roundId);
                    preparedStatement.setInt(2, index);
                    preparedStatement.setInt(3, guesses[index]);
                }
                
                @Override
                public int getBatchSize() {
                    return guesses.length;
                }
            }
        );
        
        
        // get the solutions for that game
        solutionValues = getSolution(gameId);
        
        // get the user guesses of that round
        guessValuesList = jdbcTemplate.query(SELECT_GUESSES_SQL, new GuessValueMapper(), roundId);
        
        // group the list of guessValues to an array
        guessValues = new int[guessValuesList.size()];
        guessValuesList.forEach(
            guessValue -> {
                guessValues[guessValue.index] = guessValue.value;
            }
        );
        
        // group into Round
        return new Round(
            guessValuesList.get(0).time,
            solutionValues,
            guessValues
        );
    }
    
    /**
     * Gets the ids of all active games
     * @return the ids of all active games
     */
    @Override
    public int[] getAllGames() {
        List<Integer> gameIdsList;
        int[] gameIds;
        
        // get all ids
        gameIdsList = jdbcTemplate.query(SELECT_GAMEIDS_SQL, new IntegerMapper());
        
        // convert to array
        gameIds = new int[gameIdsList.size()];
        for (int i = 0; i < gameIdsList.size(); i++) {
            gameIds[i] = gameIdsList.get(i);
        }
        
        return gameIds;
    }
    
    /**
     * Gets the data of the most recent round of a select game
     * @param gameId the id of the select game
     * @return the Round data
     */
    @Transactional
    @Override
    public Round getGame(int gameId) {
        List<RoundGuessSlot> roundGuessSlots;
        int[] solutionValues;
        int[] guessValues;
        
        // get the most recent round id and time
        roundGuessSlots = jdbcTemplate.query(SELECT_ALL_ROUNDS_SQL + "LIMIT 4 ", new RoundGuessSlotsMapper(), gameId);
        
        // get the solutions for that game
        solutionValues = getSolution(gameId);
        
        // if no guesses have been made
        if (roundGuessSlots.isEmpty()) {
            return null;
        }
        
        // group the list of roundGuessSlots to an array of guessValues
        guessValues = new int[roundGuessSlots.size()];
        roundGuessSlots.forEach(
            roundGuessSlot -> {
                guessValues[roundGuessSlot.index] = roundGuessSlot.value;
            }
        );
        
        // group into a Round
        return new Round(
            roundGuessSlots.get(0).time,
            solutionValues,
            guessValues
        );
    }
    
    /**
     * Get the array of rounds for a select game
     * @param gameId the id of the select game
     * @return the array of Rounds
     */
    @Transactional
    @Override
    public Round[] getRounds(int gameId) {
        int numRounds;
        List<RoundGuessSlot> roundGuessSlots;
        ArrayList<Round> rounds;
        int[] solutionValues;
        HashMap<Integer, Timestamp> times;
        HashMap<Integer, int[]> guesses;
        
        // get the number of rounds
        numRounds = jdbcTemplate.queryForObject(COUNT_ROUNDS_SQL, new IntegerMapper(), gameId);
        
        // return an empty array if no rounds exist
        if (numRounds == 0) {
            return new Round[0];
        }
        
        // get the solutions for that game
        solutionValues = getSolution(gameId);
        
        // initialize fields
        rounds = new ArrayList(numRounds);
        times = new HashMap<>();
        guesses = new HashMap<>();
        
        // get all the roundGuessSlots for the gameId
        roundGuessSlots = jdbcTemplate.query(SELECT_ALL_ROUNDS_SQL, new RoundGuessSlotsMapper(), gameId);
        
        // pull the times and guesses from the roundGuessSlots 
        roundGuessSlots.forEach(
            roundGuessSlot -> {
                int[] guess;
                
                times.put(roundGuessSlot.roundId, roundGuessSlot.time);
                guess = guesses.get(roundGuessSlot.roundId);
                // create the Integer array if DNE
                if (guess == null) {
                    guess = new int[4];
                    guesses.put(roundGuessSlot.roundId, guess);
                }
                
                guess[roundGuessSlot.index] = roundGuessSlot.value;
            }
        );
        
        // generate the rounds
        times.entrySet().forEach(
            entrySet -> {
                rounds.add(
                    new Round(
                        entrySet.getValue(),
                        solutionValues,
                        guesses.get(entrySet.getKey())
                    )
                );
            }
        );
        
        return rounds.toArray(new Round[numRounds]);
    }
    
    private int[] getSolution(int gameId) {
        List<SolutionValue> solutionValuesList;
        int[] solutionValues;
        
        // get the solutions for that game
        solutionValuesList = jdbcTemplate.query(SELECT_SOLUTION_SQL, new SolutionValueMapper(), gameId);
        
        // group the list of solutionValues to an array
        solutionValues = new int[4];
        solutionValuesList.forEach(
            solutionValue -> {
                solutionValues[solutionValue.index] = solutionValue.value;
            }
        );
        
        return solutionValues;
    }
    
    
    private static final class SolutionValue {
        public int index;
        public int value;
        
        public SolutionValue(int index, int value) {
            this.index = index;
            this.value = value;
        }
    }
    
    private static final class SolutionValueMapper implements RowMapper<SolutionValue> {
        @Override
        public SolutionValue mapRow(ResultSet resultSet, int index) throws SQLException {
            return new SolutionValue(
                resultSet.getInt("solutionNumber"),
                resultSet.getInt("value")
            );
        }
    }
    
    private static final class GuessValue {
        public Timestamp time;
        public int index;
        public int value;
        
        public GuessValue(Timestamp time, int index, int value) {
            this.time = time;
            this.index = index;
            this.value = value;
        }
    }
    
    private static final class GuessValueMapper implements RowMapper<GuessValue> {
        @Override
        public GuessValue mapRow(ResultSet resultSet, int index) throws SQLException {
            return new GuessValue(
                resultSet.getTimestamp("time"),
                resultSet.getInt("guessNumber"),
                resultSet.getInt("value")
            );
        }
    }
    
    private static final class IntegerMapper implements RowMapper<Integer> {
        @Override
        public Integer mapRow(ResultSet resultSet, int index) throws SQLException {
            return resultSet.getInt(1); // first column is 1 💢
        }
    }
    
    private static final class RoundGuessSlot {
        public int roundId;
        public Timestamp time;
        public int index;
        public int value;
        public RoundGuessSlot(int roundId, Timestamp time, int guessNumber, int guessValue) {
            this.roundId = roundId;
            this.time = time;
            this.index = guessNumber;
            this.value = guessValue;
        }
    }
    
    private static final class RoundGuessSlotsMapper implements RowMapper<RoundGuessSlot> {
        @Override
        public RoundGuessSlot mapRow(ResultSet resultSet, int index) throws SQLException {
            return new RoundGuessSlot(
                resultSet.getInt("roundId"),
                resultSet.getTimestamp("time"),
                resultSet.getInt("guessNumber"),
                resultSet.getInt("guessValue")
            );
        }
    }
}