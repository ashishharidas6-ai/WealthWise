package com.smartfinance.Views;

import com.smartfinance.Controller.Admin.ClientCellController;
import com.smartfinance.Models.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.io.IOException;

public class ClientCellFactory implements Callback<ListView<Client>, ListCell<Client>> {
    @Override
    public ListCell<Client> call(ListView<Client> param) {
        return new ListCell<>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ClientCell.fxml"));
                        ClientCellController controller = new ClientCellController(client);
                        loader.setController(controller);
                        setGraphic(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
