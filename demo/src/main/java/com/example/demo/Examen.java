package com.example.demo;


public class Examen {

    private int id;
    private String dateExamen;
    private String etatExamen;

    public Examen(int id, String dateExamen, String etatExamen) {
        this.id = id;
        this.dateExamen = dateExamen;
        this.etatExamen = etatExamen;

    }

    public int getId() { return id; }
    public String getDateExamen() { return dateExamen; }
    public String getEtatExamen() { return etatExamen; }

}

