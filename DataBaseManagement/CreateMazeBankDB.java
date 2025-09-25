import java.sql.*;

public class CreateMazeBankDB {
    public static void main(String[] args) {
        try {
            // Create connection to SQLite database (will create file if it doesn't exist)
            Connection connection = DriverManager.getConnection("jdbc:sqlite:mazebank.db");
            Statement statement = connection.createStatement();
            
            System.out.println("Creating mazebank.db database...");
            
            // Create Clients table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS Clients (" +
                "FirstName TEXT NOT NULL, " +
                "LastName TEXT NOT NULL, " +
                "PayeeAddress TEXT NOT NULL UNIQUE, " +
                "Password TEXT NOT NULL, " +
                "Date TEXT NOT NULL)"
            );
            
            // Create WalletAccounts table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS WalletAccounts (" +
                "Owner TEXT NOT NULL, " +
                "AccountNumber TEXT NOT NULL UNIQUE, " +
                "Balance REAL NOT NULL DEFAULT 0.0, " +
                "TransactionLimit REAL NOT NULL DEFAULT 1000.0)"
            );
            
            // Create SavingsAccounts table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS SavingsAccounts (" +
                "Owner TEXT NOT NULL, " +
                "AccountNumber TEXT NOT NULL UNIQUE, " +
                "Balance REAL NOT NULL DEFAULT 0.0, " +
                "WithdrawalLimit REAL NOT NULL DEFAULT 500.0)"
            );
            
            // Create Admins table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS Admins (" +
                "Username TEXT NOT NULL UNIQUE, " +
                "Password TEXT NOT NULL)"
            );
            
            System.out.println("Tables created successfully!");
            
            // Insert sample admin
            statement.execute(
                "INSERT OR IGNORE INTO Admins (Username, Password) " +
                "VALUES ('admin', 'admin123')"
            );
            
            // Insert sample clients
            statement.execute(
                "INSERT OR IGNORE INTO Clients (FirstName, LastName, PayeeAddress, Password, Date) " +
                "VALUES ('John', 'Doe', 'john.doe@email.com', 'password123', '2024-01-15')"
            );
            
            statement.execute(
                "INSERT OR IGNORE INTO Clients (FirstName, LastName, PayeeAddress, Password, Date) " +
                "VALUES ('Jane', 'Smith', 'jane.smith@email.com', 'password456', '2024-01-20')"
            );
            
            statement.execute(
                "INSERT OR IGNORE INTO Clients (FirstName, LastName, PayeeAddress, Password, Date) " +
                "VALUES ('Bob', 'Johnson', 'bob.johnson@email.com', 'password789', '2024-01-25')"
            );
            
            // Insert corresponding wallet accounts
            statement.execute(
                "INSERT OR IGNORE INTO WalletAccounts (Owner, AccountNumber, Balance, TransactionLimit) " +
                "VALUES ('John Doe', 'WLT001234567', 1500.50, 1000.0)"
            );
            
            statement.execute(
                "INSERT OR IGNORE INTO WalletAccounts (Owner, AccountNumber, Balance, TransactionLimit) " +
                "VALUES ('Jane Smith', 'WLT001234568', 2300.75, 1000.0)"
            );
            
            statement.execute(
                "INSERT OR IGNORE INTO WalletAccounts (Owner, AccountNumber, Balance, TransactionLimit) " +
                "VALUES ('Bob Johnson', 'WLT001234569', 850.25, 1000.0)"
            );
            
            // Insert corresponding savings accounts
            statement.execute(
                "INSERT OR IGNORE INTO SavingsAccounts (Owner, AccountNumber, Balance, WithdrawalLimit) " +
                "VALUES ('John Doe', 'SAV001234567', 5000.00, 500.0)"
            );
            
            statement.execute(
                "INSERT OR IGNORE INTO SavingsAccounts (Owner, AccountNumber, Balance, WithdrawalLimit) " +
                "VALUES ('Jane Smith', 'SAV001234568', 7500.50, 500.0)"
            );
            
            statement.execute(
                "INSERT OR IGNORE INTO SavingsAccounts (Owner, AccountNumber, Balance, WithdrawalLimit) " +
                "VALUES ('Bob Johnson', 'SAV001234569', 3200.75, 500.0)"
            );
            
            System.out.println("Sample data inserted successfully!");
            
            // Verify data
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM Clients");
            if (rs.next()) {
                System.out.println("Total clients in database: " + rs.getInt(1));
            }
            
            rs = statement.executeQuery("SELECT COUNT(*) FROM WalletAccounts");
            if (rs.next()) {
                System.out.println("Total wallet accounts: " + rs.getInt(1));
            }
            
            rs = statement.executeQuery("SELECT COUNT(*) FROM SavingsAccounts");
            if (rs.next()) {
                System.out.println("Total savings accounts: " + rs.getInt(1));
            }
            
            // Close connections
            rs.close();
            statement.close();
            connection.close();
            
            System.out.println("mazebank.db database created successfully!");
            
        } catch (Exception e) {
            System.err.println("Error creating database:");
            e.printStackTrace();
        }
    }
}