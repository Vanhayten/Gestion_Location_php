package com.example.gestionlocationnew;

public class list_recette {
    private String Id;
    private int Prix;
    private String wehi;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getPrix() {
        return Prix;
    }

    public void setPrix(int prix) {
        Prix = prix;
    }

    public String getWehi() {
        return wehi;
    }

    public void setWehi(String wehi) {
        this.wehi = wehi;
    }

    public list_recette(String id, int prix, String wehi) {
        Id = id;
        Prix = prix;
        this.wehi = wehi;
    }
}
