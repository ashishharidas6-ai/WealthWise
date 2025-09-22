package com.example.wealthwise.Controller.Admin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.ResourceBundle;
public class CreateClientController implements Initializable {
    public TextField first_name;
    public TextField last_name;
    public TextField password;
    public CheckBox payee_address_wallet;
    public CheckBox  add_wallet_check;
    public TextField wallet_balance;
    public CheckBox saving_check;
    public TextField account_balance;
    public Button create_client_btn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
    }


}
