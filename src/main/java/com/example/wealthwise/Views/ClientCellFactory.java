package com.example.wealthwise.Views;

import com.example.wealthwise.Controller.Admin.ClientCellController;
import com.example.wealthwise.Models.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class ClientCellFactory extends ListCell<Client> {


    @Override
    protected void updateItem(Client client, boolean empty) {
        super.updateItem(client, empty);
        if(empty || client == null){
            setText(null);
            setGraphic(null);
        }else{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ClientCell.fxml"));
                ClientCellController controller = new ClientCellController(client);
                loader.setController(controller);
                setText(null);
                setGraphic(loader.load());
                
                // Debug: Print that a cell was created
                System.out.println("Created cell for client: " + client.firstNameProperty().get() + " " + client.lastNameProperty().get());
                
            } catch (Exception e) {
                System.err.println("Error creating client cell:");
                e.printStackTrace();
                // Set a simple text fallback if FXML loading fails
                setText(client.firstNameProperty().get() + " " + client.lastNameProperty().get());
                setGraphic(null);
            }
        }
    }
}
