package com.example.demo;


public class Seance {

    private int id;
    private String dateSeance;
    private String heure;
    private String condidat;
    private String moniteur;
    private String vehicule;
    private String typePermis;
    private String typeSeance;
    private String etatSeance;

    public Seance(int id, String dateSeance, String heure, String condidat, String moniteur, String vehicule, String typePermis, String typeSeance, String etatSeance) {
        this.id = id;
        this.dateSeance = dateSeance;
        this.heure = heure;
        this.condidat = condidat;
        this.moniteur = moniteur;
        this.vehicule = vehicule;
        this.typePermis = typePermis;
        this.typeSeance = typeSeance;
        this.etatSeance = etatSeance;
    }

    public int getId() { return id; }
    public String getDateSeance() { return dateSeance; }
    public String getHeure() { return heure; }
    public String getCondidat() { return condidat; }
    public String getMoniteur() { return moniteur; }
    public String getVehicule() { return vehicule; }
    public String getTypePermis() { return typePermis; }
    public String getTypeSeance() { return typeSeance; }
    public String getEtatSeance() { return etatSeance; }
}

