package com.example.wealthwise.Controller.Admin;

import com.example.wealthwise.Models.Model;
import com.example.wealthwise.Views.AdminMenuOption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable{
    public Button create_client;
    public Button client_btn;
    public Button deposit_btn;
    public Button logout_btn;

    private void addListeners() {
        create_client.setOnAction(event -> onCreateClient());
        client_btn.setOnAction(event -> onClients());

    // Add other listeners as needed
    }

    private void onCreateClient() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOption.CREATE_CLIENT);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addlisteners(){
     create_client.setOnAction(event->onCreateClients());
     client_btn.setOnAction(event->onClients());
    }
    private void onCreateClients(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOption.CREATE_CLIENT);
    }
    private void onClients(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOption.CLIENTS);
    }
}