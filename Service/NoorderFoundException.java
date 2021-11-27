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
public class NoorderFoundException extends Exception{
    public NoorderFoundException(String message)
    {
        super(message);
    }
    public NoorderFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
