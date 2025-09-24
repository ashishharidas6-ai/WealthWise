package com.example.wealthwise.Controller.Admin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class depositController implements Initializable {
    @FXML
    public TextField search_payee;
    
    @FXML
    public Button search_btn_payee;
    
    @FXML
    public ListView<?> payee_list;
    
    @FXML
    public TextField amount_deposit;
    
    @FXML
    public Button deposit_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize controller logic here
        System.out.println("DepositController initialized successfully");
    }
}
