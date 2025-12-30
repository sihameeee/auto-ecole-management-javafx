package com.example.demo;

public class CondidatExamen {
    private int id;

    private String condidat;
    private String typePermis;
    private String typeExamen;
    private String decision;

    public CondidatExamen(int id, String condidat, String typePermis, String typeExamen, String decision) {
        this.id = id;
        this.condidat = condidat;
        this.typePermis = typePermis;
        this.typeExamen = typeExamen;
        this.decision = decision;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCondidat() { return condidat; }
    public void setCondidat(String condidat) { this.condidat = condidat; }
    public String getTypePermis() { return typePermis; }
    public void setTypePermis(String typePermis) { this.typePermis = typePermis; }
    public String getTypeExamen() { return typeExamen; }
    public void setTypeExamen(String typeExamen) { this.typeExamen = typeExamen; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

}

