package com.smartfinance.Controller.Admin;

import com.smartfinance.Models.Client;
import com.smartfinance.Models.Model;
import com.smartfinance.Views.ClientCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

// Forcing a file update
public class ClientsController implements Initializable {
    @FXML
    public ListView<Client> clients_listview;

    private Model model;
    private ObservableList<Client> clientsObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Model.getInstance();
        initializeClientsList();
        loadAllClients();
        setupClientCreatedListener();
        setupClientDeletedListener();
    }

    private void initializeClientsList() {
        clientsObservableList = FXCollections.observableArrayList();
        clients_listview.setItems(clientsObservableList);
        clients_listview.setCellFactory(new ClientCellFactory());
    }

    public void loadAllClients() {
        try {
            clientsObservableList.clear();
            List<Client> allClients = model.getAllClients();
            clientsObservableList.addAll(allClients);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupClientCreatedListener() {
        model.setClientCreatedListener(client -> {
            javafx.application.Platform.runLater(() -> {
                clientsObservableList.add(0, client);
            });
        });
    }

    private void setupClientDeletedListener() {
        model.setClientDeletedListener(client -> {
            javafx.application.Platform.runLater(() -> {
                clientsObservableList.remove(client);
            });
        });
    }
}
