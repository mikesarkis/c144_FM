/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

import com.mycompany.flooringmastery.dto.Order;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mike
 */
@Component
public class FlooringMasteryDaoImpl implements FlooringMasteryDao {

    private Map<Integer, Order> List_Order = new HashMap<>();
    private String FILE;
    private final String EXPORTFILE = "DataExport.txt";
    public static final String DELIMITER = ",";

    @Override
    public void setFileName(String filename) throws IOException {
        FILE = filename;
        File myfile = new File(FILE);
        if (myfile.exists()) {
            // do nothing
        } else {
            myfile.createNewFile();
            writeOrder();
        }
    }

    public String getfileName() {
        return FILE;
    }

    private Order unmarshallOrder(String text) // will convert each line in the file to an order object 
    {
        String[] orderTokens = text.split(DELIMITER);
        int ordernumber = Integer.parseInt(orderTokens[0]);
        String date = get_date(FILE); // for the date we will get it from the file name 
        Order temp = new Order(ordernumber, date);
        temp.set_customer_name(orderTokens[1]);
        temp.set_state(orderTokens[2]);
        String tax1 = orderTokens[3];
        BigDecimal a = new BigDecimal(tax1);
        BigDecimal tax_rate = a.setScale(2, RoundingMode.HALF_UP);
        temp.set_tax_rate(tax_rate);
        temp.set_product_type(orderTokens[4]);
        BigDecimal d = new BigDecimal(orderTokens[5]);
        BigDecimal area = d.setScale(2, RoundingMode.HALF_UP);
        temp.set_area(area);
        String cost1 = orderTokens[6];
        BigDecimal b = new BigDecimal(cost1);
        BigDecimal cost_per_square_foot = b.setScale(2, RoundingMode.HALF_UP);
        temp.set_cost_per_square_foot(cost_per_square_foot);
        String labor_cost = orderTokens[7];
        BigDecimal c = new BigDecimal(labor_cost);
        BigDecimal labor_cost_per_foot = c.setScale(2, RoundingMode.HALF_UP);
        temp.set_labor_cost_per_square_foot(labor_cost_per_foot);
        return temp;

    }

    private void LoadOrder() throws FileNotFoundException { // will read all the orders from the file 
        Scanner scan;
        scan = new Scanner(new BufferedReader(new FileReader(FILE)));

        scan.nextLine(); // we added this line to ignore the first line since it will the header and not going to include order information 
        String currentLine;
        Order currentOrder;
        while (scan.hasNextLine()) {
            currentLine = scan.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            List_Order.put(currentOrder.get_order_number(), currentOrder);
        }
        scan.close();

    }

    private String marshallOrder(Order order) {  // will convert the order object to a string 
        BigDecimal materialcost = order.get_material_cost(order.get_area(), order.get_cost_per_square_foot()); // we will save the values that need to be calculated and then will pass it as a string 
        BigDecimal laborcost = order.get_labor_cost(order.get_area(), order.get_labor_cost_per_square_foot());
        BigDecimal tax = order.get_tax(materialcost, laborcost, order.get_tax_rate());
        BigDecimal total = order.get_total(materialcost, laborcost, tax);
        String ordertext = String.valueOf(order.get_order_number()) + DELIMITER;
        ordertext += order.get_customer_name() + DELIMITER;
        ordertext += order.get_state() + DELIMITER;
        ordertext += order.get_tax_rate().toString() + DELIMITER;
        ordertext += order.get_product_type() + DELIMITER;
        ordertext += order.get_area().toString() + DELIMITER;
        ordertext += order.get_cost_per_square_foot().toString() + DELIMITER;
        ordertext += order.get_labor_cost_per_square_foot().toString() + DELIMITER;
        ordertext += materialcost.toString() + DELIMITER;  // we will use toString() to convert BigDecimal to string and then pass it 
        ordertext += laborcost.toString() + DELIMITER;
        ordertext += tax.toString() + DELIMITER;
        ordertext += total.toString();
        return ordertext;
    }

    private void writeOrder() throws IOException { // will write the orders to the file 
        PrintWriter out;
        String firstline = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total"; // first line will include the header 
        out = new PrintWriter(new FileWriter(FILE));
        out.println(firstline);
        out.flush();
        String Ordertxt;
        List<Order> list1 = this.get_all_orders();
        for (Order current : list1) // will write every order in the list to the file 
        {
            Ordertxt = marshallOrder(current);
            out.println(Ordertxt);
            out.flush();
        }
        out.close();

    }

    private String get_date(String FileName) // will use the file name to get the order date
    {
        String day = "";
        String month = "";
        String year = "";
        String FILE = FileName;
        String[] orderTokens = FILE.split("_"); // will use this line to remove "Orders_" from the file name so we will have "Orders_" "08212017.txt" instead of "Orders_08212017.txt"
        String date = orderTokens[1].replace(".txt", ""); // will use this line to remove .txt from "08212017.txt" so we will have "08212017" in date 
        char[] ch = new char[date.length()]; // will convert date to array of charcters 
        for (int i = 0; i < date.length(); i++) {
            ch[i] = date.charAt(i);
        }
        for (int i = 0; i < ch.length; i++) // will use the array of charcters to save the day and the month and the year 
        {
            if (i == 0) // we will use the first and the second  postions to save the day
            {
                day = day + String.valueOf(ch[i]);
                day = day + String.valueOf(ch[i + 1]);
            }

            if (i == 2) // we will use the third and fourth postions to save the month
            {
                month = month + String.valueOf(ch[i]);
                month = month + String.valueOf(ch[i + 1]);
            }
            if (i == 4) // will use the rest to save the year 
            {
                year = year + String.valueOf(ch[i]);
                year = year + String.valueOf(ch[i + 1]);
                year = year + String.valueOf(ch[i + 2]);
                year = year + String.valueOf(ch[i + 3]);
            }

        }
        String order_date = day + "-" + month + "-" + year; // will save the day , the month, the year in one string called order date
        return order_date;
    }

    @Override
    public Order addOrder(String date, String name, String State, BigDecimal taxrate, String type, BigDecimal area, BigDecimal costperfoot, BigDecimal laborcostperfoot) { // will add order to the hashmap List_Order and return the object that we added 
        int number = 0;
        Order temp;
        try {
            LoadOrder();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlooringMasteryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Order> list1 = get_all_orders();
        if (list1.size() > 0) {
            for (int i = 0; i < list1.size(); i++) {
                number = list1.get(i).get_order_number();
            }
            number++;

        } else {
            number = 1;
        }
        temp = new Order(number, date);
        temp.set_customer_name(name);
        temp.set_state(State);
        temp.set_tax_rate(taxrate);
        temp.set_product_type(type);
        temp.set_area(area);
        temp.set_cost_per_square_foot(costperfoot);
        temp.set_labor_cost_per_square_foot(laborcostperfoot);
        List_Order.put(number, temp);
        try {
            writeOrder();
        } catch (IOException ex) {
            Logger.getLogger(FlooringMasteryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return List_Order.get(number);
    }

    @Override
    public Order getOrder(int number) {
        try {
            LoadOrder();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlooringMasteryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Order temp = List_Order.get(number);
        return temp;
    }

    @Override
    public List<Order> get_all_orders() { // will return all the orders in the file so naturally it will return all order for a specific date 
        try {
            LoadOrder();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlooringMasteryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>(List_Order.values());
    }

    @Override
    public Order remove_order(int number) { // will remove an order and return the removed item 
        Order temp;
        try {
            LoadOrder();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlooringMasteryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        temp = List_Order.remove(number);
        try {
            writeOrder();
        } catch (IOException ex) {
            Logger.getLogger(FlooringMasteryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
    }

    @Override
    public Order edit_order(int number, Order copy) { // will edit an order and will use choice to check what the user want to edit 

        try {
            LoadOrder();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlooringMasteryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        List_Order.get(number).set_customer_name(copy.get_customer_name());
        List_Order.get(number).set_state(copy.get_state());
        List_Order.get(number).set_tax_rate(copy.get_tax_rate());
        List_Order.get(number).set_product_type(copy.get_product_type());
        List_Order.get(number).set_cost_per_square_foot(copy.get_cost_per_square_foot());
        List_Order.get(number).set_labor_cost_per_square_foot(copy.get_labor_cost_per_square_foot());
        List_Order.get(number).set_area(copy.get_area());

        try {
            writeOrder();
        } catch (IOException ex) {
            Logger.getLogger(FlooringMasteryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Order temp = List_Order.get(number); // save the object after editing the order and return it to the user 
        return temp;
    }

    @Override
    public void exportData() throws IOException {
        try {
            LoadOrder();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlooringMasteryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        PrintWriter out;
        out = new PrintWriter(new FileWriter(EXPORTFILE), true);
        String firstline = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate";
        out.println(firstline);
        out.flush();
        String Ordertxt;
        List<Order> list1 = this.get_all_orders();
        for (Order current : list1) // will write every order in the list to the file 
        {
            Ordertxt = marshallOrder(current) + current.get_order_date();
            out.println(Ordertxt);
            out.flush();
        }
        out.close();

    }

}
