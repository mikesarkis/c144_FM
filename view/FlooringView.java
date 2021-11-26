package com.mycompany.flooringmastery.view;

import com.mycompany.flooringmastery.UserIO.UserIO;
import com.mycompany.flooringmastery.dto.Order;
import com.mycompany.flooringmastery.dto.Product;
import com.mycompany.flooringmastery.dto.Tax;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    /**
     * Method used for displaying the Main Menu then getting the menu choice
     * from the User
     *
     * @return - An integer value representing the User's menu choice
     */
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

    /**
     * Method to print out Orders
     *
     * @param orders - A list of Orders to be printed
     */
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

    /**
     * Method to ask the User for a date in the future
     *
     * @return - returns a valid date in the format YYYY-MM-dd
     */
    public String displayGetNextAvailableOrderDate() {
        boolean keepGoing = true;
        String newDate = "";
        while (keepGoing) {
            io.print("Enter an order date after the current date - " + LocalDate.now(ZoneId.systemDefault()));
            int year = io.readInt("Enter the year", Year.now().getValue(), 2100);
            int month = io.readInt("Enter the month of the order (1 - 12)", 1, 12);
            int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
            int day = io.readInt("Enter the day of the order (1 - " + daysInMonth + ")", 1, daysInMonth);
            LocalDate orderDate = LocalDate.of(year, month, day);
            LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
            if (orderDate.isAfter(currentDate)) {
                newDate = orderDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                keepGoing = false;
            } else {
                io.print("Enter a valid date in the future");
            }
        }
        return newDate;
    }

    /**
     * Method to ask the User for a Customer name and add it to a new Order. Per
     * requirements, the name: may not be blank, is allowed to contain
     * [a-z][0-9] as well as periods and comma characters. "Acme, Inc." is a
     * valid name.
     *
     * @param newOrder - new Order object to add information to
     * @return - The object with added information
     */
    public Order displayAddCustomerName(Order newOrder) {
        boolean keepGoing = true;
        while (keepGoing) {
            //Regex pattern that checks for any values that are not A-Z, a-z, 0-9, or commas and periods
            Pattern p = Pattern.compile("[^a-zA-Z0-9,.]");
            String newName = io.readString("Enter customer name)", "Invalid Input", p);
            newOrder.set_customer_name(newName);
            keepGoing = false;
        }
        return newOrder;
    }

    /**
     * Method to add a tax state to a new Order
     *
     * @param stateTaxes - A list of states that we can sell to
     * @param newOrder - The order to add information to
     * @return - An Order object with added State information
     */
    public Order displayAddStateToOrder(List<Tax> stateTaxes, Order newOrder) {
        boolean keepGoing = true;
        while (keepGoing) {
            //Regex pattern that checks for any values that are not A-Z or a-z
            Pattern p = Pattern.compile("[^a-zA-Z]");

            io.print("Known states");
            //List the known states that the User can choose from
            stateTaxes.stream().forEach(tax -> io.print(tax.getFullName()));
            //Ask the User to enter a State to be added to the order
            String newState = io.readString("Enter a state ", "Invalid Input", p);

            //If the State that the User enters is not in our list of Known States
            // then ask the User to enter a new selection
            if (!stateTaxes.stream()
                    .map(tax -> tax.getFullName())
                    .anyMatch(s -> s.equals(newState))) {
                io.print("State does not exist");
                //If the User entered a known state, add it to the new Order object
            } else {
                newOrder.set_state(newState);
                //Break out of the loop
                keepGoing = false;
            }
        }
        //Return our newORder object with added State information
        return newOrder;
    }

    /**
     * Method to add a Product type to a new Order
     *
     * @param KnownProducts - List of know products that we carry
     * @param newOrder - The Order object to add information to
     * @return - The order object with added Product information
     */
    public Order displayAddProductToOrder(List<Product> KnownProducts, Order newOrder) {
        boolean keepGoing = true;
        while (keepGoing) {
            //Regex pattern that checks for any values that are not A-Z or a-z
            Pattern p = Pattern.compile("[^a-zA-Z]");
            //List the known products that the business carries
            io.print("Known products");
            KnownProducts.stream().forEach(product -> io.print(product.getProductName()));

            String newProduct = io.readString("Enter a product", "Invalid Input", p);
            //If the Product that the User enters is not in the list of known Products
            //ask the User to reenter a selection
            if (!KnownProducts.stream()
                    .map(product -> product.getProductName())
                    .anyMatch(s -> s.equals(newProduct))) {
                io.print("Product type does not exist");

                //If the User enters a know product add it to the newOrder
            } else {
                newOrder.set_product_type(newProduct);
                //Break out of loop
                keepGoing = false;
            }
        }
        //Return the newOrder object with added Product information 
        return newOrder;
    }

    /**
     * Method to add area to a new Order
     *
     * @param newOrder - a new Order to add area information to
     * @return - a new Order with added area information
     */
    public Order displayAddAreaToOrder(Order newOrder) {
        //Regex pattern that checks for any values that are below 100
        Pattern p = Pattern.compile("^(?!(?:\\d{1,2})$)[0-9]\\d+$");
        String newArea = io.readString("Enter an area. Minimum area is 100 ", "Invalid Input", p);
        newOrder.set_area(newArea);
        return newOrder;
    }

    public void displayAddOrderResult() {
        io.print("Order successfully added");
    }

    public void displayEditOrderBanner() {
        io.print("  * * * * * * * * * * * * * * * Edit An Order * * * * * * * * * * * * * * * ");
    }

    /*
    From the program requirements  https://academy.engagelms.com/mod/assign/view.php?id=46586
    Edit will ask the User for a date and order number.
    If the order exists for that date, it will ask the User for each piece of order data but display the existing data.
    If the User enters something new, it will replace that data; if the User hits Enter without entering data, it will leave the existing data in place.
     */
    /**
     * Method that displays the edit order menu
     *
     * @return - An integer value representing the User's menu choice
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

    /**
     * Method to ask the User for a new Customer name. User can press enter to
     * leave the name unchanged Per requirements, the name: is allowed to
     * contain [a-z][0-9] as well as periods and comma characters. "Acme, Inc."
     * is a valid name.
     *
     * @param editOrder - Order object to edit information
     * @return - The Order object with new or unchanged Name information
     */
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

    /**
     * Method to edit the tax state of an Order
     *
     * @param stateTaxes - A list of states that we can sell to
     * @param editOrder - The order to add information to
     * @return - The Order object with new or unchanged State information
     */
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

    /**
     * Method to change the Product type of an Order
     *
     * @param KnownProducts - List of know products that we carry
     * @param editOrder - The Order object to change information
     * @return - The order object with new or unchanged Product information
     */
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

    /**
     * Method to change the area of a new Order
     *
     * @param editOrder - a new Order to change area information
     * @return - an Order with new or unchanged area information
     */
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

    public void displayEditOrderSuccess() {
        io.print("Order succesfully edited");
    }

    /**
     * Method to ask the user for a date
     *
     * @return a date in the format YYYY-MM-dd
     */
    public String displayGetOrderDate() {
        io.print("Enter the date of the order");
        int year = io.readInt("Enter the year (2000- 2021)", 2000, 2021);
        int month = io.readInt("Enter the month of the order (1 - 12)", 1, 12);
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        int day = io.readInt("Enter the day of the order (1 - " + daysInMonth + ")", 1, daysInMonth);
        LocalDate newOrder = LocalDate.of(year, month, day);
        return newOrder.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public int displayGetOrderNumber() {
        return io.readInt("Enter an order number");
    }

    public void displayRemoveOrderBanner() {
        io.print("  * * * * * * * * * * * * * * * Remove An Order * * * * * * * * * * * * * * * ");
    }

    public void displayRemoveOrderSuccess() {
        io.print("Order removed successfully");
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command");
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }

    public void displayExitBanner() {
        io.print("Exiting Flooring Mastery");
    }
}
