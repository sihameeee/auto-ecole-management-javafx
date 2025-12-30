package com.example.demo;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MoniteurController {

    @FXML private TextField nomm;
    @FXML private TextField prenomm;
    @FXML private TextField usernamem;
    @FXML private TextField passwordm;
    @FXML private ChoiceBox<String> rolem;
    @FXML private ChoiceBox<String> disponibilitem;
    @FXML private Button confirmerm;
    @FXML private Button annulerm;

    private ObservableList<Moniteur> listeMoniteurs;
    private Moniteur moniteurAModifier = null;
    private boolean modeModification = false;

    public void setListeMoniteurs(ObservableList<Moniteur> liste) {
        this.listeMoniteurs = liste;
    }

    @FXML
    public void initialize() {
        rolem.getItems().addAll("admin", "user");
        disponibilitem.getItems().addAll("disponible", "non disponible");
        rolem.setValue("user");
        disponibilitem.setValue("disponible");
    }

    // =================== CHARGER POUR MODIFICATION ===================
    public void chargerMoniteurPourModification(Moniteur m) {
        if (m == null) return;

        moniteurAModifier = m;
        modeModification = true;

        nomm.setText(m.getNom());
        prenomm.setText(m.getPrenom());
        usernamem.setText(m.getUsername());
        passwordm.setText(m.getPassword());
        rolem.setValue(m.getRole());
        disponibilitem.setValue(m.getDisponibilite());
    }

    @FXML
    private void annuler() {
        ((Stage) annulerm.getScene().getWindow()).close();
    }

    @FXML
    private void confirmer() {

        String nom = nomm.getText().trim();
        String prenom = prenomm.getText().trim();
        String username = usernamem.getText().trim();
        String password = passwordm.getText().trim();
        String role = rolem.getValue();
        String dispo = disponibilitem.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || username.isEmpty() || password.isEmpty()) {
            alert("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")) {
            alert("Erreur", "Le mot de passe doit contenir au moins 8 caractères avec lettres et chiffres !");
            return;
        }

        if (existeMoniteur(username, password, role)) {
            alert("Erreur", "Ce username et mot de passe sont déjà utilisés !");
            return;
        }

        try (Connection conn = database.connectDb()) {

            if (modeModification) {
                String sql = "UPDATE moniteur SET nom=?, prenom=?, username=?, password=?, role=?, disponibilite=? WHERE id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nom);
                ps.setString(2, prenom);
                ps.setString(3, username);
                ps.setString(4, password);
                ps.setString(5, role);
                ps.setString(6, dispo);
                ps.setInt(7, moniteurAModifier.getId());
                ps.executeUpdate();

                moniteurAModifier.setNom(nom);
                moniteurAModifier.setPrenom(prenom);
                moniteurAModifier.setUsername(username);
                moniteurAModifier.setPassword(password);
                moniteurAModifier.setRole(role);
                moniteurAModifier.setDisponibilite(dispo);

                listeMoniteurs.set(listeMoniteurs.indexOf(moniteurAModifier), moniteurAModifier);
                alert("Succès", "Moniteur modifié !");
            } else {

                String sql = "INSERT INTO moniteur(nom, prenom, username, password, role, disponibilite) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, nom);
                ps.setString(2, prenom);
                ps.setString(3, username);
                ps.setString(4, password);
                ps.setString(5, role);
                ps.setString(6, dispo);
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                int id = rs.next() ? rs.getInt(1) : 0;

                listeMoniteurs.add(new Moniteur(id, nom, prenom, username, password, role, dispo));
                alert("Succès", "Moniteur ajouté !");
            }

            ((Stage) confirmerm.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean existeMoniteur(String username, String password, String role) {
        String sql = "SELECT id FROM moniteur WHERE username=? AND password=? AND role=?";
        try (Connection conn = database.connectDb();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            return ps.executeQuery().next();
        } catch (Exception e) {
            return false;
        }
    }

    private void alert(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
}
