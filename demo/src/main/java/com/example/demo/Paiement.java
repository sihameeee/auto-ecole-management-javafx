package com.example.demo;


public class Paiement {

    private int id;
    private String date;
    private String condidat;
    private String permis;
    private String total;
    private String paye;
    private String reste;
    private String etat;

    public Paiement(int id, String date, String condidat, String permis, String total, String paye, String reste, String etat) {
        this.id = id;
        this.date = date;
        this.condidat = condidat;
        this.permis = permis;
        this.total = total;
        this.paye = paye;
        this.reste = reste;
        this.etat = etat;
    }

    public int getId() { return id; }
    public String getDate() { return date; }
    public String getCondidat() { return condidat; }
    public String getPermis() { return permis; }
    public String getTotal() { return total; }
    public String getPaye() { return paye; }
    public String getReste() { return reste; }
    public String getEtat() { return etat; }
}

