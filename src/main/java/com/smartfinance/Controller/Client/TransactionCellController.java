package com.smartfinance.Controller.Client;

import com.smartfinance.Models.Client;
import com.smartfinance.Models.Model;
import com.smartfinance.Models.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TransactionCellController implements Initializable {

    @FXML
    public Label category_lbl;
    @FXML
    public Label amount_lbl;
    @FXML
    public Label receiver_lbl;
    @FXML
    public Label sender_lbl;
    @FXML
    public Label trans_date_lbl;
    @FXML
    public ImageView in_icon;

    private Transaction transaction;

    public TransactionCellController() {
        // default constructor for FXML
    }

    public TransactionCellController(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (transaction == null) return;

        try {
            // Set transaction info
            category_lbl.setText(transaction.getCategory() != null ? transaction.getCategory().toString() : "N/A");
            amount_lbl.setText("₹" + String.format("%.2f", transaction.getAmount()));
            sender_lbl.setText(transaction.getSender() != null ? transaction.getSender() : "N/A");
            receiver_lbl.setText(transaction.getReceiver() != null ? transaction.getReceiver() : "N/A");
            trans_date_lbl.setText(transaction.getDate() != null ?
                    transaction.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "N/A");

            // Set icon based on incoming/outgoing
            Client currentClient = Model.getInstance().getLoggedInClient();
            if (currentClient != null) {
                String currentAddress = currentClient.getPayeeAddress();
                String receiver = transaction.getReceiver();
                if (currentAddress != null && receiver != null && receiver.equals(currentAddress)) {
                    // Incoming transaction
                    java.io.InputStream stream = getClass().getResourceAsStream("/Images/incoming.png");
                    if (stream != null) {
                        in_icon.setImage(new Image(stream));
                    } else {
                        in_icon.setImage(null);
                    }
                } else {
                    // Outgoing transaction
                    java.io.InputStream stream = getClass().getResourceAsStream("/Images/outgoing.png");
                    if (stream != null) {
                        in_icon.setImage(new Image(stream));
                    } else {
                        in_icon.setImage(null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback default values
            category_lbl.setText("Error");
            amount_lbl.setText("Error");
            sender_lbl.setText("Error");
            receiver_lbl.setText("Error");
            trans_date_lbl.setText("Error");
            in_icon.setImage(null);
        }
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        initialize(null, null); // re-initialize with the new transaction
    }
}
