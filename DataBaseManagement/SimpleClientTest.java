import com.example.wealthwise.Models.*;
import java.time.LocalDate;
import java.util.List;

public class SimpleClientTest {
    public static void main(String[] args) {
        System.out.println("=== Simple Client List Test ===");
        
        try {
            // Test 1: Create Model instance
            System.out.println("1. Creating Model instance...");
            Model model = Model.getInstance();
            System.out.println("   Model created successfully");
            
            // Test 2: Get all clients
            System.out.println("2. Getting all clients...");
            List<Client> clients = model.getAllClients();
            System.out.println("   Returned " + clients.size() + " clients");
            
            // Test 3: Display client details
            System.out.println("3. Client details:");
            for (int i = 0; i < clients.size(); i++) {
                Client client = clients.get(i);
                System.out.println("   Client " + (i+1) + ":");
                
                try {
                    String firstName = client.firstNameProperty().get();
                    String lastName = client.lastNameProperty().get();
                    String email = client.payeeAddressProperty().get();
                    LocalDate date = client.dateProperty().get();
                    
                    System.out.println("     Name: " + firstName + " " + lastName);
                    System.out.println("     Email: " + email);
                    System.out.println("     Date: " + date);
                    
                    // Test wallet account
                    if (client.walletAccountProperty().get() != null) {
                        String walletNumber = client.walletAccountProperty().get().accNumberProperty().get();
                        System.out.println("     Wallet: " + walletNumber);
                    } else {
                        System.out.println("     Wallet: NULL");
                    }
                    
                    // Test savings account
                    if (client.savingsAccountProperty().get() != null) {
                        String savingsNumber = client.savingsAccountProperty().get().accNumberProperty().get();
                        System.out.println("     Savings: " + savingsNumber);
                    } else {
                        System.out.println("     Savings: NULL");
                    }
                    
                } catch (Exception clientEx) {
                    System.err.println("     Error reading client data: " + clientEx.getMessage());
                    clientEx.printStackTrace();
                }
                
                System.out.println();
            }
            
            // Test 4: Test if clients can be used in UI components
            System.out.println("4. Testing client data for UI compatibility...");
            for (Client client : clients) {
                try {
                    // Simulate what ClientCellController does
                    String firstName = client.firstNameProperty().get();
                    String lastName = client.lastNameProperty().get();
                    String payeeAddress = client.payeeAddressProperty().get();
                    
                    System.out.println("   UI Test - Name: " + firstName + " " + lastName + " (" + payeeAddress + ")");
                    
                    // Test account access
                    if (client.walletAccountProperty().get() != null && 
                        client.walletAccountProperty().get().accNumberProperty().get() != null) {
                        System.out.println("   UI Test - Wallet: " + client.walletAccountProperty().get().accNumberProperty().get());
                    }
                    
                    if (client.savingsAccountProperty().get() != null && 
                        client.savingsAccountProperty().get().accNumberProperty().get() != null) {
                        System.out.println("   UI Test - Savings: " + client.savingsAccountProperty().get().accNumberProperty().get());
                    }
                    
                    if (client.dateProperty().get() != null) {
                        System.out.println("   UI Test - Date: " + client.dateProperty().get().toString());
                    }
                    
                } catch (Exception uiEx) {
                    System.err.println("   UI Test Error: " + uiEx.getMessage());
                    uiEx.printStackTrace();
                }
            }
            
            System.out.println("=== Test Complete ===");
            
        } catch (Exception e) {
            System.err.println("Test failed with error:");
            e.printStackTrace();
        }
    }
}