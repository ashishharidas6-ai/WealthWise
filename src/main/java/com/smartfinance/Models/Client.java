package com.smartfinance.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Client {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty payeeAddress;
    private final ObjectProperty<WalletAccount> walletAccount;
    private final ObjectProperty<SavingsAccount> savingsAccount;
    private final ObjectProperty<LocalDate> dateCreated;
    private final ObjectProperty<RiskProfile> riskProfile;
    private ObservableList<Transaction> transactionHistory;

    public Client(String firstName, String lastName, String payeeAddress, WalletAccount wallet, SavingsAccount savings, LocalDate dateCreated) {
        this(firstName, lastName, payeeAddress, wallet, savings, dateCreated, RiskProfile.MODERATE); // Default risk
    }

    public Client(String firstName, String lastName, String payeeAddress, WalletAccount wallet, SavingsAccount savings, LocalDate dateCreated, RiskProfile riskProfile) {
        this.firstName = new SimpleStringProperty(this, "firstName", firstName);
        this.lastName = new SimpleStringProperty(this, "lastName", lastName);
        this.payeeAddress = new SimpleStringProperty(this, "payeeAddress", payeeAddress);
        this.walletAccount = new SimpleObjectProperty<>(this, "walletAccount", wallet);
        this.savingsAccount = new SimpleObjectProperty<>(this, "savingsAccount", savings);
        this.dateCreated = new SimpleObjectProperty<>(this, "dateCreated", dateCreated);
        this.riskProfile = new SimpleObjectProperty<>(this, "riskProfile", riskProfile);
    }

    // Properties
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty payeeAddressProperty() { return payeeAddress; }
    public ObjectProperty<WalletAccount> walletAccountProperty() { return walletAccount; }
    public ObjectProperty<SavingsAccount> savingsAccountProperty() { return savingsAccount; }
    public ObjectProperty<LocalDate> dateCreatedProperty() { return dateCreated; }
    public ObjectProperty<RiskProfile> riskProfileProperty() { return riskProfile; }

    // Getters
    public String getFirstName() { return firstName.get(); }
    public String getLastName() { return lastName.get(); }
    public String getPayeeAddress() { return payeeAddress.get(); }
    public WalletAccount getWalletAccount() { return walletAccount.get(); }
    public SavingsAccount getSavingsAccount() { return savingsAccount.get(); }
    public LocalDate getDateCreated() { return dateCreated.get(); }
    public RiskProfile getRiskProfile() { return riskProfile.get(); }
    public ObservableList<Transaction> getTransactionHistory() { return transactionHistory; }

    // Setters
    public void setFirstName(String firstName) { this.firstName.set(firstName); }
    public void setLastName(String lastName) { this.lastName.set(lastName); }
    public void setPayeeAddress(String payeeAddress) { this.payeeAddress.set(payeeAddress); }
    public void setWalletAccount(WalletAccount wallet) { this.walletAccount.set(wallet); }
    public void setSavingsAccount(SavingsAccount savings) { this.savingsAccount.set(savings); }
    public void setDateCreated(LocalDate dateCreated) { this.dateCreated.set(dateCreated); }
    public void setRiskProfile(RiskProfile riskProfile) { this.riskProfile.set(riskProfile); }
    public void setTransactionHistory(ObservableList<Transaction> transactionHistory) { this.transactionHistory = transactionHistory; }

    @Override
    public String toString() {
        return String.format("Client[Name: %s %s, Payee: %s]",
                getFirstName(), getLastName(), getPayeeAddress());
    }
}