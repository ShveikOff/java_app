// Task 1 account number с типом int не может принять целиком текущий формат номеров счетов типа 4169 5853 5823 2345
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

    // Метод для инициализации базы даннхы и таблицы счетов при запуске приложения
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
        } catch (SQLException e) {// Обработка исключений, связанных с работой базой данных
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
            preparedStatement.setString(2, clientPincode); // Установка значения третьего параметра (пинкод)
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
            preparedStatement.setInt(2, clientID); // Устновка значения второго параметра (номер клиента)
            preparedStatement.setDouble(3, initialBalance); // Установка значения третьего параметра (начальный баланс)
            preparedStatement.executeUpdate(); // Выполнение SQL запроса на добавление записи
        } catch (SQLException e) { // Обработка исключений связанных с работой с базой данных
            System.err.println("Error adding account: " + e.getMessage()); // Вывод сообщения об ошибке
            ErrorLogger.logError("Error adding account: " + e.getMessage());
        }
    }

    // Метод для получения баланса счета из базы данных
    public static double getBalance(int accountNumber) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number = ?")) {
            preparedStatement.setInt(1, accountNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting balance: " + e.getMessage());
            ErrorLogger.logError("Error getting balance: " + e.getMessage());
        }
        ErrorLogger.logError("Account not found: " + accountNumber);
        return 0.0;
    }
    /*
    public static double getBalance(int accountNumber) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); // Установка соединения с базой данных
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number = ?")) { // Подготовка SQL запроса с парамтром
            preparedStatement.setInt(1, accountNumber); // Установка значения параметра (номер счета)
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполнение SQL запроса на получение результата
            if (resultSet.next()) { // Проверка наличия результата
                return resultSet.getDouble("balance"); // Возвращение баланса из результата запроса
            }
        } catch (SQLException e) { // Обработка исключений, связанных с работой с базой данных
            System.err.println("Error getting balance: " + e.getMessage()); // Вывод сообщения об ошибке
            ErrorLogger.logError("Error getting balance: " + e.getMessage());
        }
        ErrorLogger.logError("Account not found:");
        return 0.0; // Возвращаем 0, если счет не найден или произошла ошибка
    }
    */

    public static String getClientName(int client_id) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); // Установка соединения с базой даннхы
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT client_name FROM clients WHERE client_id = ?")) { // Подготовка SQL запроса с параметрами
            preparedStatement.setInt(1, client_id); // Установка значения параметра (номер клиента)
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполнение SQL запроса на получение результата
            if (resultSet.next()) { // Проверка наличия результата
                return resultSet.getString("client_name"); // Возращение имени клиента из результата запроса
            }
        } catch (SQLException e) { // Обработка исключений, связанных с работой с базой данных
            System.err.println("Error getting client_name: " + e.getMessage()); // Вывод сообщения об ошибке
            ErrorLogger.logError("Error getting client_name: " + e.getMessage());
        }
        ErrorLogger.logError("client_name not found");
        return "client_name not found"; // Возвращаем сообщение, если номер клиента не был найден или произошла ошибка
    }

    public static boolean authenticate(int clientID, String pincode) {
        String pincodeFromDB = null;
    
        // Попытка извлечения пинкода из базы данных для данного clientID
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT client_pincode FROM clients WHERE client_id = ?")) {
            preparedStatement.setInt(1, clientID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                pincodeFromDB = resultSet.getString("client_pincode");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching pincode: " + e.getMessage());
            ErrorLogger.logError("Error fetching pincode: " + e.getMessage());
            return false; // Возвращаем false в случае ошибки
        }
    
        // Сравниваем извлеченный пинкод с предоставленным
        return pincode != null && pincode.equals(pincodeFromDB);
    }
    
    // Метод для обновления баланса счета в базе данных
    private static void updateBalance(int accountNumber, double amountToAdd, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE account_number = ?")) {
            preparedStatement.setDouble(1, amountToAdd);
            preparedStatement.setInt(2, accountNumber);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating balance failed, no rows affected.");
            }
        }
    }

    /*
    public static void updateBalance(int accountNumber, double amountToAdd) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); // Установка соединения с базой данных
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE account_number = ?")) { // Подготовка SQL запроса с параметрами
            preparedStatement.setDouble(1, amountToAdd); // Установка значения первого параметра (прибавляемой к текущему балансу суммы)
            preparedStatement.setInt(2, accountNumber); // Установка значения второго параметра (номер счета)
            preparedStatement.executeUpdate(); // Выполнение SQL запроса на обновление записи
        } catch (SQLException e) { // Обработка исключений, связанных с базой данных
            System.err.println("Error updating balance: " + e.getMessage()); // Вывод сообщения об ошибке
            ErrorLogger.logError("Error updating balance: " + e.getMessage());
        }
    }
    */

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

    public static int getAccountNumberByClientId(int client_id) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); // Установка соединения с базой данных
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT account_number FROM accounts WHERE client_id = ?")) { // Подготовка SQL запроса с параметрами
            preparedStatement.setInt(1, client_id); // Установка значения параметра (номер телефона клиента)
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

    // Метод для совершения транзакий account to account
    public static void transAction (int sender_accountNumber, int receiver_accountNubmer, double transaction_amount) {
        double sender_balance = getBalance(sender_accountNumber);
        if (sender_balance < transaction_amount) {
            System.out.println("not enough money on balance");
            ErrorLogger.logError("not enough money on balance");
            return;
        }
        
        int sender_client_id = get_client_id_by_account_number(sender_accountNumber);
        int receiver_client_id = get_client_id_by_account_number(receiver_accountNubmer);

        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Begin transaction
            connection.setAutoCommit(false);
    
            // Insert transaction record
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
    
            // Update sender balance
            double sender_amountToAdd = transaction_amount * (-1);
            updateBalance(sender_accountNumber, sender_amountToAdd, connection);
    
            // Update receiver balance
            double receiver_amountToAdd = transaction_amount;
            updateBalance(receiver_accountNubmer, receiver_amountToAdd, connection);
    
            // Commit transaction
            connection.commit();
        } catch (SQLException e) {
            System.err.println("Error during transaction: " + e.getMessage());
            ErrorLogger.logError("Error during transaction: " + e.getMessage());
        }
        
        /*
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); // Установка соединения с базой данных
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transactions VALUES (?, ?, ?, ?, ?, ?)")) { // Подготовка SQL запроса с параметрами
            preparedStatement.setInt(1, sender_accountNumber); // Установка значения первого параметра (номер счета отправителя)
            preparedStatement.setInt(2, receiver_accountNubmer); // Установка значения второго параметра (номер счета получателя)
            preparedStatement.setInt(3, sender_client_id); // Установка значения третьего параметра (номер клиента отправителя)
            preparedStatement.setInt(4, receiver_client_id); // Установка значения четвертого параметра (номер клиента получателя)
            preparedStatement.setDouble(5, transaction_amount); // Установка значения пятого параметра (сумма транзакции)
            LocalDateTime curDateTime = LocalDateTime.now();
            preparedStatement.setObject(6, curDateTime); // Установка значения шестого параметра (время транзакции)
            preparedStatement.executeUpdate(); // Выполнение SQL запроса на обновление записи
        } catch (SQLException e) { // Обработка исключений, связанных с базой данных
            System.err.println("Error during transaction: " + e.getMessage()); // Вывод сообщения об ошибке
            ErrorLogger.logError("Error during transaction: " + e.getMessage()); 
        }
        double sender_amountToAdd = transaction_amount * (-1);
        double receiver_amountToAdd = transaction_amount;
        updateBalance(sender_accountNumber, sender_amountToAdd);
        updateBalance(receiver_accountNubmer, receiver_amountToAdd);
        */
    }
    
    public static void phonetransAction (int sender_account_number, String receiver_client_phone, double transaction_amount) {
        int receiver_account_number = getAccountNumberByClientPhone(receiver_client_phone);
        if (receiver_account_number == 0) {
            System.out.println("unknown phone number");
            ErrorLogger.logError("unknown phone number");
            return;
        }
        transAction(sender_account_number, receiver_account_number, transaction_amount);
    }

    public static ObservableList<Transaction> getTransactions() {
        ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();
    
        String query = "SELECT sender_account_number, receiver_account_number, sender_client_id, " +
                       "receiver_client_id, amount, transaction_time FROM transactions";
    
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
    
            while (resultSet.next()) {
                String userCard = String.valueOf(resultSet.getInt("sender_account_number"));
                String userName = String.valueOf(resultSet.getInt("sender_client_id"));
                double amount = resultSet.getDouble("amount");
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
    
    /*
    public static ObservableList<Transaction> getTransactions() {
        ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();

        String query = "SELECT sender_account_number, receiver_account_number, sender_client_id, " +
                       "receiver_client_id, amount, transaction_time FROM transactions";

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String userCard = String.valueOf(resultSet.getInt("sender_account_number"));
                String userName = String.valueOf(resultSet.getInt("sender_client_id"));
                double amount = resultSet.getDouble("amount");
                LocalDate date = resultSet.getTimestamp("transaction_time").toLocalDateTime().toLocalDate();

                Transaction transaction = new Transaction(userCard, userName, amount, date);
                transactionsList.add(transaction);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
            ErrorLogger.logError("Error fetching transactions: " + e.getMessage());
        }

        return transactionsList;
    }
    */
}

/*
public static List<Transaction> getTransactionHistory(int clientID) {
    List<Transaction> transactions = new ArrayList<>();

    // SQL запрос для получения истории транзакций по client_id
    String query = "SELECT * FROM transactions WHERE sender_client_id = ? OR receiver_client_id = ?";

    try (Connection connection = DriverManager.getConnection(DATABASE_URL);
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, clientID);
        preparedStatement.setInt(2, clientID);
        ResultSet resultSet = preparedStatement.executeQuery();

        // Обработка результата запроса и добавление транзакций в список
        while (resultSet.next()) {
            int senderAccountNumber = resultSet.getInt("sender_account_number");
            int receiverAccountNumber = resultSet.getInt("receiver_account_number");
            int senderClientID = resultSet.getInt("sender_client_id");
            int receiverClientID = resultSet.getInt("receiver_client_id");
            double amount = resultSet.getDouble("amount");
            LocalDateTime transactionTime = resultSet.getTimestamp("transaction_time").toLocalDateTime();

            Transaction transaction = new Transaction(senderAccountNumber, receiverAccountNumber, senderClientID, receiverClientID, amount, transactionTime);
            transactions.add(transaction);
        }
    } catch (SQLException e) {
        System.err.println("Error fetching transaction history: " + e.getMessage());
        ErrorLogger.logError("Error fetching trasaction history: " + e.getMessage()); 
    }

    return transactions;
}
*/

/*
// Класс Transaction для хранения данных о транзакциях
public static class Transaction {
    private int senderAccountNumber;
    private int receiverAccountNumber;
    private int senderClientID;
    private int receiverClientID;
    private double amount;
    private LocalDateTime transactionTime;

    public Transaction(int senderAccountNumber, int receiverAccountNumber, int senderClientID, int receiverClientID, double amount, LocalDateTime transactionTime) {
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.senderClientID = senderClientID;
        this.receiverClientID = receiverClientID;
        this.amount = amount;
        this.transactionTime = transactionTime;
    }

    // Геттеры для полей
    public int getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public int getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public int getSenderClientID() {
        return senderClientID;
    }

    public int getReceiverClientID() {
        return receiverClientID;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "senderAccountNumber=" + senderAccountNumber +
                ", receiverAccountNumber=" + receiverAccountNumber +
                ", senderClientID=" + senderClientID +
                ", receiverClientID=" + receiverClientID +
                ", amount=" + amount +
                ", transactionTime=" + transactionTime +
                '}';
    }
}
*/
