/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.serivce;

import com.mycompany.flooringmastery.dao.FlooringMasteryDaoImpl;
import static com.mycompany.flooringmastery.dao.FlooringMasteryDaoImpl.DELIMITER;
import com.mycompany.flooringmastery.dto.Order;
import com.mycompany.flooringmastery.dto.Product;
import com.mycompany.flooringmastery.dto.Tax;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mike
 */
public class FlooringMasteryServiceLayerFileImpl implements FlooringMasteryServiceLayer{
    List<FlooringMasteryDaoImpl>list;
    private final String TAXFILE= "Taxes.txt";;
    private final String PRODUCTFILE = "Products.txt";
    public FlooringMasteryServiceLayerFileImpl()
    {
        list = = new ArrayList<>();
    }

      private boolean validatefile(String filename) throws NodateFoundException
      {
          boolean found = false;
            for(int i=0;i<list.size();i++)
                {
                    if(list.get(i).getfileName().equals(filename))
                    {
                        
                       found = true;
                    }
                }
          try{  if( found == true)
            {
                return found;
            }
          else
          throw new NodateFoundException("no order found on this date");
          }catch (NodateFoundException e)
          {
              System.out.println(e.getMessage());
               return false;
          }
           
      }
    private String fixdate(String date)
    {
        String date2 = date.replace("/","");
        return date2;
    }
    private int getlocation(List<FlooringMasteryDaoImpl>list, String filename)
    {
        boolean isfound = false;
        int index=0;
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getfileName().equals(filename))
            {
                isfound = true;
                index = i;
            }
        }
        if(isfound == true)
        {
            return index;
        }
        else
            return -1;
    }
    @Override
    public Order addorder(String date, String name, String State, BigDecimal taxrate, String type, BigDecimal area, BigDecimal costperfoot,BigDecimal laborcostperfoot) throws NodateFoundException{
        String fileName;
        String datefixed =  fixdate(date);
        Order temp;
        temp = new Order(1, date);
        fileName = "Orders_"+datefixed+".txt";
        int index = getlocation(list, fileName);
        if(index < 0)
           {
             try {
                    FlooringMasteryDaoImpl dao1 = new FlooringMasteryDaoImpl(fileName);
                    dao1.addOrder(date,name,State,taxrate,type,area,costperfoot, laborcostperfoot);
                    list.add(dao1);
                 } catch (IOException ex) {
                        Logger.getLogger(FlooringMasteryServiceLayerFileImpl.class.getName()).log(Level.SEVERE, null, ex);
                 }
                  return temp;
              }
            if (index >=0) {
                list.get(index).addOrder(date,name,State,taxrate,type,area,costperfoot, laborcostperfoot);
              }


       
        return temp;
    }

    @Override
    public Order editOrder(int number, String date, Order copy) throws  NodateFoundException{
        String fileName;
        String datefixed =  fixdate(date);
        fileName = "Orders_"+datefixed+".txt";
        Order temp;
        if( validatefile(fileName))
            {
               int index = getlocation(list, fileName);
              temp = list.get(index).edit_order(number, copy);
            }
        else
            temp = null;
        return temp;

    }

    @Override
    public void removeOrder(int number, String date) throws NodateFoundException{
        String fileName;
        String datefixed =  fixdate(date);
        fileName = "Orders_"+datefixed+".txt";
        if( validatefile(fileName))
            {
               int index = getlocation(list, fileName);
               list.get(index).remove_order(number);
            }

    }

    @Override
    public Order getOrder(int number, String date) throws  NodateFoundException{
        String fileName;
        String datefixed =  fixdate(date);
        fileName = "Orders_"+datefixed+".txt";
        Order temp;
        if( validatefile(fileName))
            {
               int index = getlocation(list, fileName);
               temp = list.get(index).getOrder(number);
            }
        else
            temp = null;
        return temp;
    }

    @Override
    public List<Order> getOrders(String date) throws  NodateFoundException{
        String fileName;
        String datefixed =  fixdate(date);
        fileName = "Orders_"+datefixed+".txt";
        List<Order> List_temp;
        if(validatefile(fileName))
            {
               int index = getlocation(list, fileName);
               List_temp = list.get(index).get_all_orders();
            }
        else
            List_temp = null;
        return List_temp;
    }


    private Tax unmarshallTax(String text)
    {
        String[] taxTokens = text.split(DELIMITER);
        String shortname= taxTokens[0];
        String longname= taxTokens[1];
        BigDecimal a = new BigDecimal(taxTokens[2]);
        BigDecimal taxrate = a.setScale(2,RoundingMode.HALF_UP);
        return new Tax(shortname,longname,taxrate);
    }
    @Override
    public List<Tax> getalltaxes() throws  FileNotFoundException
    {
        List<Tax> list1 = new ArrayList<>();
        Scanner scan; 
            scan = new Scanner(new BufferedReader(new FileReader(TAXFILE)));
            scan.nextLine();
            String currentLine;
            Tax currenttax;
           while(scan.hasNextLine())
           {
             currentLine = scan.nextLine();
             currenttax = unmarshallTax(currentLine);
             list1.add(currenttax);
            }
         scan.close();
         return list1;
            
    }
    @Override
    public BigDecimal getTaxrate(String state) throws  FileNotFoundException
    {
        List<Tax> list1 = getalltaxes();
        int index = -1;
        for (int i=0;i<list1.size();i++)
        {
            if(list1.get(i).getFullName().equals(state))
            {
                index = i;
            }
        }
        
      return list1.get(index).getTaxRate();
                
    }
    private Product unmarshallProduct(String text)
    {
        String[] ProductTokens = text.split(DELIMITER);
        String productname= ProductTokens[0];
        BigDecimal a = new BigDecimal(ProductTokens[1]);
        BigDecimal CostPerSquareFoot = a.setScale(2,RoundingMode.HALF_UP);
        BigDecimal b = new BigDecimal(ProductTokens[2]);
        BigDecimal LaborCostPerSquareFoot = b.setScale(2, RoundingMode.HALF_UP);
        return new Product(productname,CostPerSquareFoot,LaborCostPerSquareFoot);
    }
    public List<Product> getallproduct() throws FileNotFoundException
    {
        List<Product> list1 = new ArrayList<>();
        Scanner scan; 
            scan = new Scanner(new BufferedReader(new FileReader(PRODUCTFILE)));
            scan.nextLine();
            String currentLine;
            Product currentProduct;
           while(scan.hasNextLine())
           {
             currentLine = scan.nextLine();
             currentProduct = unmarshallProduct(currentLine);
             list1.add(currentProduct);
            }
         scan.close();
         return list1;
    }
    public BigDecimal getcost(String type) throws FileNotFoundException
    {
        List<Product> list1 = getallproduct();
        int index = -1;
        for (int i=0;i<list1.size();i++)
        {
            if(list1.get(i).getProductName().equals(type))
            {
                index = i;
            }
        }
        
      return list1.get(index).getCostPerSquareFoot();
    }
    @Override
     public BigDecimal getLaborcost(String type) throws FileNotFoundException
    {
        List<Product> list1 = getallproduct();
        int index = -1;
        for (int i=0;i<list1.size();i++)
        {
            if(list1.get(i).getProductName().equals(type))
            {
                index = i;
            }
        }
        
      return list1.get(index).getLaborCostPerSquareFoot();
    }
        


    
}
