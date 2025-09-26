package com.example.wealthwise.Controller.Admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.example.wealthwise.Models.Model;
import com.example.wealthwise.Models.DatabaseDriver;
import com.example.wealthwise.Models.Client;
import com.example.wealthwise.Views.ClientCellFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class depositController implements Initializable {
    @FXML
    public TextField search_payee;
    @FXML
    public Button search_btn_payee;
    @FXML
    public ListView<Client> payee_list;  // now holds Client objects
    @FXML
    public TextField amount_deposit;
    @FXML
    public Button depositbtn_payee;

    private final ObservableList<Client> payeeObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("DepositController initialized successfully");

        payee_list.setItems(payeeObservableList);
        payee_list.setCellFactory(listView -> new ClientCellFactory());

        search_btn_payee.setOnAction(event -> searchPayees());
        depositbtn_payee.setOnAction(event -> depositAmount());
    }

    private void searchPayees() {
        String searchText = search_payee.getText().trim();
        if (searchText.isEmpty()) {
            System.out.println("Search text is empty");
            return;
        }

        DatabaseDriver databaseDriver = Model.getInstance().getDatabaseDriver();
        payeeObservableList.clear();

        List<Client> clients = new ArrayList<>();
        try (ResultSet resultSet = databaseDriver.searchClient(searchText)) {
            while (resultSet != null && resultSet.next()) {
                String fName = resultSet.getString("FirstName");
                String lName = resultSet.getString("LastName");
                String pAddress = resultSet.getString("PayeeAddress");
                LocalDate date = LocalDate.parse(resultSet.getString("Date"));
                com.example.wealthwise.Models.WalletAccount wallet = Model.getInstance().getWalletAccount(pAddress);
                com.example.wealthwise.Models.SavingsAccount savings = Model.getInstance().getSavingsAccount(pAddress);
                clients.add(new Client(fName, lName, pAddress, wallet, savings, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (clients.isEmpty()) {
            System.out.println("No payees found for: " + searchText);
        } else {
            payeeObservableList.addAll(clients);
        }
    }

    private void depositAmount() {
        Client selectedClient = payee_list.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            System.out.println("No payee selected");
            return;
        }

        String amountText = amount_deposit.getText().trim();
        if (amountText.isEmpty()) {
            System.out.println("Amount is empty");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                System.out.println("Amount must be positive");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format");
            return;
        }

        // Get current savings balance from database
        DatabaseDriver databaseDriver = Model.getInstance().getDatabaseDriver();
        double currentBalance = 0.0;
        try (ResultSet resultSet = databaseDriver.getSavingsAccount(selectedClient.getPayeeAddress())) {
            if (resultSet != null && resultSet.next()) {
                currentBalance = resultSet.getDouble("Balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to get current balance");
            return;
        }
        double newBalance = currentBalance + amount;

        boolean success = databaseDriver.updateSavingsBalance(selectedClient.getPayeeAddress(), newBalance);

        if (success) {
            // Update the client's savings account balance
            selectedClient.getSavingsAccount().setBalance(newBalance);
            System.out.println("Deposit successful: " + amount + " to " + selectedClient + ". New balance: " + newBalance);
            amount_deposit.clear();
            payee_list.getSelectionModel().clearSelection();
            // Refresh the list to update displayed balances
            payee_list.refresh();
        } else {
            System.out.println("Deposit failed for: " + selectedClient);
        }
    }
}
