//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.demo;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLDocumentController implements Initializable {
    @FXML
    private AnchorPane main_form;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginBtn;
    @FXML
    private Button close;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private double x = (double)0.0F;
    private double y = (double)0.0F;

    public void loginAdmin() {
        String sql = "SELECT * FROM moniteur WHERE username = ? and password = ?";
        this.connect = database.connectDb();

        try {
            this.prepare = this.connect.prepareStatement(sql);
            this.prepare.setString(1, this.username.getText());
            this.prepare.setString(2, this.password.getText());
            this.result = this.prepare.executeQuery();
            if (!this.username.getText().isEmpty() && !this.password.getText().isEmpty()) {
                if (this.result.next()) {
                    getData.username = this.username.getText();
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Message d'information");
                    alert.setHeaderText((String)null);
                    alert.setContentText("Connexion Reusite!");
                    alert.showAndWait();
                    this.loginBtn.getScene().getWindow().hide();
                    Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("dashboard.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    root.setOnMousePressed((event) -> {
                        this.x = event.getSceneX();
                        this.y = event.getSceneY();
                    });
                    root.setOnMouseDragged((event) -> {
                        stage.setX(event.getScreenX() - this.x);
                        stage.setY(event.getScreenY() - this.y);
                    });
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText((String)null);
                    alert.setContentText("Nom d'utilisateur / Mot de passe faux");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText((String)null);
                alert.setContentText("Touts les champs doivent etre remplis");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        System.exit(0);
    }

    public void initialize(URL url, ResourceBundle rb) {
    }
}
