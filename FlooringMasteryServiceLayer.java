/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.serivce;

import com.mycompany.flooringmastery.dto.Order;
import com.mycompany.flooringmastery.dto.Product;
import com.mycompany.flooringmastery.dto.Tax;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Mike
 */
public interface FlooringMasteryServiceLayer {
    public Order addorder(String date, String name, String State, BigDecimal taxrate, String type,BigDecimal costperfoot,BigDecimal laborcostperfoot, BigDecimal area ) throws NodateFoundException;
    public Order editOrder(int number, String date, Order copy) throws  NodateFoundException;
    public void removeOrder(int number, String date) throws  NodateFoundException;
    public Order getOrder(int number, String date) throws  NodateFoundException;
    public List<Order> getOrders(String date) throws  NodateFoundException;
    public List<Tax> getalltaxes() throws  FileNotFoundException;
    public BigDecimal getTaxrate(String state) throws  FileNotFoundException;
    public List<Product> getallproduct() throws FileNotFoundException;
    public BigDecimal getcost(String type) throws FileNotFoundException;
    public BigDecimal getLaborcost(String type) throws FileNotFoundException;
    
    
}
