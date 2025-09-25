package com.example.wealthwise.Models;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDriver {
    private Connection conn;

    public DatabaseDriver() {
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:mazebank.db");
            createTablesIfNotExist();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTablesIfNotExist() {
        try (Statement statement = conn.createStatement()) {
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
                "CreationDate TEXT NOT NULL)"
            );

            // Create SavingsAccounts table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS SavingsAccounts (" +
                "Owner TEXT NOT NULL, " +
                "AccountNumber TEXT NOT NULL UNIQUE, " +
                "Balance REAL NOT NULL DEFAULT 0.0, " +
                "CreationDate TEXT NOT NULL)"
            );

            // Create Admine table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS Admine (" +
                "Username TEXT NOT NULL UNIQUE, " +
                "Password TEXT NOT NULL)"
            );

            // Create Transactions table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS Transactions (" +
                "Sender TEXT NOT NULL, " +
                "Receiver TEXT NOT NULL, " +
                "Amount REAL NOT NULL, " +
                "Message TEXT)"
            );

            // Create Budgets table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS Budgets (" +
                "Owner TEXT NOT NULL, " +
                "BudgetName TEXT NOT NULL, " +
                "BudgetAmount REAL NOT NULL, " +
                "BudgetSpent REAL NOT NULL DEFAULT 0.0, " +
                "CreationDate TEXT NOT NULL)"
            );

            // Insert sample admin if not exists
            statement.execute(
                "INSERT OR IGNORE INTO Admine (Username, Password) " +
                "VALUES ('admin', 'admin123')"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     *Client Section
     */
    public ResultSet getClientsData(String pAddress, String password) {
        PreparedStatement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.prepareStatement("SELECT * FROM Clients WHERE PayeeAddress = ? AND Password = ?");
            statement.setString(1, pAddress);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getAllClientsData() {
        PreparedStatement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.prepareStatement("SELECT * FROM Clients");
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void createClient(String fName, String lName, String pAddress, String password, LocalDate date) {
        PreparedStatement statement;
        try {
            statement = this.conn.prepareStatement("INSERT INTO " +
                    "Clients (FirstName, LastName, PayeeAddress, Password, Date)" +
                    "VALUES (?, ?, ?, ?, ?);");
            statement.setString(1, fName);
            statement.setString(2, lName);
            statement.setString(3, pAddress);
            statement.setString(4, password);
            statement.setString(5, date.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createWalletAccount(String owner, String accNum, double balance, LocalDate creationDate) {
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement("INSERT INTO WalletAccounts (Owner, AccountNumber, Balance, CreationDate) VALUES (?, ?, ?, ?)");
            statement.setString(1, owner);
            statement.setString(2, accNum);
            statement.setDouble(3, balance);
            statement.setString(4, creationDate.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createSavingsAccount(String owner, String accNum, double balance, LocalDate creationDate) {
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement("INSERT INTO SavingsAccounts (Owner, AccountNumber, Balance, CreationDate) VALUES (?, ?, ?, ?)");
            statement.setString(1, owner);
            statement.setString(2, accNum);
            statement.setDouble(3, balance);
            statement.setString(4, creationDate.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getWalletAccount(String owner) {
        PreparedStatement statement;
        ResultSet resultSet = null;
        try {
            statement = conn.prepareStatement("SELECT * FROM WalletAccounts WHERE Owner = ?");
            statement.setString(1, owner);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getSavingsAccount(String owner) {
        PreparedStatement statement;
        ResultSet resultSet = null;
        try {
            statement = conn.prepareStatement("SELECT * FROM SavingsAccounts WHERE Owner = ?");
            statement.setString(1, owner);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void updateClientData(String pAddress, String fName, String lName, String password) {
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement("UPDATE Clients SET FirstName = ?, LastName = ?, Password = ? WHERE PayeeAddress = ?");
            statement.setString(1, fName);
            statement.setString(2, lName);
            statement.setString(3, password);
            statement.setString(4, pAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWalletBalance(String pAddress, double balance) {
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement("UPDATE WalletAccounts SET Balance = ? WHERE Owner = ?");
            statement.setDouble(1, balance);
            statement.setString(2, pAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSavingsBalance(String pAddress, double balance) {
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement("UPDATE SavingsAccounts SET Balance = ? WHERE Owner = ?");
            statement.setDouble(1, balance);
            statement.setString(2, pAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClient(String pAddress) {
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement("DELETE FROM Clients WHERE PayeeAddress = ?");
            statement.setString(1, pAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWalletAccount(String pAddress) {
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement("DELETE FROM WalletAccounts WHERE Owner = ?");
            statement.setString(1, pAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSavingsAccount(String pAddress) {
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement("DELETE FROM SavingsAccounts WHERE Owner = ?");
            statement.setString(1, pAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet searchClient(String pAddress) {
        PreparedStatement statement;
        ResultSet resultSet = null;
        try {
            statement = conn.prepareStatement("SELECT * FROM Clients WHERE PayeeAddress = ?");
            statement.setString(1, pAddress);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /*
     *Admin Section
     */
    public ResultSet getAdminData(String username, String password) {
        PreparedStatement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.prepareStatement("SELECT * FROM Admins WHERE Username = ? AND Password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void createTransaction(String sender, String receiver, double amount, String message) {
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement("INSERT INTO Transactions (Sender, Receiver, Amount, Message) VALUES (?, ?, ?, ?)");
            statement.setString(1, sender);
            statement.setString(2, receiver);
            statement.setDouble(3, amount);
            statement.setString(4, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAllTransactions() {
        PreparedStatement statement;
        ResultSet resultSet = null;
        try {
            statement = conn.prepareStatement("SELECT * FROM Transactions");
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void addBudget(String owner, String budgetName, double budgetAmount, double budgetSpent, LocalDate creationDate) {
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement("INSERT INTO Budgets (Owner, BudgetName, BudgetAmount, BudgetSpent, CreationDate) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, owner);
            statement.setString(2, budgetName);
            statement.setDouble(3, budgetAmount);
            statement.setDouble(4, budgetSpent);
            statement.setString(5, creationDate.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getBudgets(String owner) {
        PreparedStatement statement;
        ResultSet resultSet = null;
        try {
            statement = conn.prepareStatement("SELECT * FROM Budgets WHERE Owner = ?");
            statement.setString(1, owner);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void updateBudget(String owner, String budgetName, double budgetAmount, double budgetSpent) {
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement("UPDATE Budgets SET BudgetAmount = ?, BudgetSpent = ? WHERE Owner = ? AND BudgetName = ?");
            statement.setDouble(1, budgetAmount);
            statement.setDouble(2, budgetSpent);
            statement.setString(3, owner);
            statement.setString(4, budgetName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBudget(String owner, String budgetName) {
        PreparedStatement statement;
        try {
            statement = conn.prepareStatement("DELETE FROM Budgets WHERE Owner = ? AND BudgetName = ?");
            statement.setString(1, owner);
            statement.setString(2, budgetName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Client> searchClients(String search) {
        List<Client> clients = new ArrayList<>();
        try {
            String query = "SELECT c.FirstName, c.LastName, c.PayeeAddress, c.Date, wa.AccountNumber AS WalletAccountNumber, wa.Balance AS WalletBalance, sa.AccountNumber AS SavingsAccountNumber, sa.Balance AS SavingsBalance " +
                    "FROM Clients c " +
                    "LEFT JOIN WalletAccounts wa ON c.PayeeAddress = wa.Owner " +
                    "LEFT JOIN SavingsAccounts sa ON c.PayeeAddress = sa.Owner " +
                    "WHERE c.PayeeAddress LIKE ? OR c.FirstName LIKE ? OR c.LastName LIKE ?";
            PreparedStatement statement = conn.prepareStatement(query);
            String searchPattern = "%" + search + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String fName = resultSet.getString("FirstName");
                String lName = resultSet.getString("LastName");
                String pAddress = resultSet.getString("PayeeAddress");
                LocalDate date = LocalDate.parse(resultSet.getString("Date"));
                WalletAccount wallet = new WalletAccount(pAddress, resultSet.getString("WalletAccountNumber"), resultSet.getDouble("WalletBalance"), (int) 1000.0);
                SavingsAccount savings = new SavingsAccount(pAddress, resultSet.getString("SavingsAccountNumber"), resultSet.getDouble("SavingsBalance"), 500.0);
                clients.add(new Client(fName, lName, pAddress, wallet, savings, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public boolean depositToWallet(String payeeAddress, double amount) {
        try {
            String query = "UPDATE WalletAccounts SET Balance = Balance + ? WHERE Owner = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setDouble(1, amount);
            statement.setString(2, payeeAddress);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean depositToSavings(String payeeAddress, double amount) {
        try {
            String query = "UPDATE SavingsAccounts SET Balance = Balance + ? WHERE Owner = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setDouble(1, amount);
            statement.setString(2, payeeAddress);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Client> searchClientsByPayeeAddress(String payeeAddress) {
        List<Client> clients = new ArrayList<>();
        try {
            String query = "SELECT c.FirstName, c.LastName, c.PayeeAddress, c.Date, wa.AccountNumber AS WalletAccountNumber, wa.Balance AS WalletBalance, sa.AccountNumber AS SavingsAccountNumber, sa.Balance AS SavingsBalance " +
                    "FROM Clients c " +
                    "LEFT JOIN WalletAccounts wa ON c.PayeeAddress = wa.Owner " +
                    "LEFT JOIN SavingsAccounts sa ON c.PayeeAddress = sa.Owner " +
                    "WHERE c.PayeeAddress = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, payeeAddress);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String fName = resultSet.getString("FirstName");
                String lName = resultSet.getString("LastName");
                String pAddress = resultSet.getString("PayeeAddress");
                LocalDate date = LocalDate.parse(resultSet.getString("Date"));
                WalletAccount wallet = new WalletAccount(pAddress, resultSet.getString("WalletAccountNumber"), resultSet.getDouble("WalletBalance"), (int) 1000.0);
                SavingsAccount savings = new SavingsAccount(pAddress, resultSet.getString("SavingsAccountNumber"), resultSet.getDouble("SavingsBalance"), 500.0);
                clients.add(new Client(fName, lName, pAddress, wallet, savings, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
}
