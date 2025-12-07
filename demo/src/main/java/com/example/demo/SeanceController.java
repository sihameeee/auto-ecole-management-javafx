package com.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SeanceController {

    @FXML private Button annulers;
    @FXML private Button confirmers;

    @FXML private TextField condidats;
    @FXML private TextField moniteurs;
    @FXML private TextField vehicules;
    @FXML private TextField heures;
    @FXML private DatePicker dates;

    @FXML private ChoiceBox<String> etats;
    @FXML private ChoiceBox<String> types;

    @FXML private ListView<String> listCandidats;
    @FXML private ListView<String> listMoniteurs;
    @FXML private ListView<String> listVehicules;

    private boolean candidatValide = false;
    private boolean moniteurValide = false;
    private boolean vehiculeValide = false;

    private List<String> candidatss = new ArrayList<>();
    private List<String> moniteurss = new ArrayList<>();
    private List<String> vehiculess = new ArrayList<>();

    // 🔹 Connexion BDD (à adapter)
    private Connection conn = database.connectDb();

    @FXML
    private void initialize() {
        try {
            // Charger les données depuis la BDD
            candidatss = getCandidatsFromDB();
            moniteurss = getMoniteursFromDB();
            vehiculess = getVehiculesFromDB();

            // Setup auto-complétion
            setupAutoComplete(condidats, listCandidats, candidatss, () -> {
                candidatValide = true;
                try {
                    types.setValue(getTypePermisByCandidat(condidats.getText()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            setupAutoComplete(moniteurs, listMoniteurs, moniteurss, () -> moniteurValide = true);
            setupAutoComplete(vehicules, listVehicules, vehiculess, () -> vehiculeValide = true);

            // ChoiceBox initialisation
            etats.getItems().addAll("A venir", "En cours", "Terminée", "Annulée");
            etats.setValue("A venir");

            types.getItems().addAll("A", "B", "C", "D", "E");
            types.setValue("B");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("initialize called");
        System.out.println("candidats count = " + candidatss.size());

    }

    // Méthode générique pour setup auto-complétion
    private void setupAutoComplete(TextField tf, ListView<String> lv, List<String> data, Runnable onSelect) {
        lv.setVisible(false);

        // Filtrage dynamique
        tf.textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.isEmpty()) {
                lv.getItems().clear();
                lv.setVisible(false);
                return;
            }

            List<String> result = data.stream()
                    .filter(s -> s.toLowerCase().contains(newText.toLowerCase()))
                    .toList();

            // Mise à jour UI sur JavaFX Thread
            Platform.runLater(() -> {
                lv.getItems().setAll(result);
                lv.setVisible(!result.isEmpty());
            });
        });

        // Enter dans TextField -> si une suggestion existe, l'appliquer
        tf.setOnAction(e -> {
            if (!lv.getItems().isEmpty()) {
                lv.getSelectionModel().selectFirst();
                applySelection(tf, lv, onSelect);
            }
        });

        // Quand la sélection change dans la ListView -> appliquer tout de suite
        lv.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                // debug
                System.out.println("ListView selection changed -> " + newSel);
                // applique la sélection directement
                Platform.runLater(() -> {
                    tf.setText(newSel);
                    lv.setVisible(false);
                    try { onSelect.run(); } catch (Exception ex) { ex.printStackTrace(); }
                    tf.requestFocus();
                    tf.positionCaret(tf.getText().length());
                });
            }
        });

        // Clic souris : single click souvent OK, mais utiliser mouseReleased pour le timing
        lv.setOnMouseReleased(e -> {
            String sel = lv.getSelectionModel().getSelectedItem();
            System.out.println("mouseReleased on list, sel=" + sel);
            if (sel != null) applySelection(tf, lv, onSelect);
        });

        // Navigation clavier
        tf.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case DOWN -> {
                    if (!lv.getItems().isEmpty()) {
                        lv.requestFocus();
                        lv.getSelectionModel().selectFirst();
                    }
                }
            }
        });

        lv.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> applySelection(tf, lv, onSelect);
                case UP -> {
                    if (lv.getSelectionModel().getSelectedIndex() == 0) {
                        tf.requestFocus();
                    }
                }
            }
        });

        // Masquer liste quand perte de focus
        tf.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) lv.setVisible(false);
        });
    }

    private void applySelection(TextField tf, ListView<String> lv, Runnable onSelect) {
        String selected = lv.getSelectionModel().getSelectedItem();
        System.out.println("applySelection -> " + selected);
        if (selected != null && !selected.isEmpty()) {
            tf.setText(selected);
            lv.setVisible(false);
            try { onSelect.run(); } catch (Exception ex) { ex.printStackTrace(); }
            tf.requestFocus();
            tf.positionCaret(tf.getText().length());
        }
    }

    // Méthode pour remplir le TextField depuis ListView
    private void remplir(TextField tf, ListView<String> lv, Runnable onSelect) {
        String selected = lv.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tf.setText(selected);
            lv.setVisible(false);
            onSelect.run();

            tf.requestFocus();
            tf.positionCaret(tf.getText().length());
        }
    }

    // ==================== AJOUT SEANCE ====================
    @FXML
    private void ajouterSeance() {
        if (!candidatValide || !moniteurValide || !vehiculeValide) {
            showAlert("Erreur", "Vous devez choisir les valeurs depuis la liste !");
            return;
        }

        try {
            String candidat = condidats.getText().trim();
            String moniteur = moniteurs.getText().trim();
            String vehicule = vehicules.getText().trim();

            // Date et heure validation
            LocalDate date = dates.getValue();
            if (date == null || date.isBefore(LocalDate.now())) {
                showAlert("Erreur", "Date invalide !");
                return;
            }

            String heure = heures.getText();
            if (!Pattern.matches("([01]?\\d|2[0-3]):([0-5]\\d)", heure)) {
                showAlert("Erreur", "Heure invalide !");
                return;
            }

            int candidatId = getCandidatIdByName(candidat);
            int moniteurId = getMoniteurIdByName(moniteur);
            int vehiculeId = getVehiculeIdByMarque(vehicule);
            String type = getTypePermisByCandidat(candidat);

            String etat = etats.getValue();

            String sql = """
                INSERT INTO seance(date_seance, heure, condidat_id, moniteur_id, vehicule_id, type, etat)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(date));
            ps.setTime(2, Time.valueOf(LocalTime.parse(heure)));
            ps.setInt(3, candidatId);
            ps.setInt(4, moniteurId);
            ps.setInt(5, vehiculeId);
            ps.setString(6, type);
            ps.setString(7, etat);
            ps.executeUpdate();
            ps.close();

            showAlert("Succès", "Séance ajoutée !");
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", e.getMessage());
        }
    }

    private void clearFields() {
        dates.setValue(null);
        heures.clear();
        condidats.clear();
        moniteurs.clear();
        vehicules.clear();
        types.setValue("B");
        etats.setValue("A venir");

        candidatValide = false;
        moniteurValide = false;
        vehiculeValide = false;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void annuler() {
        ((Stage) annulers.getScene().getWindow()).close();
    }

    // ==================== BDD UTILITAIRES ====================
    private List<String> getCandidatsFromDB() throws SQLException {
        List<String> list = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nom, prenom FROM condidats");
        System.out.println("Loaded candidats: " + list);
        while (rs.next()) list.add(rs.getString("nom") + " " + rs.getString("prenom"));
        return list;
    }

    private List<String> getMoniteursFromDB() throws SQLException {
        List<String> list = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nom, prenom FROM moniteur");
        while (rs.next()) list.add(rs.getString("nom") + " " + rs.getString("prenom"));
        return list;
    }

    private List<String> getVehiculesFromDB() throws SQLException {
        List<String> list = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT marque FROM vehicules");
        while (rs.next()) list.add(rs.getString("marque"));
        return list;
    }

    private int getCandidatIdByName(String nomPrenom) throws SQLException {
        String[] parts = nomPrenom.split(" ");
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM condidats WHERE nom=? AND prenom=?");
        ps.setString(1, parts[0]);
        ps.setString(2, parts[1]);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("id") : -1;
    }

    private int getMoniteurIdByName(String nomPrenom) throws SQLException {
        String[] parts = nomPrenom.split(" ");
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM moniteur WHERE nom=? AND prenom=?");
        ps.setString(1, parts[0]);
        ps.setString(2, parts[1]);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("id") : -1;
    }

    private int getVehiculeIdByMarque(String marque) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM vehicules WHERE marque=?");
        ps.setString(1, marque);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt("id") : -1;
    }

    private String getTypePermisByCandidat(String nomPrenom) throws SQLException {
        String[] parts = nomPrenom.split(" ");
        PreparedStatement ps = conn.prepareStatement("SELECT permis FROM condidats WHERE nom=? AND prenom=?");
        ps.setString(1, parts[0]);
        ps.setString(2, parts[1]);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getString("permis") : "";
    }
}
