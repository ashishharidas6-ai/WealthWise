package com.smartfinance.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Account {

    private final StringProperty ownerName;  // owner of the account
    private final StringProperty accNumber;  // unique number for each account
    private final DoubleProperty balance;    // current balance on the account
    private final StringProperty dateCreated; // date the account was created

    public Account(String ownerName, String accNumber, double balance, String dateCreated) {
        this.ownerName = new SimpleStringProperty(this, "ownerName",
                ownerName != null ? ownerName : "Unknown");
        this.accNumber = new SimpleStringProperty(this, "accNumber",
                accNumber != null ? accNumber : "0");
        this.balance = new SimpleDoubleProperty(this, "balance", balance);
        this.dateCreated = new SimpleStringProperty(this, "dateCreated",
                dateCreated != null ? dateCreated : "Unknown");
    }

    // JavaFX Properties
    public StringProperty ownerNameProperty() { return ownerName; }
    public StringProperty accNumberProperty() { return accNumber; }
    public DoubleProperty balanceProperty() { return balance; }
    public StringProperty dateCreatedProperty() { return dateCreated; }

    // Convenience Getters
    public String getOwnerName() { return ownerName.get(); }
    public String getAccNumber() { return accNumber.get(); }
    public double getBalance() { return balance.get(); }
    public String getDateCreated() { return dateCreated.get(); }

    // Convenience Setters
    public void setOwnerName(String ownerName) { this.ownerName.set(ownerName); }
    public void setAccNumber(String accNumber) { this.accNumber.set(accNumber); }
    public void setBalance(double balance) { this.balance.set(balance); }

    // Common account operations
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance.set(this.balance.get() + amount);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= this.balance.get()) {
            this.balance.set(this.balance.get() - amount);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Account[%s] Owner: %s, Balance: %.2f",
                getAccNumber(), getOwnerName(), getBalance());
    }
}
