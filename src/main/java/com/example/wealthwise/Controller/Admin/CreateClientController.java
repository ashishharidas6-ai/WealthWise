package com.example.wealthwise.Controller.Admin;

import com.example.wealthwise.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateClientController implements Initializable {
    @FXML
    public TextField first_name;
    @FXML
    public TextField last_name;
    @FXML
    public TextField password;
    @FXML
    public TextField payee_address;
    @FXML
    public Button create_client_button;
    @FXML
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_button.setOnAction(event -> onCreateClient());
    }

    private void onCreateClient() {
        String firstName = first_name.getText();
        String lastName = last_name.getText();
        String pAddress = payee_address.getText();
        String pass = password.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || pAddress.isEmpty() || pass.isEmpty()) {
            error_lbl.setText("Please fill all the fields");
            return;
        }

        Model.getInstance().createNewClient(firstName, lastName, pAddress, pass);
        error_lbl.setText("Client Created Successfully!");
        clearFields();
    }

    private void clearFields() {
        first_name.clear();
        last_name.clear();
        payee_address.clear();
        password.clear();
    }
}