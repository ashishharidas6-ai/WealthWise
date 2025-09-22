package com.example.wealthwise.Controller.Client;

import com.example.wealthwise.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    public BorderPane client_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case TRANSACTIONS -> client_parent.setCenter(Model.getInstance().getViewFactory().getTransactionView());
                case INVESTMENT -> client_parent.setCenter(Model.getInstance().getViewFactory().getInvestmentView());
                case BUDGET -> client_parent.setCenter(Model.getInstance().getViewFactory().getBudgetView());
                case ACCOUNTS -> client_parent.setCenter(Model.getInstance().getViewFactory().getAccountView());
                case PROFILE -> client_parent.setCenter(Model.getInstance().getViewFactory().getProfileView());
                case REPORT -> client_parent.setCenter(Model.getInstance().getViewFactory().getReportView());
                default -> client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());

            }
        });
        // Set default center on startup
        client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
    }
}
