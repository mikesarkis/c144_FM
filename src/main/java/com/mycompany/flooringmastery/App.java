package com.mycompany.flooringmastery;

import com.mycompany.flooringmastery.controller.FlooringController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author john, steven, mikaeil
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Spring boot dependency injection using Annotations 
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.mycompany.flooringmastery");
        appContext.refresh();

        FlooringController controller = appContext.getBean("flooringController", FlooringController.class);
        controller.run();
    }
}
