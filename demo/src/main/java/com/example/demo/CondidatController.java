package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDFont;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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
    @FXML private Button imprimer;
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
                    "Numéro invalide : il doit contenir 10 chiffres \n et commencer par 05, 06 ou 07.")
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

    public void afficherDetails(Condidat c) {
        modeModification = false;  // empêche update

        // remplir les champs
        nomf.setText(c.getNom());
        prenomf.setText(c.getPrenom());
        adressef.setText(c.getAdresse());
        numtelf.setText(c.getTelephone());
        datenaissf.setValue(LocalDate.parse(c.getDateNaissance()));
        typef.setValue(c.getPermis());
        etatf.setValue(c.getEtat());

        // rendre TOUT les champs non modifiables
        nomf.setDisable(true);
        prenomf.setDisable(true);
        adressef.setDisable(true);
        numtelf.setDisable(true);
        datenaissf.setDisable(true);
        typef.setDisable(true);
        etatf.setDisable(true);

        // cacher bouton confirmer
        confirmer.setVisible(false);

        // afficher bouton imprimer
        imprimer.setVisible(true);

        // stocker candidat courant
        this.condidatAModifier = c;
    }

    @FXML
    private void imprimerPDF() {

        Condidat c = condidatAModifier;

        if (c == null) {
            new Alert(Alert.AlertType.ERROR, "Aucun candidat sélectionné pour l'impression.").show();
            return;
        }

        try (PDDocument doc = new PDDocument()) {

            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                // ---- Titre ----
                PDFont fontTitle = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                // ici tu peux aussi utiliser 'new' via reflection si tu veux vraiment
                cs.beginText();
                cs.setFont(fontTitle, 20);
                cs.newLineAtOffset(50, 750);
                cs.showText("Fiche du Candidat");
                cs.endText();

                int y = 700;
                int step = 20;

                PDFont fontText  = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                writeLine(cs, "Nom : " + safe(c.getNom()), y, fontText); y -= step;
                writeLine(cs, "Prénom : " + safe(c.getPrenom()), y, fontText); y -= step;
                writeLine(cs, "Téléphone : " + safe(c.getTelephone()), y, fontText); y -= step;
                writeLine(cs, "Adresse : " + safe(c.getAdresse()), y, fontText); y -= step;
                writeLine(cs, "Date de naissance : " + safe(String.valueOf(c.getDateNaissance())), y, fontText); y -= step;
                writeLine(cs, "Permis : " + safe(c.getPermis()), y, fontText); y -= step;
                writeLine(cs, "État : " + safe(c.getEtat()), y, fontText);
            }

            String filename = "Candidat_" + c.getId() + ".pdf";
            doc.save(filename);

            new Alert(Alert.AlertType.INFORMATION, "PDF généré : " + filename).show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Erreur lors de la génération du PDF : " + e.getMessage()).show();
        }
    }

    private void writeLine(PDPageContentStream cs, String text, int y, PDFont font) throws IOException {
        cs.beginText();
        cs.setFont(font, 12);
        cs.newLineAtOffset(50, y);
        cs.showText(text);
        cs.endText();
    }

    private String safe(String s) {
        return (s == null) ? "" : s;
    }

    @FXML
    private void ouvrirWord2() {
        try {
            InputStream is = getClass().getResourceAsStream("Condidature.docx");

            if (is == null) {
                System.out.println("Fichier introuvable dans resources");
                return;
            }

            File tempFile = Files.createTempFile("liste_examen", ".docx").toFile();
            tempFile.deleteOnExit();

            Files.copy(is, tempFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            Desktop.getDesktop().open(tempFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
