package com.smartfinance.Controller.Client;

import com.smartfinance.Models.Model;
import com.smartfinance.Views.ClientMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;


public  class ClientMenuController implements Initializable {
    public Button dashboard_btn;
    public Button account_btn;
    public Button transaction_btn;
    public Button investment_btn;
    public Button budget_btn;
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
        budget_btn.setOnAction(actionEvent -> onBudget());
        account_btn.setOnAction(actionEvent -> onAccount());
        profile_btn.setOnAction(actionEvent -> onProfile());
        report_btn.setOnAction(actionEvent -> onReport());
        logout_btn.setOnAction(actionEvent -> onLogout());

    }

    private void onDashboard() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
    }

    private void onTransactions() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.TRANSACTIONS);
    }

    private void onInvestment() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.INVESTMENT);
    }

    private void onBudget() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.BUDGET);
    }

    private void onAccount() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.ACCOUNTS);
    }

    private void onProfile() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.PROFILE);
    }

    private void onReport() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.REPORT);
    }

    private void onLogout() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.LOGOUT);
    }


}

