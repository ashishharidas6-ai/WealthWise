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
            if (client.getSavingsAccount() != null) {
                acc_no.setText(client.getSavingsAccount().getAccNumber());
                limit_sav.setText("₹" + String.format("%.2f", ((SavingsAccount) client.getSavingsAccount()).getWithdrawalLimit()));
                date_created.setText(client.getSavingsAccount().getDateCreated());
                balance_sav.setText("₹" + String.format("%.2f", client.getSavingsAccount().getBalance()));
            }

            // Wallet Account
            if (client.getWalletAccount() != null) {
                acc_no_wal.setText(client.getWalletAccount().getAccNumber());
                limit_wal.setText(String.valueOf(((WalletAccount) client.getWalletAccount()).getTransactionLimit()));
                date_created_wal.setText(client.getWalletAccount().getDateCreated());
                balance_wal.setText("₹" + String.format("%.2f", client.getWalletAccount().getBalance()));
            }
        }
    }



}
