/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

import com.mycompany.flooringmastery.dto.Order;
import java.util.List;

/**
 *
 * @author Mike
 */
public interface FlooringMasteryDao {
    public Order addOrder(int number, Order order);
    public List<Order> get_all_orders();
    public Order remove_order(int number);
    public Order edit_order(int number , String data, int choice);
    
}
