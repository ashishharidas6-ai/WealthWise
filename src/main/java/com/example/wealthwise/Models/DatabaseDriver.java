package com.example.wealthwise.Models;

import java.sql.*;

public class DatabaseDriver {
    private Connection connection;

    public DatabaseDriver() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:mazebank.db");
            System.out.println("[DatabaseDriver] Connected to database successfully.");
            ensureTransactionsTableColumns();
        } catch (SQLException e) {
            System.err.println("[DatabaseDriver] Connection failed!");
            e.printStackTrace();
        }
    }

    private void ensureTransactionsTableColumns() {
        try {
            connection.createStatement().execute("ALTER TABLE Transactions ADD COLUMN Category TEXT");
        } catch (SQLException ignored) {}
        try {
            connection.createStatement().execute("ALTER TABLE Transactions ADD COLUMN Date TEXT");
        } catch (SQLException ignored) {}
    }

    public Connection getConnection() {
        return connection;
    }

    // ===============================
    // LOGIN
    // ===============================
    public ResultSet getClientsData(String payeeAddress, String password) {
        try {
            String query = "SELECT * FROM Clients WHERE PayeeAddress = ? AND Password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, payeeAddress);
            stmt.setString(2, password);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getAdminData(String username, String password) {
        try {
            String query = "SELECT * FROM Admin WHERE Username = ? AND Password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===============================
    // ACCOUNT FETCH
    // ===============================
    public ResultSet getWalletAccount(String owner) {
        try {
            String query = "SELECT * FROM WalletAccounts WHERE Owner = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, owner);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getSavingsAccount(String owner) {
        try {
            String query = "SELECT * FROM SavingsAccounts WHERE Owner = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, owner);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===============================
    // ACCOUNT CREATION
    // ===============================
    public String getNextAccountNumber() {
        try {
            String query = "SELECT COUNT(*) FROM WalletAccounts";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                int count = rs.getInt(1);
                return String.format("%04d", count + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0001";
    }

    public void createWalletAccount(String owner, String accNumber, double balance, java.time.LocalDate dateCreated) {
        try {
            String query = "INSERT INTO WalletAccounts (Owner, AccNumber, Balance, TransactionLimit, date_created) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, owner);
            stmt.setString(2, accNumber);
            stmt.setDouble(3, balance);
            stmt.setInt(4, 1000);
            stmt.setString(5, dateCreated.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createSavingsAccount(String owner, String accNumber, double balance, java.time.LocalDate dateCreated) {
        try {
            String query = "INSERT INTO SavingsAccounts (Owner, AccNumber, Balance, TransactionLimit, date_created) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, owner);
            stmt.setString(2, accNumber);
            stmt.setDouble(3, balance);
            stmt.setDouble(4, 10000.0);
            stmt.setString(5, dateCreated.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===============================
    // GET ACCOUNT CREATION DATE
    // ===============================
    public java.time.LocalDate getWalletAccountCreationDate(String owner) {
        try {
            String query = "SELECT date_created FROM WalletAccounts WHERE Owner = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, owner);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return java.time.LocalDate.parse(rs.getString("date_created"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public java.time.LocalDate getSavingsAccountCreationDate(String owner) {
        try {
            String query = "SELECT date_created FROM SavingsAccounts WHERE Owner = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, owner);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return java.time.LocalDate.parse(rs.getString("date_created"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===============================
    // CLIENT MANAGEMENT
    // ===============================
    public ResultSet getAllClientsData() {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery("SELECT * FROM Clients");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet searchClient(String payeeAddress) {
        try {
            String query = "SELECT * FROM Clients WHERE PayeeAddress LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + payeeAddress + "%");
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteClient(String payeeAddress) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Clients WHERE PayeeAddress = ?");
            stmt.setString(1, payeeAddress);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createClient(String firstName, String lastName, String payeeAddress, String password, java.time.LocalDate date) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO Clients (FirstName, LastName, PayeeAddress, Password, Date) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, payeeAddress);
            stmt.setString(4, password);
            stmt.setString(5, date.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===============================
    // DEPOSIT & TRANSACTIONS
    // ===============================
    public boolean depositToWallet(String payeeAddress, double amount) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE WalletAccounts SET Balance = Balance + ? WHERE Owner = ?");
            stmt.setDouble(1, amount);
            stmt.setString(2, payeeAddress);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean depositToSavings(String payeeAddress, double amount) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE SavingsAccounts SET Balance = Balance + ? WHERE Owner = ?");
            stmt.setDouble(1, amount);
            stmt.setString(2, payeeAddress);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateWalletBalance(String payeeAddress, double newBalance) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE WalletAccounts SET Balance = ? WHERE Owner = ?");
            stmt.setDouble(1, newBalance);
            stmt.setString(2, payeeAddress);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTransaction(String sender, String receiver, double amount, String category, String message) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO Transactions (Sender, Receiver, Amount, Category, Message, Date) VALUES (?, ?, ?, ?, ?, datetime('now'))"
            );
            stmt.setString(1, sender);
            stmt.setString(2, receiver);
            stmt.setDouble(3, amount);
            stmt.setString(4, category);
            stmt.setString(5, message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getTransactionsByPayee(String payeeAddress) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM Transactions WHERE Sender = ? OR Receiver = ? ORDER BY Date DESC"
            );
            stmt.setString(1, payeeAddress);
            stmt.setString(2, payeeAddress);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
