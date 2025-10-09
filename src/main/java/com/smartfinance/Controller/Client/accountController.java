package com.smartfinance.Controller.Client;

import com.smartfinance.Models.Client;
import com.smartfinance.Models.Model;
import com.smartfinance.Models.SavingsAccount;
import com.smartfinance.Models.WalletAccount;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class accountController implements Initializable {

    @FXML
    private Label acc_no;
    @FXML
    private Label limit_sav;
    @FXML
    private Label date_created;
    @FXML
    private Label balance_sav;

    @FXML
    private Label acc_no_wal;
    @FXML
    private Label limit_wal;
    @FXML
    private Label date_created_wal;
    @FXML
    private Label balance_wal;

    @FXML
    private TextField amount_to_wallet;
    @FXML
    private Button transfer_to_wallet_btn;
    @FXML
    private TextField amount_to_account;
    @FXML
    private Button transfer_to_account_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshAccountData();

        // Button actions
        transfer_to_wallet_btn.setOnAction(event -> transferToWallet());
        transfer_to_account_btn.setOnAction(event -> transferToAccount());
    }

    /**
     * ✅ Refreshes both wallet and savings data from DB and updates UI
     */
    private void refreshAccountData() {
        Client client = Model.getInstance().getLoggedInClient();
        if (client == null) return;

        String payee = client.getPayeeAddress();

        // Reload latest accounts from DB
        SavingsAccount savings = Model.getInstance().getSavingsAccount(payee);
        WalletAccount wallet = Model.getInstance().getWalletAccount(payee);

        if (savings != null) {
            acc_no.setText(savings.getAccNumber());
            limit_sav.setText("₹" + String.format("%.2f", savings.getWithdrawalLimit()));
            date_created.setText(formatDate(savings.getSavingsAccountCreationDate()));
            balance_sav.setText("₹" + String.format("%.2f", savings.getBalance()));
        } else {
            acc_no.setText("N/A");
            limit_sav.setText("N/A");
            date_created.setText("N/A");
            balance_sav.setText("N/A");
        }

        if (wallet != null) {
            acc_no_wal.setText(wallet.getAccNumber());
            limit_wal.setText("₹" + String.format("%.2f", wallet.getDepositLimit()));
            date_created_wal.setText(formatDate(wallet.getWalletAccountCreationDate()));
            balance_wal.setText("₹" + String.format("%.2f", wallet.getBalance()));
        } else {
            acc_no_wal.setText("N/A");
            limit_wal.setText("N/A");
            date_created_wal.setText("N/A");
            balance_wal.setText("N/A");
        }
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) return "Unknown";
        try {
            LocalDate date = LocalDate.parse(dateString);
            return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            return dateString;
        }
    }

    /**
     * ✅ Transfer from Savings → Wallet (like funding UPI wallet)
     */
    private void transferToWallet() {
        Client client = Model.getInstance().getLoggedInClient();
        if (client == null) return;

        try {
            double amount = Double.parseDouble(amount_to_wallet.getText().trim());
            if (amount <= 0) {
                showAlert("Invalid Amount", "Please enter a positive amount.");
                return;
            }

            SavingsAccount savings = (SavingsAccount) client.getSavingsAccount();
            WalletAccount wallet = (WalletAccount) client.getWalletAccount();

            if (savings == null || wallet == null) {
                showAlert("Error", "Account information not available.");
                return;
            }

            if (amount > savings.getBalance()) {
                showAlert("Insufficient Funds", "Not enough balance in savings account.");
                return;
            }

            if (amount > savings.getWithdrawalLimit()) {
                showAlert("Limit Exceeded", "Amount exceeds Transaction limit of ₹" + savings.getWithdrawalLimit());
                return;
            }

            if (!wallet.canDeposit(amount)) {
                showAlert("Wallet Limit", "Deposit exceeds wallet limit (₹" + wallet.getDepositLimit() + ")");
                return;
            }

            // ✅ Update balances in DB
            Model.getInstance().getDatabaseDriver().updateSavingsBalance(savings.getOwnerName(), savings.getBalance() - amount);
            Model.getInstance().getDatabaseDriver().updateWalletBalance(wallet.getOwnerName(), wallet.getBalance() + amount);

            refreshAccountData();
            Model.getInstance().refreshClientData();
            amount_to_wallet.clear();
            showAlert("Success", "₹" + String.format("%.2f", amount) + " transferred to wallet successfully.");

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number.");
        }
    }

    /**
     * ✅ Transfer from Wallet → Savings (like adding back to main bank)
     */
    private void transferToAccount() {
        Client client = Model.getInstance().getLoggedInClient();
        if (client == null) return;

        try {
            double amount = Double.parseDouble(amount_to_account.getText().trim());
            if (amount <= 0) {
                showAlert("Invalid Amount", "Please enter a positive amount.");
                return;
            }

            SavingsAccount savings = (SavingsAccount) client.getSavingsAccount();
            WalletAccount wallet = (WalletAccount) client.getWalletAccount();

            if (savings == null || wallet == null) {
                showAlert("Error", "Account information not available.");
                return;
            }

            if (amount > wallet.getBalance()) {
                showAlert("Insufficient Funds", "Not enough balance in wallet account.");
                return;
            }

            // ✅ Update balances in DB
            Model.getInstance().getDatabaseDriver().updateWalletBalance(wallet.getOwnerName(), wallet.getBalance() - amount);
            Model.getInstance().getDatabaseDriver().updateSavingsBalance(savings.getOwnerName(), savings.getBalance() + amount);

            refreshAccountData();
            amount_to_account.clear();
            showAlert("Success", "₹" + String.format("%.2f", amount) + " transferred to savings account successfully.");

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
