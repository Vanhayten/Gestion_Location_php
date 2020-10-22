package com.example.gestionlocationnew;

public class list_feedback {

    private String matricul;
    private String date_db;
    private String date_fin;
    private String montant;

    public String getMatricul() {
        return matricul;
    }

    public void setMatricul(String matricul) {
        this.matricul = matricul;
    }

    public String getDate_db() {
        return date_db;
    }

    public void setDate_db(String date_db) {
        this.date_db = date_db;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public list_feedback(String matricul, String date_db, String date_fin, String montant) {
        this.matricul = matricul;
        this.date_db = date_db;
        this.date_fin = date_fin;
        this.montant = montant;
    }
}
