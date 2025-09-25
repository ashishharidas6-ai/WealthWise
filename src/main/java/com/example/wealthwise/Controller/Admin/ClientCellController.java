package com.example.wealthwise.Controller.Admin;

import com.example.wealthwise.Models.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ClientCellController implements Initializable {
    @FXML
    public Label date_label;
    @FXML
    public Label fname_lbl;
    @FXML
    public Label lname_lbl;
    @FXML
    public Label pAddress_lbl;
    @FXML
    public Label wallet_acc_number;
    @FXML
    public Label sav_acc_number;
    @FXML
    public Label wallet_balance;
    @FXML
    public Label sav_balance;
    @FXML
    public Button delete_btn;

    private final Client client;

    public ClientCellController(Client client){
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (client != null) {
            try {
                // Set client information from database columns (FirstName, LastName, PayeeAddress)
                String firstName = client.firstNameProperty().get();
                String lastName = client.lastNameProperty().get();
                String payeeAddress = client.payeeAddressProperty().get();
                
                // Debug: Print the client data being displayed
                System.out.println("=== ClientCellController Debug ===");
                System.out.println("Client object: " + (client != null ? "NOT NULL" : "NULL"));
                System.out.println("FirstName: '" + firstName + "'");
                System.out.println("LastName: '" + lastName + "'");
                System.out.println("PayeeAddress: '" + payeeAddress + "'");
                System.out.println("WalletAccount: " + (client.walletAccountProperty().get() != null ? "NOT NULL" : "NULL"));
                System.out.println("SavingsAccount: " + (client.savingsAccountProperty().get() != null ? "NOT NULL" : "NULL"));
                System.out.println("Date: " + (client.dateProperty().get() != null ? client.dateProperty().get().toString() : "NULL"));
                System.out.println("=== End Debug ===");
                
                // Display client information in labels
                fname_lbl.setText(firstName != null ? firstName : "N/A");
                lname_lbl.setText(lastName != null ? lastName : "N/A");
                pAddress_lbl.setText(payeeAddress != null ? payeeAddress : "N/A");
                
                // Set wallet account number
                if (client.walletAccountProperty().get() != null &&
                    client.walletAccountProperty().get().accNumberProperty().get() != null) {
                    wallet_acc_number.setText(client.walletAccountProperty().get().accNumberProperty().get());
                    wallet_balance.setText("$" + String.format("%.2f", client.walletAccountProperty().get().balanceProperty().get()));
                } else {
                    wallet_acc_number.setText("N/A");
                    wallet_balance.setText("N/A");
                }
                
                // Set savings account number
                if (client.savingsAccountProperty().get() != null &&
                    client.savingsAccountProperty().get().accNumberProperty().get() != null) {
                    sav_acc_number.setText(client.savingsAccountProperty().get().accNumberProperty().get());
                    sav_balance.setText("$" + String.format("%.2f", client.savingsAccountProperty().get().balanceProperty().get()));
                } else {
                    sav_acc_number.setText("N/A");
                    sav_balance.setText("N/A");
                }
                
                // Format and set the date from database Date column
                if (client.dateProperty().get() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    date_label.setText(client.dateProperty().get().format(formatter));
                } else {
                    date_label.setText("N/A");
                }
                
                // Set up delete button action (optional - for future implementation)
                delete_btn.setOnAction(event -> onDeleteClient());
                
            } catch (Exception e) {
                e.printStackTrace();
                // Set default values if there's an error
                fname_lbl.setText("Error");
                lname_lbl.setText("Error");
                pAddress_lbl.setText("Error");
                wallet_acc_number.setText("Error");
                sav_acc_number.setText("Error");
                wallet_balance.setText("Error");
                sav_balance.setText("Error");
                date_label.setText("Error");
            }
        } else {
            // Handle null client case
            fname_lbl.setText("N/A");
            lname_lbl.setText("N/A");
            pAddress_lbl.setText("N/A");
            wallet_acc_number.setText("N/A");
            sav_acc_number.setText("N/A");
            wallet_balance.setText("N/A");
            sav_balance.setText("N/A");
            date_label.setText("N/A");
        }
    }
    
    private void onDeleteClient() {
        if (client != null) {
            try {
                String clientName = client.firstNameProperty().get() + " " + client.lastNameProperty().get();
                String clientEmail = client.payeeAddressProperty().get();
                
                System.out.println("Delete button clicked for client: " + clientName + " (" + clientEmail + ")");
                
                // Show confirmation dialog
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Client");
                alert.setHeaderText("Delete Client: " + clientName);
                alert.setContentText("Are you sure you want to delete this client?\n\n" +
                                   "This will permanently remove:\n" +
                                   "• Client account: " + clientName + "\n" +
                                   "• Email: " + clientEmail + "\n" +
                                   "• Wallet account\n" +
                                   "• Savings account\n\n" +
                                   "This action cannot be undone.");
                
                java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
                
                if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
                    // User confirmed deletion
                    System.out.println("User confirmed deletion of client: " + clientName);
                    
                    // Delete client from database
                    com.example.wealthwise.Models.Model model = com.example.wealthwise.Models.Model.getInstance();
                    boolean deleted = model.deleteClient(clientEmail);
                    
                    if (deleted) {
                        // Show success message
                        javafx.scene.control.Alert successAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Client Deleted");
                        successAlert.setHeaderText("Success");
                        successAlert.setContentText("Client " + clientName + " has been successfully deleted.");
                        successAlert.showAndWait();
                        
                        System.out.println("Client " + clientName + " deleted successfully");
                    } else {
                        // Show error message
                        javafx.scene.control.Alert errorAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                        errorAlert.setTitle("Deletion Failed");
                        errorAlert.setHeaderText("Error");
                        errorAlert.setContentText("Failed to delete client " + clientName + ".\nPlease try again or contact system administrator.");
                        errorAlert.showAndWait();
                        
                        System.err.println("Failed to delete client " + clientName);
                    }
                } else {
                    System.out.println("User cancelled deletion of client: " + clientName);
                }
                
            } catch (Exception e) {
                System.err.println("Error in delete client operation:");
                e.printStackTrace();
                
                // Show error dialog
                javafx.scene.control.Alert errorAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                errorAlert.setTitle("System Error");
                errorAlert.setHeaderText("Unexpected Error");
                errorAlert.setContentText("An unexpected error occurred while deleting the client.\nError: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }
}
