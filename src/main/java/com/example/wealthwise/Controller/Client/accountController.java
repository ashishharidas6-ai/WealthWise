package com.example.wealthwise.Controller.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.ResourceBundle;
public class accountController implements Initializable {

    public Label acc_no;
    public Label limit_sav;
    public Label date_created;
    public Label balance_sav;

    public Label acc_no_wal;
    public Label limit_wal;
    public Label date_created_wal;
    public Label balance_wal;

    public TextField amount_to_wallet;
    public Button transfer_to_wallet_btn;
    public TextField amount_to_account;
    public Button transfer_to_account_btn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }



}
