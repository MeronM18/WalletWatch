package com.example.fxproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.stage.StageStyle.UNDECORATED;

public class LoginController {
    @FXML
    private Button exitButton; //create exit button
    @FXML
    private Label loginMessage; //login message that reads to user
    @FXML
    private TextField usernameTextField; //username textfield that user inputs
    @FXML
    private PasswordField passwordTextField; //password textfield that user inputs

    private static final String USERNAME = "Meron"; //saves login information for successful login
    private static final String PASSWORD = "Matti";

    public void loginButtonOnAction(ActionEvent e) { //login button action
        String username = usernameTextField.getText(); //get user input for username and password
        String password = passwordTextField.getText();

        if (username.equals(USERNAME) && password.equals(PASSWORD)) { //if statement that checks if user input matches login creds
            loginMessage.setText("Login Successful!");
            closeCurrentWindow(); //closes login window
            openNewWindow(); //opens the main window
        } else if (!username.equals(USERNAME)) {
            loginMessage.setText("Wrong Username, try again");
        } else {
            loginMessage.setText("Wrong Password, try again");
        }
    }

    private void openNewWindow() { //opens the main window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WalletWatch.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("WalletWatch");
            stage.setScene(new Scene(root));
            stage.initStyle(UNDECORATED);
            stage.show(); // Show the new window
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close(); //method that closes the original window
    }

    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close(); //closes the window when exit button closed
    }
}