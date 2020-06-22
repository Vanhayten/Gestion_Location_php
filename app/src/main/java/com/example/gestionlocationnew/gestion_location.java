package com.example.gestionlocationnew;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class gestion_location extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="Location.db";

    //declaration table Identifie
    private static final String Table1 = "Identifie";
    private static final String Col_login = "Login";
    private static final String Col_Mdp = "Mdp";
    private static final String Col_Nom = "Nom";
    private static final String Col_Prenom = "Prenom";
    private static final String Col_Role = "Role";

    //declaration table Client
    private static final String Table2 = "Clients";
    private static final String Col_nom = "nom";
    private static final String Col_prenom = "prenom";
    private static final String Col_adresse = "adresse";
    private static final String Col_cin = "cin";
    private static final String Col_tel = "tel";
    private static final String Col_activité = "activité";
    private static final String Col_date_début  = "date_début ";
    private static final String Col_date_fin  = "date_fin ";
    private static final String Col_nbJour = "Nombre_jour";
    private static final String Col_prix_location = "prix";
    private static final String Col_Type_Payement = "Type_Payement";
    private static final String Col_vehicule = "vehicule";



    //declaration table véhicules
    private static final String Table3 = "véhicules";
    private static final String Col_Marque_Vihicule = "MarqueVihicule";
    private static final String Col_DateCirculation = "DateCirculation";
    private static final String Col_immatriculation = "immatriculation";
    private static final String Col_MarqueCombustion = "MarqueCombustion";
    private static final String Col_ValeurDentrée  = "ValeurDentrée";
    private static final String Col_Date_Effet_Assurance = "Date_Effet_Assurance";
    private static final String Col_Date_Echeance = "Date_Echeance";
    private static final String Col_Couleur_Vehicule = "Couleur_Vehicule";

    //declarationtable recette
    private static final String Table4 = "Recette";
    private static final String Col_IdRecette= "Id_Recette";
    private static final String Col_NbJour= "Nb_jour";
    private static final String Col_prix= "Prix";
    private static final String Col_DateEncaissement= "Date_Encaissement";
    private static final String Col_MoyenPayment= "Moyen_payment";
    private static final String Col_immatriculationR = "immatriculation";

    //declaration table charge
    private static final String Table5 = "Charge";
    private static final String Col_IdCharge= "Id_Charge";
    private static final String Col_Date= "Date";
    private static final String Col_Montant= "Montant";
    private static final String Col_Payment= "Payment";
    private static final String Col_designation= "designation";

    //declaration table Evenment
    private static final String Table6 = "Evenement";
    private static final String Col_IdEvenement= "Id_Evenement";
    private static final String Col_DateDebut= "Date_Debut";
    private static final String Col_DateFin= "Date_Fin";
    private static final String Col_Rappel= "Rappel";
    private static final String Col_Nature= "Nature";
    private static final String Col_Responsable= "Responsable";

    //declaration table reparation
    private static final String Table7 = "reparation";
    private static final String Col_imatriculation_reparation = "imatriculation";
    private static final String Col_pieces_changes = "Colpieces_changes";
    private static final String Col_main_douvre = "main_douvre";
    private static final String Col_ref_facture = "ref_facture";
    private static final String Col_date_reparation= "date_reparation";
    private static final String Col_montant_reparation = "montant_reparation";

    //declaration table assurance
    private static final String Table8 = "assurance";
    private static final String Col_imatriculation_asurance  = "imatriculation_asurance";
    private static final String Col_date_debut_assurance  = "date_debut";
    private static final String Col_date_fin_assurance  = "date_fin";
    private static final String Col_compagnie_assurance = "compagnie_assurance";
    private static final String Col_prime_assurance = "prime_assurance";

    //declaration table sinistre
    private static final String Table9 = "sinistre";
    private static final String Col_id_sinistre  = "id_sinistre";
    private static final String Col_imatriculation_sinistre  = "imatriculation_sinistre";
    private static final String Col_date_sinistre = "date_sinistre";
    private static final String Col_ganre_daccident  = "ganre_daccident";
    private static final String Col_montant_reparation_sinistre = "montant_reparation";
    private static final String Col_responsabilite = "responsabilite";
    private static final String Col_MONTANT_PRIS_EN_CHARGE = "MONTANT_PRIS_EN_CHARGE";

    //declaration table vidange
    private static final String Table10 = "vidange";
    private static final String Col_imatriculation_vidange  = "imatriculation_vidange";
    private static final String Col_date_vidange = "date_vidange";
    private static final String Col_kilomaitrage  = "kilomaitrage";
    private static final String Col_filtre = "filtre";
    private static final String Col_type_vidage = "type_vidage";

    //declaration table visite_technique
    private static final String Table11 = "visite_technique";
    private static final String Col_imatriculation_visite  = "imatriculation_visite";
    private static final String Col_date_visite = "date_visite";
    private static final String Col_date_prch_visite  = "prch_visite";

    //declaration table Vehucile_chosi
    private static final String Table12 = "vehicule_choisi";
    private static final String Col_id  = "id";
    private static final String Col_idRecette = "id_Recette";
    private static final String Col_matricule  = "Matricule";



    public gestion_location(Context context) {
        super(context, DATABASE_NAME, null, 2);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table1 = "Create table "+Table1+" ("+Col_login+" text primary key ,"+Col_Mdp+" text,"+Col_Nom+" text,"+Col_Prenom+" text,"+Col_Role+" text)";
        String table2 = "Create table "+Table2+" ("+Col_nom+" text  ,"+Col_prenom+" text,"+Col_adresse+" text,"+Col_cin+" text primary key,"+Col_tel+" text,"+Col_activité+" text,"+Col_date_début+" Date,"+Col_date_fin+" Date,"+Col_nbJour+" Integer,"+Col_prix_location+" Integer,"+Col_Type_Payement+" text,"+Col_vehicule+" text)";
        String table3 = "Create table "+Table3+" ("+Col_Marque_Vihicule+" text ,"+Col_DateCirculation+" date ,"+Col_immatriculation+" text primary key,"+Col_MarqueCombustion+" text,"+Col_ValeurDentrée+" Integer,"+Col_Date_Effet_Assurance+" date,"+Col_Date_Echeance+" date,"+Col_Couleur_Vehicule+" text)";
        String table4 = "Create table "+Table4+" ("+Col_IdRecette+" text primary key ,"+Col_NbJour+" integer,"+Col_prix+" integer,"+Col_DateEncaissement+" date,"+Col_MoyenPayment+" integer,"+Col_immatriculationR+" text)";
        String table5 = "Create table "+Table5+" ("+Col_IdCharge+" Integer PRIMARY KEY AUTOINCREMENT,"+Col_Date+" date,"+Col_Montant+" integer,"+Col_Payment+" text,"+Col_designation+" text)";
        String table6 = "Create table "+Table6+" ("+Col_IdEvenement+" text primary key,"+Col_DateDebut+" date ,"+Col_DateFin+" date,"+Col_Rappel+" text,"+Col_Nature+" text,"+Col_Responsable+" text)";
        String table7 = "Create table "+Table7+" ("+Col_imatriculation_reparation+" text,"+Col_pieces_changes+" text,"+Col_main_douvre+" TEXT,"+Col_ref_facture+" TEXT,"+Col_date_reparation+"  DATE,"+Col_montant_reparation+" integer)";
        String table8 = "Create table "+Table8+" ("+Col_imatriculation_asurance+" text,"+Col_date_debut_assurance+" date,"+Col_date_fin_assurance+" TEXT,"+Col_compagnie_assurance+" text,"+Col_prime_assurance+" integer)";
        String table9 = "Create table "+Table9+" ("+Col_id_sinistre+" Integer PRIMARY KEY AUTOINCREMENT , "+Col_imatriculation_sinistre+" text,"+Col_date_sinistre+" date,"+Col_ganre_daccident+" TEXT,"+Col_montant_reparation_sinistre+" Integer,"+Col_responsabilite+" Text,"+Col_MONTANT_PRIS_EN_CHARGE+" Integer)";
        String table10 = "Create table "+Table10+" ("+Col_imatriculation_vidange+" text , "+Col_date_vidange+" date , "+Col_kilomaitrage+" integer , "+Col_filtre+" TEXT , "+Col_type_vidage+" Integer)";
        String table11 = "Create table "+Table11+" ("+Col_imatriculation_visite+" text , "+Col_date_visite+" date , "+Col_date_prch_visite+" date)";
        String table12 = "Create table "+Table12+" ("+Col_id+"Integer PRIMARY KEY AUTOINCREMENT , "+Col_idRecette+" Text , "+Col_matricule+" Text)";



        db.execSQL(table1);
        db.execSQL(table2);
        db.execSQL(table3);
        db.execSQL(table4);
        db.execSQL(table5);
        db.execSQL(table6);
        db.execSQL(table7);
        db.execSQL(table8);
        db.execSQL(table9);
        db.execSQL(table10);
        db.execSQL(table11);
        db.execSQL(table12);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Table1);
        db.execSQL("DROP TABLE IF EXISTS "+Table2);
        db.execSQL("DROP TABLE IF EXISTS "+Table3);
        db.execSQL("DROP TABLE IF EXISTS "+Table4);
        db.execSQL("DROP TABLE IF EXISTS "+Table5);
        db.execSQL("DROP TABLE IF EXISTS "+Table6);
        db.execSQL("DROP TABLE IF EXISTS "+Table7);
        db.execSQL("DROP TABLE IF EXISTS "+Table8);
        db.execSQL("DROP TABLE IF EXISTS "+Table9);
        db.execSQL("DROP TABLE IF EXISTS "+Table10);
        db.execSQL("DROP TABLE IF EXISTS "+Table11);
        db.execSQL("DROP TABLE IF EXISTS "+Table12);
        onCreate(db);
    }
    public boolean insert_emp(String Login ,String Pass,String nom,String prenom,String role){
        boolean res=false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues tab_ch = new ContentValues();
        tab_ch.put(Col_login,Login);
        tab_ch.put(Col_Mdp,Pass);
        tab_ch.put(Col_Nom,nom);
        tab_ch.put(Col_Prenom,prenom);
        tab_ch.put(Col_Role,role);
        long result = db.insert(Table1,null,tab_ch);
        if(result == -1){
            res=false;
        }
        else{
            res=true;
        }
        return res;
    }
//insert client
public boolean insert_client(String nom , String prenom, String adr, String cin, String tel, String activi, String d1, String d2, int nb, int prix, String type, String vehiculeChosi){
    boolean res=false;
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues tab_ch = new ContentValues();
    tab_ch.put(Col_nom,nom);
    tab_ch.put(Col_prenom,prenom);
    tab_ch.put(Col_adresse,adr);
    tab_ch.put(Col_cin,cin);
    tab_ch.put(Col_tel,tel);
    tab_ch.put(Col_activité,activi);
    tab_ch.put(Col_date_début,d1);
    tab_ch.put(Col_date_fin,d2);
    tab_ch.put(Col_nbJour,nb);
    tab_ch.put(Col_prix_location,prix);
    tab_ch.put(Col_Type_Payement,type);
    tab_ch.put(Col_vehicule,vehiculeChosi);
    long result = db.insert(Table2,null,tab_ch);
    if(result == -1){
        res=false;
    }
    else{
        res=true;
    }
    return res;
}

    //insert vidange
    public boolean insert_vidange(String matricule ,String Date,Integer km,String filter,Integer type){
        boolean res=false;
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues tab_ch = new ContentValues();
        tab_ch.put(Col_imatriculation_vidange,matricule);
        tab_ch.put(Col_date_vidange,Date);
        tab_ch.put(Col_kilomaitrage,km);
        tab_ch.put(Col_filtre,filter);
        tab_ch.put(Col_type_vidage,type);
        long result = db.insert(Table10,null,tab_ch);
        if(result == -1){
            res=false;
        }
        else{
            res=true;
        }
        return res;
    }


    //methode ajouter vehucle
        public boolean insert_vehiucle(String Nom,String date , String imatricul, String marque, Integer valeur, String Date_Effet, String Date_echance, String couleur){
        boolean res=false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues tab_ch1 = new ContentValues();
        tab_ch1.put(Col_Marque_Vihicule,Nom);
        tab_ch1.put(Col_DateCirculation,date);
        tab_ch1.put(Col_immatriculation,imatricul);
        tab_ch1.put(Col_MarqueCombustion,marque);
        tab_ch1.put(Col_ValeurDentrée,valeur);
        tab_ch1.put(Col_Date_Effet_Assurance,Date_Effet);
        tab_ch1.put(Col_Date_Echeance,Date_echance);
        tab_ch1.put(Col_Couleur_Vehicule,couleur);
        long result = db.insert(Table3,null,tab_ch1);
        if(result == -1){
            res=false;
        }
        else{
            res=true;
        }
        return res;
    }
//insert charge

    public boolean insert_charge(String date ,Integer montant,String paym,String designation){
        boolean res=false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues tab_ch = new ContentValues();
        tab_ch.put(Col_Date,date);
        tab_ch.put(Col_Montant,montant);
        tab_ch.put(Col_Payment,paym);
        tab_ch.put(Col_designation,designation);

        long result = db.insert(Table5,null,tab_ch);
        if(result == -1){
            res=false;
        }
        else{
            res=true;
        }
        return res;
    }






    //remlisage de sinistre

    public boolean insert_sinistre(String matr , String date, String ganre, Integer montant, String respo,Integer montant1){
        boolean res=false;
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues tab_ch = new ContentValues();
        tab_ch.put(Col_imatriculation_sinistre,matr);
        tab_ch.put(Col_date_sinistre,date);
        tab_ch.put(Col_ganre_daccident,ganre);
        tab_ch.put(Col_montant_reparation_sinistre,montant);
        tab_ch.put(Col_responsabilite,respo);
        tab_ch.put(Col_MONTANT_PRIS_EN_CHARGE,montant1);
        long result = db.insert(Table9,null,tab_ch);
        if(result == -1){
            res=false;
        }
        else{
            res=true;
        }
        return res;
    }

    public Cursor Get_all_sinistre(String st1){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from "+Table9+" where "+Col_imatriculation_sinistre+" ='"+st1+"'",null);
        return c;
    }


    public Cursor Get_connection(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from "+Table1,null);
        return c;
    }

//methode modifier vihicule


    public void modifier_vihucle(String Nom,String date , String imatricul, String marque, Integer valeur, String Date_Effet, String Date_echance, String couleur){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(Col_Marque_Vihicule,Nom);
        v.put(Col_DateCirculation,date);
        v.put(Col_MarqueCombustion,marque);
        v.put(Col_ValeurDentrée,valeur);
        v.put(Col_Date_Effet_Assurance,Date_Effet);
        v.put(Col_Date_Echeance,Date_echance);
        v.put(Col_Couleur_Vehicule,couleur);
        db.update(Table3,v,""+Col_immatriculation+"=?",new String[]{imatricul});
    }

    //methode aficher les vihicule
    public Cursor Get_attvihucle(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from "+Table3,null);
        return c;
    }

    public Integer suprimer_vihucle(String lb){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Table3,""+Col_immatriculation+"=?",new String[]{lb});
    }
    // insert assurance
    public boolean insert_assurance( String mat, String dat1, String dat2, String comp, Integer prime){
        boolean res=false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues tab_ch1 = new ContentValues();
        tab_ch1.put(Col_imatriculation_asurance,mat);
        tab_ch1.put(Col_date_debut_assurance,dat1);
        tab_ch1.put(Col_date_fin_assurance,dat2);
        tab_ch1.put(Col_compagnie_assurance,comp);
        tab_ch1.put(Col_prime_assurance,prime);
        long result = db.insert(Table8,null,tab_ch1);
        if(result == -1){
            res=false;
        }
        else{
            res=true;
        }
        return res;
    }


    // insert reparation
    public boolean insert_reparation( String imma, String Pc , String Md, String rf,String Da, Integer Mont){
        boolean res=false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues tab_ch1 = new ContentValues();
        tab_ch1.put(Col_imatriculation_reparation,imma);
        tab_ch1.put(Col_pieces_changes,Pc);
        tab_ch1.put(Col_main_douvre,Md);
        tab_ch1.put(Col_ref_facture,rf);
        tab_ch1.put(Col_date_reparation,Da);
        tab_ch1.put(Col_montant_reparation,Mont);
        long result = db.insert(Table7,null,tab_ch1);
        if(result == -1){
            res=false;
        }
        else{
            res=true;
        }
        return res;
    }


}
