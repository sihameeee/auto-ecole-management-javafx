package com.example.demo;

public class CondidatExamen {
    private int id;
    private String nom;
    private String prenom;
    private String typePermis;
    private String typeExamen;
    private String decision;

    public CondidatExamen(int id, String nom, String prenom, String typePermis, String typeExamen, String decision) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.typePermis = typePermis;
        this.typeExamen = typeExamen;
        this.decision = decision;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getTypePermis() { return typePermis; }
    public String getTypeExamen() { return typeExamen; }
    public String getDecision() { return decision; }

}

