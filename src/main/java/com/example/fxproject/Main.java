package com.example.fxproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;

import static javafx.stage.StageStyle.UNDECORATED;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException { //creates stage for login page
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 816, 539);
        stage.initStyle(UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}