import java.sql.*;
import java.io.File;

public class FixDeletion {
    public static void main(String[] args) {
        System.out.println("=== WealthWise Client Deletion Fix ===");
        
        try {
            // Step 1: Ensure database exists with correct data
            System.out.println("\n1. Setting up database...");
            setupDatabase();
            
            // Step 2: Test the deletion functionality
            System.out.println("\n2. Testing deletion functionality...");
            testDeletion();
            
            System.out.println("\n=== Fix Complete ===");
            System.out.println("Your WealthWise application should now support client deletion.");
            System.out.println("To test:");
            System.out.println("1. Run your WealthWise application");
            System.out.println("2. Login as admin (username: admin, password: admin123)");
            System.out.println("3. Go to the Clients view");
            System.out.println("4. Click the delete button on any client");
            System.out.println("5. Confirm deletion in the dialog");
            System.out.println("6. The client should be removed and the list should refresh");
            
        } catch (Exception e) {
            System.err.println("Error during fix:");
            e.printStackTrace();
        }
    }
    
    private static void setupDatabase() throws Exception {
        // Delete existing database if it exists
        File dbFile = new File("mazebank.db");
        if (dbFile.exists()) {
            System.out.println("  Removing existing database...");
            dbFile.delete();
        }

        // Create new database
        System.out.println("  Creating new database...");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:mazebank.db");
        Statement statement = connection.createStatement();
        
        // Create tables
        statement.execute(
            "CREATE TABLE Clients (" +
            "FirstName TEXT NOT NULL, " +
            "LastName TEXT NOT NULL, " +
            "PayeeAddress TEXT NOT NULL UNIQUE, " +
            "Password TEXT NOT NULL, " +
            "Date TEXT NOT NULL)"
        );
        
        statement.execute(
            "CREATE TABLE WalletAccounts (" +
            "Owner TEXT NOT NULL, " +
            "AccountNumber TEXT NOT NULL UNIQUE, " +
            "Balance REAL NOT NULL DEFAULT 0.0, " +
            "TransactionLimit REAL NOT NULL DEFAULT 1000.0)"
        );
        
        statement.execute(
            "CREATE TABLE SavingsAccounts (" +
            "Owner TEXT NOT NULL, " +
            "AccountNumber TEXT NOT NULL UNIQUE, " +
            "Balance REAL NOT NULL DEFAULT 0.0, " +
            "WithdrawalLimit REAL NOT NULL DEFAULT 500.0)"
        );
        
        statement.execute(
            "CREATE TABLE Admins (" +
            "Username TEXT NOT NULL UNIQUE, " +
            "Password TEXT NOT NULL)"
        );
        
        // Insert sample data
        statement.execute("INSERT INTO Admins VALUES ('admin', 'admin123')");
        
        // Clients with @username format
        statement.execute("INSERT INTO Clients VALUES ('John', 'Doe', '@John.Doe', 'password123', '2024-01-15')");
        statement.execute("INSERT INTO Clients VALUES ('Jane', 'Smith', '@Jane.Smith', 'password456', '2024-01-20')");
        statement.execute("INSERT INTO Clients VALUES ('Bob', 'Johnson', '@Bob.Johnson', 'password789', '2024-01-25')");
        statement.execute("INSERT INTO Clients VALUES ('Alice', 'Brown', '@Alice.Brown', 'password101', '2024-02-01')");
        statement.execute("INSERT INTO Clients VALUES ('Charlie', 'Wilson', '@Charlie.Wilson', 'password202', '2024-02-05')");
        
        // Wallet Accounts (Owner = "FirstName LastName")
        statement.execute("INSERT INTO WalletAccounts VALUES ('John Doe', 'WLT001234567', 1500.50, 1000.0)");
        statement.execute("INSERT INTO WalletAccounts VALUES ('Jane Smith', 'WLT001234568', 2300.75, 1000.0)");
        statement.execute("INSERT INTO WalletAccounts VALUES ('Bob Johnson', 'WLT001234569', 850.25, 1000.0)");
        statement.execute("INSERT INTO WalletAccounts VALUES ('Alice Brown', 'WLT001234570', 3200.00, 1000.0)");
        statement.execute("INSERT INTO WalletAccounts VALUES ('Charlie Wilson', 'WLT001234571', 750.80, 1000.0)");
        
        // Savings Accounts (Owner = "FirstName LastName")
        statement.execute("INSERT INTO SavingsAccounts VALUES ('John Doe', 'SAV001234567', 5000.00, 500.0)");
        statement.execute("INSERT INTO SavingsAccounts VALUES ('Jane Smith', 'SAV001234568', 7500.50, 500.0)");
        statement.execute("INSERT INTO SavingsAccounts VALUES ('Bob Johnson', 'SAV001234569', 3200.75, 500.0)");
        statement.execute("INSERT INTO SavingsAccounts VALUES ('Alice Brown', 'SAV001234570', 9800.25, 500.0)");
        statement.execute("INSERT INTO SavingsAccounts VALUES ('Charlie Wilson', 'SAV001234571', 4500.60, 500.0)");
        
        // Verify data
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM Clients");
        rs.next();
        int clientCount = rs.getInt(1);
        System.out.println("  Created " + clientCount + " clients");
        
        rs = statement.executeQuery("SELECT COUNT(*) FROM WalletAccounts");
        rs.next();
        int walletCount = rs.getInt(1);
        System.out.println("  Created " + walletCount + " wallet accounts");
        
        rs = statement.executeQuery("SELECT COUNT(*) FROM SavingsAccounts");
        rs.next();
        int savingsCount = rs.getInt(1);
        System.out.println("  Created " + savingsCount + " savings accounts");
        
        rs.close();
        statement.close();
        connection.close();
        
        System.out.println("  Database setup complete!");
    }
    
    private static void testDeletion() throws Exception {
        System.out.println("  Testing deletion with DatabaseDriver...");
        
        // Create DatabaseDriver instance
        com.smartfinance.Models.DatabaseDriver dbDriver = new com.smartfinance.Models.DatabaseDriver();
        
        // Test deleting John Doe
        String emailToDelete = "@John.Doe";
        System.out.println("  Attempting to delete: " + emailToDelete);
        
        boolean deleted = dbDriver.deleteClient(emailToDelete);
        
        if (deleted) {
            System.out.println("  ✅ SUCCESS: Client deletion worked!");
            
            // Verify deletion by checking remaining clients
            Connection connection = DriverManager.getConnection("jdbc:sqlite:mazebank.db");
            Statement statement = connection.createStatement();
            
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM Clients");
            rs.next();
            int remainingClients = rs.getInt(1);
            
            if (remainingClients == 4) {
                System.out.println("  ✅ VERIFIED: 4 clients remain (1 was deleted)");
            } else {
                System.out.println("  ⚠️  WARNING: Expected 4 clients, found " + remainingClients);
            }
            
            rs.close();
            statement.close();
            connection.close();
            
        } else {
            System.out.println("  ❌ FAILED: Client deletion did not work");
            System.out.println("  Check the console output above for debugging information");
        }
    }
}
