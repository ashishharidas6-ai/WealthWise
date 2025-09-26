package com.example.wealthwise.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class WalletAccount extends Account {

    private final IntegerProperty transactionLimit;


    public WalletAccount(String ownerName, String accNumber, double balance, int transactionLimit, String dateCreated) {
        super(ownerName, accNumber, balance, dateCreated);
        this.transactionLimit=new SimpleIntegerProperty(this,"transactionLimit",transactionLimit);
    }
    public IntegerProperty transactionLimitProperty() { return transactionLimit;}
    public int getTransactionLimit() { return transactionLimit.get(); }


}
