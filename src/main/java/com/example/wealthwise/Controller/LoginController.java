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
        Model.getInstance().getViewFactory().closeStage(stage);

        // Open client window
       if(Model.getInstance().getViewFactory().getLoginAccountType()==AccountType.CLIENT){
           Model.getInstance().getViewFactory().showClientWindow();
       }else{
           Model.getInstance().getViewFactory().showAdminWindow();
       }
    }

}
