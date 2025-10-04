import java.sql.*;

public class TestDeletion {
    public static void main(String[] args) {
        try {
            System.out.println("=== Testing Client Deletion ===");
            
            // First, create the database using the same logic as QuickDBSetup
            System.out.println("1. Creating database...");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:mazebank.db");
            Statement statement = connection.createStatement();
            
            // Drop and recreate tables
            statement.execute("DROP TABLE IF EXISTS Clients");
            statement.execute("DROP TABLE IF EXISTS WalletAccounts");
            statement.execute("DROP TABLE IF EXISTS SavingsAccounts");
            statement.execute("DROP TABLE IF EXISTS Admins");
            
            statement.execute("CREATE TABLE Clients (FirstName TEXT, LastName TEXT, PayeeAddress TEXT, Password TEXT, Date TEXT)");
            statement.execute("CREATE TABLE WalletAccounts (Owner TEXT, AccountNumber TEXT, Balance REAL, TransactionLimit REAL)");
            statement.execute("CREATE TABLE SavingsAccounts (Owner TEXT, AccountNumber TEXT, Balance REAL, WithdrawalLimit REAL)");
            statement.execute("CREATE TABLE Admins (Username TEXT, Password TEXT)");
            
            // Insert test data
            statement.execute("INSERT INTO Clients VALUES ('John', 'Doe', '@John.Doe', 'password123', '2024-01-15')");
            statement.execute("INSERT INTO Clients VALUES ('Jane', 'Smith', '@Jane.Smith', 'password456', '2024-01-20')");
            
            statement.execute("INSERT INTO WalletAccounts VALUES ('John Doe', 'WLT001234567', 1500.50, 1000.0)");
            statement.execute("INSERT INTO WalletAccounts VALUES ('Jane Smith', 'WLT001234568', 2300.75, 1000.0)");
            
            statement.execute("INSERT INTO SavingsAccounts VALUES ('John Doe', 'SAV001234567', 5000.00, 500.0)");
            statement.execute("INSERT INTO SavingsAccounts VALUES ('Jane Smith', 'SAV001234568', 7500.50, 500.0)");
            
            System.out.println("Database created with test data.");
            
            // Show initial data
            System.out.println("\n2. Initial clients in database:");
            ResultSet rs = statement.executeQuery("SELECT FirstName, LastName, PayeeAddress FROM Clients");
            while (rs.next()) {
                System.out.println("  " + rs.getString("FirstName") + " " + rs.getString("LastName") + " (" + rs.getString("PayeeAddress") + ")");
            }
            rs.close();
            
            // Test deletion using DatabaseDriver
            System.out.println("\n3. Testing deletion using DatabaseDriver...");
            
            // Create DatabaseDriver instance (this will create a new connection)
            com.smartfinance.Models.DatabaseDriver dbDriver = new com.smartfinance.Models.DatabaseDriver();
            
            // Try to delete John Doe
            String emailToDelete = "@John.Doe";
            System.out.println("Attempting to delete client with email: " + emailToDelete);
            
            boolean deleted = dbDriver.deleteClient(emailToDelete);
            
            System.out.println("\n4. Deletion result: " + (deleted ? "SUCCESS" : "FAILED"));
            
            // Show remaining clients
            System.out.println("\n5. Remaining clients in database:");
            rs = statement.executeQuery("SELECT FirstName, LastName, PayeeAddress FROM Clients");
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("  " + rs.getString("FirstName") + " " + rs.getString("LastName") + " (" + rs.getString("PayeeAddress") + ")");
            }
            rs.close();
            
            if (count == 1) {
                System.out.println("SUCCESS: One client was deleted, one remains.");
            } else {
                System.out.println("ISSUE: Expected 1 remaining client, but found " + count);
            }
            
            statement.close();
            connection.close();
            
        } catch (Exception e) {
            System.err.println("Error during test:");
            e.printStackTrace();
        }
    }
}
