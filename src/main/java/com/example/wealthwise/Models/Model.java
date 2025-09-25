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
    private Consumer<Client> clientCreatedListener;

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
               String accNumber = accountData.getString("AccountNumber");
               double balance = accountData.getDouble("Balance");
               // Using a default transaction limit
               return new WalletAccount(owner, accNumber, balance, 1000);
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
               String accNumber = accountData.getString("AccountNumber");
               double balance = accountData.getDouble("Balance");
               // Using a default withdrawal limit
               return new SavingsAccount(owner, accNumber, balance, 500.0);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return null;
   }

   public boolean deleteClient(String payeeAddress) {
       DatabaseDriver databaseDriver = getDatabaseDriver();
       try {
           databaseDriver.deleteClient(payeeAddress);
           databaseDriver.deleteWalletAccount(payeeAddress);
           databaseDriver.deleteSavingsAccount(payeeAddress);
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

   public void setClientCreatedListener(Consumer<Client> listener) {
       this.clientCreatedListener = listener;
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