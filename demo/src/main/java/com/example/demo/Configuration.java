package com.example.demo;

public class Configuration {

    private int id;
    private String typePermis;
    private double prix;   // or BigDecimal if you prefer more accuracy

    public Configuration(int id, String typePermis, double prix) {
        this.id = id;
        this.typePermis = typePermis;
        this.prix = prix;
    }

    public int getId() { return id; }

    public String getTypePermis() { return typePermis; }

    public double getPrix() { return prix; }

    public void setId(int id) { this.id = id; }

    public void setTypePermis(String typePermis) { this.typePermis = typePermis; }

    public void setPrix(double prix) { this.prix = prix; }
}
