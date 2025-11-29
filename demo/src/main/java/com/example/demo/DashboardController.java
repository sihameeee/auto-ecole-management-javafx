package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController {

    // ----- PANELS -----
    @FXML
    private AnchorPane rootPane;

    @FXML
    private AnchorPane accueilanc;

    @FXML
    private AnchorPane candidatsanc;

    @FXML
    private AnchorPane vehiculesanc;

    @FXML
    private AnchorPane seancesanc;

    @FXML
    private AnchorPane examensanc;

    @FXML
    private AnchorPane paiementanc;

    @FXML
    private AnchorPane statistiquesanc;

    @FXML
    private AnchorPane configurationanc;

    @FXML
    private TableView<Condidat> condidattable;

    @FXML
    private TableColumn<Condidat, String> nomcondidat;

    @FXML
    private TableColumn<Condidat, String> prenomcondidat;

    @FXML
    private TableColumn<Condidat, String> numcondidat;

    @FXML
    private TableColumn<Condidat, String> adressecondidat;

    @FXML
    private TableColumn<Condidat, String> datenaisscondidat;

    @FXML
    private TableColumn<Condidat, String> typecondidat;

    @FXML
    private TableColumn<Condidat, String> etatcondidat;

    private ObservableList<Condidat> listeCondidats = FXCollections.observableArrayList();

    @FXML
    private TableView<Moniteur> moniteurtable;

    @FXML
    private TableColumn<Moniteur, String> nommoniteur;

    @FXML
    private TableColumn<Moniteur, String> prenommoniteur;

    @FXML
    private TableColumn<Moniteur, String> usernamemoniteur;

    @FXML
    private TableColumn<Moniteur, String> passwordmoniteur;

    @FXML
    private TableColumn<Moniteur, String> rolemoniteur;

    @FXML
    private TableColumn<Moniteur, String> disponibilitemoniteur;

    private ObservableList<Moniteur> listeMoniteurs = FXCollections.observableArrayList();

    @FXML private TextField prixconfigtext;
    @FXML private ChoiceBox<String> typeconfigtext;

    @FXML
    private TableView<Configuration> configurationtable;

    @FXML
    private TableColumn<Configuration, String> typeconfig;

    @FXML
    private TableColumn<Configuration, String> prixconfig;

    private ObservableList<Configuration> listeConfig = FXCollections.observableArrayList();

    @FXML private Button ajouterconfig;
    @FXML private Button modifierconfig;
    @FXML private Button supprimerconfig;


    @FXML private TextField marquetextf;
    @FXML private TextField matriculetextf;
    @FXML private ChoiceBox<String> disponibilitetextf;
    @FXML private ChoiceBox<String> typetextf;

    @FXML
    private TableView<Vehicule> vehiculetable;

    @FXML
    private TableColumn<Vehicule, String> marquevehicule;

    @FXML
    private TableColumn<Vehicule, String> matriculevehicule;

    @FXML
    private TableColumn<Vehicule, String> typevehicule;

    @FXML
    private TableColumn<Vehicule, String> disponibilitevehicule;

    private ObservableList<Vehicule> listeVehicules = FXCollections.observableArrayList();

    @FXML private Button ajoutervehicule;
    @FXML private Button modifiervehicule;
    @FXML private Button supprimervehicule;

    @FXML
    private Button deconnecter;
    private double x = (double)0.0F;
    private double y = (double)0.0F;
    // ====== INIT ======
    @FXML
    public void initialize() {
        showPanel(accueilanc);  // page par défaut
            chargerColonnesCondidat();
            chargerDonneesCondidat();

        chargerColonnesMoniteur();
        chargerDonneesMoniteur();

        chargerColonnesConfiguration();
        chargerDonneesConfiguration();

        chargerColonnesVehicule();
        chargerDonneesVehicule();

    }

    // Affiche le panel choisi et masque tous les autres
    private void showPanel(AnchorPane panel) {
        accueilanc.setVisible(false);
        candidatsanc.setVisible(false);
        vehiculesanc.setVisible(false);
        seancesanc.setVisible(false);
        examensanc.setVisible(false);
        paiementanc.setVisible(false);
        statistiquesanc.setVisible(false);
        configurationanc.setVisible(false);

        panel.setVisible(true);
        panel.toFront();
    }

    // ====== MENU EVENTS ======
    @FXML
    private void openAccueil() {
        showPanel(accueilanc);
    }

    @FXML
    private void openCandidats() {
        showPanel(candidatsanc);
    }

    @FXML
    private void openVehicules() {
        showPanel(vehiculesanc);
    }

    @FXML
    private void openSeances() {
        showPanel(seancesanc);
    }

    @FXML
    private void openExamens() {
        showPanel(examensanc);
    }

    @FXML
    private void openPaiement() {
        showPanel(paiementanc);
    }

    @FXML
    private void openStatistiques() {
        showPanel(statistiquesanc);
    }

    @FXML
    private void openConfiguration() {
        showPanel(configurationanc);
    }



    // ====== BOUTON AJOUTER UN CONDIDAT ======

    private void chargerColonnesCondidat() {
        nomcondidat.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomcondidat.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        numcondidat.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        adressecondidat.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        datenaisscondidat.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        typecondidat.setCellValueFactory(new PropertyValueFactory<>("permis"));
        etatcondidat.setCellValueFactory(new PropertyValueFactory<>("etat"));
    }

    private void chargerDonneesCondidat() {
        try {
            Connection conn = database.connectDb();
            String sql = "SELECT id, nom, prenom, adresse, telephone, date_naissance, etat, permis FROM condidats";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            listeCondidats.clear();

            while (rs.next()) {
                listeCondidats.add(new Condidat(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone"),
                        rs.getString("adresse"),
                        rs.getString("date_naissance"),
                        rs.getString("permis"),
                        rs.getString("etat")
                ));
            }

            condidattable.setItems(listeCondidats);

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void ajouterCondidat() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/formulaireCondidat.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Ajouter un candidat");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    // ====== BOUTON AJOUTER UN MONITEUR ======

    private void chargerColonnesMoniteur() {
        nommoniteur.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenommoniteur.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        usernamemoniteur.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordmoniteur.setCellValueFactory(new PropertyValueFactory<>("password"));
        rolemoniteur.setCellValueFactory(new PropertyValueFactory<>("role"));
        disponibilitemoniteur.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
    }

    private void chargerDonneesMoniteur() {
        try {
            Connection conn = database.connectDb();
            String sql = "SELECT * FROM moniteur";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            listeMoniteurs.clear();

            while (rs.next()) {
                listeMoniteurs.add(new Moniteur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("disponibilite")
                ));
            }

            moniteurtable.setItems(listeMoniteurs);

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterMoniteur() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/formulaireMoniteur.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Ajouter un moniteur");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    // ====== BOUTON AJOUTER UNE SEANCE ======
    @FXML
    private void ajouterseance() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/formulaireSeance.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Ajouter une Seance");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    // ====== BOUTON AJOUTER UN paiement ======
    @FXML
    private void ajouterPaiement() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/formulairePaiement.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Paiement");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    // ====== BOUTON CONFIGURATION ======

    private void chargerColonnesConfiguration() {
        typeconfig.setCellValueFactory(new PropertyValueFactory<>("typePermis"));
        prixconfig.setCellValueFactory(new PropertyValueFactory<>("prix"));
    }

    private void chargerDonneesConfiguration() {
        try {
            Connection conn = database.connectDb();
            String sql = "SELECT id, type_permis, prix FROM configuration";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            listeConfig.clear();

            while (rs.next()) {
                listeConfig.add(new Configuration(
                        rs.getInt("id"),
                        rs.getString("type_permis"),
                        rs.getDouble("prix")
                ));
            }

            configurationtable.setItems(listeConfig);

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// ====== BOUTON VEHICULE ======

    private void chargerColonnesVehicule() {
        marquevehicule.setCellValueFactory(new PropertyValueFactory<>("marque"));
        matriculevehicule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        typevehicule.setCellValueFactory(new PropertyValueFactory<>("type"));
        disponibilitevehicule.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
    }

    private void chargerDonneesVehicule() {
        try {
            Connection conn = database.connectDb();
            String sql = "SELECT * FROM vehicules";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            listeVehicules.clear();

            while (rs.next()) {
                listeVehicules.add(new Vehicule(
                        rs.getInt("id"),
                        rs.getString("marque"),
                        rs.getString("matricule"),
                        rs.getString("type"),
                        rs.getString("disponibilite")
                ));
            }

            vehiculetable.setItems(listeVehicules);

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void logout() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Message de Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sure de se deconnecter ?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {

                deconnecter.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void minimize() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setIconified(true);
    }

    public void close() {
        System.exit(0);
    }
}
