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

            // Enable foreign key enforcement for SQLite (affects new tables and FK behavior)
            try (Statement pragma = conn.createStatement()) {
                pragma.execute("PRAGMA foreign_keys = ON");
            }

            createTablesIfNotExist();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTablesIfNotExist() {
        try (Statement statement = conn.createStatement()) {
            // Clients
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS Clients (" +
                            "FirstName TEXT NOT NULL, " +
                            "LastName TEXT NOT NULL, " +
                            "PayeeAddress TEXT NOT NULL UNIQUE, " +
                            "Password TEXT NOT NULL, " +
                            "Date TEXT NOT NULL)"
            );

            // WalletAccounts with FK -> Clients (ON DELETE CASCADE for future DBs)
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS WalletAccounts (" +
                            "Owner TEXT NOT NULL, " +
                            "AccountNumber TEXT NOT NULL UNIQUE, " +
                            "Balance REAL NOT NULL DEFAULT 0.0, " +
                            "CreationDate TEXT NOT NULL, " +
                            "FOREIGN KEY(Owner) REFERENCES Clients(PayeeAddress) ON DELETE CASCADE)"
            );

            // SavingsAccount with FK -> Clients (ON DELETE CASCADE for future DBs)
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS SavingsAccount (" +
                            "Owner TEXT NOT NULL, " +
                            "AccountNumber TEXT NOT NULL UNIQUE, " +
                            "Balance REAL NOT NULL DEFAULT 0.0, " +
                            "CreationDate TEXT NOT NULL, " +
                            "FOREIGN KEY(Owner) REFERENCES Clients(PayeeAddress) ON DELETE CASCADE)"
            );

            // Admins
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS Admins (" +
                            "Username TEXT NOT NULL UNIQUE, " +
                            "Password TEXT NOT NULL)"
            );

            // Transactions (kept without FK so history is preserved; you can change if desired)
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS Transactions (" +
                            "Sender TEXT NOT NULL, " +
                            "Receiver TEXT NOT NULL, " +
                            "Amount REAL NOT NULL, " +
                            "Message TEXT, " +
                            "Date TEXT NOT NULL)"
            );

            // Budgets with FK -> Clients (ON DELETE CASCADE for future DBs)
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS Budgets (" +
                            "Owner TEXT NOT NULL, " +
                            "BudgetName TEXT NOT NULL, " +
                            "BudgetAmount REAL NOT NULL, " +
                            "BudgetSpent REAL NOT NULL DEFAULT 0.0, " +
                            "CreationDate TEXT NOT NULL, " +
                            "FOREIGN KEY(Owner) REFERENCES Clients(PayeeAddress) ON DELETE CASCADE)"
            );

            // Insert sample admin
            statement.execute(
                    "INSERT OR IGNORE INTO Admins (Username, Password) " +
                            "VALUES ('admin', 'admin123')"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* =====================
       Client Section
    ====================== */

    public ResultSet getClientsData(String pAddress, String password) {
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM Clients WHERE PayeeAddress = ? AND Password = ?");
            statement.setString(1, pAddress);
            statement.setString(2, password);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getAllClientsData() {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Clients");
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createClient(String fName, String lName, String pAddress, String password, LocalDate date) {
        String sql = "INSERT INTO Clients (FirstName, LastName, PayeeAddress, Password, Date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
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
        String sql = "INSERT INTO WalletAccount (owner, account_number, transaction_limit, balance, date_created) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, owner);
            statement.setString(2, accNum);
            statement.setInt(3, 1000); // default transaction limit
            statement.setDouble(4, balance);
            statement.setString(5, creationDate.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createSavingsAccount(String owner, String accNum, double balance, LocalDate creationDate) {
        String sql = "INSERT INTO SavingsAccount (owner, account_number, transaction_limit, balance, date_created) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, owner);
            statement.setString(2, accNum);
            statement.setDouble(3, 500.0); // default withdrawal limit
            statement.setDouble(4, balance);
            statement.setString(5, creationDate.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getWalletAccount(String owner) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM WalletAccount WHERE owner = ?");
            statement.setString(1, owner);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getSavingsAccount(String owner) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM SavingsAccount WHERE owner = ?");
            statement.setString(1, owner);
            ResultSet rs = statement.executeQuery();
            System.out.println("Get Savings Account: owner=" + owner);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateClientData(String pAddress, String fName, String lName, String password) {
        String sql = "UPDATE Clients SET FirstName = ?, LastName = ?, Password = ? WHERE PayeeAddress = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
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
        String sql = "UPDATE WalletAccounts SET Balance = ? WHERE Owner = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setDouble(1, balance);
            statement.setString(2, pAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateSavingsBalance(String pAddress, double balance) {
        // First check if row exists
        String checkSql = "SELECT COUNT(*) FROM SavingsAccount WHERE Owner = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, pAddress);
            ResultSet rs = checkStmt.executeQuery();
            int count = rs.next() ? rs.getInt(1) : 0;
            System.out.println("Check Savings Account exists: pAddress=" + pAddress + ", count=" + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "UPDATE SavingsAccount SET Balance = ? WHERE Owner = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setDouble(1, balance);
            statement.setString(2, pAddress);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Update Savings Balance: pAddress=" + pAddress + ", balance=" + balance + ", rowsAffected=" + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- FIXED deleteClient: delete dependent rows first (wallets, savings, budgets, transactions) ----------
    public void deleteClient(String pAddress) {
        String deleteWallets = "DELETE FROM WalletAccounts WHERE Owner = ?";
        String deleteSavings = "DELETE FROM SavingsAccount WHERE Owner = ?";
        String deleteBudgets = "DELETE FROM Budgets WHERE Owner = ?";
        String deleteTransactions = "DELETE FROM Transactions WHERE Sender = ? OR Receiver = ?";
        String deleteClient = "DELETE FROM Clients WHERE PayeeAddress = ?";

        try {
            // start transaction
            conn.setAutoCommit(false);

            try (PreparedStatement stWallet = conn.prepareStatement(deleteWallets);
                 PreparedStatement stSavings = conn.prepareStatement(deleteSavings);
                 PreparedStatement stBudgets = conn.prepareStatement(deleteBudgets);
                 PreparedStatement stTrans = conn.prepareStatement(deleteTransactions);
                 PreparedStatement stClient = conn.prepareStatement(deleteClient)) {

                stWallet.setString(1, pAddress);
                stWallet.executeUpdate();

                stSavings.setString(1, pAddress);
                stSavings.executeUpdate();

                stBudgets.setString(1, pAddress);
                stBudgets.executeUpdate();

                stTrans.setString(1, pAddress);
                stTrans.setString(2, pAddress);
                stTrans.executeUpdate();

                stClient.setString(1, pAddress);
                int rowsDeleted = stClient.executeUpdate();

                conn.commit();

                if (rowsDeleted == 0) {
                    System.out.println("deleteClient: no client found with PayeeAddress = " + pAddress);
                } else {
                    System.out.println("deleteClient: removed client and related records for " + pAddress);
                }
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWalletAccount(String pAddress) {
        String sql = "DELETE FROM WalletAccounts WHERE Owner = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, pAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSavingsAccount(String pAddress) {
        String sql = "DELETE FROM SavingsAccount WHERE Owner = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, pAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet searchClient(String pAddress) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Clients WHERE PayeeAddress = ?");
            statement.setString(1, pAddress);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* =====================
       Admin Section
    ====================== */

    public ResultSet getAdminData(String username, String password) {
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM Admins WHERE Username = ? AND Password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createTransaction(String sender, String receiver, double amount, String message) {
        String sql = "INSERT INTO Transactions (Sender, Receiver, Amount, Message, Date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, sender);
            statement.setString(2, receiver);
            statement.setDouble(3, amount);
            statement.setString(4, message);
            statement.setString(5, LocalDate.now().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAllTransactions() {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Transactions");
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* =====================
       Budgets Section
    ====================== */

    public void addBudget(String owner, String budgetName, double budgetAmount, double budgetSpent, LocalDate creationDate) {
        String sql = "INSERT INTO Budgets (Owner, BudgetName, BudgetAmount, BudgetSpent, CreationDate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
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
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Budgets WHERE Owner = ?");
            statement.setString(1, owner);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateBudget(String owner, String budgetName, double budgetAmount, double budgetSpent) {
        String sql = "UPDATE Budgets SET BudgetAmount = ?, BudgetSpent = ? WHERE Owner = ? AND BudgetName = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
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
        String sql = "DELETE FROM Budgets WHERE Owner = ? AND BudgetName = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, owner);
            statement.setString(2, budgetName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* =====================
       Search Clients
    ====================== */

    public List<Client> searchClients(String search) {
        List<Client> clients = new ArrayList<>();
        String query =
                "SELECT c.FirstName, c.LastName, c.PayeeAddress, c.Date, " +
                        "wa.AccountNumber AS WalletAccountNumber, wa.Balance AS WalletBalance, wa.CreationDate AS WalletCreationDate, " +
                        "sa.AccountNumber AS SavingsAccountNumber, sa.Balance AS SavingsBalance, sa.CreationDate AS SavingsCreationDate " +
                        "FROM Clients c " +
                        "LEFT JOIN WalletAccounts wa ON c.PayeeAddress = wa.Owner " +
                        "LEFT JOIN SavingsAccounts sa ON c.PayeeAddress = sa.Owner " +
                        "WHERE c.PayeeAddress LIKE ? OR c.FirstName LIKE ? OR c.LastName LIKE ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            String searchPattern = "%" + search + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String fName = resultSet.getString("FirstName");
                    String lName = resultSet.getString("LastName");
                    String pAddress = resultSet.getString("PayeeAddress");
                    LocalDate date = LocalDate.parse(resultSet.getString("Date"));

                    WalletAccount wallet = new WalletAccount(
                            pAddress,
                            resultSet.getString("WalletAccountNumber"),
                            resultSet.getDouble("WalletBalance"),
                            1000,
                            resultSet.getString("WalletCreationDate")
                    );
                    SavingsAccount savings = new SavingsAccount(
                            pAddress,
                            resultSet.getString("SavingsAccountNumber"),
                            resultSet.getDouble("SavingsBalance"),
                            500,
                            resultSet.getString("SavingsCreationDate")
                    );
                    clients.add(new Client(fName, lName, pAddress, wallet, savings, date));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public boolean depositToWallet(String payeeAddress, double amount) {
        String query = "UPDATE WalletAccounts SET Balance = Balance + ? WHERE Owner = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setDouble(1, amount);
            statement.setString(2, payeeAddress);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean depositToSavings(String payeeAddress, double amount) {
        String query = "UPDATE SavingsAccounts SET Balance = Balance + ? WHERE Owner = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setDouble(1, amount);
            statement.setString(2, payeeAddress);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Client> searchClientsByPayeeAddress(String payeeAddress) {
        List<Client> clients = new ArrayList<>();
        String query =
                "SELECT c.FirstName, c.LastName, c.PayeeAddress, c.Date, " +
                        "wa.AccountNumber AS WalletAccountNumber, wa.Balance AS WalletBalance, wa.CreationDate AS WalletCreationDate, " +
                        "sa.AccountNumber AS SavingsAccountNumber, sa.Balance AS SavingsBalance, sa.CreationDate AS SavingsCreationDate " +
                        "FROM Clients c " +
                        "LEFT JOIN WalletAccounts wa ON c.PayeeAddress = wa.Owner " +
                        "LEFT JOIN SavingsAccounts sa ON c.PayeeAddress = sa.Owner " +
                        "WHERE c.PayeeAddress = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, payeeAddress);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String fName = resultSet.getString("FirstName");
                    String lName = resultSet.getString("LastName");
                    String pAddress = resultSet.getString("PayeeAddress");
                    LocalDate date = LocalDate.parse(resultSet.getString("Date"));

                    WalletAccount wallet = new WalletAccount(
                            pAddress,
                            resultSet.getString("WalletAccountNumber"),
                            resultSet.getDouble("WalletBalance"),
                            1000,
                            resultSet.getString("WalletCreationDate")
                    );
                    SavingsAccount savings = new SavingsAccount(
                            pAddress,
                            resultSet.getString("SavingsAccountNumber"),
                            resultSet.getDouble("SavingsBalance"),
                            500,
                            resultSet.getString("SavingsCreationDate")
                    );
                    clients.add(new Client(fName, lName, pAddress, wallet, savings, date));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
    public void getClientData(String payeeAddress) {
        String query =
                "SELECT c.FirstName, c.LastName, c.PayeeAddress, c.Date, " +
                        "wa.AccountNumber AS WalletAccountNumber, wa.Balance AS WalletBalance, wa.CreationDate AS WalletCreationDate, " +
                        "sa.AccountNumber AS SavingsAccountNumber, sa.Balance AS SavingsBalance, sa.CreationDate AS SavingsCreationDate " +
                        "FROM Clients c " +
                        "LEFT JOIN WalletAccounts wa ON c.PayeeAddress = wa.Owner " +
                        "LEFT JOIN SavingsAccounts sa ON c.PayeeAddress = sa.Owner " +
                        "WHERE c.PayeeAddress = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, payeeAddress);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String fName = resultSet.getString("FirstName");
                    String lName = resultSet.getString("LastName");
                    String pAddress = resultSet.getString("PayeeAddress");
                    LocalDate date = LocalDate.parse(resultSet.getString("Date"));

                    WalletAccount wallet = new WalletAccount(
                            pAddress,
                            resultSet.getString("WalletAccountNumber"),
                            resultSet.getDouble("WalletBalance"),
                            1000,
                            resultSet.getString("WalletCreationDate")
                    );
                    SavingsAccount savings = new SavingsAccount(
                            pAddress,
                            resultSet.getString("SavingsAccountNumber"),
                            resultSet.getDouble("SavingsBalance"),
                            500,
                            resultSet.getString("SavingsCreationDate")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
