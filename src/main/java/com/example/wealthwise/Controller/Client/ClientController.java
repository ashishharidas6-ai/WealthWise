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
                case "Transactions" -> client_parent.setCenter(Model.getInstance().getViewFactory().getTransactionView());
                case "Investment" -> client_parent.setCenter(Model.getInstance().getViewFactory().getInvestmentView());
                case "Accounts" -> client_parent.setCenter(Model.getInstance().getViewFactory().getAccountView());
                case "Profile" -> client_parent.setCenter(Model.getInstance().getViewFactory().getProfileView());
                case "Reports" -> client_parent.setCenter(Model.getInstance().getViewFactory().getReportView());
                default -> client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());

            }
        });
        // Set default center on startup
        client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
    }
}
