package com.example.wealthwise.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class WalletAccount extends Account {

    private final IntegerProperty transactionLimit;


    public WalletAccount(String ownerName, String accNumber, double balance, int transactionLimit) {
        super(ownerName, accNumber, balance);
        this.transactionLimit=new SimpleIntegerProperty(this,"transactionLimit",transactionLimit);
    }
    public IntegerProperty transactionLimitProperty() { return transactionLimit;}


}
