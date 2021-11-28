/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.service;

import com.mycompany.flooringmastery.dao.FlooringMasteryDao;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mike
 */
@Component
public class FlooringMasteryServiceLayerFileImpl implements FlooringMasteryServiceLayer {

    FlooringMasteryDao dao;
    private final String TAXFILE = "Taxes.txt";
    private final String PRODUCTFILE = "Products.txt";

    @Autowired
    public FlooringMasteryServiceLayerFileImpl(FlooringMasteryDao dao) {
        this.dao = dao;
    }

    private boolean validateOrderFile(String filename) throws NoOrderFoundException, IOException {
        boolean found = false;
        dao.setFileName(filename);
        List<Order> list = dao.get_all_orders();

        if (list.size() > 0) {
            return found;
        } else {
            throw new NoOrderFoundException("There are no orders for this date");
        }

    }

    private boolean validateSpecificOrder(int number, String filename) throws IOException, NoOrderFoundException, NoSpecificOrderException {
        boolean found = false;
        dao.setFileName(filename);
        List<Order> list = dao.get_all_orders();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get_order_number() == number) {
                found = true;
            }
        }
        if (found == true) {
            return found;
        } else {
            throw new NoSpecificOrderException("this specific order was not  found on this date");
        }
    }

    private String fixDate(String date) {
        String date2 = date.replace("-", "");
        return date2;
    }

    /*   private int getlocation(List<FlooringMasteryDaoImpl>list, String filename)
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
    } */
    @Override
    public Order addOrder(String date, String name, String State, BigDecimal taxrate, String type, BigDecimal area, BigDecimal costperfoot, BigDecimal laborcostperfoot) throws IOException {
        String fileName;
        String datefixed = fixDate(date);
        fileName = "Orders_" + datefixed + ".txt";
        Order temp;
        dao.setFileName(fileName);
        temp = dao.addOrder(date, name, State, taxrate, type, area, costperfoot, laborcostperfoot);
        return temp;
    }

    @Override
    public Order editOrder(int number, String date, Order copy) throws IOException, NoOrderFoundException, NoSpecificOrderException {
        String fileName;
        String datefixed = fixDate(date);
        fileName = "Orders_" + datefixed + ".txt";

        Order temp;
        validateOrderFile(fileName);
        validateSpecificOrder(number, fileName);
        dao.setFileName(fileName);
        temp = dao.edit_order(number, copy);
        return temp;

    }

    @Override
    public void removeOrder(int number, String date) throws IOException, NoOrderFoundException, NoSpecificOrderException {
        String fileName;
        String datefixed = fixDate(date);
        fileName = "Orders_" + datefixed + ".txt";
        validateOrderFile(fileName);
        validateSpecificOrder(number, fileName);
        dao.setFileName(fileName);
        dao.remove_order(number);
    }

    @Override
    public Order getOrder(int number, String date) throws IOException, NoOrderFoundException, NoSpecificOrderException {
        String fileName;
        String datefixed = fixDate(date);
        fileName = "Orders_" + datefixed + ".txt";
        Order temp = null;
        validateOrderFile(fileName);
        validateSpecificOrder(number, fileName);
        dao.setFileName(fileName);
        temp = dao.getOrder(number);

        return temp;
    }

    @Override
    public List<Order> getOrders(String date) throws NoOrderFoundException, IOException {
        String fileName;
        String datefixed = fixDate(date);
        fileName = "Orders_" + datefixed + ".txt";
        List<Order> List_temp;
        validateOrderFile(fileName);
        dao.setFileName(fileName);
        List_temp = dao.get_all_orders();
        return List_temp;
    }

    private Tax unmarshallTax(String text) {
        String[] taxTokens = text.split(DELIMITER);
        String shortname = taxTokens[0];
        String longname = taxTokens[1];
        BigDecimal a = new BigDecimal(taxTokens[2]);
        BigDecimal taxrate = a.setScale(2, RoundingMode.HALF_UP);
        return new Tax(shortname, longname, taxrate);
    }

    @Override
    public List<Tax> getAllTaxes() throws FileNotFoundException {
        List<Tax> list1 = new ArrayList<>();
        Scanner scan;
        scan = new Scanner(new BufferedReader(new FileReader(TAXFILE)));
        scan.nextLine();
        String currentLine;
        Tax currenttax;
        while (scan.hasNextLine()) {
            currentLine = scan.nextLine();
            currenttax = unmarshallTax(currentLine);
            list1.add(currenttax);
        }
        scan.close();
        return list1;

    }

    @Override
    public BigDecimal getTaxRate(String state) throws FileNotFoundException {
        List<Tax> list1 = getAllTaxes();
        int index = -1;
        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i).getFullName().equals(state)) {
                index = i;
            }
        }

        return list1.get(index).getTaxRate();
    }

    private Product unmarshallProduct(String text) {
        String[] ProductTokens = text.split(DELIMITER);
        String productname = ProductTokens[0];
        BigDecimal a = new BigDecimal(ProductTokens[1]);
        BigDecimal CostPerSquareFoot = a.setScale(2, RoundingMode.HALF_UP);
        BigDecimal b = new BigDecimal(ProductTokens[2]);
        BigDecimal LaborCostPerSquareFoot = b.setScale(2, RoundingMode.HALF_UP);
        return new Product(productname, CostPerSquareFoot, LaborCostPerSquareFoot);
    }

    @Override
    public List<Product> getAllProduct() throws FileNotFoundException {
        List<Product> list1 = new ArrayList<>();
        Scanner scan;
        scan = new Scanner(new BufferedReader(new FileReader(PRODUCTFILE)));
        scan.nextLine();
        String currentLine;
        Product currentProduct;
        while (scan.hasNextLine()) {
            currentLine = scan.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            list1.add(currentProduct);
        }
        scan.close();
        return list1;
    }

    @Override
    public BigDecimal getCost(String type) throws FileNotFoundException {
        List<Product> list1 = getAllProduct();
        int index = -1;
        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i).getProductName().equals(type)) {
                index = i;
            }
        }

        return list1.get(index).getCostPerSquareFoot();
    }

    @Override
    public BigDecimal getLaborCost(String type) throws FileNotFoundException {
        List<Product> list1 = getAllProduct();
        int index = -1;
        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i).getProductName().equals(type)) {
                index = i;
            }
        }

        return list1.get(index).getLaborCostPerSquareFoot();
    }

    @Override
    public void exportData(String date) throws FileNotFoundException, IOException {
        String fileName;
        String datefixed = fixDate(date);
        fileName = "Orders_" + datefixed + ".txt";
        dao.setFileName(fileName);
        dao.exportData();

    }
}
