package com.example.wealthwise.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Client {
    private final StringProperty fname;
    private final StringProperty lname;
    private final StringProperty pAddress;
    private final ObjectProperty<Account> walletAcc;
    private final ObjectProperty<Account> savAcc;
    private final ObjectProperty<LocalDate> date;

    public Client(String fname, String lname, String pAddress, Account walletAcc, Account savAcc, LocalDate date) {
        this.fname = new SimpleStringProperty(this, "First name", fname);
        this.lname = new SimpleStringProperty(this, "Last name", lname);
        this.pAddress = new SimpleStringProperty(this, "Payee Address", pAddress);
        this.walletAcc = new SimpleObjectProperty<>(this, "Wallet account", walletAcc);
        this.savAcc = new SimpleObjectProperty<>(this, "Savings account", savAcc);
        this.date = new SimpleObjectProperty<>(this, "date", date);
    }

    // JavaFX Properties (for UI binding)
    public StringProperty firstNameProperty() { return fname; }
    public StringProperty lastNameProperty() { return lname; }
    public StringProperty payeeAddressProperty() { return pAddress; }
    public ObjectProperty<Account> walletAccountProperty() { return walletAcc; }
    public ObjectProperty<Account> savingsAccountProperty() { return savAcc; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }

    // Convenience getters
    public String getFirstName() { return fname.get(); }
    public String getLastName() { return lname.get(); }
    public String getPayeeAddress() { return pAddress.get(); }
    public Account getWalletAccount() { return walletAcc.get(); }
    public Account getSavingsAccount() { return savAcc.get(); }
    public LocalDate getDate() { return date.get(); }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + " (" + getPayeeAddress() + ")";
    }
}
