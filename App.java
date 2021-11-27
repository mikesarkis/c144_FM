/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mycompany.flooringmastery.controller.FlooringController;
import java.io.IOException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
/**
 *
 * @author Mike
 */
public class App {
        public static void main(String[] args) throws IOException {
        //Spring boot dependency injection using Annotations 
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.mycompany.flooringmastery");
        appContext.refresh();
        FlooringController controller = appContext.getBean("flooringController", FlooringController.class);
        controller.run();
    }
    
}
