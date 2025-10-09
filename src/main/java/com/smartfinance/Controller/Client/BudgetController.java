package com.smartfinance.Controller.Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.ResourceBundle;

public class BudgetController implements Initializable {
    @FXML
    private Button create_budget_btn;
    @FXML
    private ListView<String> budget_list;
    @FXML
    private TextField category_budget;
    @FXML
    private TextField amount_budget;
    @FXML
    private TextField change_budget;
    @FXML
    private TextField change_amount;
    @FXML
    private Button change_btn;
    @FXML
    private TextField delete_category;
    @FXML
    private Button delete_btn;

    private ObservableList<String> budgets = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        budget_list.setItems(budgets);

        // Sample budgets


        // ✅ Create budget
        create_budget_btn.setOnAction(event -> {
            String category = category_budget.getText().trim();
            String amount = amount_budget.getText().trim();

            if (!category.isEmpty() && !amount.isEmpty()) {
                budgets.add(category + ": ₹" + amount);
                category_budget.clear();
                amount_budget.clear();
            }
        });

        // ✅ Change budget (supports selection OR category name)
        change_btn.setOnAction(event -> {
            String categoryToChange = "";
            String newAmount = change_amount.getText().trim();

            int selectedIndex = budget_list.getSelectionModel().getSelectedIndex();

            if (selectedIndex >= 0) {
                // Use selected item's category
                String selectedItem = budgets.get(selectedIndex);
                String[] parts = selectedItem.split(": ₹");
                if (parts.length == 2) {
                    categoryToChange = parts[0];
                } else {
                    showWarning("Invalid Selection", "Selected budget format is invalid.");
                    return;
                }
            } else {
                // Use category from text field
                categoryToChange = change_budget.getText().trim();
            }

            if (categoryToChange.isEmpty() || newAmount.isEmpty()) {
                showWarning("Invalid Input", "Please enter the category (or select) and new amount.");
                return;
            }

            // Find and update the budget
            boolean found = false;
            for (int i = 0; i < budgets.size(); i++) {
                if (budgets.get(i).startsWith(categoryToChange + ":")) {
                    budgets.set(i, categoryToChange + ": ₹" + newAmount);
                    found = true;
                    break;
                }
            }

            if (!found) {
                showWarning("Category Not Found", "No budget found for category: " + categoryToChange);
            }

            change_budget.clear();
            change_amount.clear();
        });

        // ✅ Delete budget (by selection OR category name)
        delete_btn.setOnAction(event -> {
            int selectedIndex = budget_list.getSelectionModel().getSelectedIndex();
            String categoryToDelete = delete_category.getText().trim();

            if (selectedIndex >= 0) {
                budgets.remove(selectedIndex);
            } else if (!categoryToDelete.isEmpty()) {
                boolean removed = budgets.removeIf(item -> item.startsWith(categoryToDelete));
                if (!removed) {
                    showWarning("Category not found",
                            "No budget found with category: " + categoryToDelete);
                }
                delete_category.clear();
            } else {
                showWarning("Nothing to delete",
                        "Please select a budget or enter a category to delete.");
            }
        });
    }

    /**
     * Utility method to show warning alerts.
     */
    private void showWarning(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
