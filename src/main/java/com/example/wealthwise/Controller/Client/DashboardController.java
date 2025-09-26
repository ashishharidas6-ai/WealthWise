package com.example.wealthwise.Controller.Client;

import com.example.wealthwise.Models.Client;
import com.example.wealthwise.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.net.URL;

public class DashboardController implements Initializable{
    public Text user_name;
    public Label login_date;

    public Label checking_acc_bal;
    public Label checking_acc_num;
    public Label saving_acc_bal;
    public Label saving_acc_num;
    public Label income_lbl;
    public Label expense_lbl;
    public ListView transaction_listview;
    public TextField payee_fld;
    public TextField amount_fld;
    public TextArea message_fld;
    public Button send_money_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Client client = Model.getInstance().getLoggedInClient();
        if (client != null) {
            user_name.setText(client.getFirstName() + " " + client.getLastName());
            login_date.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            // Wallet Account
            if (client.getWalletAccount() != null) {
                checking_acc_num.setText(client.getWalletAccount().getAccNumber());
                checking_acc_bal.setText("₹" + String.format("%.2f", client.getWalletAccount().getBalance()));
            }

            // Savings Account
            if (client.getSavingsAccount() != null) {
                saving_acc_num.setText(client.getSavingsAccount().getAccNumber());
                saving_acc_bal.setText("₹" + String.format("%.2f", client.getSavingsAccount().getBalance()));
            }

            // Income and Expense - placeholder for now, can be calculated from transactions later
            income_lbl.setText("₹0.00");
            expense_lbl.setText("₹0.00");
        }
    }
}
