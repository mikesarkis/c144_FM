package com.mycompany.flooringmastery.service;

import com.mycompany.flooringmastery.dao.FlooringMasteryDao;
import com.mycompany.flooringmastery.dto.Order;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author john
 */
@Component
public class FlooringMasteryServiceLayerFileImplTest {

    FlooringMasteryServiceLayer service;
    FlooringMasteryDao dao;

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

    @Autowired
    public FlooringMasteryServiceLayerFileImplTest() {
        dao = new FlooringMasteryDaoStubImpl();
        service = new FlooringMasteryServiceLayerFileImpl(dao);

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

    @Test
    public void testAddOrder() {
        try {
            Order temp = service.addOrder(date, customer_name, state, TexasTaxRate, product_type, area, WoodCostPerSquareFoot, WoodLaborCostPerSquareFoot);
            if (temp == null) {
                fail("Should not be null after adding");
            }
        } catch (IOException ex) {
            fail(ex.getMessage() + " | Exception should not occur at this test");
        }
    }

    @Test
    public void testEditOrder() {
        try {
            Order temp = service.editOrder(1, date, testOrder2);
        } catch (IOException | NoOrderFoundException | NoSpecificOrderException ex) {
            fail(ex.getMessage() + " | Exception should not occur at this test");
        }
    }

    @Test
    public void testInvalidEditOrder() {
        try {
            Order temp = service.editOrder(-1, "", testOrder2);
            fail(" Exception should occur");
        } catch (IOException | NoOrderFoundException | NoSpecificOrderException ex) {
        }
    }

    @Test
    public void testRemoveOrder() {
        try {
            service.removeOrder(1, date);
        } catch (IOException | NoOrderFoundException | NoSpecificOrderException ex) {
            fail(ex.getMessage() + " | Exception should not occur at this test");
        }
    }

    @Test
    public void testInvalidRemoveOrder() {
        try {
            service.removeOrder(-1, date);
            fail("Exception should occur");
        } catch (IOException | NoOrderFoundException | NoSpecificOrderException ex) {

        }
    }

    @Test
    public void testGetOrder() {
        try {
            Order temp = service.getOrder(1, date);
        } catch (IOException | NoOrderFoundException | NoSpecificOrderException ex) {
            fail(ex.getMessage() + " | Exception should not occur at this test");
        }
    }

    @Test
    public void testInvalidGetOrder() {
        try {
            Order temp = service.getOrder(-1, date);
            fail("Exception should occur");
        } catch (IOException | NoOrderFoundException | NoSpecificOrderException ex) {

        }
    }

    @Test
    public void testGetOrders() {
        try {
            List<Order> orders = service.getOrders(date);
            if (orders.size() != 2) {
                fail("There should be 2 items in the list");
            }
        } catch (NoOrderFoundException | IOException ex) {
            fail(ex.getMessage() + " | Exception should not occur at this test");
        }
    }
}
