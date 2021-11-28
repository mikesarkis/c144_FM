package com.mycompany.flooringmastery.controller;

import com.mycompany.flooringmastery.serivce.FlooringMasteryServiceLayerFileImpl;
import com.mycompany.flooringmastery.view.FlooringView;
import com.mycompany.flooringmastery.dto.Order;
import com.mycompany.flooringmastery.dto.Product;
import com.mycompany.flooringmastery.dto.Tax;
import com.mycompany.flooringmastery.serivce.NoOrderFoundException;
import com.mycompany.flooringmastery.serivce.NospecificorderException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author john
 */
@Component
public class FlooringController {

    private FlooringMasteryServiceLayerFileImpl service;
    private FlooringView view;
    
    @Autowired
    public FlooringController(FlooringMasteryServiceLayerFileImpl service, FlooringView view) {
        this.service = service;
        this.view = view;
    }

    //Where main method from App sends us first
    public void run() {
        //Boolean to keep program running until we choose to exit
        boolean keepGoing = true;
        //int value which will be set by the user to choose a program function from the menu
        int menuSelection;

        //Until user presses '6' keep prompting the user for menu choice 
        while (keepGoing) {
            //Get menu choice from the user
            menuSelection = view.displayMenuAndGetChoice();
            //Based on user's choice, program will initiate a function
            try{
            switch (menuSelection) {
                case 1: //Display Orders
                    listOrders();
                    break;
                case 2: //Add an Order
                    addOrder();
                    break;
                case 3: //Edit an Order
                    editOrder();
                    break;
                case 4: //Remove an Order
                    removeOrder();
                    break;
                case 5: //Export All Data
                    exportData();
                    break;
                case 6: //Exit the program
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }
            }catch (NoOrderFoundException | NospecificorderException | IOException e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        //After we break out of the menu choice loop, display an exit message before ending the program
        exitMessage();
    }

    public void listOrders() throws NoOrderFoundException, IOException { //throws Exception
        view.displayOrdersBanner();
        String date = view.displayGetOrderDate();
        List<Order> list1 = service.getOrders(date);
        view.displayOrders(list1);

    }

    public void addOrder() throws FileNotFoundException, NoOrderFoundException, IOException { //throws Exception
        view.displayAddOrderBanner();
        String date = view.displayGetNextAvailableOrderDate(); //Place the order date for a time in the future
        String name = view.displayAddCustomerName();
        List<Tax> list_state = service.getalltaxes();
        String state = view.displayAddStateToOrder(list_state);
        BigDecimal taxrate = service.getTaxrate(state);
        System.out.println(taxrate);
        List<Product> list_product= service.getallproduct();
        String product_type = view.displayAddProductToOrder(list_product);
        System.out.println(product_type);
        BigDecimal costperfoot = service.getcost(product_type);
        System.out.println(costperfoot);
        BigDecimal laborcostperfoot = service.getLaborcost(product_type);
        System.out.println(laborcostperfoot);
        BigDecimal area = view.displayAddAreaToOrder();
        System.out.println(area);
        Order temp = service.addorder(date, name, state, taxrate, product_type,area, costperfoot, laborcostperfoot);
        if(temp != null)
                {
        view.displayAddOrderSuccess(); //If no error was thrown then Order was added successfully
                }
        else
        {
            view.displayordererror();
        }
    }

    public void editOrder() throws NoOrderFoundException, FileNotFoundException, IOException, NospecificorderException { //throws Exception
        view.displayEditOrderBanner();
        String dateOfOrderToRetrieve = view.displayGetOrderDatetoEdit();
        int orderNumberToRetrieve = view.displayGetOrderNumber();
        //Maintain the original copy of the object in case the User decides to discard their edits
        Order originalOrder = service.getOrder(orderNumberToRetrieve, dateOfOrderToRetrieve); 
        //Create a new Order object from the original Order. All edits will take place on this object
        if(originalOrder != null)
        {
        Order orderToEdit = new Order(originalOrder.get_order_number(), originalOrder.get_order_date() );
        orderToEdit.set_customer_name(originalOrder.get_customer_name() );
        orderToEdit.set_state(originalOrder.get_state() );
        orderToEdit.set_tax_rate(originalOrder.get_tax_rate() );
        orderToEdit.set_product_type(originalOrder.get_product_type() );
        orderToEdit.set_area(originalOrder.get_area() );
        orderToEdit = view.displayEditCustomerName(orderToEdit);
        orderToEdit = view.displayEditCustomerState(service.getalltaxes(), orderToEdit);
        BigDecimal rate = service.getTaxrate(orderToEdit.get_state());
        orderToEdit.set_tax_rate(rate);
        orderToEdit = view.displayEditProductType(service.getallproduct(), orderToEdit);
        BigDecimal cost1= service.getcost(orderToEdit.get_product_type());
        orderToEdit.set_cost_per_square_foot(cost1);
        BigDecimal laborcost = service.getLaborcost(orderToEdit.get_product_type());
        orderToEdit.set_labor_cost_per_square_foot(laborcost);
        orderToEdit = view.displayEditOrderArea(orderToEdit);
        
        int menuSelection = view.displayEditOrderMenu();
        switch (menuSelection) {
            case 1:
                //Don't do anything as the User has decided to discard their edits
                break;
            case 2:
                //service.addOrder(orderToEdit); //Possibly throw Exception if service cannot add the new Order
                service.editOrder(originalOrder.get_order_number(), dateOfOrderToRetrieve, orderToEdit);
                view.displayEditOrderSuccess();
                break;
        }
        }
    }

    public void removeOrder() throws NoOrderFoundException, IOException, NospecificorderException { //throws Exception
        view.displayRemoveOrderBanner();
        String dateOfOrderToRetrieve = view.displayGetOrderDate();
        int orderNumberToRetrieve = view.displayGetOrderNumber();
        service.removeOrder(orderNumberToRetrieve,dateOfOrderToRetrieve); //Possibly throws exception if order doesnt exist or cannot remove it
        view.displayRemoveOrderSuccess();
    }
    
    public void exportData() throws IOException { //throws Exception
        view.displayExportDataBanner();
        String dateOfOrderToRetrieve = view.displayGetOrderDate();
        service.exportData(dateOfOrderToRetrieve);
        view.displayExportDataSuccess();
    }

    public void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    public void exitMessage() {
        view.displayExitBanner();
    }
}
