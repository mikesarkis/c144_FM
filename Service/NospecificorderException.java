/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.serivce;

/**
 *
 * @author Mike
 */
public class NospecificorderException extends Exception{
        public NospecificorderException(String message)
    {
        super(message);
    }
    public NospecificorderException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
}
