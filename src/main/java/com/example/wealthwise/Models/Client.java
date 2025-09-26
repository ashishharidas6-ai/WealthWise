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
        this.fname = new SimpleStringProperty(this, "First name", fname != null ? fname : "");
        this.lname = new SimpleStringProperty(this, "Last name", lname != null ? lname : "");
        this.pAddress = new SimpleStringProperty(this, "Payee Address", pAddress != null ? pAddress : "");

        // Ensure accounts are never null
        this.walletAcc = new SimpleObjectProperty<>(this, "Wallet account",
                walletAcc != null ? walletAcc : createDefaultAccount("Wallet"));
        this.savAcc = new SimpleObjectProperty<>(this, "Savings account",
                savAcc != null ? savAcc : createDefaultAccount("Savings"));

        this.date = new SimpleObjectProperty<>(this, "date", date != null ? date : LocalDate.now());
    }

    // Utility: Create a safe placeholder account
    private Account createDefaultAccount(String type) {
        // Adjust constructor if Account has different fields
        if ("Wallet".equals(type)) {
            return new WalletAccount("Default", "0", 0.0, 1000, LocalDate.now().toString());
        } else if ("Savings".equals(type)) {
            return new SavingsAccount("Default", "0", 0.0, 500.0, LocalDate.now().toString());
        } else {
            return null;
        }
    }

    // JavaFX Properties (for UI binding)
    public StringProperty firstNameProperty() { return fname; }
    public StringProperty lastNameProperty() { return lname; }
    public StringProperty payeeAddressProperty() { return pAddress; }
    public ObjectProperty<Account> walletAccountProperty() { return walletAcc; }
    public ObjectProperty<Account> savingsAccountProperty() { return savAcc; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }

    // Convenience getters (null-safe)
    public String getFirstName() { return fname.get(); }
    public String getLastName() { return lname.get(); }
    public String getPayeeAddress() { return pAddress.get(); }
    public Account getWalletAccount() { return walletAcc.get(); }
    public Account getSavingsAccount() { return savAcc.get(); }
    public LocalDate getDate() { return date.get(); }

    // Null-safe string for debugging & UI
    @Override
    public String toString() {
        String walletStr = (getWalletAccount() != null) ? "Wallet#" + getWalletAccount().getAccNumber() : "No Wallet";
        String savStr = (getSavingsAccount() != null) ? "Savings#" + getSavingsAccount().getAccNumber() : "No Savings";

        return String.format("%s %s (%s) [%s | %s]",
                getFirstName(), getLastName(), getPayeeAddress(), walletStr, savStr);
    }
}
