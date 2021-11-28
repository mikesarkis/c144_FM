/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.flooringmastery.dto;

import java.math.BigDecimal;

/**
 *
 * @author john 
 */
public  class Tax {
    private final String fullName;
    private final String shortName;
    private final BigDecimal taxRate;
    
    public Tax(String shortName, String fullName, BigDecimal taxRate) {
        this.shortName = shortName;
        this.fullName = fullName;
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }
}
