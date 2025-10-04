package com.example.wealthwise.Models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Transaction {
    private final StringProperty sender;
    private final StringProperty receiver;
    private final DoubleProperty amount;
    private final StringProperty category;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty message;


    public Transaction(String sender, String receiver, double amount, String category, LocalDate date, String message) {
        this.sender = new SimpleStringProperty(this,"sender",sender);
        this.receiver = new SimpleStringProperty(this,"receiver",receiver);
        this.amount = new SimpleDoubleProperty(this,"amount",amount);
        this.category = new SimpleStringProperty(this,"category",category);
        this.date = new SimpleObjectProperty<>(this,"date",date);
        this.message = new SimpleStringProperty(this,"message",message);


    }
    public StringProperty senderProperty() {
        return sender;
    }
    public StringProperty receiverProperty() {
        return receiver;
    }
    public DoubleProperty amountProperty(){
        return amount;
    }
    public StringProperty categoryProperty(){return category;}
    public ObjectProperty<LocalDate> dateProperty(){
        return date;
    }
    public StringProperty messageProperty(){
        return message;
    }

    public String getSender() {
        return sender.get();
    }

    public String getReceiver() {
        return receiver.get();
    }

    public double getAmount() {
        return amount.get();
    }

    public String getCategory() {
        return category.get();
    }

    public LocalDate getDate() {
        return date.get();
    }

    public String getMessage() {
        return message.get();
    }

}
