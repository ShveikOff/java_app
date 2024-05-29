package com.example;

import javafx.beans.property.SimpleDoubleProperty; // Импорт класса для работы с простыми свойствами типа double
import javafx.beans.property.SimpleObjectProperty; // Импорт класса для работы с простыми свойствами типа объекта
import javafx.beans.property.SimpleStringProperty; // Импорт класса для работы с простыми свойствами типа строки
import java.time.LocalDate; // Импорт класса для работы с датами

// Класс, представляющий транзакцию
public class Transaction {
    // Приватные свойства класса для данных транзакции
    private final SimpleStringProperty userCard; // Номер карты пользователя
    private final SimpleStringProperty userName; // Имя пользователя
    private final SimpleDoubleProperty amount; // Сумма транзакции
    private final SimpleObjectProperty<LocalDate> date; // Дата транзакции

    // Конструктор класса
    public Transaction(String userCard, String userName, double amount, LocalDate date) {
        // Инициализация свойств класса с помощью переданных параметров
        this.userCard = new SimpleStringProperty(userCard); // Номер карты пользователя
        this.userName = new SimpleStringProperty(userName); // Имя пользователя
        this.amount = new SimpleDoubleProperty(amount); // Сумма транзакции
        this.date = new SimpleObjectProperty<>(date); // Дата транзакции
    }

    // Методы доступа к свойствам класса

    public String getUserCard() {
        return userCard.get(); // Получение значения номера карты пользователя
    }

    public SimpleStringProperty userCardProperty() {
        return userCard; // Возвращает объект SimpleStringProperty для свойства userCard
    }

    public void setUserCard(String userCard) {
        this.userCard.set(userCard); // Устанавливает новое значение для номера карты пользователя
    }

    public String getUserName() {
        return userName.get(); // Получение значения имени пользователя
    }

    public SimpleStringProperty userNameProperty() {
        return userName; // Возвращает объект SimpleStringProperty для свойства userName
    }

    public void setUserName(String userName) {
        this.userName.set(userName); // Устанавливает новое значение для имени пользователя
    }

    public double getAmount() {
        return amount.get(); // Получение значения суммы транзакции
    }

    public SimpleDoubleProperty amountProperty() {
        return amount; // Возвращает объект SimpleDoubleProperty для свойства amount
    }

    public void setAmount(double amount) {
        this.amount.set(amount); // Устанавливает новое значение для суммы транзакции
    }

    public LocalDate getDate() {
        return date.get(); // Получение значения даты транзакции
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date; // Возвращает объект SimpleObjectProperty для свойства date
    }

    public void setDate(LocalDate date) {
        this.date.set(date); // Устанавливает новое значение для даты транзакции
    }
}
