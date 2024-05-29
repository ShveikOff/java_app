package com.example;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeController {

    // Аннотации FXML, указывающие на компоненты интерфейса, которые будут инициализированы из FXML файла
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    // Поля ввода, таблица, кнопки и метки из пользовательского интерфейса
    @FXML
    private TextField AddProducts_amount;

    @FXML
    private TableView<Transaction> AddProducts_tableView;

    @FXML
    private TableColumn<Transaction, String> AddProducts_col_userCard;

    @FXML
    private TableColumn<Transaction, String> AddProducts_col_userName;

    @FXML
    private TableColumn<Transaction, Double> AddProducts_col_amount;

    @FXML
    private TableColumn<Transaction, LocalDate> AddProducts_col_date;

    @FXML
    private Button AddProducts_sendBtn;

    @FXML
    private TextField AddProducts_userCard;

    @FXML
    private TextField AddProducts_userCard1;

    @FXML
    private Button History_getBtn;

    @FXML
    private Label balance;

    @FXML
    private Label card_number;

    @FXML
    private Button home_btn;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private Button payment_btn;

    @FXML
    private AnchorPane payment_form;

    @FXML
    private Button settings_btn;

    @FXML
    private Label username;

    @FXML
    private Button changePass_button;

    @FXML
    private TextField changePass_current;

    @FXML
    private TextField changePass_new;

    @FXML
    private AnchorPane change_password_form;

    @FXML
    private Button sign_out_btn;

    @FXML
    public Label payments_error;

    @FXML
    public Label settings_error;

    // Метод переключения на экран входа
    @FXML
    void switchToLoginScreen(ActionEvent event) {
        try {
            // Загрузка экрана входа из FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            // Переход на экран входа
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            
            // Установка значения текущего пользователя в "None"
            UserSession.getInstance().setUserId(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод инициализации контроллера
    @FXML
    void initialize() {
        // Проверка наличия внедренных элементов интерфейса
        assert AddProducts_amount != null : "fx:id=\"AddProducts_amount\" was not injected: check your FXML file 'home.fxml'.";
        assert AddProducts_col_amount != null : "fx:id=\"AddProducts_col_amount\" was not injected: check your FXML file 'home.fxml'.";
        assert AddProducts_col_date != null : "fx:id=\"AddProducts_col_date\" was not injected: check your FXML file 'home.fxml'.";
        assert AddProducts_col_userCard != null : "fx:id=\"AddProducts_col_userCard\" was not injected: check your FXML file 'home.fxml'.";
        assert AddProducts_col_userName != null : "fx:id=\"AddProducts_col_userName\" was not injected: check your FXML file 'home.fxml'.";
        assert AddProducts_sendBtn != null : "fx:id=\"AddProducts_sendBtn\" was not injected: check your FXML file 'home.fxml'.";
        assert AddProducts_tableView != null : "fx:id=\"AddProducts_tableView\" was not injected: check your FXML file 'home.fxml'.";
        assert AddProducts_userCard != null : "fx:id=\"AddProducts_userCard\" was not injected: check your FXML file 'home.fxml'.";
        assert AddProducts_userCard1 != null : "fx:id=\"AddProducts_userCard1\" was not injected: check your FXML file 'home.fxml'.";
        assert balance != null : "fx:id=\"balance\" was not injected: check your FXML file 'home.fxml'.";
        assert card_number != null : "fx:id=\"card_number\" was not injected: check your FXML file 'home.fxml'.";
        assert home_btn != null : "fx:id=\"home_btn\" was not injected: check your FXML file 'home.fxml'.";
        assert mainForm != null : "fx:id=\"mainForm\" was not injected: check your FXML file 'home.fxml'.";
        assert payment_btn != null : "fx:id=\"payment_btn\" was not injected: check your FXML file 'home.fxml'.";
        assert payment_form != null : "fx:id=\"payment_form\" was not injected: check your FXML file 'home.fxml'.";
        assert settings_btn != null : "fx:id=\"settings_btn\" was not injected: check your FXML file 'home.fxml'.";
        assert username != null : "fx:id=\"username\" was not injected: check your FXML file 'home.fxml'.";
        assert changePass_button != null : "fx:id=\"changePass_button\" was not injected: check your FXML file 'home.fxml'.";
        assert changePass_current != null : "fx:id=\"changePass_current\" was not injected: check your FXML file 'home.fxml'.";
        assert changePass_new != null : "fx:id=\"changePass_new\" was not injected: check your FXML file 'home.fxml'.";
        assert change_password_form != null : "fx:id=\"change_password_form\" was not injected: check your FXML file 'home.fxml'.";
        assert sign_out_btn != null : "fx:id=\"sign_out_btn\" was not injected: check your FXML file 'home.fxml'.";
        assert payments_error != null : "fx:id=\"payments_error\" was not injected: check your FXML file 'home.fxml'.";
        assert settings_error != null : "fx:id=\"settings_error\" was not injected: check your FXML file 'home.fxml'.";


        int currentUserId = UserSession.getInstance().getUserId();
        int cardNumber = DatabaseManager.getAccountNumberByClientId(currentUserId);
        double card_balance = DatabaseManager.getBalance(cardNumber);

        payment_btn.setOnAction(event -> {
            // Переключение на форму оплаты
            mainForm.setVisible(false); // Скрывает основную форму
            change_password_form.setVisible(false); // Скрывает форму смены пароля
            payment_form.setVisible(true); // Отображает форму оплаты
        });

        settings_btn.setOnAction(event -> {
            // Переключение на форму настроек
            mainForm.setVisible(false); // Скрывает основную форму
            payment_form.setVisible(false); // Скрывает форму оплаты
            change_password_form.setVisible(true); // Отображает форму смены пароля
        });

        home_btn.setOnAction(event -> {
            // Переключение на основную форму
            mainForm.setVisible(true); // Отображает основную форму
            change_password_form.setVisible(false); // Скрывает форму смены пароля
            payment_form.setVisible(false); // Скрывает форму оплаты
            double card_balance_now = DatabaseManager.getBalance(cardNumber);
            String card_balance_now_text = String.format("%.2f", card_balance_now);
            balance.setText(card_balance_now_text); // Обновляет баланс карты
        });

        sign_out_btn.setOnAction(this::switchToLoginScreen);
        
        AddProducts_sendBtn.setOnAction(event -> {
            try {
                String amount_text = AddProducts_amount.getText();
                double amount = Double.parseDouble(amount_text);
        
                if (!AddProducts_userCard.getText().isEmpty()) {
                    String receiver_cardNumber_text = AddProducts_userCard.getText().substring(8);
                    int receiver_cardNumber = Integer.parseInt(receiver_cardNumber_text);
                    DatabaseManager.transAction(cardNumber, receiver_cardNumber, amount);
                    payments_error.setText("");
                } else if (!AddProducts_userCard1.getText().isEmpty()) {
                    String receiver_phoneNumber_text = AddProducts_userCard1.getText();
                    DatabaseManager.phonetransAction(cardNumber, receiver_phoneNumber_text, amount);
                    payments_error.setText("");
                } else {
                    System.out.println("No receiver information provided");
                    payments_error.setText("No receiver information provided");
                    ErrorLogger.logError("No receiver information provided");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid format");
                payments_error.setText("Invalid format");
                ErrorLogger.logError("Invalid format: " + e);
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                payments_error.setText("An error occurred: " + e.getMessage());
                ErrorLogger.logError("An error occurred: " + e.getMessage());
            }
        });
        
        changePass_button.setOnAction(event -> {
            try {
                String currentPassText = changePass_current.getText();
                String newPassText = changePass_new.getText();
                
                // Вызов метода updateClientPincode для изменения пароля
                DatabaseManager.updateClientPincode(currentUserId, currentPassText, newPassText);
            } catch (NumberFormatException e) {
                System.out.println("Invalid format for pincode");
                ErrorLogger.logError("Invalid format for pincode: " + e);
            }
        });
        
        // Установка имени пользователя
        username.setText(DatabaseManager.getClientName(currentUserId));

        // Форматирование и отображение номера карты
        String card_number_text = String.valueOf(cardNumber);
        card_number.setText("4169 5853 " + card_number_text.substring(0, 4) + " " + card_number_text.substring(4));

        // Форматирование и отображение баланса карты
        String card_balance_text = String.format("%.2f", card_balance);
        balance.setText(card_balance_text);

        // Обработчик события для кнопки "History_getBtn" (название не указано)
        History_getBtn.setOnAction(event -> {
            // Настройка столбцов таблицы
            AddProducts_col_userCard.setCellValueFactory(cellData -> cellData.getValue().userCardProperty());
            AddProducts_col_userName.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
            AddProducts_col_amount.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
            AddProducts_col_date.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

            // Загрузка данных в таблицу
            loadTransactions(currentUserId);
        });

      

    }

    private void loadTransactions(int currentUserId) {
    ObservableList<Transaction> transactions = FXCollections.observableArrayList(DatabaseManager.getTransactions(currentUserId));
    AddProducts_tableView.setItems(transactions);
}

}
