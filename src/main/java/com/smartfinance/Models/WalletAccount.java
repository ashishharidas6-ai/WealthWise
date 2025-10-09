package com.smartfinance.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * WalletAccount acts as a digital piggy bank (like UPI wallet),
 * where deposits are limited by a fixed deposit limit.
 */
public class WalletAccount extends Account {

    private final DoubleProperty depositLimit;

    public WalletAccount(String ownerName, String accNumber, double balance, double depositLimit, String dateCreated) {
        super(ownerName, accNumber, balance, dateCreated);
        this.depositLimit = new SimpleDoubleProperty(this, "depositLimit", depositLimit);
    }

    // --- Getters and Properties ---
    public DoubleProperty depositLimitProperty() { return depositLimit; }

    public double getDepositLimit() { return depositLimit.get(); }

    public String getWalletAccountCreationDate() { return getDateCreated(); }

    // --- Piggy bank behavior ---
    public boolean canDeposit(double amount) {
        return (getBalance() + amount) <= getDepositLimit();
    }

    /**
     * Deposit money into wallet (with deposit limit check)
     */
    public boolean depositToWallet(double amount) {
        if (amount <= 0) {
            System.out.println("⚠️ Deposit amount must be positive.");
            return false;
        }
        if (canDeposit(amount)) {
            setBalance(getBalance() + amount);
            return true;
        } else {
            System.out.println("⚠️ Deposit exceeds wallet limit (" + getDepositLimit() + ")");
            return false;
        }
    }

    // --- Auto-refresh from DB ---
    public void refreshFromDB() {
        DatabaseDriver db = Model.getInstance().getDatabaseDriver();
        try (ResultSet rs = db.getWalletAccount(getOwnerName())) {
            if (rs != null && rs.next()) {
                setBalance(rs.getDouble("Balance"));
                this.depositLimit.set(rs.getDouble("DepositLimit"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
