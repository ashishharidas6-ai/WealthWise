package com.example.wealthwise.Controller.Admin;

import com.example.wealthwise.Models.Client;
import com.example.wealthwise.Models.Model;
import com.example.wealthwise.Views.ClientCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    @FXML
    public ListView<Client> clients_listview;
    
    private Model model;
    private ObservableList<Client> clientsObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ClientsController: Initializing...");
        model = Model.getInstance();
        initializeClientsList();
        loadAllClients();
        setupClientCreatedListener();
        System.out.println("ClientsController: Initialization complete");
    }
    
    private void initializeClientsList() {
        System.out.println("ClientsController: Initializing clients list...");
        clientsObservableList = FXCollections.observableArrayList();
        clients_listview.setItems(clientsObservableList);
        clients_listview.setCellFactory(e -> new ClientCellFactory());
        System.out.println("ClientsController: Clients list initialized with cell factory");
    }
    
    public void loadAllClients() {
        try {
            System.out.println("ClientsController: Starting to load all clients...");
            
            // Clear existing clients
            clientsObservableList.clear();
            
            // Load all clients from database
            List<Client> allClients = model.getAllClients();
            
            System.out.println("ClientsController: Loaded " + allClients.size() + " clients from model");
            
            // Add all clients to the observable list
            clientsObservableList.addAll(allClients);
            
            System.out.println("ClientsController: Added " + clientsObservableList.size() + " clients to observable list");
            System.out.println("ClientsController: ListView items count: " + clients_listview.getItems().size());
            
        } catch (Exception e) {
            System.err.println("ClientsController: Error loading clients:");
            e.printStackTrace();
        }
    }
    
    // Method to refresh the client list (can be called after creating a new client)
    public void refreshClientsList() {
        loadAllClients();
    }
    
    // Method to add a single client to the list (for real-time updates)
    public void addClientToList(Client client) {
        if (clientsObservableList != null) {
            clientsObservableList.add(0, client); // Add to the beginning of the list
        }
    }
    
    // Set up listener for when clients are created
    private void setupClientCreatedListener() {
        model.setClientCreatedListener(client -> {
            // Run on JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                if (client == null) {
                    // Null client means full refresh
                    refreshClientsList();
                } else {
                    // Add the new client to the list
                    addClientToList(client);
                }
            });
        });
    }
    
    // Public method to manually refresh the list (for debugging)
    public void manualRefresh() {
        System.out.println("ClientsController: Manual refresh triggered");
        loadAllClients();
    }
    
    // Method to get current client count (for debugging)
    public int getClientCount() {
        return clientsObservableList != null ? clientsObservableList.size() : 0;
    }
}
