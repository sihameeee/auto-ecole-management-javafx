package com.example.demo;

public class Moniteur {

        private int id;
        private String nom;
        private String prenom;
        private String username;
        private String password;
        private String role;
        private String disponibilite;

        public Moniteur(int id, String nom, String prenom, String username, String password, String role, String disponibilite) {
            this.id = id;
            this.nom = nom;
            this.prenom = prenom;
            this.username = username;
            this.password = password;
            this.role = role;
            this.disponibilite = disponibilite;
        }

        public void setId(int id) { this.id = id; }
        public int getId() { return id; }
        public void setNom(String nom) { this.nom = nom; }
        public String getNom() { return nom; }
        public void setPrenom(String prenom) { this.prenom = prenom; }
        public String getPrenom() { return prenom; }
        public void setUsername(String username) { this.username = username; }
        public String getUsername() { return username; }
        public void setPassword(String password) { this.password = password; }
        public String getPassword() { return password; }
        public void setRole(String role) { this.role = role; }
        public String getRole() { return role; }
        public void setDisponibilite(String disponibilite) { this.disponibilite = disponibilite; }
        public String getDisponibilite() { return disponibilite; }
    }


