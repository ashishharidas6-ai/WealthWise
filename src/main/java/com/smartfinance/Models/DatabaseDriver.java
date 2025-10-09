package com.smartfinance.Models;

import java.sql.*;
import java.time.LocalDate;

/**
 * DatabaseDriver - Handles all DB operations for Smart Finance app.
 * Includes auto table creation, client/account management, and transaction/investment support.
 */
public class DatabaseDriver {
    private Connection connection;

    public DatabaseDriver() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:mazebank.db");
            System.out.println("[DatabaseDriver] ✅ Connected to database successfully.");
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            ensureTablesExist();
        } catch (SQLException e) {
            System.err.println("[DatabaseDriver] ❌ Connection failed!");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    // ====================================================
    // TABLE CREATION
    // ====================================================
    private void ensureTablesExist() {
        try (Statement stmt = connection.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Clients (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    FirstName TEXT,
                    LastName TEXT,
                    PayeeAddress TEXT UNIQUE,
                    Password TEXT,
                    Date TEXT
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Admins (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Username TEXT UNIQUE,
                    Password TEXT
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS WalletAccounts (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Owner TEXT,
                    AccountNumber TEXT,
                    Balance REAL,
                    TransactionLimit REAL,
                    date_created TEXT,
                    FOREIGN KEY (Owner) REFERENCES Clients(PayeeAddress) ON DELETE CASCADE
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS SavingsAccounts (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Owner TEXT,
                    AccountNumber TEXT,
                    Balance REAL,
                    TransactionLimit REAL,
                    date_created TEXT,
                    FOREIGN KEY (Owner) REFERENCES Clients(PayeeAddress) ON DELETE CASCADE
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Transactions (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Sender TEXT,
                    Receiver TEXT,
                    Amount REAL,
                    Category TEXT,
                    Message TEXT,
                    Date TEXT
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Investments (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Owner TEXT,
                    InvestmentType TEXT,
                    AmountInvested REAL,
                    CurrentValue REAL,
                    DateInvested TEXT,
                    FOREIGN KEY (Owner) REFERENCES Clients(PayeeAddress) ON DELETE CASCADE
                )
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // ====================================================
    // AUTHENTICATION
    // ====================================================
    public ResultSet getClientsData(String payeeAddress, String password) {
        return executeQuery(
                "SELECT * FROM Clients WHERE PayeeAddress = ? AND Password = ?",
                payeeAddress, password
        );
    }

    public ResultSet getAdminData(String username, String password) {
        return executeQuery(
                "SELECT * FROM Admins WHERE Username = ? AND Password = ?",
                username, password
        );
    }

    // ====================================================
    // ACCOUNT FETCH
    // ====================================================
    public ResultSet getWalletAccount(String owner) {
        return executeQuery("SELECT * FROM WalletAccounts WHERE Owner = ?", owner);
    }

    public ResultSet getSavingsAccount(String owner) {
        return executeQuery("SELECT * FROM SavingsAccounts WHERE Owner = ?", owner);
    }

    // ====================================================
    // ACCOUNT CREATION
    // ====================================================
    public String getNextAccountNumber(String table) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(ID) FROM " + table)) {
            if (rs.next()) {
                int next = rs.getInt(1) + 1;
                return String.format("%04d", next);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0001";
    }

    public void createWalletAccount(String owner, double balance) {
        String accNumber = getNextAccountNumber("WalletAccounts");
        LocalDate date = LocalDate.now();
        executeUpdate(
                "INSERT INTO WalletAccounts (Owner, AccountNumber, Balance, DepositLimit, Date) VALUES (?, ?, ?, ?, ?)",
                owner, accNumber, balance, 10000, date.toString()
        );
    }

    public void createSavingsAccount(String owner, double balance) {
        String accNumber = getNextAccountNumber("SavingsAccounts");
        LocalDate date = LocalDate.now();
        executeUpdate(
                "INSERT INTO SavingsAccounts (Owner, AccountNumber, Balance, TransactionLimit, date_created) VALUES (?, ?, ?, ?, ?)",
                owner, accNumber, balance, 100000, date.toString()
        );
    }

    // ====================================================
    // CLIENT MANAGEMENT
    // ====================================================
    public boolean createClient(String firstName, String lastName, String payeeAddress, String password,
                                double savingsBalance, double walletBalance) {
        LocalDate date = LocalDate.now();
        try {
            connection.setAutoCommit(false);

            executeUpdate(
                    "INSERT INTO Clients (FirstName, LastName, PayeeAddress, Password, Date) VALUES (?, ?, ?, ?, ?)",
                    firstName, lastName, payeeAddress, password, date.toString()
            );

            createWalletAccount(payeeAddress, walletBalance);
            createSavingsAccount(payeeAddress, savingsBalance);

            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    public ResultSet getAllClientsData() {
        return executeQuery("SELECT * FROM Clients");
    }

    public ResultSet searchClients(String searchText) {
        String query = "%" + searchText + "%";
        return executeQuery(
                "SELECT * FROM Clients WHERE PayeeAddress LIKE ? OR FirstName LIKE ? OR LastName LIKE ?",
                query, query, query
        );
    }

    public boolean deleteClient(String payeeAddress) {
        try {
            connection.setAutoCommit(false);

            executeUpdate("DELETE FROM WalletAccounts WHERE Owner = ?", payeeAddress);
            executeUpdate("DELETE FROM SavingsAccounts WHERE Owner = ?", payeeAddress);
            int deleted = executeUpdate("DELETE FROM Clients WHERE PayeeAddress = ?", payeeAddress);

            connection.commit();
            return deleted > 0;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    // ====================================================
    // TRANSACTIONS
    // ====================================================
    public void createTransaction(String sender, String receiver, double amount, String category, String message) {
        executeUpdate(
                "INSERT INTO Transactions (Sender, Receiver, Amount, Category, Message, Date) VALUES (?, ?, ?, ?, ?, datetime('now'))",
                sender, receiver, amount, category, message
        );
    }

    public ResultSet getTransactionsByPayee(String payeeAddress) {
        return executeQuery(
                "SELECT * FROM Transactions WHERE Sender = ? OR Receiver = ? ORDER BY Date DESC",
                payeeAddress, payeeAddress
        );
    }

    public ResultSet getExpendituresByCategory(String payeeAddress) {
        return executeQuery(
                "SELECT Category, SUM(Amount) as Total FROM Transactions WHERE Sender = ? GROUP BY Category",
                payeeAddress
        );
    }

    // ====================================================
    // INVESTMENTS
    // ====================================================
    public ResultSet getInvestments(String owner) {
        return executeQuery("SELECT * FROM Investments WHERE Owner = ? ORDER BY DateInvested DESC", owner);
    }

    public void createInvestment(String owner, String investmentType, double amountInvested, double currentValue) {
        executeUpdate(
                "INSERT INTO Investments (Owner, InvestmentType, AmountInvested, CurrentValue, DateInvested) VALUES (?, ?, ?, ?, datetime('now'))",
                owner, investmentType, amountInvested, currentValue
        );
    }

    public boolean updateInvestmentValue(int id, double newValue) {
        return executeUpdate("UPDATE Investments SET CurrentValue = ? WHERE ID = ?", newValue, id) > 0;
    }

    // ====================================================
    // BALANCE OPERATIONS
    // ====================================================
    public boolean depositToSavings(String owner, double amount) {
        return executeUpdate("UPDATE SavingsAccounts SET Balance = Balance + ? WHERE Owner = ?", amount, owner) > 0;
    }

    public boolean depositToWallet(String owner, double amount) {
        return executeUpdate("UPDATE WalletAccounts SET Balance = Balance + ? WHERE Owner = ?", amount, owner) > 0;
    }

    public void updateSavingsBalance(String owner, double newBalance) {
        executeUpdate("UPDATE SavingsAccounts SET Balance = ? WHERE Owner = ?", newBalance, owner);
    }

    public void updateWalletBalance(String owner, double newBalance) {
        executeUpdate("UPDATE WalletAccounts SET Balance = ? WHERE Owner = ?", newBalance, owner);
    }

    public boolean updateBalance(String table, String owner, double newBalance) {
        String sql = "UPDATE " + table + " SET Balance = ? WHERE Owner = ?";
        return executeUpdate(sql, newBalance, owner) > 0;
    }

    // ====================================================
    // HELPER METHODS
    // ====================================================
    private ResultSet executeQuery(String sql, Object... params) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                stmt.setObject(i + 1, params[i]);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int executeUpdate(String sql, Object... params) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++)
                stmt.setObject(i + 1, params[i]);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


}
