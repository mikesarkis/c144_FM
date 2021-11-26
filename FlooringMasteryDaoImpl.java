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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Mike
 */
public class FlooringMasteryDaoImpl implements FlooringMasteryDao{
    private Map<Integer, Order> List_Order = new HashMap<>(); 
    private final String FILE;
    public static final String DELIMITER = ",";
    public FlooringMasteryDaoImpl(String filename) // we will pass the file name to the Dao after getting the order date from the user 
    {
        FILE = filename;
        File myfile = new File(FILE);
        myfile.createNewFile();
    }
    private Order unmarshallOrder(String text)   // will convert each line in the file to an order object 
    {
        String[] orderTokens = text.split(DELIMITER);
        int ordernumber = Integer.parseInt(orderTokens[0]);
        String date = get_date(FILE); // for the date we will get it from the file name 
        Order temp = new Order(ordernumber, date);
        temp.set_customer_name(orderTokens[1]);
        temp.set_state(orderTokens[2]);
        temp.set_tax_rate(orderTokens[3]);
        temp.set_product_type(orderTokens[4]);
        temp.set_area(orderTokens[5]);
        temp.set_cost_per_square_foot(orderTokens[6]);
        temp.set_labor_cost_per_square_foot(orderTokens[7]);
        return temp;

    }
    private void LoadOrder()throws  FileNotFoundException { // will read all the orders from the file 
        Scanner scan; 
            scan = new Scanner(new BufferedReader(new FileReader(FILE)));

         scan.nextLine(); // we added this line to ignore the first line since it will the header and not going to include order information 
         String currentLine;
         Order currentOrder;
         while(scan.hasNextLine())
         {
             currentLine = scan.nextLine();
             currentOrder = unmarshallOrder(currentLine);
             List_Order.put(currentOrder.get_order_number(), currentOrder);
         }
         scan.close();

    }
    private String marshallOrder(Order order){  // will convert the order object to a string 
        BigDecimal materialcost  =   order.get_material_cost(order.get_area(),order.get_cost_per_square_foot() ); // we will save the values that need to be calculated and then will pass it as a string 
        BigDecimal laborcost = order.get_labor_cost(order.get_area(), order.get_labor_cost_per_square_foot());
        BigDecimal tax = order.get_tax(materialcost, laborcost, order.get_tax_rate());
        BigDecimal total = order.get_total(materialcost, laborcost, tax);
        String ordertext = String.valueOf(order.get_order_number()) + DELIMITER;
        ordertext += order.get_customer_name() + DELIMITER;
        ordertext += order.get_state() +  DELIMITER;
        ordertext += order.get_tax_rate().toString() + DELIMITER;
        ordertext += order.get_product_type() + DELIMITER;
        ordertext += order.get_area().toString() + DELIMITER;
        ordertext += order.get_cost_per_square_foot().toString() +  DELIMITER;
        ordertext += order.get_labor_cost_per_square_foot().toString() + DELIMITER;
        ordertext += materialcost.toString() +  DELIMITER;  // we will use toString() to convert BigDecimal to string and then pass it 
        ordertext += laborcost.toString() +  DELIMITER;
        ordertext += tax.toString() +  DELIMITER;
        ordertext += total.toString() +  DELIMITER;
        return ordertext;
    }
    private void writeOrder() throws IOException { // will write the orders to the file 
        PrintWriter out;
        String firstline = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total"; // first line will include the header 
        out = new PrintWriter(new FileWriter(FILE));
        out.println(firstline);
        out.flush();
        String Ordertxt;
        List<Order> list1 =  this.get_all_orders();
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
        String day= "";
        String month="";
        String year="";
        String FILE = FileName;
        String[] orderTokens = FILE.split("_"); // will use this line to remove "Orders_" from the file name so we will have "Orders_" "08212017.txt" instead of "Orders_08212017.txt"
        String date =  orderTokens[1].replace(".txt",""); // will use this line to remove .txt from "08212017.txt" so we will have "08212017" in date 
        char[] ch = new char[date.length()]; // will convert date to array of charcters 
        for (int i = 0; i < date.length(); i++) {
            ch[i] = date.charAt(i);
        }
        for(int i=0 ; i< ch.length;i++) // will use the array of charcters to save the day and the month and the year 
        {
            System.out.println(ch[i]);
          if(i == 0)   // we will use the first and the second  postions to save the day
           {
            day = day + String.valueOf(ch[i]);
            day = day + String.valueOf(ch[i+1]);
           }
           
          if(i ==2) // we will use the third and fourth postions to save the month
          {
              month = month+ String.valueOf(ch[i]);
              month = month+ String.valueOf(ch[i+1]);
          }
         if(i ==4) // will use the rest to save the year 
          {
              year = year + String.valueOf(ch[i]);
              year = year + String.valueOf(ch[i+1]);
              year = year + String.valueOf(ch[i+2]);
              year = year + String.valueOf(ch[i+3]);
          }
            
        }
        String order_date= day+"-"+month+"-"+year; // will save the day , the month, the year in one string called order date
        return order_date;
    }
    @Override
    public Order addOrder(int number, Order order) { // will add order to the hashmap List_Order and return the object that we added 
        try {
            LoadOrder();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlooringMasteryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
         List_Order.put(number, order);
        try {
            writeOrder();
        } catch (IOException ex) {
            Logger.getLogger(FlooringMasteryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return List_Order.get(number);
        }

    @Override
    public List<Order> get_all_orders() { // will retrun all the orders in the file so natuarlly it will return all order for a specific date 
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
    public Order edit_order(int number,String data, int choice) { // will edit an order and will use choice to check what the user want to edit 

            try {
            LoadOrder();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlooringMasteryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
            if(choice == 1) // if choice =1 edit the customer name 
            {
                List_Order.get(number).set_customer_name(data);
            }
            if (choice == 2)// if choice =2 edit the state
            {
                List_Order.get(number).set_state(data);
            }
            if(choice == 3) // if choice = 3 edit the product type 
            {
                List_Order.get(number).set_product_type(data);
            }
            if(choice ==4) // if choice =4 edit the area 
            {
                List_Order.get(number).set_area(data);
            }
            try {
            writeOrder();
        } catch (IOException ex) {
            Logger.getLogger(FlooringMasteryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Order  temp = List_Order.get(number); // save the object after editing the order and return it to the user 
        return temp;
    }
        
    }


    

