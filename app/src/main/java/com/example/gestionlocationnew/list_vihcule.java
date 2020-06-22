package com.example.gestionlocationnew;

public class list_vihcule {
    private int color;
    private String matr;
    private  String marque;

    public int getColor() {
        return color;
    }

    public void setColor(int Color) {
        this.color = Color;
    }

    public String getMatr() {
        return matr;
    }

    public void setMatr(String matr) {
        this.matr = matr;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

   /* public list_vihcule(String matr, String marque) {
        this.matr = matr;
        this.marque = marque;
    }*/

    public list_vihcule( String matr, String marque,int color) {
        this.color = color;
        this.matr = matr;
        this.marque = marque;
    }

}
