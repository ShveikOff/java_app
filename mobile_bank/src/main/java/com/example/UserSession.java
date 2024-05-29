package com.example;

// Класс для управления пользовательской сессией
public class UserSession {
    private static UserSession instance; // Статическое поле для хранения единственного экземпляра класса

    private int userId; // Идентификатор пользователя

    // Приватный конструктор предотвращает создание других экземпляров класса
    private UserSession() {
    }

    // Метод для получения единственного экземпляра класса
    public static synchronized UserSession getInstance() {
        // Если экземпляр класса еще не создан, создаем его
        if (instance == null) {
            instance = new UserSession();
        }
        return instance; // Возвращает единственный экземпляр класса
    }

    // Методы доступа к идентификатору пользователя

    public int getUserId() {
        return userId; // Возвращает идентификатор пользователя
    }

    public void setUserId(int userId) {
        this.userId = userId; // Устанавливает идентификатор пользователя
    }
}
