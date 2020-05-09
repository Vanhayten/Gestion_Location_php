package com.example.gestionlocationnew;

public class list_client {
   private String cin;
   private String nom;
   private String prenom;

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

   /* public list_client(String cin, String nom, String prenom) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
    }*/

    public list_client(String cin, String nom) {
        this.cin = cin;
        this.nom = nom;
    }
}
