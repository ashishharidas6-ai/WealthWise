package com.example.wealthwise.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class SavingsAccount extends Account {
    private final DoubleProperty withdrawalLimit;

    public SavingsAccount(String ownerName, String accNumber, double balance, double withdrawalLimit) {
        super(ownerName, accNumber, balance);
        this.withdrawalLimit = new SimpleDoubleProperty(this,"withdrawalLimit",withdrawalLimit);
    }
    public DoubleProperty withdrawalLimit() {return withdrawalLimit;}

}
