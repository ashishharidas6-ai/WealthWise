import java.sql.*;

public class DebugDatabase {
    public static void main(String[] args) {
        try {
            // Create database connection
            Connection connection = DriverManager.getConnection("jdbc:sqlite:mazebank.db");
            Statement statement = connection.createStatement();
            
            System.out.println("=== Creating Database ===");
            
            // Create tables
            statement.execute("DROP TABLE IF EXISTS Clients");
            statement.execute("DROP TABLE IF EXISTS WalletAccounts");
            statement.execute("DROP TABLE IF EXISTS SavingsAccounts");
            statement.execute("DROP TABLE IF EXISTS Admins");
            
            statement.execute("CREATE TABLE Clients (FirstName TEXT, LastName TEXT, PayeeAddress TEXT, Password TEXT, Date TEXT)");
            statement.execute("CREATE TABLE WalletAccounts (Owner TEXT, AccountNumber TEXT, Balance REAL, TransactionLimit REAL)");
            statement.execute("CREATE TABLE SavingsAccounts (Owner TEXT, AccountNumber TEXT, Balance REAL, WithdrawalLimit REAL)");
            statement.execute("CREATE TABLE Admins (Username TEXT, Password TEXT)");
            
            // Insert data with your updated format
            statement.execute("INSERT INTO Admins VALUES ('admin', 'admin123')");
            
            statement.execute("INSERT INTO Clients VALUES ('John', 'Doe', '@John.Doe', 'password123', '2024-01-15')");
            statement.execute("INSERT INTO Clients VALUES ('Jane', 'Smith', '@Jane.Smith', 'password456', '2024-01-20')");
            statement.execute("INSERT INTO Clients VALUES ('Bob', 'Johnson', '@Bob.Johnson', 'password789', '2024-01-25')");
            
            statement.execute("INSERT INTO WalletAccounts VALUES ('John Doe', 'WLT001234567', 1500.50, 1000.0)");
            statement.execute("INSERT INTO WalletAccounts VALUES ('Jane Smith', 'WLT001234568', 2300.75, 1000.0)");
            statement.execute("INSERT INTO WalletAccounts VALUES ('Bob Johnson', 'WLT001234569', 800.25, 1000.0)");
            
            statement.execute("INSERT INTO SavingsAccounts VALUES ('John Doe', 'SAV001234567', 5000.00, 500.0)");
            statement.execute("INSERT INTO SavingsAccounts VALUES ('Jane Smith', 'SAV001234568', 7500.50, 500.0)");
            statement.execute("INSERT INTO SavingsAccounts VALUES ('Bob Johnson', 'SAV001234569', 3200.75, 500.0)");
            
            System.out.println("Database created successfully!");
            
            // Test reading clients
            System.out.println("\n=== Reading Clients ===");
            ResultSet rs = statement.executeQuery("SELECT * FROM Clients");
            while (rs.next()) {
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String payeeAddress = rs.getString("PayeeAddress");
                System.out.println("Client: " + firstName + " " + lastName + " (" + payeeAddress + ")");
            }
            
            // Test deletion
            System.out.println("\n=== Testing Deletion ===");
            String testEmail = "@John.Doe";
            System.out.println("Attempting to delete client with PayeeAddress: " + testEmail);
            
            // First get the client name
            PreparedStatement selectStmt = connection.prepareStatement("SELECT FirstName, LastName FROM Clients WHERE PayeeAddress = ?");
            selectStmt.setString(1, testEmail);
            ResultSet clientRs = selectStmt.executeQuery();
            
            if (clientRs.next()) {
                String clientName = clientRs.getString("FirstName") + " " + clientRs.getString("LastName");
                System.out.println("Found client: " + clientName);
                
                // Delete accounts
                PreparedStatement deleteWallet = connection.prepareStatement("DELETE FROM WalletAccounts WHERE Owner = ?");
                deleteWallet.setString(1, clientName);
                int walletDeleted = deleteWallet.executeUpdate();
                
                PreparedStatement deleteSavings = connection.prepareStatement("DELETE FROM SavingsAccounts WHERE Owner = ?");
                deleteSavings.setString(1, clientName);
                int savingsDeleted = deleteSavings.executeUpdate();
                
                // Delete client
                PreparedStatement deleteClient = connection.prepareStatement("DELETE FROM Clients WHERE PayeeAddress = ?");
                deleteClient.setString(1, testEmail);
                int clientDeleted = deleteClient.executeUpdate();
                
                System.out.println("Deletion results:");
                System.out.println("  Wallet accounts deleted: " + walletDeleted);
                System.out.println("  Savings accounts deleted: " + savingsDeleted);
                System.out.println("  Client deleted: " + clientDeleted);
                
                if (clientDeleted > 0) {
                    System.out.println("SUCCESS: Client deletion worked!");
                } else {
                    System.out.println("FAILED: Client deletion failed!");
                }
            } else {
                System.out.println("FAILED: Client not found with PayeeAddress: " + testEmail);
            }
            
            // Show remaining clients
            System.out.println("\n=== Remaining Clients ===");
            ResultSet remainingRs = statement.executeQuery("SELECT * FROM Clients");
            while (remainingRs.next()) {
                String firstName = remainingRs.getString("FirstName");
                String lastName = remainingRs.getString("LastName");
                String payeeAddress = remainingRs.getString("PayeeAddress");
                System.out.println("Client: " + firstName + " " + lastName + " (" + payeeAddress + ")");
            }
            
            connection.close();
            
        } catch (Exception e) {
            System.err.println("Error:");
            e.printStackTrace();
        }
    }
}