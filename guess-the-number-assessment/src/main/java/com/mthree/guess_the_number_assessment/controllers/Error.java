/*
 * @author Steven Nguyen
 * @email: steven.686295@gmail.com
 * @date: 22 Aug 2021
 */

package com.mthree.guess_the_number_assessment.controllers;

import java.time.LocalDateTime;

/**
 * 
 * @author Steven
 */
public class Error {

    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    
    public Error(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}