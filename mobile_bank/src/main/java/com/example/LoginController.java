package com.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {
    public static int save_userId;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    public Label login_error;

    @FXML
    void loginAdmin(ActionEvent event) {
        try {
            int userId = Integer.parseInt(username.getText());
            if (DatabaseManager.authenticate(userId, password.getText())) {
                // Успешная аутентификация, переключение сцены
                UserSession.getInstance().setUserId(userId);
                login_error.setText("");
                try {
                    Stage stage = (Stage) loginBtn.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Обработка неудачной аутентификации (например, показать сообщение об ошибке)
                System.out.println("Authentication failed.");
                login_error.setText("Authentication failed.");
                ErrorLogger.logError("Authentication failed");
            }
        } catch (NumberFormatException e) {
            // Обработка ошибки формата числа
            System.out.println("Invalid user ID format.");
            login_error.setText("Invalid user ID format.");
            ErrorLogger.logError("Invalid user ID format: " + username.getText());
        }
    }

    @FXML
    void initialize() {
        assert loginBtn != null : "fx:id=\"loginBtn\" was not injected: check your FXML file 'login.fxml'.";
        assert main_form != null : "fx:id=\"main_form\" was not injected: check your FXML file 'login.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'login.fxml'.";
        assert username != null : "fx:id=\"username\" was not injected: check your FXML file 'login.fxml'.";
        assert login_error != null : "fx:id=\"login_error\" was not injected: check your FXML file 'login.fxml'.";

    }

}
