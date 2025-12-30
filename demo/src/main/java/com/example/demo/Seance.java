package com.example.demo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Seance {

    private int id;
    private LocalDate dateSeance;
    private String heure;

    private String condidat;      // "Nom Prenom"
    private String moniteur;      // "Nom Prenom"
    private String vehicule;
    private String typePermis;
    private String typeSeance;
    private String etatSeance;

    public Seance(
            int id,
            LocalDate dateSeance,
            String heure,
            String condidat,
            String moniteur,
            String vehicule,
            String typePermis,
            String typeSeance,
            String etatSeance
    ) {
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
    public void setId(int id) { this.id = id; }
    public String getCondidat() { return condidat; }
    public void setCondidat(String condidat) { this.condidat = condidat; }
    public String getMoniteur() { return moniteur; }
    public void setMoniteur(String moniteur) { this.moniteur = moniteur; }
    public LocalDate getDateSeance() { return dateSeance; }
    public void setDate(LocalDate dateSeance) { this.dateSeance = dateSeance; }
    public String getHeure() { return heure; }
    public void setHeure(String heure) { this.heure = heure; }
    public String getHeureHHmm() {
        return heure.toString().substring(0,5); // format "HH:mm"
    }

    public String getVehicule() { return vehicule; }
    public void setVehicule(String vehicule) { this.vehicule = vehicule; }
    public String getTypePermis() { return typePermis; }
    public void setTypePermis(String typePermis) { this.typePermis = typePermis; }
    public String getTypeSeance() { return typeSeance; }
    public void setTypeSeance(String typeSeance) { this.typeSeance = typeSeance; }
    public String getEtatSeance() { return etatSeance; }
    public void setEtatSeance(String etatSeance) { this.etatSeance = etatSeance; }
}
