/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.service;

import com.mycompany.flooringmastery.dto.Order;
import com.mycompany.flooringmastery.dto.Product;
import com.mycompany.flooringmastery.dto.Tax;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Mike
 */
public interface FlooringMasteryServiceLayer {
    public Order addOrder(String date, String name, String State, BigDecimal taxrate, String type,BigDecimal costperfoot,BigDecimal laborcostperfoot, BigDecimal area ) throws FileNotFoundException, IOException;
    public Order editOrder(int number, String date, Order copy) throws IOException, NoOrderFoundException , NoSpecificOrderException;
    public void removeOrder(int number, String date) throws IOException, NoOrderFoundException , NoSpecificOrderException;
    public Order getOrder(int number, String date) throws IOException, NoOrderFoundException , NoSpecificOrderException;
    public List<Order> getOrders(String date) throws  NoOrderFoundException, IOException;
    public List<Tax> getAllTaxes() throws  FileNotFoundException;
    public BigDecimal getTaxRate(String state) throws  FileNotFoundException;
    public List<Product> getAllProduct() throws FileNotFoundException;
    public BigDecimal getCost(String type) throws FileNotFoundException;
    public BigDecimal getLaborCost(String type) throws FileNotFoundException;
    public void exportData(String date) throws FileNotFoundException, IOException;
    
    
}
