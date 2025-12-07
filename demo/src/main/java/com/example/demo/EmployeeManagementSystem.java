//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EmployeeManagementSystem extends Application {
    private double x = (double)0.0F;
    private double y = (double)0.0F;

    public void start(Stage stage) throws Exception {
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/com/example/demo/LoginPage.fxml"));
        Scene scene = new Scene(root);
        root.setOnMousePressed((event) -> {
            this.x = event.getSceneX();
            this.y = event.getSceneY();
        });
        root.setOnMouseDragged((event) -> {
            stage.setX(event.getScreenX() - this.x);
            stage.setY(event.getScreenY() - this.y);
            stage.setOpacity(0.8);
        });
        root.setOnMouseReleased((event) -> stage.setOpacity((double)1.0F));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
