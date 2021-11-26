package com.mycompany.flooringmastery.controller;

import com.mycompany.flooringmastery.dto.Order;
import com.mycompany.flooringmastery.service.FlooringServiceLayer;
import com.mycompany.flooringmastery.view.FlooringView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author john
 */
@Component
public class FlooringController {

    private FlooringServiceLayer service;
    private FlooringView view;

    @Autowired
    public FlooringController(FlooringServiceLayer service, FlooringView view) {
        this.service = service;
        this.view = view;
    }

    //Where main method from App sends us first
    public void run() {
        //Boolean to keep program running until we choose to exit
        boolean keepGoing = true;
        //int value which will be set by the user to choose a program function from the menu
        int menuSelection = 0;
        try {
            //Until user presses '6' keep prompting the user for menu choice 
            while (keepGoing) {
                //Get menu choice from the user
                menuSelection = view.displayMenuAndGetChoice();
                //Based on user's choice, program will initiate a function
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
            }
            //After we break out of the menu choice loop, display an exit message before ending the program
            exitMessage();

            //If there are any errors, catch them and print the error
        } catch (Exception e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    public void listOrders() { //throws Exception
        view.displayOrdersBanner();
        String date = view.displayGetOrderDate();
        //     view.displayOrders(service.getOrdersByDate(date)); //Possible Exception thrown from service layer if order at that date doesnt exist
    }

    public void addOrder() { //throws Exception
        view.displayAddOrderBanner();
        String date = view.displayGetNextAvailableOrderDate(); //Place the order date for a time in the future
        // int nextAvailableOrderNumber = service.getNextAvailableOrderNumber();
        //Order newOrder = new Order(nextAvailableOrderNumber, date); 
        //newOrder = view.displayAddCustomerName(newOrder);
        //newOrder = view.displayAddStateToOrder(service.getKnownStates(), newOrder);
        //newOrder = view.displayAddProductToOrder(service.getKnownProducts(), newOrder);
        //newOrder = view.displayAddAreaToOrder(newOrder);

        //service.addOrder(newOrder) //Might throw Exception if order cannot be added
        view.displayAddOrderSuccess(); //If no error was thrown then Order was added successfully
    }

    public void editOrder() { //throws Exception
        view.displayEditOrderBanner();
        String dateOfOrderToRetrieve = view.displayGetOrderDate();
        int orderNumberToRetrieve = view.displayGetOrderNumber();
        //Maintain the original copy of the object in case the User decides to discard their edits
        //Order originalOrder = service.getOrder(dateOfOrderToRetrieve, orderNumberToRetrieve); //Could throw Exception if order does
        //Create a new Order object from the original Order. All edits will take place on this object
        //Order orderToEdit = new Order(originalOrder.get_order_number(), originalOrder.get_order_date() );
        //orderToEdit.set_customer_name(originalOrder.get_customer_name() );
        //orderToEdit.set_state(originalOrder.get_state() );
        //orderToEdit.set_tax_rate(originalOrder.get_tax_rate() );
        //orderToEdit.set_product_type(originalOrder.get_product_type() );
        //orderToEdit.set_area(originalOrder.get_area() );

        //orderToEdit = view.displayEditCustomerName(orderToEdit);
        //orderToEdit = view.displayEditCustomerState(service.getKnownStates(), orderToEdit);
        //orderToEdit = view.displayEditProductType(service.getKnownProducts, orderToEdit);
        //orderToEdit = view.displayEditOrderArea(orderToEdit);
        int menuSelection = view.displayEditOrderMenu();
        switch (menuSelection) {
            case 1:
                //Don't do anything as the User has decided to discard their edits
                break;
            case 2:
                //service.addOrder(orderToEdit); //Possibly throw Exception if service cannot add the new Order
                view.displayEditOrderSuccess();
                break;
        }
    }

    public void removeOrder() { //throws Exception
        view.displayRemoveOrderBanner();
        String dateOfOrderToRetrieve = view.displayGetOrderDate();
        int orderNumberToRetrieve = view.displayGetOrderNumber();
        //serivce.removeOrder(dateOfOrderToRetrieve, orderNumberToRetrieve); //Possibly throws exception if order doesnt exist or cannot remove it
        view.displayRemoveOrderSuccess();
    }
    
    public void exportData() { //throws Exception
        view.displayExportDataBanner();
        //service.exportData(); //Possibly throw Exception if error exporting data
        view.displayExportDataSuccess();
    }

    public void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    public void exitMessage() {
        view.displayExitBanner();
    }
}
