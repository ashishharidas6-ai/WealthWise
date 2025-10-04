import com.smartfinance.Models.*;
import com.smartfinance.Controller.Admin.ClientsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class TestClientList extends Application {
    
    public static void main(String[] args) {
        // Test the Model.getAllClients() method first
        System.out.println("=== Testing Model.getAllClients() ===");
        
        try {
            Model model = Model.getInstance();
            List<Client> clients = model.getAllClients();
            
            System.out.println("Number of clients returned: " + clients.size());
            
            for (int i = 0; i < clients.size(); i++) {
                Client client = clients.get(i);
                System.out.println("Client " + (i+1) + ":");
                System.out.println("  Name: " + client.firstNameProperty().get() + " " + client.lastNameProperty().get());
                System.out.println("  Email: " + client.payeeAddressProperty().get());
                System.out.println("  Date: " + client.dateProperty().get());
                
                if (client.walletAccountProperty().get() != null) {
                    System.out.println("  Wallet: " + client.walletAccountProperty().get().accNumberProperty().get());
                }
                
                if (client.savingsAccountProperty().get() != null) {
                    System.out.println("  Savings: " + client.savingsAccountProperty().get().accNumberProperty().get());
                }
                System.out.println();
            }
            
        } catch (Exception e) {
            System.err.println("Error testing Model.getAllClients():");
            e.printStackTrace();
        }
        
        // Launch JavaFX application to test UI
        System.out.println("=== Launching JavaFX Test ===");
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Create a simple test scene with just the clients list
            VBox root = new VBox();
            
            // Load the Clients.fxml if it exists, or create a simple ListView
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Clients.fxml"));
                root.getChildren().add(loader.load());
                System.out.println("Loaded Clients.fxml successfully");
            } catch (Exception e) {
                System.err.println("Could not load Clients.fxml:");
                e.printStackTrace();
                
                // Create a simple test UI
                javafx.scene.control.Label label = new javafx.scene.control.Label("Client List Test - Check Console Output");
                root.getChildren().add(label);
            }
            
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Client List Test");
            primaryStage.setScene(scene);
            primaryStage.show();
            
            System.out.println("JavaFX application started successfully");
            
        } catch (Exception e) {
            System.err.println("Error starting JavaFX application:");
            e.printStackTrace();
        }
    }
}
