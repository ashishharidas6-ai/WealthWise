package com.example.wealthwise.Controller.Admin;

import com.example.wealthwise.Models.*;
import com.example.wealthwise.Views.ClientCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class depositController implements Initializable {

    @FXML
    private TextField search_payee;

    @FXML
    private Button search_btn_payee;

    @FXML
    private ListView<Client> payee_list;

    @FXML
    private TextField amount_deposit;

    @FXML
    private Button depositbtn_payee;

    private final ObservableList<Client> payeeObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        payee_list.setItems(payeeObservableList);
        payee_list.setCellFactory(listView -> new ClientCellFactory());

        search_btn_payee.setOnAction(event -> searchPayees());
        depositbtn_payee.setOnAction(event -> depositAmount());
    }

    private void searchPayees() {
        String searchText = search_payee.getText().trim();
        payeeObservableList.clear();

        if (searchText.isEmpty()) {
            System.out.println("Please enter a Payee Address to search.");
            return;
        }

        DatabaseDriver databaseDriver = Model.getInstance().getDatabaseDriver();

        try (ResultSet resultSet = databaseDriver.searchClient(searchText)) {
            while (resultSet != null && resultSet.next()) {
                String fName = resultSet.getString("FirstName");
                String lName = resultSet.getString("LastName");
                String pAddress = resultSet.getString("PayeeAddress");

                WalletAccount wallet = Model.getInstance().getWalletAccount(pAddress);
                SavingsAccount savings = Model.getInstance().getSavingsAccount(pAddress);
                Client client = new Client(fName, lName, pAddress, wallet, savings, null);

                payeeObservableList.add(client);
            }

            if (payeeObservableList.isEmpty()) {
                System.out.println("No client found for: " + searchText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void depositAmount() {
        Client selectedClient = payee_list.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            System.out.println("Please select a client first.");
            return;
        }

        String amountText = amount_deposit.getText().trim();
        if (amountText.isEmpty()) {
            System.out.println("Amount field is empty.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                System.out.println("Amount must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format.");
            return;
        }

        DatabaseDriver databaseDriver = Model.getInstance().getDatabaseDriver();
        boolean success = databaseDriver.depositToSavings(selectedClient.getPayeeAddress(), amount);

        if (success) {
            // Update in-memory balance
            double newBalance = selectedClient.getSavingsAccount().getBalance() + amount;
            selectedClient.getSavingsAccount().setBalance(newBalance);

            System.out.println("✅ Deposit successful: " + amount +
                    " added to " + selectedClient.getPayeeAddress() +
                    ". New balance: " + newBalance);

            amount_deposit.clear();
            payee_list.refresh();
        } else {
            System.out.println("❌ Deposit failed for: " + selectedClient.getPayeeAddress());
        }
    }
}
