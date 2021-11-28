package com.mycompany.flooringmastery.dto;

import java.math.BigDecimal;

/**
 *
 * @author john
 */
public class Product {

    private final String ProductName;
    private final BigDecimal CostPerSquareFoot;
    private final BigDecimal LaborCostPerSquareFoot;

    public Product(String ProductName, BigDecimal CostPerSquareFoot, BigDecimal LaborCostPerSquareFoot) {
        this.ProductName = ProductName;
        this.CostPerSquareFoot = CostPerSquareFoot;
        this.LaborCostPerSquareFoot = LaborCostPerSquareFoot;
    }

    public String getProductName() {
        return ProductName;
    }

    public BigDecimal getCostPerSquareFoot() {
        return CostPerSquareFoot;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return LaborCostPerSquareFoot;
    }
}
