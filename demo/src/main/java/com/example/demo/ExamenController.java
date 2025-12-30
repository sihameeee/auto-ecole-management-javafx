package com.example.demo;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class ExamenController {

    @FXML
    private Button annuler;

    @FXML
    private Button confirmerexamen;

    @FXML
    private DatePicker dateexamen;

    @FXML
    private ChoiceBox<String> etatexamen;

    private Examen examenAModifier = null;
    private boolean modeModification = false;

    private ObservableList<Examen> listeExamen;

    public void setListeExamen(ObservableList<Examen> liste) {
        this.listeExamen = liste;
    }

    public void setExamenAModifier(Examen examen) {
        this.examenAModifier = examen;
        this.modeModification = true;

        dateexamen.setValue(examen.getDateExamen());
        etatexamen.setValue(examen.getEtatExamen());
    }

    @FXML
    public void initialize() {
        etatexamen.getItems().addAll("Annuler", "A venir", "Termine");
        etatexamen.setValue("A venir");
    }

    @FXML
    private void annuler() {
        ((Stage) annuler.getScene().getWindow()).close();
    }

    @FXML
    private void confirmer() {

        if (dateexamen.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs").show();
            return;
        }

        LocalDate date = dateexamen.getValue();
        String etat = etatexamen.getValue();

        if (date == null || date.isBefore(LocalDate.now())) {
            new Alert(Alert.AlertType.ERROR, "Date Invalide !").show();
            return;
        }
        try {
            Connection conn = database.connectDb();

            if (!modeModification) {
                String sql = "INSERT INTO examen (date_examen, etat_examen) VALUES (?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.setString(1, date.toString());
                ps.setString(2, etat);
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                int id = rs.next() ? rs.getInt(1) : 0;

                Examen nouveau = new Examen(id, date, etat);
                if (listeExamen != null)
                    listeExamen.add(nouveau);

                new Alert(Alert.AlertType.INFORMATION, "Examen ajouté avec succès !").show();

                ps.close();
            } else {
                String sql = "UPDATE examen SET date_examen=?, etat_examen=? WHERE id=?";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, date.toString());
                ps.setString(2, etat);
                ps.setInt(3, examenAModifier.getId());

                ps.executeUpdate();

                examenAModifier.setDateExamen(date);
                examenAModifier.setEtatExamen(etat);

                listeExamen.set(listeExamen.indexOf(examenAModifier), examenAModifier);

                new Alert(Alert.AlertType.INFORMATION, "Examen modifié avec succès !").show();

                ps.close();
            }

            conn.close();
            ((Stage) confirmerexamen.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chargerExamenPourModification(Examen e) {
        this.examenAModifier = e;
        this.modeModification = true;

        // Remplir les champs du formulaire
        dateexamen.setValue(e.getDateExamen());
        etatexamen.setValue(e.getEtatExamen());
    }

}
