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
    public void setId(int id) { this.id = id; }
    public int getId() { return id; }
    public void setNom(String nom) { this.nom = nom; }
    public String getNom() { return nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getPrenom() { return prenom; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getTelephone() { return telephone; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getAdresse() { return adresse; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getDateNaissance() { return dateNaissance; }
    public void setPermis(String permis) { this.permis = permis; }
    public String getPermis() { return permis; }
    public void setEtat(String etat) { this.etat = etat; }
    public String getEtat() { return etat; }
}

