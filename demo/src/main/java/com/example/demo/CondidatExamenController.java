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

public class CondidatExamenController {
    private boolean modeEdition = false;
    private int examenconIdEdit = -1;
    private CondidatExamen condidatAModifier;

    @FXML private Button annulerec;
    @FXML private Button confirmerec;
    private Examen examen;

    @FXML private TextField condidats;

    @FXML private ChoiceBox<String> decision;       // état de la séance
    @FXML private ChoiceBox<String> typeexamen;       // type de permis (A/B/...)
    @FXML private ChoiceBox<String> typepermis; // code/creneau/circulation

    @FXML private ListView<String> listCandidats;

    private boolean candidatValide = false;

    private List<String> candidatss = new ArrayList<>();
    private boolean suppressAutoComplete = false;


    private ObservableList<CondidatExamen> listeCondidatExamen;


    private Connection conn = database.connectDb();
    /** THIS METHOD WILL BE CALLED FROM DashboardController */
    public void setListeCondidatExamen(ObservableList<CondidatExamen> liste) {
        this.listeCondidatExamen = liste;
    }

    public void setExamen(Examen e) {
        this.examen = e;
    }


    @FXML
    private void initialize() {
        try {
            candidatss = getCandidatsFromDB();

            setupAutoComplete(condidats, listCandidats, candidatss, () -> {
                candidatValide = true;
                try {
                    String permis = getTypePermisByCandidatSafe(condidats.getText());
                    if (permis != null && !permis.isBlank()) typepermis.setValue(permis);
                } catch (Exception e) { e.printStackTrace(); }
            });

            decision.getItems().addAll("En attente", "Echoue", "Reussi");
            decision.setValue("En attente");

            typepermis.getItems().addAll("A", "B", "C", "D", "E");

            typeexamen.getItems().addAll("code", "creneau", "circulation");

        } catch (Exception e) { e.printStackTrace(); }

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
    private void ajouter() {
        if (!candidatValide) {
            showAlert("Erreur", "Vous devez choisir les valeurs depuis la liste !");
            return;
        }

        try {
            String candidat = condidats.getText().trim();

            int candidatId = getCandidatIdByNameSafe(candidat);

            String typePermis = getTypePermisByCandidatSafe(candidat);
            typepermis.setDisable(true);
            String typeexamenValue = typeexamen.getValue();
            String Decision = decision.getValue();

            if (modeEdition) {
                String sql = "UPDATE examen_candidat SET candidat_id=?, type_examen=?, decision=? WHERE id=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, candidatId);
                    ps.setString(2, typeexamenValue);
                    ps.setString(3, Decision);
                    ps.setInt(4, examenconIdEdit);

                    ps.executeUpdate();
                }
                showAlert("Succès", "Candidat modifiée avec succès !");
                System.out.println("Index = " + listeCondidatExamen.indexOf(condidatAModifier));

                if (listeCondidatExamen != null) {
                    for (int i = 0; i < listeCondidatExamen.size(); i++) {
                        CondidatExamen ce = listeCondidatExamen.get(i);

                        if (ce.getId() == examenconIdEdit) {
                            ce.setCondidat(condidats.getText());
                            ce.setTypeExamen(typeexamen.getValue());
                            ce.setTypePermis(typepermis.getValue());
                            ce.setDecision(decision.getValue());

                            // 🔥 force notification
                            listeCondidatExamen.set(i, ce);
                            break;
                        }
                    }
                }

                clearFields();
            } else {

                String sql = "INSERT INTO examen_candidat (examen_id, candidat_id, type_examen, decision) VALUES (?, ?, ?, ?)";

                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, examen.getId());    // <-- le vrai examen sélectionné !
                    ps.setInt(2, candidatId);
                    ps.setString(3, typeexamenValue);
                    ps.setString(4, Decision);

                    ps.executeUpdate();

                    ResultSet rs = ps.getGeneratedKeys();
                    int id = rs.next() ? rs.getInt(1) : 0;

                    CondidatExamen nouveau = new CondidatExamen(
                            id,
                            condidats.getText(),
                            typeexamen.getValue(),
                            typepermis.getValue(),
                            decision.getValue()
                    );

                    if (listeCondidatExamen != null) {
                        listeCondidatExamen.add(nouveau);
                    }

                    rs.close();
                }

                showAlert("Succès",
                        "Candidat ajouté avec succès !");

            }



                clearFields();
                ((Stage) confirmerec.getScene().getWindow()).close();


        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", e.getMessage());
        }
    }

    private void clearFields() {
        condidats.clear();
        typeexamen.setValue(" ");
        typepermis.setValue(" ");
        decision.setValue("En attente");

        candidatValide = false;
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

    @FXML
    private void annuler() {
        ((Stage) annulerec.getScene().getWindow()).close();
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

    private CondidatExamen condidatExamenAModifier = null;

    public void setModeModification(CondidatExamen ce) {
        if (ce == null) return;

        this.condidatAModifier = ce;
        this.modeEdition = true;
        this.examenconIdEdit = ce.getId();

        suppressAutoComplete = true;

        condidats.setText(ce.getCondidat());
        typeexamen.setValue(ce.getTypeExamen());
        typepermis.setValue(ce.getTypePermis());
        decision.setValue(ce.getDecision());

        listCandidats.setVisible(false);

        suppressAutoComplete = false;
        candidatValide = true;
    }



    // ==================== Chargement pour édition ====================
    public void chargerCondidatPourEdition(CondidatExamen e) {
        if (e == null) return;
        modeEdition = true;
        examenconIdEdit = e.getId();

        suppressAutoComplete = true;

        condidats.setText(e.getCondidat());

        typeexamen.setValue(e.getTypeExamen());
        decision.setValue(e.getDecision());
        typepermis.setValue(e.getTypePermis());

        // masquer les ListView
        listCandidats.setVisible(false);

        suppressAutoComplete = false;
        candidatValide = true;
    }
}
