package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorLogger {
    private static String logFilePath;

    // Метод инициализации, который создает текстовый файл, если его нет
    public static void initialize(String filePath) {
        logFilePath = filePath; // Установка пути к файлу лога
        File logFile = new File(logFilePath); // Создание объекта File для указанного пути
        try {
            if (!logFile.exists()) { // Проверка существования файла
                logFile.createNewFile(); // Создание файла, если он не существует
            }
        } catch (IOException e) {
            System.err.println("Error initializing log file: " + e.getMessage()); // Вывод сообщения об ошибке при инициализации файла
        }
    }

    // Статичный метод для записи сообщений в текстовый файл
    public static void logError(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // Получение текущего времени в формате "год-месяц-день час:минута:секунда"
            writer.write(timestamp + " - " + message); // Запись сообщения в формате "время - сообщение"
            writer.newLine(); // Переход на новую строку для следующего сообщения
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage()); // Вывод сообщения об ошибке при записи в файл лога
        }
    }
}
