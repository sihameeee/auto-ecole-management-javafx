package com.example.demo;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SeanceController {
    private boolean modeEdition = false;
    private int seanceIdEdit = -1;

    @FXML private Button annulers;
    @FXML private Button confirmers;

    @FXML private TextField condidats;
    @FXML private TextField moniteurs;
    @FXML private TextField vehicules;
    @FXML private TextField heures;
    @FXML private DatePicker dates;
    @FXML private Label vehiculee;

    @FXML private ChoiceBox<String> etats;       // état de la séance
    @FXML private ChoiceBox<String> types;       // type de permis (A/B/...)
    @FXML private ChoiceBox<String> typeseance; // code/creneau/circulation

    @FXML private ListView<String> listCandidats;
    @FXML private ListView<String> listMoniteurs;
    @FXML private ListView<String> listVehicules;

    private boolean candidatValide = false;
    private boolean moniteurValide = false;
    private boolean vehiculeValide = false;

    private List<String> candidatss = new ArrayList<>();
    private List<String> moniteurss = new ArrayList<>();
    private List<String> vehiculess = new ArrayList<>();
    private boolean suppressAutoComplete = false;

    private Seance seanceAModifier;
    private ObservableList<Seance> listeSeance;


    private Connection conn = database.connectDb();
    /** THIS METHOD WILL BE CALLED FROM DashboardController */
    public void setListeSeances(ObservableList<Seance> liste) {
        this.listeSeance = liste;
    }



    public void setListeSeance(ObservableList<Seance> listeSeance) {
        this.listeSeance = listeSeance;
    }

    @FXML
    private void initialize() {
        try {
            candidatss = getCandidatsFromDB();
            moniteurss = getMoniteursFromDB();
            vehiculess = getVehiculesFromDB();

            setupAutoComplete(condidats, listCandidats, candidatss, () -> {
                candidatValide = true;
                try {
                    String permis = getTypePermisByCandidatSafe(condidats.getText());
                    if (permis != null && !permis.isBlank()) types.setValue(permis);
                } catch (Exception e) { e.printStackTrace(); }
            });

            setupAutoComplete(moniteurs, listMoniteurs, moniteurss, () -> moniteurValide = true);
            setupAutoComplete(vehicules, listVehicules, vehiculess, () -> vehiculeValide = true);

            etats.getItems().addAll("A venir", "Terminée", "Annulée");
            etats.setValue("A venir");

            types.getItems().addAll("A", "B", "C", "D", "E");

            typeseance.getItems().addAll("code", "creneau", "circulation");

        } catch (Exception e) { e.printStackTrace(); }

        typeseance.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.equalsIgnoreCase("code")) {
                vehicules.setVisible(false);
                vehiculee.setVisible(false);
                vehiculeValide = true;
            } else {
                vehicules.setVisible(true);
                vehiculee.setVisible(true);
                vehiculeValide = false;
            }
        });
    }

    // ==================== Auto-complétion ====================
    private void setupAutoComplete(TextField tf, ListView<String> lv, List<String> data, Runnable onSelect) {
        lv.setVisible(false);

        tf.textProperty().addListener((obs, oldText, newText) -> {
            if (suppressAutoComplete) return; // Ignore pendant chargement
            if (newText == null || newText.isEmpty()) {
                lv.getItems().clear();
                lv.setVisible(false);
                return;
            }

            List<String> result = data.stream()
                    .filter(s -> s.toLowerCase().contains(newText.toLowerCase()))
                    .toList();

            Platform.runLater(() -> {
                lv.getItems().setAll(result);
                lv.setVisible(!result.isEmpty());
            });
        });

        lv.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) applySelection(tf, lv, onSelect);
        });

        lv.setOnMouseReleased(e -> applySelection(tf, lv, onSelect));

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
                case UP -> { if (lv.getSelectionModel().getSelectedIndex() == 0) tf.requestFocus(); }
            }
        });

        tf.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) lv.setVisible(false);
        });
    }

    private void applySelection(TextField tf, ListView<String> lv, Runnable onSelect) {
        String selected = lv.getSelectionModel().getSelectedItem();
        if (selected != null && !selected.isEmpty()) {
            tf.setText(selected);
            lv.setVisible(false);
            try { onSelect.run(); } catch (Exception ex) { ex.printStackTrace(); }
            tf.requestFocus();
            tf.positionCaret(tf.getText().length());
        }
    }

    // ==================== Ajout / Modification ====================
    @FXML
    private void ajouterseance() {
        if (!candidatValide || !moniteurValide) {
            showAlert("Erreur", "Vous devez choisir les valeurs depuis la liste !");
            return;
        }




        try {
            String candidat = condidats.getText().trim();
            String moniteur = moniteurs.getText().trim();
            String vehicule = vehicules.getText().trim();
            String heure = heures.getText().trim();
            LocalDate date = dates.getValue();
            if (date == null || date.isBefore(LocalDate.now())) {
                showAlert("Erreur", "Date invalide !");
                return;
            }





            String heureStr = heures.getText();
            if (!Pattern.matches("([01]?\\d|2[0-3]):([0-5]\\d)", heureStr)) {
                showAlert("Erreur", "Heure invalide ! (HH:mm)");
                return;
            }


            int candidatId = getCandidatIdByNameSafe(candidat);
            int moniteurId = getMoniteurIdByNameSafe(moniteur);
            int vehiculeId = -1;

            // Vérifier si le candidat est disponible
            if (!isCandidatDisponible(candidatId, date, heure)) {
                showAlert("Erreur", "Le candidat est déjà pris à cette date/heure !");
                return;
            }

// Vérifier si le moniteur est disponible
            if (!isMoniteurDisponible(moniteurId, date, heure)) {
                showAlert("Erreur", "Le moniteur est déjà pris à cette date/heure !");
                return;
            }

// Vérifier si le véhicule est disponible (sauf si type = code)
            if (typeseance.getValue() == null || !typeseance.getValue().equalsIgnoreCase("code")) {
                if (!isVehiculeDisponible(vehiculeId, date, heure)) {
                    showAlert("Erreur", "Le véhicule est déjà pris à cette date/heure !");
                    return;
                }
            }

            if (typeseance.getValue() == null || !typeseance.getValue().equalsIgnoreCase("code")) {
                if (!vehiculeValide) {
                    showAlert("Erreur", "Vous devez sélectionner un véhicule depuis la liste !");
                    return;
                }
                vehiculeId = getVehiculeIdByMarqueSafe(vehicule);
                if (vehiculeId == -1) {
                    showAlert("Erreur", "Véhicule introuvable !");
                    return;
                }
            }

            String typePermis = getTypePermisByCandidatSafe(candidat);
            types.setDisable(true);
            String typeSeanceValue = typeseance.getValue();
            String etat = etats.getValue();

            if (modeEdition) {
                String sql = "UPDATE seance SET date_seance=?, heure=?, condidat_id=?, moniteur_id=?, vehicule_id=?,typeseance=?, type=?, etat=? WHERE id=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setDate(1, Date.valueOf(date));
                    ps.setString(2, heure);
                    ps.setInt(3, candidatId);
                    ps.setInt(4, moniteurId);
                    if (vehiculeId == -1) ps.setNull(5, Types.INTEGER);
                    else ps.setInt(5, vehiculeId);
                    ps.setString(6, typeSeanceValue);
                    ps.setString(7, typePermis);
                    ps.setString(8, etat);
                    ps.setInt(9, seanceIdEdit);

                    ps.executeUpdate();
                }
                showAlert("Succès", "Séance modifiée !");
                seanceAModifier.setDate(dates.getValue());
                seanceAModifier.setHeure(heures.getText());
                seanceAModifier.setCondidat(condidats.getText());
                seanceAModifier.setMoniteur(moniteurs.getText());
                seanceAModifier.setVehicule(vehicules.getText());
                seanceAModifier.setTypeSeance(typeseance.getValue());
                seanceAModifier.setTypePermis(types.getValue());
                seanceAModifier.setEtatSeance(etats.getValue());

                // 2. forcer JavaFX à se réveiller
                int index = listeSeance.indexOf(seanceAModifier);
                if (index >= 0) {
                    listeSeance.set(index, seanceAModifier);
                }

                modeEdition = false;
                seanceIdEdit = -1;
                clearFields();
            } else {

                String sql = "INSERT INTO seance(date_seance, heure, condidat_id, moniteur_id, vehicule_id,typeseance, type, etat) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDate(1, Date.valueOf(date));
                    ps.setString(2, heure);
                    ps.setInt(3, candidatId);
                    ps.setInt(4, moniteurId);
                    if (vehiculeId == -1) ps.setNull(5, Types.INTEGER);
                    else ps.setInt(5, vehiculeId);
                    ps.setString(6, typeSeanceValue);
                    ps.setString(7, typePermis);
                    ps.setString(8, etat);

                    ps.executeUpdate();
                    ResultSet rs = ps.getGeneratedKeys();
                        int id = rs.next() ? rs.getInt(1) : 0;

                        Seance nouveau = new Seance(
                                id,
                                dates.getValue(),
                                heures.getText(),
                                condidats.getText(),
                                moniteurs.getText(),
                                vehicules.getText(),
                                typeseance.getValue(),
                                types.getValue(),
                                etats.getValue()
                        );
                        if (listeSeance != null) listeSeance.add(nouveau);

                    rs.close();
                    ps.close();

                    showAlert("Succès", "Séance ajoutée !");
                }



                clearFields();
                ((Stage) confirmers.getScene().getWindow()).close();
            }

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
        etats.setValue("A venir");

        candidatValide = false;
        moniteurValide = false;
        vehiculeValide = false;
    }

    private void showAlert(String title, String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }

    private boolean isCandidatDisponible(int candidatId, LocalDate date, String heure) throws SQLException {
        String sql = "SELECT COUNT(*) FROM seance WHERE condidat_id=? AND date_seance=? AND heure=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, candidatId);
            ps.setDate(2, Date.valueOf(date));
            ps.setString(3, heure);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) == 0; // 0 = disponible
        }
    }

    private boolean isMoniteurDisponible(int moniteurId, LocalDate date, String heure) throws SQLException {
        String sql = "SELECT COUNT(*) FROM seance WHERE moniteur_id=? AND date_seance=? AND heure=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, moniteurId);
            ps.setDate(2, Date.valueOf(date));
            ps.setString(3, heure);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) == 0;
        }
    }

    private boolean isVehiculeDisponible(int vehiculeId, LocalDate date, String heure) throws SQLException {
        String sql = "SELECT COUNT(*) FROM seance WHERE vehicule_id=? AND date_seance=? AND heure=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vehiculeId);
            ps.setDate(2, Date.valueOf(date));
            ps.setString(3, heure);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) == 0;
        }
    }


    @FXML
    private void annuler() {
        ((Stage) annulers.getScene().getWindow()).close();
    }

    // ==================== BDD UTILITAIRES ====================
    private List<String> getCandidatsFromDB() {
        List<String> list = new ArrayList<>();
        if (conn == null) return list;
        String query = "SELECT nom, prenom FROM condidats";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) list.add(rs.getString("nom") + " " + rs.getString("prenom"));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private List<String> getMoniteursFromDB() {
        List<String> list = new ArrayList<>();
        if (conn == null) return list;
        String query = "SELECT nom, prenom FROM moniteur";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) list.add(rs.getString("nom") + " " + rs.getString("prenom"));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private List<String> getVehiculesFromDB() {
        List<String> list = new ArrayList<>();
        if (conn == null) return list;
        String query = "SELECT marque FROM vehicules";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) list.add(rs.getString("marque"));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private int getCandidatIdByNameSafe(String nomPrenom) {
        if (nomPrenom == null || nomPrenom.isBlank()) return -1;
        String[] parts = splitNomPrenom(nomPrenom);
        if (parts == null) return -1;
        String sql = "SELECT id FROM condidats WHERE nom=? AND prenom=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, parts[0]);
            ps.setString(2, parts[1]);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt("id") : -1; }
        } catch (SQLException e) { e.printStackTrace(); return -1; }
    }

    private int getMoniteurIdByNameSafe(String nomPrenom) {
        if (nomPrenom == null || nomPrenom.isBlank()) return -1;
        String[] parts = splitNomPrenom(nomPrenom);
        if (parts == null) return -1;
        String sql = "SELECT id FROM moniteur WHERE nom=? AND prenom=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, parts[0]);
            ps.setString(2, parts[1]);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt("id") : -1; }
        } catch (SQLException e) { e.printStackTrace(); return -1; }
    }

    private int getVehiculeIdByMarqueSafe(String marque) {
        if (marque == null || marque.isBlank()) return -1;
        String sql = "SELECT id FROM vehicules WHERE marque=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, marque);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt("id") : -1; }
        } catch (SQLException e) { e.printStackTrace(); return -1; }
    }

    private String getTypePermisByCandidatSafe(String nomPrenom) {
        if (nomPrenom == null || nomPrenom.isBlank()) return "";
        String[] parts = splitNomPrenom(nomPrenom);
        if (parts == null) return "";
        String sql = "SELECT permis FROM condidats WHERE nom=? AND prenom=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, parts[0]);
            ps.setString(2, parts[1]);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getString("permis") : ""; }
        } catch (SQLException e) { e.printStackTrace(); return ""; }
    }

    private String[] splitNomPrenom(String nomPrenom) {
        String trimmed = nomPrenom.trim();
        int idx = trimmed.indexOf(' ');
        if (idx <= 0 || idx == trimmed.length() - 1) return null;
        String nom = trimmed.substring(0, idx).trim();
        String prenom = trimmed.substring(idx + 1).trim();
        return new String[]{nom, prenom};
    }

    // ==================== Chargement pour édition ====================
    public void chargerSeancePourEdition(Seance s) {
        if (s == null) return;

        this.seanceAModifier = s;   // ← OBLIGATOIRE
        modeEdition = true;
        seanceIdEdit = s.getId();

        suppressAutoComplete = true;

        condidats.setText(s.getCondidat());
        moniteurs.setText(s.getMoniteur());
        vehicules.setText(s.getVehicule());

        dates.setValue(s.getDateSeance());
        heures.setText(s.getHeureHHmm());
        typeseance.setValue(s.getTypeSeance());
        etats.setValue(s.getEtatSeance());
        types.setValue(s.getTypePermis());

        listCandidats.setVisible(false);
        listMoniteurs.setVisible(false);
        listVehicules.setVisible(false);

        suppressAutoComplete = false;
        candidatValide = true;
        moniteurValide = true;
        vehiculeValide = (s.getVehicule() != null && !s.getVehicule().isBlank());
    }

}
