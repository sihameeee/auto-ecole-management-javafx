package com.example.demo;


public class SeanceToday {

    private int id;
    private String heure;
    private String condidat;
    private String moniteur;
    private String vehicule;
    private String typePermis;
    private String typeSeance;
    private String etatSeance;

    public SeanceToday(int id, String heure, String condidat, String moniteur, String vehicule, String typePermis, String typeSeance, String etatSeance) {
        this.id = id;
        this.heure = heure;
        this.condidat = condidat;
        this.moniteur = moniteur;
        this.vehicule = vehicule;
        this.typePermis = typePermis;
        this.typeSeance = typeSeance;
        this.etatSeance = etatSeance;
    }

    public int getId() { return id; }
    public String getHeure() { return heure; }
    public String getCondidat() { return condidat; }
    public String getMoniteur() { return moniteur; }
    public String getVehicule() { return vehicule; }
    public String getTypePermis() { return typePermis; }
    public String getTypeSeance() { return typeSeance; }
    public String getEtatSeance() { return etatSeance; }
}

