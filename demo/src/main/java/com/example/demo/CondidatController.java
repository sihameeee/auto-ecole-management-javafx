package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class CondidatController {

    @FXML private TextField nomf;
    @FXML private TextField prenomf;
    @FXML private TextField adressef;
    @FXML private TextField numtelf;
    @FXML private DatePicker datenaissf;
    @FXML private ChoiceBox<String> etatf;
    @FXML private ChoiceBox<String> typef;
    @FXML private Button confirmer;
    @FXML private Button annuler;
    @FXML
    private TableView<Condidat> condidattable;

    private Condidat condidatAModifier = null;
    private boolean modeModification = false;

    private ObservableList<Condidat> listeCondidats;

    /** THIS METHOD WILL BE CALLED FROM DashboardController */
    public void setListeCondidats(ObservableList<Condidat> liste) {
        this.listeCondidats = liste;
    }

    @FXML
    public void initialize() {
        etatf.getItems().addAll("Actif", "Non actif");
        typef.getItems().addAll("A", "B", "C", "D", "E");
        etatf.setValue("Actif");
        typef.setValue("B");
    }

    @FXML
    private void annuler() {
        ((Stage) annuler.getScene().getWindow()).close();
    }

    @FXML
    private void confirmer() {

        if (nomf.getText().isEmpty() ||
                prenomf.getText().isEmpty() ||
                adressef.getText().isEmpty() ||
                numtelf.getText().isEmpty() ||
                datenaissf.getValue() == null) {

            new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs").show();
            return;
        }

        // --- 1️⃣ Validation nom ---
        if (!nomf.getText().matches("^[A-Za-zÀ-ÿ]{2,}$")) {
            new Alert(Alert.AlertType.ERROR,
                    "Nom invalide : uniquement des lettres, minimum 2 caractères.").show();
            return;
        }

        // --- 2️⃣ Validation prénom ---
        if (!prenomf.getText().matches("^[A-Za-zÀ-ÿ]{2,}$")) {
            new Alert(Alert.AlertType.ERROR,
                    "Prénom invalide : uniquement des lettres, minimum 2 caractères.").show();
            return;
        }

        // --- 3️⃣ Validation numéro de téléphone ---
        if (!numtelf.getText().matches("^(05|06|07)[0-9]{8}$")) {
            new Alert(Alert.AlertType.ERROR,
                    "Numéro invalide : il doit contenir 10 chiffres et commencer par 05, 06 ou 07.")
                    .show();
            return;
        }

        // --- 4️⃣ Validation âge >= 17 ans ---
        LocalDate naissance = datenaissf.getValue();
        int age = LocalDate.now().getYear() - naissance.getYear();
        if (age < 17) {
            new Alert(Alert.AlertType.ERROR,
                    "L'âge du candidat doit être d'au moins 17 ans.")
                    .show();
            return;
        }

        try {
            Connection conn = database.connectDb();
            if (!modeModification) {
            String sql =
                    "INSERT INTO condidats (nom, prenom, adresse, telephone, date_naissance, etat, permis) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, nomf.getText());
            ps.setString(2, prenomf.getText());
            ps.setString(3, adressef.getText());
            ps.setString(4, numtelf.getText());
            ps.setString(5, datenaissf.getValue().toString());
            ps.setString(6, etatf.getValue());
            ps.setString(7, typef.getValue());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int id = (rs.next()) ? rs.getInt(1) : 0;

            Condidat nouveau = new Condidat(
                    id,
                    nomf.getText(),
                    prenomf.getText(),
                    numtelf.getText(),
                    adressef.getText(),
                    datenaissf.getValue().toString(),
                    typef.getValue(),
                    etatf.getValue()
            );

            // ADD TO MAIN TABLE LIST
            if (listeCondidats != null)
                listeCondidats.add(nouveau);

            new Alert(Alert.AlertType.INFORMATION, "Condidat ajouté avec succès !").show();

            } else {
                // UPDATE
                String sql = "UPDATE condidats SET nom=?, prenom=?, adresse=?, telephone=?, " +
                        "date_naissance=?, etat=?, permis=? WHERE id=?";

                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, nomf.getText());
                ps.setString(2, prenomf.getText());
                ps.setString(3, adressef.getText());
                ps.setString(4, numtelf.getText());
                ps.setString(5, datenaissf.getValue().toString());
                ps.setString(6, etatf.getValue());
                ps.setString(7, typef.getValue());
                ps.setInt(8, condidatAModifier.getId());

                ps.executeUpdate();
                new Alert(Alert.AlertType.INFORMATION, "Condidat modifie avec succès !").show();
                // MAJ DANS LA LISTE
                condidatAModifier.setNom(nomf.getText());
                condidatAModifier.setPrenom(prenomf.getText());
                condidatAModifier.setAdresse(adressef.getText());
                condidatAModifier.setTelephone(numtelf.getText());
                condidatAModifier.setDateNaissance(datenaissf.getValue().toString());
                condidatAModifier.setPermis(typef.getValue());
                condidatAModifier.setEtat(etatf.getValue());


                ps.close();
                conn.close();
            }


            ((Stage) confirmer.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chargerCondidatPourModification(Condidat c) {
        this.condidatAModifier = c;
        this.modeModification = true;

        // Remplir les champs du formulaire
        nomf.setText(c.getNom());
        prenomf.setText(c.getPrenom());
        adressef.setText(c.getAdresse());
        numtelf.setText(c.getTelephone());
        datenaissf.setValue(LocalDate.parse(c.getDateNaissance()));
        etatf.setValue(c.getEtat());
        typef.setValue(c.getPermis());
    }

}
