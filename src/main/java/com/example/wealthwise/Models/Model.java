package com.example.wealthwise.Models;

import com.example.wealthwise.Views.ViewFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Model {
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;
    private static Model model;
    private boolean clientLoginSuccessFlag;
    private boolean adminLoginSuccessFlag;
    private Client loggedInClient;

    private Consumer<Client> clientCreatedListener;
    private Consumer<Client> clientDeletedListener;
    private Runnable onClientDataRefreshed; // ✅ dashboard refresh callback

    private Model() {
        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }

    // ✅ --- ACCOUNT LOADERS ---
    public WalletAccount getWalletAccount(String owner) {
        try (ResultSet rs = databaseDriver.getWalletAccount(owner)) {
            if (rs != null && rs.next()) {
                return new WalletAccount(
                        owner,
                        rs.getString("account_number"),
                        rs.getDouble("balance"),
                        rs.getInt("transaction_limit"),
                        rs.getString("date_created")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SavingsAccount getSavingsAccount(String owner) {
        try (ResultSet rs = databaseDriver.getSavingsAccount(owner)) {
            if (rs != null && rs.next()) {
                return new SavingsAccount(
                        owner,
                        rs.getString("account_number"),
                        rs.getDouble("balance"),
                        rs.getDouble("transaction_limit"),
                        rs.getString("date_created")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ --- CLIENT MANAGEMENT ---
    public void createNewClient(String firstName, String lastName, String payeeAddress, String password) {
        LocalDate date = LocalDate.now();
        databaseDriver.createClient(firstName, lastName, payeeAddress, password, date);
        // Create accounts for the new client
        String accNum = databaseDriver.getNextAccountNumber();
        databaseDriver.createWalletAccount(payeeAddress, accNum, 0.0, date);
        databaseDriver.createSavingsAccount(payeeAddress, accNum, 0.0, date);
        if (clientCreatedListener != null) {
            WalletAccount wallet = getWalletAccount(payeeAddress);
            SavingsAccount savings = getSavingsAccount(payeeAddress);
            Client newClient = new Client(firstName, lastName, payeeAddress, wallet, savings, date);
            newClient.setTransactionHistory(loadClientTransactions(payeeAddress));
            clientCreatedListener.accept(newClient);
        }
    }

    public boolean deleteClient(Client client) {
        try {
            boolean deleted = databaseDriver.deleteClient(client.getPayeeAddress());
            if (deleted && clientDeletedListener != null) {
                clientDeletedListener.accept(client);
            }
            return deleted;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ --- LOGIN HANDLERS ---
    public void evaluateClientCred(String payeeAddress, String password) {
        ResultSet rs = databaseDriver.getClientsData(payeeAddress, password);
        try {
            clientLoginSuccessFlag = rs != null && rs.next();
            if (clientLoginSuccessFlag) {
                String fName = rs.getString("FirstName");
                String lName = rs.getString("LastName");
                String pAddress = rs.getString("PayeeAddress");
                LocalDate date = LocalDate.parse(rs.getString("Date"));

                WalletAccount wallet = getWalletAccount(pAddress);
                SavingsAccount savings = getSavingsAccount(pAddress);

                loggedInClient = new Client(fName, lName, pAddress, wallet, savings, date);
                loggedInClient.setTransactionHistory(loadClientTransactions(pAddress)); // ✅ load transactions
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clientLoginSuccessFlag = false;
        }
    }

    public void evaluateAdminCred(String username, String password) {
        try (ResultSet rs = databaseDriver.getAdminData(username, password)) {
            adminLoginSuccessFlag = rs != null && rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            adminLoginSuccessFlag = false;
        }
    }

    // ✅ --- FLAGS & LISTENERS ---
    public boolean getClientLoginSuccessFlag() { return clientLoginSuccessFlag; }
    public boolean getAdminLoginSuccessFlag() { return adminLoginSuccessFlag; }
    public Client getLoggedInClient() { return loggedInClient; }

    public void setClientCreatedListener(Consumer<Client> listener) { this.clientCreatedListener = listener; }
    public void setClientDeletedListener(Consumer<Client> listener) { this.clientDeletedListener = listener; }

    public void setOnClientDataRefreshed(Runnable listener) { this.onClientDataRefreshed = listener; }

    // ✅ --- CLIENT UTILITIES ---
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        try (ResultSet rs = databaseDriver.getAllClientsData()) {
            while (rs != null && rs.next()) {
                String fName = rs.getString("FirstName");
                String lName = rs.getString("LastName");
                String pAddress = rs.getString("PayeeAddress");
                LocalDate date = LocalDate.parse(rs.getString("Date"));
                WalletAccount wallet = getWalletAccount(pAddress);
                SavingsAccount savings = getSavingsAccount(pAddress);

                Client client = new Client(fName, lName, pAddress, wallet, savings, date);
                client.setTransactionHistory(loadClientTransactions(pAddress));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public boolean clientExists(String payeeAddress) {
        try (ResultSet rs = databaseDriver.searchClient(payeeAddress)) {
            return rs != null && rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ --- TRANSACTION MANAGEMENT ---
    public boolean transferMoney(String senderAddress, String receiverAddress, double amount, String category, String message) {
        DatabaseDriver db = getDatabaseDriver();
        try {
            db.getConnection().setAutoCommit(false);

            ResultSet senderWallet = db.getWalletAccount(senderAddress);
            if (senderWallet == null || !senderWallet.next()) return false;

            double senderBalance = senderWallet.getDouble("Balance");
            if (senderBalance < amount) return false;

            db.updateWalletBalance(senderAddress, senderBalance - amount);

            ResultSet receiverWallet = db.getWalletAccount(receiverAddress);
            double receiverBalance = receiverWallet != null && receiverWallet.next()
                    ? receiverWallet.getDouble("Balance") : 0.0;
            db.updateWalletBalance(receiverAddress, receiverBalance + amount);

            db.createTransaction(senderAddress, receiverAddress, amount, category, message);

            db.getConnection().commit();
            return true;
        } catch (SQLException e) {
            try { db.getConnection().rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
            return false;
        } finally {
            try { db.getConnection().setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    // ✅ Loads transactions for a specific client
    public List<Transaction> loadClientTransactions(String payeeAddress) {
        List<Transaction> list = new ArrayList<>();
        try (ResultSet rs = databaseDriver.getTransactionsByPayee(payeeAddress)) {
            while (rs != null && rs.next()) {
                String category = rs.getString("Category");
                if (category == null) category = "Transfer"; // default for old records
                String dateStr = rs.getString("Date");
                LocalDate date = null;
                if (dateStr != null) {
                    date = LocalDate.parse(dateStr.split(" ")[0]);
                } else {
                    date = LocalDate.now(); // fallback for old records
                }
                list.add(new Transaction(
                        rs.getString("Sender"),
                        rs.getString("Receiver"),
                        rs.getDouble("Amount"),
                        category,
                        date,
                        rs.getString("Message")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ --- REFRESH DASHBOARD / CLIENT DATA ---
    public void refreshClientData() {
        if (loggedInClient != null) {
            String payee = loggedInClient.getPayeeAddress();
            loggedInClient.walletAccountProperty().set(getWalletAccount(payee));
            loggedInClient.savingsAccountProperty().set(getSavingsAccount(payee));
            loggedInClient.setTransactionHistory(loadClientTransactions(payee)); // ✅ refresh transactions

            if (onClientDataRefreshed != null) onClientDataRefreshed.run();
        }
    }
}
