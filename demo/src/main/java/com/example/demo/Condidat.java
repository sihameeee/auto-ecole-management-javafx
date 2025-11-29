package com.example.demo;


public class Condidat {

    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;
    private String dateNaissance;
    private String permis;
    private String etat;

    public Condidat(int id, String nom, String prenom, String telephone, String adresse, String dateNaissance, String permis, String etat) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.permis = permis;
        this.etat = etat;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getTelephone() { return telephone; }
    public String getAdresse() { return adresse; }
    public String getDateNaissance() { return dateNaissance; }
    public String getPermis() { return permis; }
    public String getEtat() { return etat; }
}

