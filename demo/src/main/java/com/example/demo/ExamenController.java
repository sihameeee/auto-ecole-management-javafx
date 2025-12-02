package com.example.demo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class ExamenController {

        @FXML
        private Button annuler;

        @FXML
        private Button confirmerexamen;

        @FXML
        private DatePicker dateexamen;

        @FXML
        private ChoiceBox<String> etatexamen;

        @FXML
        void annuler(ActionEvent event) {

        }

        @FXML
        void confirmer(ActionEvent event) {

        }

        private ObservableList<Examen> listeExamen;

        /** THIS METHOD WILL BE CALLED FROM DashboardController */
        public void setListeExamen(ObservableList<Examen> liste) {
            this.listeExamen = liste;
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

            try {
                Connection conn = database.connectDb();

                String sql =
                        "INSERT INTO examen (date_examen, etat_examen) " +
                                "VALUES (?, ?)";

                PreparedStatement ps =
                        conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.setString(1, dateexamen.getValue().toString());
                ps.setString(2, etatexamen.getValue());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                int id = (rs.next()) ? rs.getInt(1) : 0;

                Examen nouveau = new Examen(
                        id,
                        dateexamen.getValue().toString(),
                        etatexamen.getValue()
                );

                // ADD TO MAIN TABLE LIST
                if (listeExamen!= null)
                    listeExamen.add(nouveau);

                new Alert(Alert.AlertType.INFORMATION, "Examen ajouté avec succès !").show();

                ps.close();
                conn.close();

                ((Stage) confirmerexamen.getScene().getWindow()).close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


