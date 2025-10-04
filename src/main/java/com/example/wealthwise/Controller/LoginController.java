package com.example.wealthwise.Controller;

import com.example.wealthwise.Models.Model;
import com.example.wealthwise.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.scene.control.*;

import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<AccountType> acc_selector;
    public Label payee_address;
    public TextField payee_address_fld;
    public PasswordField password_fld;
    public Button login_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT,AccountType.ADMIN));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        acc_selector.valueProperty().addListener(observable -> Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue()));
        login_btn.setOnAction(actionEvent -> onLogin());
    }

    private void onLogin() {
        // Get current stage (login window)
        Stage stage = (Stage) login_btn.getScene().getWindow();

        // Close it


        // Open client window
       if(Model.getInstance().getViewFactory().getLoginAccountType()==AccountType.CLIENT){
           //evaluation
           Model.getInstance().evaluateClientCred(payee_address_fld.getText(),password_fld.getText());
           if(Model.getInstance().getClientLoginSuccessFlag()){
               Model.getInstance().getViewFactory().showClientWindow();
               //close login state
               Model.getInstance().getViewFactory().closeStage(stage);
           }else{
               payee_address_fld.setText("");
               password_fld.setText("");
               error_lbl.setText("Invalid Credentials!");
           }
       }else{
           //evaluation
           Model.getInstance().evaluateAdminCred(payee_address_fld.getText(),password_fld.getText());
           if(Model.getInstance().getAdminLoginSuccessFlag()){
               Model.getInstance().getViewFactory().showAdminWindow();
               //close login state
               Model.getInstance().getViewFactory().closeStage(stage);
           }else{
               payee_address_fld.setText("");
               password_fld.setText("");
               error_lbl.setText("Invalid Credentials!");
           }
       }
    }

}
