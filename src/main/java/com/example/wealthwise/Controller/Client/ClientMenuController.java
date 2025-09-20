package com.example.wealthwise.Controller.Client;

import com.example.wealthwise.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;


public  class ClientMenuController implements Initializable {
    public Button dashboard_btn;
    public Button account_btn;
    public Button transaction_btn;
    public Button investment_btn;
    public Button logout_btn;
    public Button profile_btn;
    public Button report_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners() {
        dashboard_btn.setOnAction(actionEvent -> onDashboard());
        transaction_btn.setOnAction(actionEvent -> onTransactions());
        investment_btn.setOnAction(actionEvent -> onInvestment());
        account_btn.setOnAction(actionEvent -> onAccount());
        profile_btn.setOnAction(actionEvent -> onProfile());
        report_btn.setOnAction(actionEvent -> onReport());

    }

    private void onDashboard() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set("Dashboard");
    }

    private void onTransactions() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set("Transactions");
    }

    private void onInvestment() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set("Investment");
    }

    private void onAccount() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set("Accounts");
    }

    private void onProfile() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set("Profile");
    }

    private void onReport() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set("Reports");
    }


}

