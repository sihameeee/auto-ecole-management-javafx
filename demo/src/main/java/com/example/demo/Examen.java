package com.example.demo;


import java.time.LocalDate;

public class Examen {

    private int id;
    private LocalDate dateExamen;
    private String etatExamen;

    public Examen(int id, LocalDate dateExamen, String etatExamen) {
        this.id = id;
        this.dateExamen = dateExamen;
        this.etatExamen = etatExamen;

    }

    public int getId() { return id; }
    public LocalDate getDateExamen() { return dateExamen; }
    public void setDateExamen(LocalDate dateExamen) { this.dateExamen = dateExamen; }
    public String getEtatExamen() { return etatExamen; }
    public void setEtatExamen(String etatExamen) { this.etatExamen = etatExamen; }

}

