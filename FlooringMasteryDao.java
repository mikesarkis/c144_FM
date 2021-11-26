/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

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
public interface FlooringMasteryDao {
     public Order addOrder(String date, String name, String State, BigDecimal taxrate, String type, BigDecimal area, BigDecimal costperfoot,BigDecimal laborcostperfoot);
    public List<Order> get_all_orders();
    public Order remove_order(int number);
    public Order edit_order(int number,Order copy);
    public Order getOrder(int number);

    
}
