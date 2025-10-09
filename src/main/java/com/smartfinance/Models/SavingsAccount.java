package com.smartfinance.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SavingsAccount extends Account {
    private final DoubleProperty withdrawalLimit;

    public SavingsAccount(String ownerName, String accNumber, double balance, double withdrawalLimit, String dateCreated) {
        super(ownerName, accNumber, balance, dateCreated);
        this.withdrawalLimit = new SimpleDoubleProperty(this,"withdrawalLimit",withdrawalLimit);
    }
    public DoubleProperty withdrawalLimitProperty() {return withdrawalLimit;}
    public double getWithdrawalLimit() { return withdrawalLimit.get(); }
    public String getSavingsAccountCreationDate() { return getDateCreated(); }

    // Method to refresh account data from database
    public void refreshFromDB() {
        DatabaseDriver db = Model.getInstance().getDatabaseDriver();
        try (ResultSet rs = db.getSavingsAccount(getOwnerName())) {
            if (rs != null && rs.next()) {
                setBalance(rs.getDouble("Balance"));
                // Update withdrawal limit if needed
                // this.withdrawalLimit.set(rs.getDouble("TransactionLimit"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
