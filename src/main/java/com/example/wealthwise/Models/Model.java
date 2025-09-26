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
   public WalletAccount getWalletAccount(String owner) {
       DatabaseDriver databaseDriver = getDatabaseDriver();
       try (ResultSet accountData = databaseDriver.getWalletAccount(owner)) {
           if (accountData != null && accountData.next()) {
               String accNumber = accountData.getString("account_number");
               double balance = accountData.getDouble("balance");
               int transactionLimit = accountData.getInt("transaction_limit");
               String dateCreated = accountData.getString("date_created");
               return new WalletAccount(owner, accNumber, balance, transactionLimit, dateCreated);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return null;
   }

   public SavingsAccount getSavingsAccount(String owner) {
       DatabaseDriver databaseDriver = getDatabaseDriver();
       try (ResultSet accountData = databaseDriver.getSavingsAccount(owner)) {
           if (accountData != null && accountData.next()) {
               String accNumber = accountData.getString("account_number");
               double balance = accountData.getDouble("balance");
               double withdrawalLimit = accountData.getDouble("transaction_limit");
               String dateCreated = accountData.getString("date_created");
               return new SavingsAccount(owner, accNumber, balance, withdrawalLimit, dateCreated);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return null;
   }

   public boolean deleteClient(Client client) {
       DatabaseDriver databaseDriver = getDatabaseDriver();
       String payeeAddress = client.getPayeeAddress();
       try {
           databaseDriver.deleteClient(payeeAddress);
           // Notify listener after successful deletion
           if (clientDeletedListener != null) {
               clientDeletedListener.accept(client);
           }
           return true;
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

   public void createNewClient(String firstName, String lastName, String payeeAddress, String password) {
       LocalDate date = LocalDate.now();
       databaseDriver.createClient(firstName, lastName, payeeAddress, password, date);
       // Create wallet account
       String walletAccNum = payeeAddress + "_wallet";
       databaseDriver.createWalletAccount(payeeAddress, walletAccNum, 0.0, date);
       // Create savings account
       String savingsAccNum = payeeAddress + "_savings";
       databaseDriver.createSavingsAccount(payeeAddress, savingsAccNum, 0.0, date);
       // Notify listener
       if (clientCreatedListener != null) {
           WalletAccount wallet = getWalletAccount(payeeAddress);
           SavingsAccount savings = getSavingsAccount(payeeAddress);
           Client newClient = new Client(firstName, lastName, payeeAddress, wallet, savings, date);
           clientCreatedListener.accept(newClient);
       }
   }

   public void evaluateclientCred(String payeeAddress, String password) {
       ResultSet resultSet = databaseDriver.getClientsData(payeeAddress, password);
       try {
           clientLoginSuccessFlag = resultSet != null && resultSet.next();
           if (clientLoginSuccessFlag) {
               String fName = resultSet.getString("FirstName");
               String lName = resultSet.getString("LastName");
               String pAddress = resultSet.getString("PayeeAddress");
               LocalDate date = LocalDate.parse(resultSet.getString("Date"));
               WalletAccount wallet = getWalletAccount(pAddress);
               SavingsAccount savings = getSavingsAccount(pAddress);
               loggedInClient = new Client(fName, lName, pAddress, wallet, savings, date);
           }
       } catch (SQLException e) {
           e.printStackTrace();
           clientLoginSuccessFlag = false;
       }
   }

   public void evaluateAdminCred(String username, String password) {
       ResultSet resultSet = databaseDriver.getAdminData(username, password);
       try {
           adminLoginSuccessFlag = resultSet != null && resultSet.next();
       } catch (SQLException e) {
           e.printStackTrace();
           adminLoginSuccessFlag = false;
       }
   }

   public boolean getClientLoginSuccessFlag() {
       return clientLoginSuccessFlag;
   }

   public boolean getAdminLoginSuccessFlag() {
       return adminLoginSuccessFlag;
   }

   public Client getLoggedInClient() {
       return loggedInClient;
   }

   public void setClientCreatedListener(Consumer<Client> listener) {
       this.clientCreatedListener = listener;
   }

   public void setClientDeletedListener(Consumer<Client> listener) {
       this.clientDeletedListener = listener;
   }

   public List<Client> getAllClients() {
       List<Client> clients = new ArrayList<>();
       try (ResultSet resultSet = databaseDriver.getAllClientsData()) {
           while (resultSet != null && resultSet.next()) {
               String fName = resultSet.getString("FirstName");
               String lName = resultSet.getString("LastName");
               String pAddress = resultSet.getString("PayeeAddress");
               LocalDate date = LocalDate.parse(resultSet.getString("Date"));
               WalletAccount wallet = getWalletAccount(pAddress);
               SavingsAccount savings = getSavingsAccount(pAddress);
               clients.add(new Client(fName, lName, pAddress, wallet, savings, date));
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return clients;
   }
}
// ... existing code ...