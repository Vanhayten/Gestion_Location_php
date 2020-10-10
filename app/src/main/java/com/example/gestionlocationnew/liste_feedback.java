package com.example.gestionlocationnew;

public class liste_feedback {
    private Float rtin;
    private String Date;
    private String Des;

    public Float getRtin() {
        return rtin;
    }

    public void setRtin(Float rtin) {
        this.rtin = rtin;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }

    public liste_feedback(Float rtin, String date, String des) {
        this.rtin = rtin;
        Date = date;
        Des = des;
    }
}
