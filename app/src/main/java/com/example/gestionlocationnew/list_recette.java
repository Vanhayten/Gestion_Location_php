package com.example.gestionlocationnew;

public class list_recette {
    private String Id;
    private int Prix;

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


    public list_recette( int prix,String id) {
        Id = id;
        Prix = prix;
    }
}
