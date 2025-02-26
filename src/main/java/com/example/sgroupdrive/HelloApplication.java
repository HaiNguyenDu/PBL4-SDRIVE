package com.example.sgroupdrive;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1260, 720);
        scene.getStylesheets()
                .add(Objects.requireNonNull(getClass().getResource("/Styles/homepage.css")).toExternalForm());
        stage.setTitle("SDrive");
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/n.png"))));
    }

    public static void main(String[] args) {  
        launch();
    }
}