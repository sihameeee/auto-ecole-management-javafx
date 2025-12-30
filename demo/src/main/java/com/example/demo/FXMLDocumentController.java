//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.demo;

import java.awt.*;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;


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
    @FXML
    private ImageView logo;

    public void loginAdmin() {

        String sql = "SELECT username, role FROM moniteur WHERE username=? AND password=?";
        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());
            result = prepare.executeQuery();

            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                new Alert(AlertType.ERROR, "Tous les champs doivent être remplis").show();
                return;
            }

            if (result.next()) {

                // 🔥 Sauvegarder session
                Session.setUsername(result.getString("username"));
                Session.setRole(result.getString("role"));  // admin / user

                Alert alert = new Alert(AlertType.INFORMATION, "Connexion réussie !");
                alert.showAndWait();

                loginBtn.getScene().getWindow().hide();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed(event -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged(event -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                });

                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();

            } else {
                new Alert(AlertType.ERROR, "Nom d'utilisateur ou mot de passe incorrect").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void close() {
        System.exit(0);
    }

    public void initialize(URL url, ResourceBundle rb) {
        Image image = new Image(
                getClass().getResource("Logo.png").toExternalForm()
        );
        logo.setImage(image);
    }
}
