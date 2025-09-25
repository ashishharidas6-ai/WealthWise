package com.example.wealthwise.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Account {

    private StringProperty ownerName; // owner of the account
    private StringProperty accNumber;// unique number for each account
    private DoubleProperty balance; // current balance on the account

    public Account(String ownerName, String accNumber, double balance) {
       this.ownerName=new SimpleStringProperty(this,"ownerName",ownerName);
       this.accNumber=new SimpleStringProperty(this,"accNumber",accNumber);
       this.balance=new SimpleDoubleProperty(this,"balance",balance);
    }

    public StringProperty ownerNameProperty() {
        return ownerName;
    }

    public StringProperty accNumberProperty() {
        return accNumber;
    }

    public DoubleProperty balanceProperty() {
        return balance;
    }
}
