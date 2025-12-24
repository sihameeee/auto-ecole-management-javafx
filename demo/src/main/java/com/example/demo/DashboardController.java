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
import java.time.LocalDate;
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
    private TextField cherchercondidattext;

    @FXML
    private TextField cherchervehicule;

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

    private Vehicule vehiculeSelectionne = null;

    private ObservableList<Vehicule> listeVehicules = FXCollections.observableArrayList();

    @FXML private Button ajoutervehicule;
    @FXML private Button modifiervehicule;
    @FXML private Button supprimervehicule;

    @FXML
    private TableView<CondidatExamen> tableconex;

    @FXML
    private TableColumn<CondidatExamen, String> nomconex;

    @FXML
    private TableColumn<CondidatExamen, String> prenomconex;

    @FXML
    private TableColumn<CondidatExamen, String> typeconex;

    @FXML
    private TableColumn<CondidatExamen, String> permisconex;

    @FXML
    private TableColumn<CondidatExamen, String> decisionconex;

    private ObservableList<CondidatExamen> listeCondidatExamen = FXCollections.observableArrayList();

    @FXML private Button ajouterconex;
    @FXML private Button modifierconex;
    @FXML private Button supprimerconex;

    @FXML
    private TableView<Examen> tableexamen;

    @FXML
    private TableColumn<Examen, String> dateexamen;

    @FXML
    private TableColumn<Examen, String> etatexamen;

    private ObservableList<Examen> listeExamen = FXCollections.observableArrayList();

    @FXML private Button nouveauexamen;

    @FXML
    private TableView<Seance> tableseance;

    @FXML
    private TableColumn<Seance, String> dates;

    @FXML
    private TableColumn<Seance, String> heures;

    @FXML
    private TableColumn<Seance, String> condidatss;

    @FXML
    private TableColumn<Seance, String> moniteurss;

    @FXML
    private TableColumn<Seance, String> vehiculess;

    @FXML
    private TableColumn<Seance, String> permisss;

    @FXML
    private TableColumn<Seance, String> typess;

    @FXML
    private TableColumn<Seance, String> etatss;

    private ObservableList<Seance> listeSeance = FXCollections.observableArrayList();
    @FXML private Button configuration;
    @FXML private Button ajouters;
    @FXML private Button modifiers;
    @FXML private Button supprimers;
    @FXML private Button annulervehicule;

    @FXML
    private TextField rechercherseance;

    private ObservableList<SeanceToday> listeSeanceToday = FXCollections.observableArrayList();

    @FXML
    private TableView<SeanceToday> emploidutempstable;

    @FXML
    private TableColumn<SeanceToday, String> heurea;

    @FXML
    private TableColumn<SeanceToday, String> condidata;

    @FXML
    private TableColumn<SeanceToday, String> moniteura;

    @FXML
    private TableColumn<SeanceToday, String> vehiculea;

    @FXML
    private TableColumn<SeanceToday, String> permisa;

    @FXML
    private TableColumn<SeanceToday, String> typea;

    @FXML
    private TableColumn<SeanceToday, String> etata;

    @FXML
    private TableView<Paiement> tablepaiement;

    @FXML
    private TableColumn<Paiement, String> datep;

    @FXML
    private TableColumn<Paiement, String> condidatp;

    @FXML
    private TableColumn<Paiement, String> permisp;

    @FXML
    private TableColumn<Paiement, String> totalp;

    @FXML
    private TableColumn<Paiement, String> payep;

    @FXML
    private TableColumn<Paiement, String> restep;

    @FXML
    private TableColumn<Paiement, String> etatp;

    private ObservableList<Paiement> listePaiement = FXCollections.observableArrayList();

    ObservableList<Vehicule> listeFiltrée = FXCollections.observableArrayList();

    @FXML private Button ajouterp;
    @FXML private Button modifierp;
    @FXML private Button supprimerp;

    @FXML
    private TextField chercherpaiement;

    @FXML
    private ChoiceBox<String> filtreEtat;

    @FXML
    private Button deconnecter;
    private double x = (double)0.0F;
    private double y = (double)0.0F;
    // ====== INIT ======
    @FXML
    public void initialize() {

        String role = Session.getRole();

        if (role != null && role.equalsIgnoreCase("user")) {
            configuration.setVisible(false);
            configuration.setManaged(false); // enlève l'espace vide
        }

        showPanel(accueilanc);  // page par défaut
            chargerColonnesCondidat();
            chargerDonneesCondidat();
        supprimervehicule.setVisible(false);
        modifiervehicule.setVisible(false);
        vehiculetable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                chargerVehiculeDansForm(newV);

                ajoutervehicule.setVisible(false);   // ✅ cacher Ajouter
                annulervehicule.setVisible(false);
                modifiervehicule.setVisible(true);   // ✅ afficher Modifier
                supprimervehicule.setVisible(true);
                vehiculeSelectionne = newV;
            }
        });

        vehiculetable.setOnMouseClicked(event -> {
            if (vehiculetable.getSelectionModel().getSelectedItem() == null) {
                resetModeAjout();
            }
        });


        chargerColonnesMoniteur();
        chargerDonneesMoniteur();

        chargerColonnesConfiguration();
        chargerDonneesConfiguration();

        chargerColonnesVehicule();
        chargerDonneesVehicule();

        chargerColonnesExamenCondidat();

        chargerColonnesExamen();
        chargerDonneesExamen();

        tableexamen.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                examenId = newValue.getId();   // ← store examen ID
                chargerDonneesExamenCondidat(examenId);  // ← load candidates
            }
        });

        tableexamen.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tableexamen.getSelectionModel().isEmpty()) {
                modifierExamen();
            }
        });

        chargerColonnesSeance();
        chargerDonneesSeance();

        chargerColonnesEmploidutemps();
        chargerDonneesEmploidutemps();

        chargerColonnesPaiement();
        chargerDonneesPaiement();

        // 🔹 Recherche en direct
        cherchercondidattext.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerCondidats(newValue);
        });

        cherchervehicule.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerVehicule(newValue);
        });

        chercherpaiement.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerPaiement(newValue);
        });

        rechercherseance.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerSeances(newValue);
        });

        condidattable.setRowFactory(tv -> {
            TableRow<Condidat> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Condidat c = row.getItem();
                    ouvrirFicheCondidat(c);  // ↑ nouvelle méthode
                }
            });
            return row;
        });

        filtreEtat.getItems().addAll(
                "Toutes",
                "Terminée",
                "A venir",
                "Annulée"
        );

        filtreEtat.setValue("Toutes");
        filtreEtat.valueProperty().addListener((obs, oldV, newV) -> {
            filtrerParEtat(newV);
        });


        // Message au survol de la souris
        Tooltip tooltip = new Tooltip(
                "Double-cliquez sur un candidat pour afficher ses informations"
        );

        Tooltip.install(condidattable, tooltip);

        // Message au survol de la souris
        Tooltip tooltip1 = new Tooltip(
                "Double-cliquez sur un examen pour modifier ses informations"
        );

        Tooltip.install(tableexamen, tooltip1);

        // Message au survol de la souris
        Tooltip tooltip2 = new Tooltip(
                "Cliquez sur un vehicule pour modifier ses informations"
        );
        Tooltip.install(vehiculetable, tooltip2);
        // Message au survol de la souris


        typetextf.getItems().addAll("voiture", "camion", "bus", "moto");
        disponibilitetextf.getItems().addAll("disponible", "non disponible");

        vehiculetable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Vehicule v = vehiculetable.getSelectionModel().getSelectedItem();

                if (v != null) {
                    vehiculeSelectionne = v;

                    marquetextf.setText(v.getMarque());
                    matriculetextf.setText(v.getMatricule());
                    typetextf.setValue(v.getType());
                    disponibilitetextf.setValue(v.getDisponibilite());
                }
            }
        });



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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/formulaireCondidat.fxml"));
            Parent root = loader.load();
            // récupérer le contrôleur du formulaire
            CondidatController controller = loader.getController();
            controller.setListeCondidats(listeCondidats); // ❤️ transmettre la liste

            Stage stage = new Stage();
            stage.setTitle("Ajouter un candidat");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setOnHidden(event -> condidattable.refresh());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    @FXML
    private void modifierCondidat() {

        Condidat selected = condidattable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un candidat").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/formulaireCondidat.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur du formulaire
            CondidatController controller = loader.getController();
            controller.setListeCondidats(listeCondidats);
            controller.chargerCondidatPourModification(selected); // ❤️ ENVOIE LE CONDIDAT À MODIFIER

            Stage stage = new Stage();
            stage.setTitle("Modifier un candidat");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setOnHidden(event -> condidattable.refresh());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimerCondidat() {
        // 1️⃣ Récupérer le candidat sélectionné
        Condidat selectionne = condidattable.getSelectionModel().getSelectedItem();

        if (selectionne == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un candidat à supprimer.").show();
            return;
        }

        // 2️⃣ Confirmer la suppression
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Voulez-vous vraiment supprimer ce candidat ?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();

        if (confirmation.getResult() != ButtonType.YES) {
            return;
        }

        try {
            // 3️⃣ Supprimer de la BDD
            Connection conn = database.connectDb();
            String sql = "DELETE FROM condidats WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, selectionne.getId());
            ps.executeUpdate();
            ps.close();
            conn.close();

            // 4️⃣ Supprimer de la liste pour mettre à jour le TableView
            listeCondidats.remove(selectionne);

            new Alert(Alert.AlertType.INFORMATION, "Condidat supprimé avec succès !").show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression.").show();
        }
    }

    private void filtrerCondidats(String recherche) {
        if (listeCondidats == null) return;

        String texte = recherche.trim().toLowerCase();

        if (texte.isEmpty()) {
            // Champ vide → afficher tous les candidats
            condidattable.setItems(listeCondidats);
            return;
        }

        ObservableList<Condidat> listeFiltrée = FXCollections.observableArrayList();

        for (Condidat c : listeCondidats) {
            String nomPrenom = (c.getNom() + " " + c.getPrenom()).toLowerCase();
            if (nomPrenom.contains(texte)) {
                listeFiltrée.add(c);
            }
        }

        condidattable.setItems(listeFiltrée);
    }

    private void ouvrirFicheCondidat(Condidat c) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/formulaireCondidat.fxml"));
            Parent root = loader.load();

            // Récupérer le controller
            CondidatController controller = loader.getController();
            controller.afficherDetails(c);  // méthode à créer

            Stage stage = new Stage();
            stage.setTitle("Détails du candidat");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
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
    private void chargerColonnesSeance() {
        dates.setCellValueFactory(new PropertyValueFactory<>("dateSeance"));
        heures.setCellValueFactory(new PropertyValueFactory<>("heure"));
        condidatss.setCellValueFactory(new PropertyValueFactory<>("condidat"));
        moniteurss.setCellValueFactory(new PropertyValueFactory<>("moniteur"));
        vehiculess.setCellValueFactory(new PropertyValueFactory<>("vehicule"));
        permisss.setCellValueFactory(new PropertyValueFactory<>("typePermis"));
        typess.setCellValueFactory(new PropertyValueFactory<>("typeSeance"));
        etatss.setCellValueFactory(new PropertyValueFactory<>("etatSeance"));
    }

    private void chargerDonneesSeance() {
        try {
            Connection conn = database.connectDb();
            String sql = "SELECT " +
                    "    s.id," +
                    "    s.date_seance," +
                    "    s.heure," +
                    "    CONCAT(c.nom, ' ', c.prenom) AS candidat," +
                    "    CONCAT(m.nom, ' ', m.prenom) AS moniteur," +
                    "    v.marque AS vehicule," +
                    "    s.typeseance," +
                    "    c.permis," +
                    "    s.etat" +
                    "    FROM seance s" +
                    "    JOIN condidats c ON s.condidat_id = c.id" +
                    "    JOIN moniteur m ON s.moniteur_id = m.id" +
                    "    LEFT JOIN vehicules v ON s.vehicule_id = v.id;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            listeSeance.clear();

            while (rs.next()) {
                listeSeance.add(new Seance(
                        rs.getInt("id"),
                        rs.getDate("date_seance").toLocalDate(),
                        rs.getString("heure"),
                        rs.getString("candidat"),
                        rs.getString("moniteur"),
                        rs.getString("vehicule"),
                        rs.getString("permis"),
                        rs.getString("typeseance"),
                        rs.getString("etat")
                ));
            }

            tableseance.setItems(listeSeance);

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimerSeance() {
        // 1️⃣ Récupérer le candidat sélectionné
        Seance selectionne = tableseance.getSelectionModel().getSelectedItem();

        if (selectionne == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une seance à supprimer.").show();
            return;
        }

        // 2️⃣ Confirmer la suppression
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Voulez-vous vraiment supprimer cette seance ?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();

        if (confirmation.getResult() != ButtonType.YES) {
            return;
        }

        try {
            // 3️⃣ Supprimer de la BDD
            Connection conn = database.connectDb();
            String sql = "DELETE FROM seance WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, selectionne.getId());
            ps.executeUpdate();
            ps.close();
            conn.close();

            // 4️⃣ Supprimer de la liste pour mettre à jour le TableView
            listeSeance.remove(selectionne);

            new Alert(Alert.AlertType.INFORMATION, "Seance supprimés avec succès !").show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression.").show();
        }
    }

private void filtrerParEtat(String etat) {

    if (etat.equals("Toutes")) {
        tableseance.setItems(listeSeance);
        return;
    }

    ObservableList<Seance> listeFiltrée = FXCollections.observableArrayList();

    for (Seance s : listeSeance) {
        if (s.getEtatSeance().equalsIgnoreCase(etat)) {
            listeFiltrée.add(s);
        }
    }

    tableseance.setItems(listeFiltrée);
}


@FXML
    private void modifierSeance() {
        Seance selected = tableseance.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une séance").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/formulaireSeance.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur du formulaire de séance
            SeanceController controller = loader.getController();
            controller.chargerSeancePourEdition(selected); // ⚡ ENVOIE LA SÉANCE À MODIFIER

            Stage stage = new Stage();
            stage.setTitle("Modifier une séance");
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            // Quand la fenêtre se ferme, rafraîchir la TableView
            stage.setOnHidden(event -> tableseance.refresh());

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void ajouterSeance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/formulaireSeance.fxml"));
            Parent root = loader.load();
            // récupérer le contrôleur du formulaire
            SeanceController controller = loader.getController();
            controller.setListeSeances(listeSeance); // ❤️ transmettre la liste

            Stage stage = new Stage();
            stage.setTitle("Ajouter une Seance");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setOnHidden(event -> tableseance.refresh());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    private void filtrerSeances(String recherche) {
        if (listeSeance == null) return;

        String texte = recherche.trim().toLowerCase();

        if (texte.isEmpty()) {
            // Champ vide → afficher tous les candidats
            tableseance.setItems(listeSeance);
            return;
        }

        ObservableList<Seance> listeFiltrée = FXCollections.observableArrayList();

        for (Seance s : listeSeance) {
            String nomPrenom = (s.getMoniteur() + " " + s.getCondidat()+ " " +
                    s.getDateSeance().toString()).toLowerCase();
            if (nomPrenom.contains(texte)) {
                listeFiltrée.add(s);
            }
        }

        tableseance.setItems(listeFiltrée);
    }


    // ====== BOUTON AJOUTER UN paiement ======
    private void filtrerPaiement(String recherche) {
        if (listePaiement == null) return;

        String texte = recherche.trim().toLowerCase();

        if (texte.isEmpty()) {
            // Champ vide → afficher tous les candidats
            tablepaiement.setItems(listePaiement);
            return;
        }

        ObservableList<Paiement> listeFiltrée = FXCollections.observableArrayList();

        for (Paiement p : listePaiement) {
            String nomPrenom = (p.getCondidat()+ " " +
                    p.getDate().toString()).toLowerCase();
            if (nomPrenom.contains(texte)) {
                listeFiltrée.add(p);
            }
        }

        tablepaiement.setItems(listeFiltrée);
    }

    private void chargerColonnesPaiement() {
        datep.setCellValueFactory(new PropertyValueFactory<>("date"));
        condidatp.setCellValueFactory(new PropertyValueFactory<>("condidat"));
        permisp.setCellValueFactory(new PropertyValueFactory<>("permis"));
        totalp.setCellValueFactory(new PropertyValueFactory<>("total"));
        payep.setCellValueFactory(new PropertyValueFactory<>("paye"));
        restep.setCellValueFactory(new PropertyValueFactory<>("reste"));
        etatp.setCellValueFactory(new PropertyValueFactory<>("etat"));
    }

    private void chargerDonneesPaiement() {
        try {
            Connection conn = database.connectDb();
            String sql = "SELECT " +
                    "    p.id," +
                    "    p.date_paiement," +
                    "    p.condidat_id," +
                    "    c.nom, c.prenom," +
                    "    c.permis," +
                    "    p.montant_total," +
                    "    p.montant_paye," +
                    "    p.montant_restant," +
                    "    p.etat" +
                    "    FROM paiement p" +
                    "    JOIN condidats c ON p.condidat_id = c.id";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            listePaiement.clear();

            while (rs.next()) {
                listePaiement.add(new Paiement(
                        rs.getInt("id"),
                        rs.getString("date_paiement"),

                        rs.getString("nom") + " " + rs.getString("prenom"),
                        rs.getInt("condidat_id"),
                        rs.getString("permis"),
                        rs.getDouble("montant_total"),
                        rs.getDouble("montant_paye"),
                        rs.getDouble("montant_restant"),
                        rs.getString("etat")
                ));
            }

            tablepaiement.setItems(listePaiement);

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterPaiement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/formulairePaiement.fxml"));
            Parent root = loader.load();
            // récupérer le contrôleur du formulaire
            PaiementController controller = loader.getController();
            controller.setListePaiement(listePaiement); // ❤️ transmettre la liste

            Stage stage = new Stage();
            stage.setTitle("Ajouter un paiement");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setOnHidden(event -> tablepaiement.refresh());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    @FXML
    private void modifierPaiement() {

        Paiement selection = tablepaiement.getSelectionModel().getSelectedItem();

        if (selection == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un paiement.").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/formulairePaiement.fxml"));
            Parent root = loader.load();

            PaiementController controller = loader.getController();
            controller.setListePaiement(listePaiement);      // ObservableList
            controller.setModeModification(selection);       // 🔥 envoyer paiement

            Stage stage = new Stage();
            stage.setTitle("Modifier paiement");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimerPaiment() {
        // 1️⃣ Récupérer le candidat sélectionné
        Paiement selectionne = tablepaiement.getSelectionModel().getSelectedItem();

        if (selectionne == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un paiement à supprimer.").show();
            return;
        }

        // 2️⃣ Confirmer la suppression
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Voulez-vous vraiment supprimer ce paiement ?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();

        if (confirmation.getResult() != ButtonType.YES) {
            return;
        }

        try {
            // 3️⃣ Supprimer de la BDD
            Connection conn = database.connectDb();
            String sql = "DELETE FROM paiement WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, selectionne.getId());
            ps.executeUpdate();
            ps.close();
            conn.close();

            // 4️⃣ Supprimer de la liste pour mettre à jour le TableView
            listePaiement.remove(selectionne);

            new Alert(Alert.AlertType.INFORMATION, "Paiement supprimé avec succès !").show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression.").show();
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
public void setListeVehicules(ObservableList<Vehicule> listeVehicules) {
    this.listeVehicules = listeVehicules;
}

    private void chargerVehiculeDansForm(Vehicule v) {
        marquetextf.setText(v.getMarque());
        matriculetextf.setText(v.getMatricule());
        typetextf.setValue(v.getType());
        disponibilitetextf.setValue(v.getDisponibilite());
    }

    private void resetModeAjout() {
        vehiculeSelectionne = null;

        marquetextf.clear();
        matriculetextf.clear();
        typetextf.setValue(null);
        disponibilitetextf.setValue(null);

        ajoutervehicule.setVisible(true);
        annulervehicule.setVisible(true);
        modifiervehicule.setVisible(false);
        supprimervehicule.setVisible(false);
    }


    @FXML
    private void ajouterVehicule() {

        String marque = marquetextf.getText().trim();
        String matricule = matriculetextf.getText().trim();
        String type = typetextf.getValue();
        String disponibilite = disponibilitetextf.getValue();

        if (marque.isEmpty() || matricule.isEmpty() || type == null || disponibilite == null) {
            new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs").show();
            return;
        }

        if (!matricule.matches("^[0-9]{10}$")) {
            new Alert(Alert.AlertType.ERROR, "Matricule invalide (10 chiffres obligatoires)").show();
            return;
        }

        try {
            Connection conn = database.connectDb();

            String sql = "INSERT INTO vehicules(marque, matricule, type, disponibilite) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, marque);
            ps.setString(2, matricule);
            ps.setString(3, type);
            ps.setString(4, disponibilite);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int id = 0;
            if (rs.next()) id = rs.getInt(1);

            // ✅ AJOUT DIRECT DANS LA TABLE (REFRESH INSTANT)
            listeVehicules.add(new Vehicule(id, marque, matricule, type, disponibilite));

            new Alert(Alert.AlertType.INFORMATION, "Véhicule ajouté avec succès").show();

            marquetextf.clear();
            matriculetextf.clear();
            typetextf.setValue(null);
            disponibilitetextf.setValue(null);

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur BDD : " + e.getMessage()).show();
        }
    }

    @FXML
    private void clearForm() {
        marquetextf.clear();
        matriculetextf.clear();
        typetextf.setValue(null);
        disponibilitetextf.setValue(null);

        vehiculeSelectionne = null; // annule la sélection pour éviter la modification
    }


    @FXML
    private void modifierVehicule() {

        if (vehiculeSelectionne == null) {
            new Alert(Alert.AlertType.ERROR, "Veuillez sélectionner un véhicule").show();
            return;
        }

        String marque = marquetextf.getText().trim();
        String matricule = matriculetextf.getText().trim();
        String type = typetextf.getValue();
        String disponibilite = disponibilitetextf.getValue();

        try {
            Connection conn = database.connectDb();

            String sql = "UPDATE vehicules SET marque=?, matricule=?, type=?, disponibilite=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, marque);
            ps.setString(2, matricule);
            ps.setString(3, type);
            ps.setString(4, disponibilite);
            ps.setInt(5, vehiculeSelectionne.getId());

            ps.executeUpdate();

            // ✅ METTRE À JOUR DANS LA TABLE
            vehiculeSelectionne.setMarque(marque);
            vehiculeSelectionne.setMatricule(matricule);
            vehiculeSelectionne.setType(type);
            vehiculeSelectionne.setDisponibilite(disponibilite);

            vehiculetable.refresh();

            new Alert(Alert.AlertType.INFORMATION, "Véhicule modifié avec succès").show();

            vehiculeSelectionne = null;

            marquetextf.clear();
            matriculetextf.clear();
            typetextf.setValue(null);
            disponibilitetextf.setValue(null);

            // ✅ Revenir au mode ajout
            resetModeAjout();
            vehiculetable.getSelectionModel().clearSelection();

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur BDD : " + e.getMessage()).show();
        }
    }

    @FXML
    private void supprimerVehicule() {

        Vehicule selectionne = vehiculetable.getSelectionModel().getSelectedItem();


        // 2️⃣ Confirmer la suppression
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Voulez-vous vraiment supprimer ce vehicule ?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();

        if (confirmation.getResult() != ButtonType.YES) {
            return;
        }

        try {
            // 3️⃣ Supprimer de la BDD
            Connection conn = database.connectDb();
            String sql = "DELETE FROM vehicules WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, selectionne.getId());
            ps.executeUpdate();
            ps.close();
            conn.close();

            // 4️⃣ Supprimer de la liste pour mettre à jour le TableView
            listeVehicules.remove(selectionne);

            new Alert(Alert.AlertType.INFORMATION, "Vehicule supprimé avec succès !").show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression.").show();
        }
    }

    private void filtrerVehicule(String recherche) {
        if (listeVehicules == null) return;

        String texte = recherche.trim().toLowerCase();

        if (texte.isEmpty()) {
            // Champ vide → afficher tous les candidats
            vehiculetable.setItems(listeVehicules);
            return;
        }

        ObservableList<Vehicule> listeFiltrée = FXCollections.observableArrayList();

        for (Vehicule v : listeVehicules) {
            String Marque = (v.getMarque()).toLowerCase();
            if (Marque.contains(texte)) {
                listeFiltrée.add(v);
            }
        }

        vehiculetable.setItems(listeFiltrée);
    }

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

    // ====== BOUTON EXAMEN ======

    private void chargerColonnesExamen() {
        dateexamen.setCellValueFactory(new PropertyValueFactory<>("dateExamen"));
        etatexamen.setCellValueFactory(new PropertyValueFactory<>("etatExamen"));
    }

    private void chargerDonneesExamen() {
        try {
            Connection conn = database.connectDb();
            String sql = "SELECT id, date_examen, etat_examen FROM examen";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            listeExamen.clear();

            while (rs.next()) {
                listeExamen.add(new Examen(
                        rs.getInt("id"),
                        rs.getDate("date_examen").toLocalDate(),
                        rs.getString("etat_examen")
                ));
            }

            tableexamen.setItems(listeExamen);

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterExamen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/formulaireExamen.fxml"));
            Parent root = loader.load();
            // récupérer le contrôleur du formulaire
            ExamenController controller = loader.getController();
            controller.setListeExamen(listeExamen); // ❤️ transmettre la liste

            Stage stage = new Stage();
            stage.setTitle("Ajouter un examen");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setOnHidden(event -> tableexamen.refresh());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();


        }
    }

    @FXML
    private void modifierExamen() {

        Examen selected = tableexamen.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un examen").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/formulaireExamen.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur du formulaire
            ExamenController controller = loader.getController();
            controller.setListeExamen(listeExamen);
            controller.chargerExamenPourModification(selected); // ❤️ ENVOIE LE CONDIDAT À MODIFIER

            Stage stage = new Stage();
            stage.setTitle("Modifier un examen");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setOnHidden(event -> tableexamen.refresh());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    private void supprimerCondidatExamen() {

        CondidatExamen selection = tableconex.getSelectionModel().getSelectedItem();

        if (selection == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un candidat.").show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Voulez-vous vraiment supprimer ce candidat de l'examen ?");
        alert.setHeaderText(null);

        if (alert.showAndWait().get() == ButtonType.OK) {
            try (Connection c = database.connectDb()) {

                String sql = "DELETE FROM condidat_examen WHERE id = ?";
                PreparedStatement ps = c.prepareStatement(sql);
                ps.setInt(1, selection.getId());
                ps.executeUpdate();

                tableconex.getItems().remove(selection);

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression.").show();
            }
        }
    }

    @FXML
    private void modifierCondidatExamen() {
        CondidatExamen selection = tableconex.getSelectionModel().getSelectedItem();


        if (selection == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un candidat.").show();
            return;
        }
        Examen examenSelectionne =
                tableexamen.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo/formulaireCondidatExamen.fxml")
            );
            Parent root = loader.load();

            CondidatExamenController controller = loader.getController();
            controller.setModeModification(selection);
            controller.setExamen(examenSelectionne);


            Stage stage = new Stage();
            stage.setTitle("Modifier candidat examen");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ====== BOUTON ExamenCondidat ======

    private void chargerColonnesExamenCondidat() {
        nomconex.setCellValueFactory(new PropertyValueFactory<>("condidat"));
        permisconex.setCellValueFactory(new PropertyValueFactory<>("typePermis"));
        typeconex.setCellValueFactory(new PropertyValueFactory<>("typeExamen"));
        decisionconex.setCellValueFactory(new PropertyValueFactory<>("decision"));
    }
    private int examenId;
    private void chargerDonneesExamenCondidat(int examenId) {
        try {
            Connection conn = database.connectDb();
            String sql = "SELECT ce.id, CONCAT(c.nom, ' ', c.prenom) AS condidat, c.permis, ce.type_examen, ce.decision FROM examen_candidat ce JOIN condidats c ON ce.candidat_id = c.id WHERE ce.examen_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, examenId);
            ResultSet rs = ps.executeQuery();

            listeCondidatExamen.clear();

            while (rs.next()) {
                listeCondidatExamen.add(new CondidatExamen(
                        rs.getInt("id"),
                        rs.getString("condidat"),
                        rs.getString("permis"),
                        rs.getString("type_examen"),
                        rs.getString("decision")
                ));
            }

            tableconex.setItems(listeCondidatExamen);

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterCondidatExamen() {
        Examen examenSelectionne = tableexamen.getSelectionModel().getSelectedItem();

        if (examenSelectionne == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un examen.").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo/formulaireCondidatExamen.fxml")
            );
            Parent root = loader.load();

            CondidatExamenController controller = loader.getController();
            controller.setExamen(examenSelectionne);

            // envoyer la liste du tableau
            controller.setListeCondidatExamen(tableconex.getItems());

            Stage stage = new Stage();
            stage.setTitle("Ajouter un candidat à l'examen");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    //EMPLOI DU TEMPS DE LA JOURNEE
    private void chargerColonnesEmploidutemps() {
        heurea.setCellValueFactory(new PropertyValueFactory<>("heure"));
        condidata.setCellValueFactory(new PropertyValueFactory<>("condidat"));
        moniteura.setCellValueFactory(new PropertyValueFactory<>("moniteur"));
        vehiculea.setCellValueFactory(new PropertyValueFactory<>("vehicule"));
        permisa.setCellValueFactory(new PropertyValueFactory<>("typePermis"));
        typea.setCellValueFactory(new PropertyValueFactory<>("typeSeance"));
        etata.setCellValueFactory(new PropertyValueFactory<>("etatSeance"));
    }

    private void chargerDonneesEmploidutemps() {
        try {
            // Connexion à la base
            Connection conn = database.connectDb();

            // Récupérer la date du jour
            LocalDate aujourdHui = LocalDate.now();

            // Requête SQL : ne récupérer que les séances du jour
            String sql = "SELECT " +
                    "s.id, " +
                    "s.heure, " +
                    "CONCAT(c.nom, ' ', c.prenom) AS condidat, " +
                    "CONCAT(m.nom, ' ', m.prenom) AS moniteur, " +
                    "v.marque AS vehicule, " +
                    "c.permis AS typePermis, " +
                    "s.type AS typeSeance, " +
                    "s.etat AS etatSeance " +
                    "FROM seance s " +
                    "JOIN condidats c ON s.condidat_id = c.id " +
                    "JOIN moniteur m ON s.moniteur_id = m.id " +
                    "JOIN vehicules v ON s.vehicule_id = v.id " +
                    "WHERE s.date_seance = ? " +
                    "ORDER BY s.heure ASC;"; // tri par heure croissante

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(aujourdHui));

            ResultSet rs = ps.executeQuery();

            // Vider la liste existante
            listeSeanceToday.clear();

            // Remplir la liste avec les séances du jour
            while (rs.next()) {
                listeSeanceToday.add(new SeanceToday(
                        rs.getInt("id"),
                        rs.getString("heure"),
                        rs.getString("condidat"),
                        rs.getString("moniteur"),
                        rs.getString("vehicule"),
                        rs.getString("typePermis"),
                        rs.getString("typeSeance"),
                        rs.getString("etatSeance")
                ));
            }

            // Lier la liste à la TableView
            emploidutempstable.setItems(listeSeanceToday);

            // Fermer les ressources
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
