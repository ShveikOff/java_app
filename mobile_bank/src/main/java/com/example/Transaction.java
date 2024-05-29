package com.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class Transaction {
    private final SimpleStringProperty userCard;
    private final SimpleStringProperty userName;
    private final SimpleDoubleProperty amount;
    private final SimpleObjectProperty<LocalDate> date;

    public Transaction(String userCard, String userName, double amount, LocalDate date) {
        this.userCard = new SimpleStringProperty(userCard);
        this.userName = new SimpleStringProperty(userName);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleObjectProperty<>(date);
    }

    public String getUserCard() {
        return userCard.get();
    }

    public SimpleStringProperty userCardProperty() {
        return userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard.set(userCard);
    }

    public String getUserName() {
        return userName.get();
    }

    public SimpleStringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public double getAmount() {
        return amount.get();
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }
}
