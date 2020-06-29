package com.example.gestionlocationnew;

public class list_charge {
    private String IDCharge;
    private String designation;
    private String montant;
    private  String mdpayer;
    private  String date;

    public String getIDCharge() {
        return IDCharge;
    }

    public void setIDCharge(String IDCharge) {
        this.IDCharge = IDCharge;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getMdpayer() {
        return mdpayer;
    }

    public void setMdpayer(String mdpayer) {
        this.mdpayer = mdpayer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public list_charge(String IDCharge ,String designation, String montant, String mdpayer, String date) {
        this.designation = designation;
        this.montant = montant;
        this.mdpayer = mdpayer;
        this.date = date;
        this.IDCharge = IDCharge;
    }
}
