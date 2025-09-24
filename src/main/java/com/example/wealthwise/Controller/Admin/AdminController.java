package com.example.wealthwise.Controller.Admin;
import com.example.wealthwise.Models.Model;
import javafx.scene.layout.BorderPane;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().addListener((observable,oldval,newval) ->{
            switch (newval){
                case CLIENTS -> admin_parent.setCenter(Model.getInstance().getViewFactory().getClientsView());
                case DEPOSIT -> admin_parent.setCenter(Model.getInstance().getViewFactory().getDepositView());
                case CREATE_CLIENT -> admin_parent.setCenter(Model.getInstance().getViewFactory().getCreateClientView());

                default -> admin_parent.setCenter(Model.getInstance().getViewFactory().getCreateClientView());

            }

        } );
    }


}
