package com.mycompany.flooringmastery.service;

/**
 *
 * @author Mike
 */
public class NoDateFoundException extends Exception {

    public NoDateFoundException(String message) {
        super(message);
    }

    public NoDateFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
