package com.smartfinance.Models;

import javafx.beans.property.*;

public class Investment {
    private final IntegerProperty id;
    private final StringProperty owner;
    private final StringProperty investmentType;
    private final DoubleProperty amountInvested;
    private final DoubleProperty currentValue;
    private final StringProperty dateInvested;

    public Investment(int id, String owner, String investmentType, double amountInvested, double currentValue, String dateInvested) {
        this.id = new SimpleIntegerProperty(this, "id", id);
        this.owner = new SimpleStringProperty(this, "owner", owner != null ? owner : "Unknown");
        this.investmentType = new SimpleStringProperty(this, "investmentType", investmentType != null ? investmentType : "Unknown");
        this.amountInvested = new SimpleDoubleProperty(this, "amountInvested", amountInvested);
        this.currentValue = new SimpleDoubleProperty(this, "currentValue", currentValue);
        this.dateInvested = new SimpleStringProperty(this, "dateInvested", dateInvested != null ? dateInvested : "Unknown");
    }

    // Properties
    public IntegerProperty idProperty() { return id; }
    public StringProperty ownerProperty() { return owner; }
    public StringProperty investmentTypeProperty() { return investmentType; }
    public DoubleProperty amountInvestedProperty() { return amountInvested; }
    public DoubleProperty currentValueProperty() { return currentValue; }
    public StringProperty dateInvestedProperty() { return dateInvested; }

    // Getters
    public int getId() { return id.get(); }
    public String getOwner() { return owner.get(); }
    public String getInvestmentType() { return investmentType.get(); }
    public double getAmountInvested() { return amountInvested.get(); }
    public double getCurrentValue() { return currentValue.get(); }
    public String getDateInvested() { return dateInvested.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setOwner(String owner) { this.owner.set(owner); }
    public void setInvestmentType(String investmentType) { this.investmentType.set(investmentType); }
    public void setAmountInvested(double amountInvested) { this.amountInvested.set(amountInvested); }
    public void setCurrentValue(double currentValue) { this.currentValue.set(currentValue); }
    public void setDateInvested(String dateInvested) { this.dateInvested.set(dateInvested); }

    // Computed properties
    public double getProfitLoss() {
        return getCurrentValue() - getAmountInvested();
    }

    public double getProfitLossPercentage() {
        if (getAmountInvested() == 0) return 0;
        return (getProfitLoss() / getAmountInvested()) * 100;
    }

    @Override
    public String toString() {
        return String.format("Investment[ID: %d, Type: %s, Invested: %.2f, Current: %.2f, P/L: %.2f (%.2f%%)]",
                getId(), getInvestmentType(), getAmountInvested(), getCurrentValue(), getProfitLoss(), getProfitLossPercentage());
    }
}