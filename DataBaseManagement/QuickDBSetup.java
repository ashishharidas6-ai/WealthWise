import java.sql.*;
import java.io.File;

public class QuickDBSetup {
    public static void main(String[] args) {
        System.out.println("=== WealthWise Database Setup ===");
        
        try {
            // Check if database already exists
            File dbFile = new File("mazebank.db");
            if (dbFile.exists()) {
                System.out.println("Database file already exists. Deleting old version...");
                dbFile.delete();
            }
            
            // Create new database
            System.out.println("Creating new mazebank.db database...");
            
            Connection connection = DriverManager.getConnection("jdbc:sqlite:mazebank.db");
            Statement statement = connection.createStatement();
            
            // Create tables
            System.out.println("Creating tables...");
            
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
            System.out.println("Inserting sample data...");
            
            // Admin
            statement.execute("INSERT INTO Admins VALUES ('admin', 'admin123')");
            
            // Clients
            statement.execute("INSERT INTO Clients VALUES ('John', 'Doe', '@John.Doe', 'password123', '2024-01-15')");
            statement.execute("INSERT INTO Clients VALUES ('Jane', 'Smith', '@Jane.Smith', 'password456', '2024-01-20')");
            statement.execute("INSERT INTO Clients VALUES ('Bob', 'Johnson', '@Bob.Johnson', 'password789', '2024-01-25')");
            statement.execute("INSERT INTO Clients VALUES ('Alice', 'Brown', '@Alice.Brown', 'password101', '2024-02-01')");
            statement.execute("INSERT INTO Clients VALUES ('Charlie', 'Wilson', '@Charlie.Wilson', 'password202', '2024-02-05')");
            
            // Wallet Accounts
            statement.execute("INSERT INTO WalletAccounts VALUES ('John Doe', 'WLT001234567', 1500.50, 1000.0)");
            statement.execute("INSERT INTO WalletAccounts VALUES ('Jane Smith', 'WLT001234568', 2300.75, 1000.0)");
            statement.execute("INSERT INTO WalletAccounts VALUES ('Bob Johnson', 'WLT001234569', 850.25, 1000.0)");
            statement.execute("INSERT INTO WalletAccounts VALUES ('Alice Brown', 'WLT001234570', 3200.00, 1000.0)");
            statement.execute("INSERT INTO WalletAccounts VALUES ('Charlie Wilson', 'WLT001234571', 750.80, 1000.0)");
            
            // Savings Accounts
            statement.execute("INSERT INTO SavingsAccounts VALUES ('John Doe', 'SAV001234567', 5000.00, 500.0)");
            statement.execute("INSERT INTO SavingsAccounts VALUES ('Jane Smith', 'SAV001234568', 7500.50, 500.0)");
            statement.execute("INSERT INTO SavingsAccounts VALUES ('Bob Johnson', 'SAV001234569', 3200.75, 500.0)");
            statement.execute("INSERT INTO SavingsAccounts VALUES ('Alice Brown', 'SAV001234570', 9800.25, 500.0)");
            statement.execute("INSERT INTO SavingsAccounts VALUES ('Charlie Wilson', 'SAV001234571', 4500.60, 500.0)");
            
            // Verify data
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM Clients");
            rs.next();
            int clientCount = rs.getInt(1);
            
            rs = statement.executeQuery("SELECT COUNT(*) FROM WalletAccounts");
            rs.next();
            int walletCount = rs.getInt(1);
            
            rs = statement.executeQuery("SELECT COUNT(*) FROM SavingsAccounts");
            rs.next();
            int savingsCount = rs.getInt(1);
            
            System.out.println("Database setup complete!");
            System.out.println("Created " + clientCount + " clients");
            System.out.println("Created " + walletCount + " wallet accounts");
            System.out.println("Created " + savingsCount + " savings accounts");
            System.out.println("Admin login: username='admin', password='admin123'");
            
            rs.close();
            statement.close();
            connection.close();
            
            System.out.println("\n=== Setup Successful ===");
            System.out.println("You can now run your WealthWise application!");
            System.out.println("The client list will show " + clientCount + " clients with full details.");
            
        } catch (Exception e) {
            System.err.println("Error setting up database:");
            e.printStackTrace();
        }
    }
}