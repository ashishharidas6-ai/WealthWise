package com.example.wealthwise.Controller.Client;

import com.example.wealthwise.Models.Transaction;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionCellController implements Initializable {

    public Label category_lbl;
    public Label amount_lbl;
    public Label recevier_lbl;
    public Label sender_lbl;
    public Label trans_date_lbl;
    public ImageView in_icon;
    private final  Transaction transaction;
public TransactionCellController(Transaction transaction){
    this.transaction=transaction;
}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }




}
