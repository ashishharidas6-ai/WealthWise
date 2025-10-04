package com.example.wealthwise.Controller.Client;

import com.example.wealthwise.Models.Client;
import com.example.wealthwise.Models.Model;
import com.example.wealthwise.Models.Transaction;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    public ListView<Transaction> transactions_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Client client = Model.getInstance().getLoggedInClient();
        if (client != null) {
            // Set cell factory to use TransactionCellController
            transactions_listview.setCellFactory(listView -> {
                return new ListCell<Transaction>() {
                    @Override
                    protected void updateItem(Transaction transaction, boolean empty) {
                        super.updateItem(transaction, empty);
                        if (empty || transaction == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/transactioncell.fxml"));
                                TransactionCellController controller = new TransactionCellController(transaction);
                                loader.setController(controller);
                                Parent root = loader.load();
                                setGraphic(root);
                                setPrefHeight(60);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            });

            // Load transactions from client
            transactions_listview.getItems().addAll(client.getTransactionHistory());

            // Optional: refresh automatically when Model.refreshClientData() is called
            client.getTransactionHistory().addListener((javafx.collections.ListChangeListener<Transaction>) change -> {
                transactions_listview.getItems().clear();
                transactions_listview.getItems().addAll(client.getTransactionHistory());
            });
        }
    }


}
