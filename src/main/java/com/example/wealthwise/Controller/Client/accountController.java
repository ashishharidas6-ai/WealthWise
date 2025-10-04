package com.example.wealthwise.Controller.Client;
import com.example.wealthwise.Models.Client;
import com.example.wealthwise.Models.Model;
import com.example.wealthwise.Models.SavingsAccount;
import com.example.wealthwise.Models.WalletAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
public class accountController implements Initializable {

    public Label acc_no;
    public Label limit_sav;
    public Label date_created;
    public Label balance_sav;

    public Label acc_no_wal;
    public Label limit_wal;
    public Label date_created_wal;
    public Label balance_wal;

    public TextField amount_to_wallet;
    public Button transfer_to_wallet_btn;
    public TextField amount_to_account;
    public Button transfer_to_account_btn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Client client = Model.getInstance().getLoggedInClient();
        if (client != null) {
            // Savings Account
            SavingsAccount savings = (SavingsAccount) client.getSavingsAccount();
            if (savings != null) {
                acc_no.setText(savings.getAccNumber());
                limit_sav.setText("₹" + String.format("%.2f", savings.getWithdrawalLimit()));
                date_created.setText(formatDate(savings.getSavingsAccountCreationDate()));
                balance_sav.setText("₹" + String.format("%.2f", savings.getBalance()));
            }

            // Wallet Account
            WalletAccount wallet = (WalletAccount) client.getWalletAccount();
            if (wallet != null) {
                acc_no_wal.setText(wallet.getAccNumber());
                limit_wal.setText("₹" + String.format("%.2f", wallet.getTransactionLimit()));
                date_created_wal.setText(formatDate(wallet.getWalletAccountCreationDate()));
                balance_wal.setText("₹" + String.format("%.2f", wallet.getBalance()));
            }
        }
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "Unknown";
        }
        try {
            // Assuming the date is stored as yyyy-MM-dd format
            LocalDate date = LocalDate.parse(dateString);
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return dateString; // Return as-is if parsing fails
        }
    }
}
