package com.mycompany.flooringmastery.view;

import com.mycompany.flooringmastery.UserIO.UserIO;
import com.mycompany.flooringmastery.dto.Order;
import com.mycompany.flooringmastery.dto.Product;
import com.mycompany.flooringmastery.dto.Tax;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author john
 */
@Component
public class FlooringView {

    private UserIO io;

    @Autowired
    public FlooringView(UserIO io) {
        this.io = io;
    }

    public int displayMenuAndGetChoice() {
        io.print("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("    * <<Flooring Program>>");
        io.print("   * 1. Display Orders");
        io.print("   * 2. Add an Order");
        io.print("   * 3. Edit an Order");
        io.print("   * 4. Remove an Order");
        io.print("   * 5. Export All Data");
        io.print("   * 6. Quit");
        io.print("   *");
        io.print("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public void displayOrdersBanner() {
        io.print("  * * * * * * * * * * * * * * * Display An Order * * * * * * * * * * * * * * * ");
    }

    public void displayOrders(List<Order> orders) {
        for (Order current : orders) {
            io.print("Order Number: " + current.get_order_number()
                    + " Customer Name: " + current.get_customer_name()
                    + " State: " + current.get_state()
                    + " Tax Rate: " + current.get_tax_rate()
                    + " Product Type: " + current.get_product_type()
                    + " Area: " + current.get_area()
                    + " Cost Per Square Foot: " + current.get_cost_per_square_foot()
                    + " Labor Cost per Square foot: " + current.get_labor_cost_per_square_foot()
                    + " Material Cost: " + current.get_material_cost(current.get_area(), current.get_cost_per_square_foot())
                    + " Labor Cost: " + current.get_labor_cost(current.get_material_cost(current.get_area(), current.get_cost_per_square_foot()), current.get_labor_cost_per_square_foot())
                    + " Tax: " + current.get_tax(current.get_material_cost(current.get_area(), current.get_cost_per_square_foot()), current.get_labor_cost(current.get_material_cost(current.get_area(), current.get_cost_per_square_foot()), current.get_labor_cost_per_square_foot()), current.get_tax_rate())
                    + " Total: " + current.get_total(current.get_material_cost(current.get_area(), current.get_cost_per_square_foot()),
                            current.get_labor_cost(current.get_material_cost(current.get_area(), current.get_cost_per_square_foot()), current.get_labor_cost_per_square_foot()),
                            current.get_tax(current.get_material_cost(current.get_area(), current.get_cost_per_square_foot()), current.get_labor_cost(current.get_material_cost(current.get_area(), current.get_cost_per_square_foot()), current.get_labor_cost_per_square_foot()), current.get_tax_rate())));
            io.print("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        }
    }

    public void displayAddOrderBanner() {
        io.print("  * * * * * * * * * * * * * * * Add An Order * * * * * * * * * * * * * * * ");
    }

    public Order displayAddAnOrder(int nextAvailableOrderNumber, List<Tax> stateTaxes, List<Product> KnownProducts) {
        Order newOrder;
        String date = "";
        boolean keepGoing = true;
        while (keepGoing) {
            io.print("Enter an order date after the current date - " + LocalDate.now(ZoneId.systemDefault()));
            int year = io.readInt("Enter the year (2000- 2021)", Year.now().getValue(), 2100);
            int month = io.readInt("Enter the month of the order (1 - 12)", 1, 12);
            int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
            int day = io.readInt("Enter the day of the order (1 - " + daysInMonth + ")", 1, daysInMonth);
            LocalDate orderDate = LocalDate.of(year, month, day);
            LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
            if (orderDate.isAfter(currentDate)) {
                date = orderDate.toString();
                keepGoing = false;
            } else {
                io.print("Enter a valid date in the future");
            }
        }
        newOrder = new Order(nextAvailableOrderNumber, date);

        keepGoing = true;
        while (keepGoing) {
            Pattern p = Pattern.compile("[^a-zA-Z0-9,.]");
            String newName = io.readString("Enter customer name)", p);
            if (newName.isEmpty()) {
                io.print("Invalid input");
            } else {
                newOrder.set_customer_name(newName);
                keepGoing = false;
            }
        }

        keepGoing = true;
        while (keepGoing) {
            Pattern p = Pattern.compile("[^a-zA-Z]");
            io.print("Known states");
            stateTaxes.stream().forEach(tax -> io.print(tax.getFullName()));
            String newState = io.readString("Enter a state ", p);
            if (newState.isEmpty()) {
                io.print("Invalid input");
            } else if (!stateTaxes.stream()
                    .map(tax -> tax.getFullName())
                    .anyMatch(s -> s.equals(newState))) {
                io.print("State does not exist");
            } else {
                newOrder.set_state(newState);
                keepGoing = false;
            }
        }

        keepGoing = true;
        while (keepGoing) {
            Pattern p = Pattern.compile("[^a-zA-Z]");
            io.print("Known products");
            KnownProducts.stream().forEach(product -> io.print(product.getProductName()));
            String newProduct = io.readString("Enter a product", p);
            if (newProduct.isEmpty()) {
                io.print("Invalid input");
            } else if (!KnownProducts.stream()
                    .map(product -> product.getProductName())
                    .anyMatch(s -> s.equals(newProduct))) {
                io.print("Product type does not exist");
            } else {
                newOrder.set_product_type(newProduct);
                //Could set product CostPerSquareFoot
                //But view cannot get a Product with only 
                //the Product's String name. Instead, an Order's product field
                //should not be the String name of the Product, but the actual Order object
                keepGoing = false;
            }
        }

        keepGoing = true;
        while (keepGoing) {
            Pattern p = Pattern.compile("^(?!(?:\\d{1,2})$)[0-9]\\d+$");
            String newArea = io.readString("Enter an area. Minimum area is 100 ", p);
            if (newArea.isEmpty()) {
                io.print("Invalid input");
            } else {
                newOrder.set_area(newArea);
                keepGoing = false;
            }
        }
        //Still need to set 
        //CostPerSquareFoot, LaborCostPerSquareFoot, MaterialCost, LaborCost, Tax, Total
        return newOrder;
    }

    public void displayEditOrderBanner() {
        io.print("  * * * * * * * * * * * * * * * Edit An Order * * * * * * * * * * * * * * * ");
    }

    /*
    From the program requirements  https://academy.engagelms.com/mod/assign/view.php?id=46586
    Edit will ask the user for a date and order number.
    If the order exists for that date, it will ask the user for each piece of order data but display the existing data.
    If the user enters something new, it will replace that data; if the user hits Enter without entering data, it will leave the existing data in place.
     */
    public int displayEditOrderMenu() {
        io.print("   * 1. Customer Name");
        io.print("   * 2. Customer State");
        io.print("   * 3. Product Type");
        io.print("   * 4. Area");
        io.print("   * 5. Discard changes and return to main menu");
        io.print("   * 6. Save changes and return to main menu");
        io.print("   *");
        io.print("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public Order displayEditCustomerName(Order editOrder) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9,.]");
        String newName = io.readString("Enter customer name (" + editOrder.get_customer_name() + ") :", p);
        if (newName.isEmpty()) {
            io.print("Customer name not changed");
            return editOrder;
        }
        editOrder.set_customer_name(newName);
        return editOrder;
    }

    public Order displayEditCustomerState(List<Tax> stateTaxes, Order editOrder) {
        boolean keepGoing = true;
        Pattern p = Pattern.compile("[^a-zA-Z]");

        while (keepGoing) {
            String newState = io.readString("Enter a state (" + editOrder.get_order_number() + ") :", p);
            if (newState.isEmpty()) {
                io.print("Customer state not changed");
                return editOrder;
            } else if (!stateTaxes.stream()
                    .map(tax -> tax.getFullName())
                    .anyMatch(s -> s.equals(newState))) {
                io.print("State does not exist");
            } else {
                editOrder.set_state(newState);
                keepGoing = false;
            }
        }
        return editOrder;
    }

    public Order displayEditProductType(List<Product> KnownProducts, Order editOrder) {
        boolean keepGoing = true;
        Pattern p = Pattern.compile("[^a-zA-Z]");

        while (keepGoing) {
            String newProduct = io.readString("Enter a product (" + editOrder.get_product_type() + ") :", p);
            if (newProduct.isEmpty()) {
                io.print("Product type not changed");
                return editOrder;
            } else if (!KnownProducts.stream()
                    .map(product -> product.getProductName())
                    .anyMatch(s -> s.equals(newProduct))) {
                io.print("Product type does not exist");
            } else {
                editOrder.set_product_type(newProduct);
                keepGoing = false;
            }
        }
        return editOrder;
    }

    public Order displayEditOrderArea(Order editOrder) {
        Pattern p = Pattern.compile("^(?!(?:\\d{1,2})$)[0-9]\\d+$");
        String newArea = io.readString("Enter an area. Minimum area is 100 (" + editOrder.get_area() + ") :", p);
        if (newArea.isEmpty()) {
            io.print("Area not changed");
            return editOrder;
        } else {
            editOrder.set_area(newArea);
            return editOrder;
        }
    }

    public String displayGetOrderDate() {
        io.print("Enter the date of the order");
        int year = io.readInt("Enter the year (2000- 2021)", 2000, 2021);
        int month = io.readInt("Enter the month of the order (1 - 12)", 1, 12);
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        int day = io.readInt("Enter the day of the order (1 - " + daysInMonth + ")", 1, daysInMonth);
        String Date = year + "-" + month + "-" + day;
        return Date;
    }

    public int displayGetOrderNumber() {
        return io.readInt("Enter an order number");
    }
}
