package com.example.demo;

public class Vehicule {
    private Vehicule vehiculeSelectionne = null;

        private int id;
        private String marque;
        private String matricule;
        private String type;
        private String disponibilite;

        public Vehicule(int id, String marque,String matricule, String type, String disponibilite) {
            this.id = id;
            this.marque = marque;
            this.matricule = matricule;
            this.type = type;
            this.disponibilite = disponibilite;
        }
    public void setId(int id) { this.id = id; }
    public void setMarque(String marque) { this.marque = marque; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public void setType(String type) { this.type = type; }
    public void setDisponibilite(String disponibilite) { this.disponibilite = disponibilite; }
        public int getId() { return id; }
        public String getMarque() { return marque; }
        public String getMatricule() { return matricule; }
        public String getType() { return type; }
        public String getDisponibilite() { return disponibilite; }

    }


