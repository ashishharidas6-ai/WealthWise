package com.example.wealthwise.Controller.Admin;

import com.example.wealthwise.Models.Model;
import com.example.wealthwise.Views.AdminMenuOption;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable{
    @FXML
    public Button create_client;
    
    @FXML
    public Button client_btn;
    
    @FXML
    public Button deposit_btn;
    
    @FXML
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners(){
        create_client.setOnAction(event -> onCreateClient());
        client_btn.setOnAction(event -> onClients());
        deposit_btn.setOnAction(event -> onDeposit());
        logout_btn.setOnAction(event -> onLogout());
    }

    private void onCreateClient(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOption.CREATE_CLIENT);
    }

    private void onClients(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOption.CLIENTS);
    }

    private void onDeposit(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOption.DEPOSIT);
    }

    private void onLogout(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOption.LOGOUT);
    }
}