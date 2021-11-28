package com.mycompany.flooringmastery.dao;

import com.mycompany.flooringmastery.dto.Order;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.stereotype.Component;

/**
 *
 * @author john
 */
@Component
public class FlooringMasteryDaoImplTest {

    FlooringMasteryDaoImpl testDao;
    Order testOrder1;
    Order testOrder2;

    String date = "01-01-2022";
    String customer_name = "test.company";
    String state = "Texas";
    BigDecimal TexasTaxRate = new BigDecimal("4.45");
    BigDecimal area = new BigDecimal("105.00");
    String product_type = "Wood";
    BigDecimal WoodCostPerSquareFoot = new BigDecimal("5.15");
    BigDecimal WoodLaborCostPerSquareFoot = new BigDecimal("4.75");

    String date2 = "01-01-2022";
    String customer_name2 = "test.company2";
    String state2 = "Washington";
    BigDecimal WashingtonTaxRate = new BigDecimal("9.25");
    BigDecimal area2 = new BigDecimal("200.00");
    String product_type2 = "Tile";
    BigDecimal TileCostPerSquareFoot = new BigDecimal("3.50");
    BigDecimal TileLaborCostPerSquareFoot = new BigDecimal("4.15");

    public FlooringMasteryDaoImplTest() {

    }

    @BeforeEach
    public void setUp() throws Exception {
        testDao = new FlooringMasteryDaoImpl();
        testDao.setFileName("Order_01012022.txt");

        testOrder1 = new Order(1, "01-01-2022");
        testOrder1.set_customer_name(customer_name);
        testOrder1.set_state(state);
        testOrder1.set_tax_rate(TexasTaxRate);
        testOrder1.set_product_type(product_type);
        testOrder1.set_area(area);
        testOrder1.set_cost_per_square_foot(WoodCostPerSquareFoot);
        testOrder1.set_labor_cost_per_square_foot(WoodLaborCostPerSquareFoot);

        testOrder2 = new Order(2, "01-01-2022");
        testOrder2.set_customer_name(customer_name2);
        testOrder2.set_state(state2);
        testOrder2.set_tax_rate(WashingtonTaxRate);
        testOrder2.set_product_type(product_type2);
        testOrder2.set_area(area2);
        testOrder2.set_cost_per_square_foot(TileCostPerSquareFoot);
        testOrder2.set_labor_cost_per_square_foot(TileLaborCostPerSquareFoot);
    }

    @AfterEach
    public void tearDown() throws IOException {
        File toDelete = new File("Order_01012022.txt");
        toDelete.delete();
    }

    @Test
    public void testAddingAnOrder() {
        try {
            testDao.addOrder(date, customer_name, state, TexasTaxRate, product_type, area, WoodCostPerSquareFoot, WoodLaborCostPerSquareFoot);
            Order getOrder = testDao.getOrder(1);
            assertNotNull(getOrder, "The retrieved Order must not be null");
            
            assertEquals(getOrder.get_order_number(),
                    1,
                    "Checking order number");
            assertEquals(getOrder.get_order_date(),
                    date,
                    "Checking order date");
            assertEquals(getOrder.get_customer_name(),
                    customer_name,
                    "Checking customer name");
            assertEquals(getOrder.get_state(),
                    state,
                    "Checking order's state");
            assertEquals(getOrder.get_tax_rate(),
                    TexasTaxRate,
                    "Checking order's tax rate");
            assertEquals(getOrder.get_product_type(),
                    product_type,
                    "Checking order's product type");
            assertEquals(getOrder.get_cost_per_square_foot(),
                    WoodCostPerSquareFoot,
                    "Checking product CostPerSquareFoot");
            assertEquals(getOrder.get_labor_cost_per_square_foot(),
                    WoodLaborCostPerSquareFoot,
                    "Checking product LaborCostPerSquareFoot;");
            assertEquals(getOrder.get_area(),
                    area,
                    "Checking order's area");
        } catch (IOException ex) {
            fail(ex.getMessage() + " Should not throw any Exceptions");
        }
    }

    @Test
    public void testGetAllOrders() {
        try {
            testDao.addOrder(date, customer_name, state, TexasTaxRate, product_type, area, WoodCostPerSquareFoot, WoodLaborCostPerSquareFoot);
            testDao.addOrder(date2, customer_name2, state2, WashingtonTaxRate, product_type2, area2, TileCostPerSquareFoot, TileLaborCostPerSquareFoot);
            List<Order> testOrders = testDao.get_all_orders();
            assertEquals(testDao.get_all_orders().size(), 2, "There should be 2 orders in the list");
            
            assertEquals(testOrders.get(0).get_order_number(),
                    1,
                    "Checking order number");
            assertEquals(testOrders.get(0).get_order_date(),
                    date,
                    "Checking order date");
            assertEquals(testOrders.get(0).get_customer_name(),
                    customer_name,
                    "Checking customer name");
            assertEquals(testOrders.get(0).get_state(),
                    state,
                    "Checking order's state");
            assertEquals(testOrders.get(0).get_tax_rate(),
                    TexasTaxRate,
                    "Checking order's tax rate");
            assertEquals(testOrders.get(0).get_product_type(),
                    product_type,
                    "Checking order's product type");
            assertEquals(testOrders.get(0).get_cost_per_square_foot(),
                    WoodCostPerSquareFoot,
                    "Checking product CostPerSquareFoot");
            assertEquals(testOrders.get(0).get_labor_cost_per_square_foot(),
                    WoodLaborCostPerSquareFoot,
                    "Checking product LaborCostPerSquareFoot;");
            assertEquals(testOrders.get(0).get_area(),
                    area,
                    "Checking order's area");
            
            assertEquals(testOrders.get(1).get_order_number(),
                    2,
                    "Checking order number");
            assertEquals(testOrders.get(1).get_order_date(),
                    date2,
                    "Checking order date");
            assertEquals(testOrders.get(1).get_customer_name(),
                    customer_name2,
                    "Checking customer name");
            assertEquals(testOrders.get(1).get_state(),
                    state2,
                    "Checking order's state");
            assertEquals(testOrders.get(1).get_tax_rate(),
                    WashingtonTaxRate,
                    "Checking order's tax rate");
            assertEquals(testOrders.get(1).get_product_type(),
                    product_type2,
                    "Checking order's product type");
            assertEquals(testOrders.get(1).get_cost_per_square_foot(),
                    TileCostPerSquareFoot,
                    "Checking product CostPerSquareFoot");
            assertEquals(testOrders.get(1).get_labor_cost_per_square_foot(),
                    TileLaborCostPerSquareFoot,
                    "Checking product LaborCostPerSquareFoot;");
            assertEquals(testOrders.get(1).get_area(),
                    area2,
                    "Checking order's area");
        } catch (IOException ex) {
            fail(ex.getMessage() + " Should not throw any Exceptions");
        }
    }

    @Test
    public void testRemovingAnOrder() {
        try {
            testDao.addOrder(date, customer_name, state, TexasTaxRate, product_type, area, WoodCostPerSquareFoot, WoodLaborCostPerSquareFoot);
            Order removedOrder = testDao.remove_order(1);
            assertNotNull(removedOrder, "testDao.remove_order should return the removed object, not a null Object");
            assertEquals(testDao.get_all_orders().isEmpty(), true, "There should not be any orders in memory after remove");
        } catch (IOException ex) {
            fail(ex.getMessage() + " Should not throw any Exceptions");
        }
    }

    @Test
    public void testEditingAnOrder() {
        try {
            testDao.addOrder(date, customer_name, state, TexasTaxRate, product_type, area, WoodCostPerSquareFoot, WoodLaborCostPerSquareFoot);
            testDao.edit_order(1, testOrder2);
            List<Order> testOrders = testDao.get_all_orders();
            
            assertEquals(testOrders.get(0).get_order_number(),
                    1,
                    "Checking order number");
            assertEquals(testOrders.get(0).get_order_date(),
                    date2,
                    "Checking order date");
            assertEquals(testOrders.get(0).get_customer_name(),
                    customer_name2,
                    "Checking customer name");
            assertEquals(testOrders.get(0).get_state(),
                    state2,
                    "Checking order's state");
            assertEquals(testOrders.get(0).get_tax_rate(),
                    WashingtonTaxRate,
                    "Checking order's tax rate");
            assertEquals(testOrders.get(0).get_product_type(),
                    product_type2,
                    "Checking order's product type");
            assertEquals(testOrders.get(0).get_cost_per_square_foot(),
                    TileCostPerSquareFoot,
                    "Checking product CostPerSquareFoot");
            assertEquals(testOrders.get(0).get_labor_cost_per_square_foot(),
                    TileLaborCostPerSquareFoot,
                    "Checking product LaborCostPerSquareFoot;");
            assertEquals(testOrders.get(0).get_area(),
                    area2,
                    "Checking order's area");
        } catch (IOException ex) {
            fail(ex.getMessage() + " Should not throw any Exceptions");
        }
    }
}
