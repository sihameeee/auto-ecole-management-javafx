package com.example.demo;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.application.Platform;

import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaiementController {
    @FXML private TextField nomp;
    @FXML private TextField totalp;
    @FXML private TextField payep;
    @FXML private TextField restantp;
    @FXML private ChoiceBox<String> etatp;
    @FXML private ChoiceBox<String> typep;
    @FXML private Button confirmerp;
    @FXML private Button imprimerp;
    @FXML private Button annulerp;
    @FXML private ListView<String> listCandidats;
    @FXML
    private TableView<Paiement> tablepaiement;



    private ObservableList<Paiement> listePaiement;

    /** THIS METHOD WILL BE CALLED FROM DashboardController */
    public void setListePaiement(ObservableList<Paiement> liste) {
        this.listePaiement = liste;
    }
    private Paiement paiementAModifier;
    private boolean modeModification = false;


    private Connection conn = database.connectDb();
        private List<String> candidatsList = new ArrayList<>();
        private boolean suppressAutoComplete = false;
        private int candidatIdSelected = -1;

        @FXML
        private void initialize() {
            // Charger candidats depuis la BDD
            candidatsList = getCandidatsFromDB();

            // Auto-complétion
            setupAutoComplete(nomp, listCandidats, candidatsList);

            // Etat paiement
            etatp.getItems().addAll("Partiel", "Complet", "En attente");
            etatp.setValue("Partiel");

            // Type permis (sera rempli automatiquement)
            typep.getItems().addAll("A", "B", "C", "D", "E");

            // Calcul automatique du reste
            payep.textProperty().addListener((obs, oldVal, newVal) -> calculerRestant());
        }

        private void setupAutoComplete(TextField tf, ListView<String> lv, List<String> data) {
            lv.setVisible(false);

            tf.textProperty().addListener((obs, oldText, newText) -> {
                if (suppressAutoComplete) return;
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
                if (newSel != null) applySelection(tf, lv);
            });

            lv.setOnMouseClicked(e -> applySelection(tf, lv));
        }

        private void applySelection(TextField tf, ListView<String> lv) {
            String selected = lv.getSelectionModel().getSelectedItem();
            if (selected != null && !selected.isEmpty()) {
                tf.setText(selected);
                lv.setVisible(false);

                // Récupérer ID et type permis
                candidatIdSelected = getCandidatIdByNomPrenom(selected);
                String permis = getTypePermisByCandidat(selected);
                typep.setValue(permis);

                // Récupérer montant total depuis configuration
                double total = getPrixByTypePermis(permis);
                totalp.setText(String.valueOf(total));
                restantp.setText(String.valueOf(total));
            }
        }

        private void calculerRestant() {
            try {
                double total = Double.parseDouble(totalp.getText());
                double paye = payep.getText().isEmpty() ? 0 : Double.parseDouble(payep.getText());
                if (paye > total) {
                    new Alert(Alert.AlertType.WARNING, "Le montant payé ne peut pas dépasser le total !").show();
                    payep.setText("");
                    restantp.setText(String.valueOf(total));
                    return;
                }
                restantp.setText(String.valueOf(total - paye));
            } catch (NumberFormatException ignored) {}
        }

    @FXML
    private void confirmer() {

        if (candidatIdSelected == -1) {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un candidat valide.").show();
            return;
        }

        try {
            double total = Double.parseDouble(totalp.getText());
            double paye = Double.parseDouble(payep.getText());
            double restant = Double.parseDouble(restantp.getText());
            String etat = etatp.getValue();

            if (modeModification) {

                String sql = "UPDATE paiement SET montant_total=?, montant_paye=?, montant_restant=?, etat=? WHERE id=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setDouble(1, total);
                    ps.setDouble(2, paye);
                    ps.setDouble(3, restant);
                    ps.setString(4, etat);
                    ps.setInt(5, paiementAModifier.getId());
                    ps.executeUpdate();
                }

                paiementAModifier.setTotal(total);
                paiementAModifier.setPaye(paye);
                paiementAModifier.setReste(restant);
                paiementAModifier.setEtat(etat);
                new Alert(Alert.AlertType.INFORMATION, "Paiement modifie avec succès !").show();

                // 🔥 Force refresh TableView
                int index = listePaiement.indexOf(paiementAModifier);
                listePaiement.set(index, paiementAModifier);

            } else {

                String sql = "INSERT INTO paiement(condidat_id, date_paiement, montant_total, montant_paye, montant_restant, etat) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, candidatIdSelected);
                    ps.setDate(2, Date.valueOf(LocalDate.now()));
                    ps.setDouble(3, total);
                    ps.setDouble(4, paye);
                    ps.setDouble(5, restant);
                    ps.setString(6, etat);
                    ps.executeUpdate();

                    ResultSet rs = ps.getGeneratedKeys();
                    int id = rs.next() ? rs.getInt(1) : 0;
                    String datep = LocalDate.now().toString();

                    Paiement nouveau = new Paiement(id, datep, nomp.getText(), candidatIdSelected,
                            typep.getValue(), total, paye, restant, etat);

                    listePaiement.add(nouveau);
                    new Alert(Alert.AlertType.INFORMATION, "Paiement Ajoute avec succès !").show();

                }
            }

            ((Stage) confirmerp.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    @FXML
        private void annuler() {
            ((Stage) annulerp.getScene().getWindow()).close();
        }

        // ================== Méthodes BDD ==================

    public void setModeModification(Paiement p) {

        this.paiementAModifier = p;
        this.modeModification = true;

        suppressAutoComplete = true;

        nomp.setText(p.getCondidat());
        typep.setValue(p.getPermis());
        totalp.setText(String.valueOf(p.getTotal()));
        payep.setText(String.valueOf(p.getPaye()));
        restantp.setText(String.valueOf(p.getReste()));
        etatp.setValue(p.getEtat());

        candidatIdSelected = p.getCondidatId();

        suppressAutoComplete = false;
    }


    private List<String> getCandidatsFromDB() {
            List<String> list = new ArrayList<>();
            String query = "SELECT nom, prenom FROM condidats";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    list.add(rs.getString("nom") + " " + rs.getString("prenom"));
                }
            } catch (SQLException e) { e.printStackTrace(); }
            return list;
        }

        private int getCandidatIdByNomPrenom(String nomPrenom) {
            String[] parts = nomPrenom.trim().split(" ");
            if (parts.length < 2) return -1;
            String sql = "SELECT id FROM condidats WHERE nom=? AND prenom=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, parts[0]);
                ps.setString(2, parts[1]);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? rs.getInt("id") : -1;
                }
            } catch (SQLException e) { e.printStackTrace(); return -1; }
        }

        private String getTypePermisByCandidat(String nomPrenom) {
            String[] parts = nomPrenom.trim().split(" ");
            if (parts.length < 2) return "";
            String sql = "SELECT permis FROM condidats WHERE nom=? AND prenom=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, parts[0]);
                ps.setString(2, parts[1]);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? rs.getString("permis") : "";
                }
            } catch (SQLException e) { e.printStackTrace(); return ""; }
        }

        private double getPrixByTypePermis(String typePermis) {
            String sql = "SELECT prix FROM configuration WHERE type_permis=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, typePermis);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? rs.getDouble("prix") : 0;
                }
            } catch (SQLException e) { e.printStackTrace(); return 0; }
        }
    }


