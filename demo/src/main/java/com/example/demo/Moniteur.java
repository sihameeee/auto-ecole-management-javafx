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

        public int getId() { return id; }
        public String getNom() { return nom; }
        public String getPrenom() { return prenom; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getRole() { return role; }
        public String getDisponibilite() { return disponibilite; }
    }


