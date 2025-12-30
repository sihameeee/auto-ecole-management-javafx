package com.example.demo;


public class Paiement {

    private int id;
    private String date;
    private int condidatId;
    private String condidat;
    private String permis;
    private double total;
    private double paye;
    private double reste;
    private String etat;

    public Paiement(int id, String date, String condidat,int condidatId, String permis, double total, double paye, double reste, String etat) {
        this.id = id;
        this.date = date;
        this.condidat = condidat;
        this.condidatId = condidatId;
        this.permis = permis;
        this.total = total;
        this.paye = paye;
        this.reste = reste;
        this.etat = etat;
    }
    public void setId(int id) { this.id = id; }
    public int getId() { return id; }
    public void setCondidatId(int condidatId) { this.condidatId = condidatId; }
    public int getCondidatId() { return condidatId; }
    public String getDate() { return date; }
    public void setCondidat(String condidat) { this.condidat = condidat; }
    public String getCondidat() { return condidat; }
    public void setPermis(String permis) { this.permis = permis; }
    public String getPermis() { return permis; }
    public void setTotal(double total) { this.total = total; }
    public double getTotal() { return total; }
    public void setPaye(double paye) { this.paye = paye; }
    public double getPaye() { return paye; }
    public void setReste(double reste) { this.reste = reste; }
    public double getReste() { return reste; }
    public void setEtat(String etat) { this.etat = etat; }
    public String getEtat() { return etat; }
}

