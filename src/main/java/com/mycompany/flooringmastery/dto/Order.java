/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Mike
 */
public class Order {
    int OrderNumber;
    String CustomerName;
    String State;
    String date;
    BigDecimal TaxRate;
    String ProductType;
    BigDecimal area;
    BigDecimal CostPerSquareFoot;
    BigDecimal LaborCostPerSquareFoot;
    BigDecimal MaterialCost;
    BigDecimal LaborCost;
    BigDecimal Tax;
    BigDecimal Total;
    public Order(int OrderNumber, String date)
    {
        this.OrderNumber = OrderNumber;
        this.date = date;
    }
    public String get_order_date()
    {
        return this.date;
    }
    public int get_order_number()
    {
        return this.OrderNumber;
    }
    public void set_customer_name(String name)
    {
        this.CustomerName = name;
    }
    public String get_customer_name()
    {
        return this.CustomerName;
    }
    public void set_state(String state)
    {
        this.State = state;
    }
    public String get_state()
    {
        return this.State;
    }
    public void set_tax_rate(BigDecimal rate)
    {
        this.TaxRate = rate;
    }
    public BigDecimal get_tax_rate()
    {
        return this.TaxRate;
    }
    public void set_product_type(String type)
    {
        this.ProductType = type;
    }
    public String get_product_type()
    {
        return this.ProductType;
    }
    public void set_area(BigDecimal area)
    {
        this.area = area;
    }
    public BigDecimal get_area()
    {
        return this.area;
    }
    public void set_cost_per_square_foot(BigDecimal cost)
    {
        this.CostPerSquareFoot = cost;
    }
    public BigDecimal get_cost_per_square_foot()
    {
        return this.CostPerSquareFoot;
    }
    public void set_labor_cost_per_square_foot(BigDecimal cost)
    {
        this.LaborCostPerSquareFoot = cost;
    }
    public BigDecimal get_labor_cost_per_square_foot()
    {
        return this.LaborCostPerSquareFoot;
    }
    public BigDecimal get_material_cost(BigDecimal area, BigDecimal costpersquarefoot) // will calculate the MaterialCost
    {
        this.MaterialCost = area.multiply(costpersquarefoot);
        return this.MaterialCost.setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal get_labor_cost(BigDecimal area, BigDecimal laborcostpersquarefoot)  // will calculate the  LaborCost
    {
        this.LaborCost = area.multiply(laborcostpersquarefoot);
        return this.LaborCost.setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal get_tax(BigDecimal materialCost,BigDecimal laborcost, BigDecimal taxrate) // will calculate the taxes 
    {
        BigDecimal cost = materialCost.add(laborcost);
        BigDecimal a = new BigDecimal(100);
        BigDecimal tax = a.setScale(2, RoundingMode.HALF_UP);
        BigDecimal taxrate1 = taxrate.divide(tax);
        this.Tax= cost.multiply(taxrate1);
        return this.Tax.setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal get_total(BigDecimal materialCost,BigDecimal laborcost, BigDecimal tax ) // will calculate the total cost 
    {
        BigDecimal totalcost = materialCost.add(laborcost);
        this.Total = totalcost.add(tax);
        return this.Total.setScale(2, RoundingMode.HALF_UP);
    }
       
}
