package com.example;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.ZoneId;

// Класс для управления базой данных
public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:mobile_bank.db"; // Путь к базе данных SQLite

    // Метод для инициализации базы данных и таблицы счетов при запуске приложения
    public static void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) { // Установка соединения с базой данных
            // SQL запрос для создания таблицы clients, если она не существует
            String createClientsTableQuery = "CREATE TABLE IF NOT EXISTS clients (" +
                                            "client_id INT PRIMARY KEY," +
                                            "client_name VARCHAR," +
                                            "client_pincode VARCHAR," +
                                            "client_phone VARCHAR)";
            try (Statement statement = connection.createStatement()) { // Создание объекта Statement для выполнения SQL запроса
                statement.executeUpdate(createClientsTableQuery); // Выполнение SQL запроса для создания таблицы
            }            
            // SQL запрос для создания таблицы accounts, если она не существует
            String createAccountsTableQuery = "CREATE TABLE IF NOT EXISTS accounts (" +
                                                "account_number INT PRIMARY KEY," +
                                                "client_id INT," +
                                                "balance REAL)";
            try (Statement statement = connection.createStatement()) { // Создание объекта Statement для выполнения SQL запроса
                statement.executeUpdate(createAccountsTableQuery); // Выполнение SQL запроса для создания таблицы
            }
            // SQL запрос для создания таблицы transactions, если она не существует
            String createTransactionsTableQuery = "CREATE TABLE IF NOT EXISTS transactions (" +
                                                    "sender_account_number INT," +
                                                    "receiver_account_number INT," +
                                                    "sender_client_id INT," +
                                                    "receiver_client_id INT," +
                                                    "amount REAL," +
                                                    "transaction_time DATETIME)";
            try (Statement statement = connection.createStatement()) { // Создание объекта Statement для выполнения SQL запроса
                statement.executeUpdate(createTransactionsTableQuery); // Выполнение SQL запроса для создания таблицы
            }         
        } catch (SQLException e) { // Обработка исключений, связанных с работой базой данных
            System.err.println("Error intializing database " + e.getMessage()); // Вывод сообщения об ошибке
            ErrorLogger.logError("Error intializing database: " + e.getMessage());
        }
    }

    // Метод для добавления нового клиента в базу данных
    public static void addClient(int clientID, String clientName, String clientPincode, String clientPhone) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); // Установка соединения с базой данных
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO clients VALUES (?, ?, ?, ?)")) { // Подготовка SQL запроса с параметрами
            preparedStatement.setInt(1, clientID); // Установка значения первого параметра (номер клиента)
            preparedStatement.setString(2, clientName); // Установка значения второго параметра (имя клиента)
            preparedStatement.setString(3, clientPincode); // Установка значения третьего параметра (пинкод)
            preparedStatement.setString(4, clientPhone); // Установка значения четвертого параметра (номер телефона)
            preparedStatement.executeUpdate(); // Выполнение SQL запроса на добавление записи
        } catch (SQLException e) { // Обработка исключений, связанных с работой с базой данных
            System.err.println("Error adding client: " + e.getMessage()); // Вывод сообщений об ошибке
            ErrorLogger.logError("Error adding client: " + e.getMessage());
        }
    }

    // Метод для добавления нового счета в базу данных
    public static void addAccount(int accountNumber, int clientID, double initialBalance) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); // Установка соединения с базой данных 
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accounts VALUES (?, ?, ?)")) { // Подготовка SQL запроса с параметрами
            preparedStatement.setInt(1, accountNumber); // Установка значения первого параметра (номер счета)
            preparedStatement.setInt(2, clientID); // Установка значения второго параметра (номер клиента)
            preparedStatement.setDouble(3, initialBalance); // Установка значения третьего параметра (начальный баланс)
            preparedStatement.executeUpdate(); // Выполнение SQL запроса на добавление записи
        } catch (SQLException e) { // Обработка исключений связанных с работой с базой данных
            System.err.println("Error adding account: " + e.getMessage()); // Вывод сообщения об ошибке
            ErrorLogger.logError("Error adding account: " + e.getMessage());
        }
    }

    // Метод для получения баланса счета из базы данных
    public static double getBalance(int accountNumber) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); // Установка соединения с базой данных
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number = ?")) { // Подготовка SQL запроса с параметром
            preparedStatement.setInt(1, accountNumber); // Установка значения параметра (номер счета)
            try (ResultSet resultSet = preparedStatement.executeQuery()) { // Выполнение SQL запроса на получение результата
                if (resultSet.next()) { // Проверка наличия результата
                    return resultSet.getDouble("balance"); // Возвращение баланса из результата запроса
                }
            }
        } catch (SQLException e) { // Обработка исключений, связанных с работой с базой данных
            System.err.println("Error getting balance: " + e.getMessage()); // Вывод сообщения об ошибке
            ErrorLogger.logError("Error getting balance: " + e.getMessage());
        }
        ErrorLogger.logError("Account not found: " + accountNumber);
        return 0.0; // Возвращаем 0, если счет не найден или произошла ошибка
    }

    // Метод для получения имени клиента из базы данных по его ID
    public static String getClientName(int client_id) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); // Установка соединения с базой данных
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT client_name FROM clients WHERE client_id = ?")) { // Подготовка SQL запроса с параметрами
            preparedStatement.setInt(1, client_id); // Установка значения параметра (номер клиента)
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполнение SQL запроса на получение результата
            if (resultSet.next()) { // Проверка наличия результата
                return resultSet.getString("client_name"); // Возвращение имени клиента из результата запроса
            }
        } catch (SQLException e) { // Обработка исключений, связанных с работой с базой данных
            System.err.println("Error getting client_name: " + e.getMessage()); // Вывод сообщения об ошибке
            ErrorLogger.logError("Error getting client_name: " + e.getMessage());
        }
        ErrorLogger.logError("client_name not found");
        return "client_name not found"; // Возвращаем сообщение, если номер клиента не был найден или произошла ошибка
    }

    // Метод для аутентификации клиента по его ID и пинкоду
    public static boolean authenticate(int clientID, String pincode) {
        String pincodeFromDB = null; // Переменная для хранения пинкода из базы данных
    
        // Попытка извлечения пинкода из базы данных для данного clientID
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); // Установка соединения с базой данных
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT client_pincode FROM clients WHERE client_id = ?")) { // Подготовка SQL запроса с параметром
            preparedStatement.setInt(1, clientID); // Установка значения параметра (номер клиента)
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполнение SQL запроса на получение результата
            if (resultSet.next()) { // Проверка наличия результата
                pincodeFromDB = resultSet.getString("client_pincode"); // Сохранение пинкода из результата запроса
            }
        } catch (SQLException e) { // Обработка исключений, связанных с работой с базой данных
            System.err.println("Error fetching pincode: " + e.getMessage()); // Вывод сообщения об ошибке
            ErrorLogger.logError("Error fetching pincode: " + e.getMessage());
            return false; // Возвращаем false в случае ошибки
        }
    
        // Сравниваем извлеченный пинкод с предоставленным
        return pincode != null && pincode.equals(pincodeFromDB); // Возвращаем результат сравнения пинкодов
    }
    // Метод для обновления баланса счета в базе данных
    private static void updateBalance(int accountNumber, double amountToAdd, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE account_number = ?")) {
            preparedStatement.setDouble(1, amountToAdd); // Установка значения параметра (сумма для добавления)
            preparedStatement.setInt(2, accountNumber); // Установка значения параметра (номер счета)
            int affectedRows = preparedStatement.executeUpdate(); // Выполнение SQL запроса на обновление баланса
            if (affectedRows == 0) {
                throw new SQLException("Updating balance failed, no rows affected."); // Исключение, если обновление не затронуло ни одной строки
            }
        }
    }

    // Метод для получения номера счета по номеру телефона клиента
    public static int getAccountNumberByClientPhone(String clientPhone) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); // Установка соединения с базой данных
        PreparedStatement preparedStatement = connection.prepareStatement(
            "SELECT account_number FROM accounts " +
            "INNER JOIN clients ON accounts.client_id = clients.client_id " +
            "WHERE clients.client_phone = ?")) { // Подготовка SQL запроса с параметрами
            preparedStatement.setString(1, clientPhone); // Установка значения параметра (номер телефона клиента)
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполнение SQL запроса на получение результата
            if (resultSet.next()) { // Проверка наличия результата
                return resultSet.getInt("account_number"); // Возвращение номера счета из результата запроса
            }
        } catch (SQLException e) { // Обработка исключений, связанных с работой с базой данных
            System.err.println("Error getting account number by client phone: " + e.getMessage()); // Вывод сообщения об ошибке
            ErrorLogger.logError("Error getting account number by client phone: " + e.getMessage());
        }
        ErrorLogger.logError("Account not found");
        return 0; // Возвращаем 0, если счет не найден или произошла ошибка
    }

    // Метод для получения номера счета по номеру клиента
    public static int getAccountNumberByClientId(int client_id) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); // Установка соединения с базой данных
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT account_number FROM accounts WHERE client_id = ?")) { // Подготовка SQL запроса с параметрами
            preparedStatement.setInt(1, client_id); // Установка значения параметра (номер клиента)
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполнение SQL запроса на получение результата
            if (resultSet.next()) { // Проверка наличия результата
                return resultSet.getInt("account_number"); // Возвращение номера счета из результата запроса
            }
        } catch (SQLException e) { // Обработка исключений, связанных с работой с базой данных
            System.err.println("Error getting account number by client_id: " + e.getMessage()); // Вывод сообщения об ошибке
            ErrorLogger.logError("Error getting account number by client_id: " + e.getMessage());
        }
        ErrorLogger.logError("Account not found");
        return 0; // Возвращаем 0, если счет не найден или произошла ошибка
    }

    // Метод для обновления пинкода клиента
    public static void updateClientPincode(int clientID, String currentPincode, String newPincode) {
        String currentPincodeFromDB = null;

        // Сначала проверим, соответствует ли текущий пинкод
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT client_pincode FROM clients WHERE client_id = ?")) {
            preparedStatement.setInt(1, clientID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                currentPincodeFromDB = resultSet.getString("client_pincode");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching current pincode: " + e.getMessage());
            ErrorLogger.logError("Error fetching current pincode: " + e.getMessage());
            return;
        }

        // Если пинкод из базы данных соответствует предоставленному текущему пинкоду, обновим его
        if (currentPincode != null && currentPincode.equals(currentPincodeFromDB)) {
            try (Connection connection = DriverManager.getConnection(DATABASE_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE clients SET client_pincode = ? WHERE client_id = ?")) {
                preparedStatement.setString(1, newPincode);
                preparedStatement.setInt(2, clientID);
                preparedStatement.executeUpdate();
                System.out.println("Pincode updated successfully.");
            } catch (SQLException e) {
                System.err.println("Error updating pincode: " + e.getMessage());
                ErrorLogger.logError("Error updating pincode: " + e.getMessage());
            }
        } else {
            System.out.println("Current pincode does not match.");
            ErrorLogger.logError("Current pincode does not match.");
        }
    }

    // Метод для получения client_id по account_number
    public static int get_client_id_by_account_number (int accountNumber) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); // Установка соединения с базой данных
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT client_id FROM accounts WHERE account_number = ?")) { // Подготовка SQL запроса с параметрами
            preparedStatement.setInt(1, accountNumber); // Установка значения параметра (номер счета)
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполнение SQL запроса на получение результата
            if (resultSet.next()) { // Проверка наличия результата
                return resultSet.getInt("client_id"); // Возвращение номера клиента из результата запроса
            }
        } catch (SQLException e) { // Обработка исключений, связанных с работой с базой данных
            System.err.println("Error getting balance: " + e.getMessage()); // Вывод сообщения об ошибке
            ErrorLogger.logError("Error getting balance: " + e.getMessage());
        }
        ErrorLogger.logError("Account not found");
        return 0; // Возвращаем 0, если счет не найден или произошла ошибка
    }

    // Метод для совершения транзакций между счетами
    public static void transAction (int sender_accountNumber, int receiver_accountNubmer, double transaction_amount) {
        double sender_balance = getBalance(sender_accountNumber); // Получение баланса отправителя
        if (sender_balance < transaction_amount) { // Проверка, достаточно ли средств на счете
            System.out.println("not enough money on balance");
            ErrorLogger.logError("not enough money on balance");
            return;
        }

        int sender_client_id = get_client_id_by_account_number(sender_accountNumber); // Получение client_id отправителя
        int receiver_client_id = get_client_id_by_account_number(receiver_accountNubmer); // Получение client_id получателя

        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Начало транзакции
            connection.setAutoCommit(false);

            // Вставка записи о транзакции
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transactions VALUES (?, ?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, sender_accountNumber);
                preparedStatement.setInt(2, receiver_accountNubmer);
                preparedStatement.setInt(3, sender_client_id);
                preparedStatement.setInt(4, receiver_client_id);
                preparedStatement.setDouble(5, transaction_amount);
                LocalDateTime curDateTime = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(curDateTime);
                preparedStatement.setTimestamp(6, timestamp);
                preparedStatement.executeUpdate();
            }

            // Обновление баланса отправителя
            double sender_amountToAdd = transaction_amount * (-1);
            updateBalance(sender_accountNumber, sender_amountToAdd, connection);

            // Обновление баланса получателя
            double receiver_amountToAdd = transaction_amount;
            updateBalance(receiver_accountNubmer, receiver_amountToAdd, connection);

            // Подтверждение транзакции
            connection.commit();
        } catch (SQLException e) {
            System.err.println("Error during transaction: " + e.getMessage());
            ErrorLogger.logError("Error during transaction: " + e.getMessage());
        }
    }

    // Метод для совершения транзакций по номеру телефона клиента
    public static void phonetransAction (int sender_account_number, String receiver_client_phone, double transaction_amount) {
        int receiver_account_number = getAccountNumberByClientPhone(receiver_client_phone); // Получение номера счета получателя по номеру телефона
        if (receiver_account_number == 0) { // Проверка, найден ли номер счета
            System.out.println("unknown phone number");
            ErrorLogger.logError("unknown phone number");
            return;
        }
        transAction(sender_account_number, receiver_account_number, transaction_amount); // Вызов метода транзакции
    }

    // Метод для получения списка транзакций клиента
    public static ObservableList<Transaction> getTransactions(int client_id) {
        ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();

        String query = "SELECT sender_account_number, receiver_account_number, sender_client_id, " +
                    "receiver_client_id, amount, transaction_time FROM transactions " +
                    "WHERE sender_client_id = ? OR receiver_client_id = ?";
        
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
            PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, client_id);
            statement.setInt(2, client_id);
            
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String userCardPrefix = "41695853"; // Префикс для userCard
                String userCardSuffix = String.valueOf(resultSet.getInt("sender_account_number"));
                if (client_id == resultSet.getInt("sender_client_id")) {
                    userCardSuffix = String.valueOf(resultSet.getInt("receiver_account_number"));
                }
                String userCard = userCardPrefix + userCardSuffix; // Объединяем префикс и суффикс userCard
                String userName;
                double amount = resultSet.getDouble("amount");
                
                int sender_client_id = resultSet.getInt("sender_client_id");
                int receiver_client_id = resultSet.getInt("receiver_client_id");
                
                // Определение userName в зависимости от sender_client_id и receiver_client_id
                if (client_id == sender_client_id) {
                    userName = getClientName(receiver_client_id);
                    amount = -amount; // устанавливаем отрицательное значение для отправителя
                } else {
                    userName = getClientName(sender_client_id);
                }
                
                Timestamp timestamp = resultSet.getTimestamp("transaction_time");
                
                if (timestamp != null) {
                    LocalDateTime dateTime = timestamp.toLocalDateTime();
                    LocalDate date = dateTime.toLocalDate();
                    Transaction transaction = new Transaction(userCard, userName, amount, date);
                    transactionsList.add(transaction);
                } else {
                    System.err.println("Error: transaction_time is null for one of the transactions");
                    ErrorLogger.logError("Error: transaction_time is null for one of the transactions");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
            ErrorLogger.logError("Error fetching transactions: " + e.getMessage());
        }

        return transactionsList;
    }
}
