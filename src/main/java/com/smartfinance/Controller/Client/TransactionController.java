package com.smartfinance.Controller.Client;

import com.smartfinance.Models.Client;
import com.smartfinance.Models.Model;
import com.smartfinance.Models.Transaction;
import com.opencsv.CSVWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    public ListView<Transaction> transactions_listview;
    @FXML
    public Button csv_export;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        csv_export.setOnAction(e -> exportToCSV());

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

            // Bind the transaction history to the list view
            transactions_listview.setItems(client.getTransactionHistory());
        }
    }

    private void exportToCSV() {
        Client client = Model.getInstance().getLoggedInClient();
        if (client == null || client.getTransactionHistory().isEmpty()) {
            // Show alert or something
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Transactions CSV");
        fileChooser.setInitialFileName("transactions.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        Stage stage = (Stage) csv_export.getScene().getWindow();
        java.io.File file = fileChooser.showSaveDialog(stage);
        if (file == null) return;

        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            // Write header
            String[] header = {"Sender", "Receiver", "Amount", "Category", "Message", "Date"};
            writer.writeNext(header);

            // Write data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Transaction transaction : client.getTransactionHistory()) {
                String[] row = {
                    transaction.getSender(),
                    transaction.getReceiver(),
                    String.format("%.2f", transaction.getAmount()),
                    transaction.getCategory(),
                    transaction.getMessage(),
                    transaction.getDate().format(formatter)
                };
                writer.writeNext(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Show error alert
        }
    }

}
