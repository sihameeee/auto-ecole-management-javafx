package com.example.projet_ihm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Charger ton FXML LOGIN
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashbord.fxml"));

        // Construire la scène
        Scene scene = new Scene(fxmlLoader.load(), 305, 395);

        // Ajouter le fichier CSS
        scene.getStylesheets().add(HelloApplication.class.getResource("login.css").toExternalForm());

        // Affichage
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
