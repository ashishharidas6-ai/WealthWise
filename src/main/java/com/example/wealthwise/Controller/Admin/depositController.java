package com.example.wealthwise.Controller.Admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.example.wealthwise.Models.Model;
import com.example.wealthwise.Models.DatabaseDriver;
import com.example.wealthwise.Models.Client;

import java.net.URL;
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
    public Button deposit_btn;

    private final ObservableList<Client> payeeObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("DepositController initialized successfully");

        payee_list.setItems(payeeObservableList);

        search_btn_payee.setOnAction(event -> searchPayees());
        deposit_btn.setOnAction(event -> depositAmount());
    }

    private void searchPayees() {
        String searchText = search_payee.getText().trim();
        if (searchText.isEmpty()) {
            System.out.println("Search text is empty");
            return;
        }

        DatabaseDriver databaseDriver = Model.getInstance().getDatabaseDriver();
        payeeObservableList.clear();

        List<Client> clients = databaseDriver.searchClientsByPayeeAddress(searchText);
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

        DatabaseDriver databaseDriver = Model.getInstance().getDatabaseDriver();
        boolean success = databaseDriver.depositToWallet(selectedClient.getPayeeAddress(), amount);

        if (success) {
            System.out.println("Deposit successful: " + amount + " to " + selectedClient);
            amount_deposit.clear();
            payee_list.getSelectionModel().clearSelection();
        } else {
            System.out.println("Deposit failed for: " + selectedClient);
        }
    }
}
