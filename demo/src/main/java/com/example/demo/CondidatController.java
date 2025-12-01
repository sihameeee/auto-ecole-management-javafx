package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

        try {
            Connection conn = database.connectDb();

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

            ps.close();
            conn.close();

            ((Stage) confirmer.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
