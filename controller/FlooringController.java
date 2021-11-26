package com.mycompany.flooringmastery.controller;

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
                        //To be implemented
                        break;
                    case 2: //Add an Order
                        //To be implemented
                        break;
                    case 3: //Edit an Order
                        //To be implemented
                        break;
                    case 4: //Remove an Order
                        //To be implemented
                        break;
                    case 5: //Export All Data
                        //To be implemented
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

    public void EditOrder() {
        view.displayEditOrderBanner();
        String dateOfOrderToRetrieve = view.displayGetOrderDate();
        //Order orderToEdit = service.getOrder(dateOfOrderToRetrieve);
        int menuSelection;
        boolean keepGoing = true;
        while (keepGoing) {

            menuSelection = view.displayEditOrderMenu();
            switch (menuSelection) {
                case 1:

                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        }
    }

    public void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    public void exitMessage() {
        view.displayExitBanner();
    }
}
