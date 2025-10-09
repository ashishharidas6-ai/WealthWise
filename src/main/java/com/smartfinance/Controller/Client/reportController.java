package com.smartfinance.Controller.Client;

import com.smartfinance.Models.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class reportController implements Initializable {

    @FXML
    private Button transaction_report_btn;
    @FXML
    private Button investment_report_btn;
    @FXML
    private Button balance_report_btn;
    @FXML
    private TextArea report_textarea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization if needed
    }

    @FXML
    private void onTransactionReport() {
        StringBuilder report = new StringBuilder();
        report.append("TRANSACTION REPORT\n");
        report.append("==================\n\n");

        Model model = Model.getInstance();
        String owner = model.getLoggedInClient().getPayeeAddress();

        try (ResultSet rs = model.getDatabaseDriver().getTransactionsByPayee(owner)) {
            double totalSent = 0;
            double totalReceived = 0;
            int transactionCount = 0;

            while (rs != null && rs.next()) {
                String sender = rs.getString("Sender");
                String receiver = rs.getString("Receiver");
                double amount = rs.getDouble("Amount");
                String category = rs.getString("Category");
                String date = rs.getString("Date");
                String message = rs.getString("Message");

                if (sender.equals(owner)) {
                    totalSent += amount;
                    report.append(String.format("Sent to %s: $%.2f (%s) - %s\n", receiver, amount, category, date));
                } else {
                    totalReceived += amount;
                    report.append(String.format("Received from %s: $%.2f (%s) - %s\n", sender, amount, category, date));
                }
                transactionCount++;
            }

            report.append("\nSUMMARY:\n");
            report.append(String.format("Total Transactions: %d\n", transactionCount));
            report.append(String.format("Total Sent: $%.2f\n", totalSent));
            report.append(String.format("Total Received: $%.2f\n", totalReceived));
            report.append(String.format("Net Flow: $%.2f\n", totalReceived - totalSent));

        } catch (SQLException e) {
            e.printStackTrace();
            report.append("Error generating transaction report: ").append(e.getMessage());
        }

        report_textarea.setText(report.toString());
    }

    @FXML
    private void onInvestmentReport() {
        StringBuilder report = new StringBuilder();
        report.append("INVESTMENT REPORT\n");
        report.append("=================\n\n");

        Model model = Model.getInstance();
        String owner = model.getLoggedInClient().getPayeeAddress();

        try (ResultSet rs = model.getDatabaseDriver().getInvestments(owner)) {
            double totalInvested = 0;
            double totalCurrent = 0;
            int investmentCount = 0;

            while (rs != null && rs.next()) {
                Investment investment = new Investment(
                        rs.getInt("ID"),
                        rs.getString("Owner"),
                        rs.getString("InvestmentType"),
                        rs.getDouble("AmountInvested"),
                        rs.getDouble("CurrentValue"),
                        rs.getString("DateInvested")
                );

                totalInvested += investment.getAmountInvested();
                totalCurrent += investment.getCurrentValue();
                investmentCount++;

                report.append(String.format("%s: Invested $%.2f, Current $%.2f, P/L $%.2f (%.2f%%) - %s\n",
                        investment.getInvestmentType(),
                        investment.getAmountInvested(),
                        investment.getCurrentValue(),
                        investment.getProfitLoss(),
                        investment.getProfitLossPercentage(),
                        investment.getDateInvested()));
            }

            report.append("\nSUMMARY:\n");
            report.append(String.format("Total Investments: %d\n", investmentCount));
            report.append(String.format("Total Invested: $%.2f\n", totalInvested));
            report.append(String.format("Current Value: $%.2f\n", totalCurrent));
            report.append(String.format("Total P/L: $%.2f (%.2f%%)\n",
                    totalCurrent - totalInvested,
                    totalInvested > 0 ? ((totalCurrent - totalInvested) / totalInvested) * 100 : 0));

        } catch (SQLException e) {
            e.printStackTrace();
            report.append("Error generating investment report: ").append(e.getMessage());
        }

        report_textarea.setText(report.toString());
    }

    @FXML
    private void onBalanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("BALANCE SUMMARY REPORT\n");
        report.append("======================\n\n");

        Model model = Model.getInstance();
        Client client = model.getLoggedInClient();

        WalletAccount wallet = client.getWalletAccount();
        SavingsAccount savings = client.getSavingsAccount();

        report.append(String.format("Client: %s %s\n", client.getFirstName(), client.getLastName()));
        report.append(String.format("Payee Address: %s\n\n", client.getPayeeAddress()));

        report.append("ACCOUNT BALANCES:\n");
        report.append(String.format("Wallet Account (%s): $%.2f\n", wallet.getAccNumber(), wallet.getBalance()));
        report.append(String.format("Savings Account (%s): $%.2f\n", savings.getAccNumber(), savings.getBalance()));
        report.append(String.format("Total Balance: $%.2f\n\n", wallet.getBalance() + savings.getBalance()));

        // Recent transactions summary
        report.append("RECENT TRANSACTIONS:\n");
        var transactions = client.getTransactionHistory();
        int count = 0;
        for (var transaction : transactions) {
            if (count >= 5) break; // Show last 5
            String direction = transaction.getSender().equals(client.getPayeeAddress()) ? "Sent" : "Received";
            report.append(String.format("%s: $%.2f to/from %s (%s)\n",
                    direction,
                    transaction.getAmount(),
                    transaction.getSender().equals(client.getPayeeAddress()) ? transaction.getReceiver() : transaction.getSender(),
                    transaction.getDate()));
            count++;
        }

        report_textarea.setText(report.toString());
    }
}
