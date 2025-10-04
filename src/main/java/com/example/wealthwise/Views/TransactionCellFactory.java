package com.example.wealthwise.Views;

import com.example.wealthwise.Controller.Client.TransactionCellController;
import com.example.wealthwise.Models.Transaction;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class TransactionCellFactory extends ListCell<Transaction> {
    @Override
    protected void updateItem(Transaction transaction, boolean empty) {
        super.updateItem(transaction, empty);
        if (empty || transaction == null) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/transactioncell.fxml"));
            TransactionCellController controller = new TransactionCellController(transaction);
            loader.setController(controller);
            setText(null);
            try{
                setGraphic(loader.load());
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }
}
