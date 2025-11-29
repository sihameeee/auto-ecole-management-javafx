package com.example.demo;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class MoniteurController {

    @FXML
    private TextField nomm;

    @FXML
    private TextField prenomm;

    @FXML
    private TextField usernamem;

    @FXML
    private TextField passwordm;

    @FXML
    private ChoiceBox<String> rolem;

    @FXML
    private ChoiceBox<String> disponibilitem;

    @FXML
    private Button confirmerm;

    @FXML
    private Button annulerm;

    private ObservableList<Moniteur> listeMoniteurs;

    /** THIS METHOD WILL BE CALLED FROM DashboardController */
    public void setListeMoniteurs(ObservableList<Moniteur> liste) {
        this.listeMoniteurs = liste;
    }

    // INITIALISATION
    @FXML
    public void initialize() {
        // Remplir les ChoiceBox
        rolem.getItems().addAll("admin", "user");
        disponibilitem.getItems().addAll("disponible", "non disponible");

        // Par défaut
        rolem.setValue("user");
        disponibilitem.setValue("disponible");
    }

    // =================== BOUTON ANNULER ===================
    @FXML
    private void annuler() {
        Stage stage = (Stage) annulerm.getScene().getWindow();
        stage.close();
    }

    // =================== BOUTON CONFIRMER ===================
    @FXML
    private void confirmer() {

        // ---- VALIDATION DE BASE ----
        if (nomm.getText().isEmpty() ||
                prenomm.getText().isEmpty() ||
                usernamem.getText().isEmpty() ||
                passwordm.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message d'erreur");
            alert.setHeaderText((String)null);
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
            return;
        }

        String nom = nomm.getText();
        String prenom = prenomm.getText();
        String username = usernamem.getText();
        String password = passwordm.getText();
        String role = rolem.getValue();
        String disponibilite = disponibilitem.getValue();

        // ------------------- INSERTION EN BDD -------------------

        try {
            Connection conn = database.connectDb();
            String sql = "INSERT INTO moniteur (nom, prenom, username, password, role, disponibilite) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setString(5, role);
            ps.setString(6, disponibilite);

            ps.executeUpdate();
            ps.close();
            conn.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message d'indformation'");
            alert.setHeaderText((String)null);
            alert.setContentText("Moniteur ajoute avec succee");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Fermer la fenêtre après confirmation
        Stage stage = (Stage) confirmerm.getScene().getWindow();
        stage.close();
    }
}
