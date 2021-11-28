package com.mycompany.flooringmastery.service;

import com.mycompany.flooringmastery.dao.FlooringMasteryDao;
import com.mycompany.flooringmastery.dto.Order;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author john
 */
@Component
public class FlooringMasteryDaoStubImpl implements FlooringMasteryDao {

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

    public FlooringMasteryDaoStubImpl() {
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

    @Override
    public Order addOrder(String date, String name, String State, BigDecimal taxrate, String type, BigDecimal area, BigDecimal costperfoot, BigDecimal laborcostperfoot) {
        Order temp;
        temp = new Order(1, date);
        temp.set_customer_name(name);
        temp.set_state(State);
        temp.set_tax_rate(taxrate);
        temp.set_product_type(type);
        temp.set_area(area);
        temp.set_cost_per_square_foot(costperfoot);
        temp.set_labor_cost_per_square_foot(laborcostperfoot);

        return temp;
        /*
        if (temp.get_order_number() == 1
                && temp.get_order_date().equals(date)
                && temp.get_customer_name().equals(customer_name)
                && temp.get_state().equals(state)
                && temp.get_tax_rate().equals(TexasTaxRate)
                && temp.get_product_type().equals(product_type)
                && temp.get_cost_per_square_foot().equals(WoodCostPerSquareFoot)
                && temp.get_labor_cost_per_square_foot().equals(WoodLaborCostPerSquareFoot)
                && temp.get_area().equals(area)) {

            return testOrder1;
        } else {
            return null;
        }
         */
    }

    @Override
    public List<Order> get_all_orders() {
        List<Order> list = new ArrayList<>();
        list.add(testOrder1);
        list.add(testOrder2);
        return list;
    }

    @Override
    public Order remove_order(int number) {
        return number == (testOrder1.get_order_number()) ? testOrder1 : null;
    }

    @Override
    public Order edit_order(int number, Order copy) {
        if (number == testOrder2.get_order_number()) {
            return testOrder2;
        } else {
            return null;
        }
    }

    @Override
    public Order getOrder(int number) {
        if (number == testOrder1.get_order_number()) {
            return testOrder1;
        } else {
            return null;
        }
    }

    @Override
    public void setFileName(String filename) throws IOException {

    }

    @Override
    public void exportData() throws IOException {

    }
}
