package com.mycompany.flooringmastery.dao;

import com.mycompany.flooringmastery.dto.Order;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Mike
 */
public interface FlooringMasteryDao {

    public Order addOrder(String date, String name, String State, BigDecimal taxrate, String type, BigDecimal area, BigDecimal costperfoot, BigDecimal laborcostperfoot) throws FileNotFoundException, IOException;

    public List<Order> get_all_orders() throws FileNotFoundException;

    public Order remove_order(int number) throws FileNotFoundException, IOException;

    public Order edit_order(int number, Order copy) throws FileNotFoundException, IOException;

    public Order getOrder(int number) throws FileNotFoundException;

    public void setFileName(String filename) throws IOException;

    public void exportData() throws FileNotFoundException, IOException;

}
