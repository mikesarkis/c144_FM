/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.service;

/**
 *
 * @author Mike
 */
public class DateNotValidException extends Exception {

    public DateNotValidException(String message) {
        super(message);
    }

    public DateNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

}
