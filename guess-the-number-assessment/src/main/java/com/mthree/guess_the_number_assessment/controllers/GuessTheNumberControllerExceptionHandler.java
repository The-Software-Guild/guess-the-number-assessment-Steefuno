/*
 * @author Steven Nguyen
 * @email: steven.686295@gmail.com
 * @date: 
 */

package com.mthree.guess_the_number_assessment.controllers;

import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 
 * @author Steven
 */
public class GuessTheNumberControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String MESSAGE = "Your guess is invalid. Please make sure you have 4 slots.";
    
    /**
     * On a sql constraint exception, tell the client they had a bad request
     * @param ex the exception
     * @param request the client's request
     * @return 
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public final ResponseEntity<Error> handleSqlException(
        SQLIntegrityConstraintViolationException ex,
        WebRequest request
    ) {
        Error err = new Error(MESSAGE);
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
}
