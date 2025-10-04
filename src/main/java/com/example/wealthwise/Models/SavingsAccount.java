package com.example.wealthwise.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class SavingsAccount extends Account {
    private final DoubleProperty withdrawalLimit;

    public SavingsAccount(String ownerName, String accNumber, double balance, double withdrawalLimit, String dateCreated) {
        super(ownerName, accNumber, balance, dateCreated);
        this.withdrawalLimit = new SimpleDoubleProperty(this,"withdrawalLimit",withdrawalLimit);
    }
    public DoubleProperty withdrawalLimitProperty() {return withdrawalLimit;}
    public double getWithdrawalLimit() { return withdrawalLimit.get(); }
    public String getSavingsAccountCreationDate() { return getDateCreated(); }

}
