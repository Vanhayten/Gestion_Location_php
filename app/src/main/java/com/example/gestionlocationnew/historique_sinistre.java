package com.example.gestionlocationnew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class historique_sinistre extends AppCompatActivity {
gestion_location db;
ListView ls;
TextView textView;
Bundle b;
String id_sinistre;
    private ArrayList<liste_sinistre> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_sinistre);

        Toast.makeText(this, "appuyez et maintenez pour modification ou suppression", Toast.LENGTH_LONG).show();
        
        db = new gestion_location(this);
        ls=(ListView)findViewById(R.id.list2);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_sinistre,ls,false);
        ls.addHeaderView(header);

        ArrayList<liste_sinistre> list = new ArrayList<>();
        textView=(TextView)findViewById (R.id.matricule);

        b = getIntent().getExtras();
        String matr =b.getString ( "matricule" );
        String login =b.getString ( "login" );
        textView.setText ("la liste des sinistres de matricule : "+b.getString ( "matricule" ) );
        SQLiteDatabase table = db.getReadableDatabase ();
        String requet = "select * from sinistre where imatriculation_sinistre ='"+matr+"' and login ="+login+"";
        Cursor c = table.rawQuery ( requet, null );
        while (c.moveToNext ()){
            liste_sinistre listeS= new liste_sinistre();
            listeS.setdate(c.getString(2));
            listeS.setgenre(c.getString(3));
            listeS.setmontant(c.getString(4));
            listeS.setresp(c.getString(5));
            listeS.setmontant1(c.getString(6));
            list.add(listeS);

        }

        arrayList = list;
        PageAdapter_sinistre adapter_sinistre = new PageAdapter_sinistre(this,arrayList);
        ls.setAdapter(adapter_sinistre);

       ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
               final TextView date,genre,montant,resp,montant1;
               date =(TextView)view.findViewById(R.id.date);
               genre =(TextView)view.findViewById(R.id.genre);
               montant =(TextView)view.findViewById(R.id.montant);
               resp =(TextView)view.findViewById(R.id.resp);
               montant1 =(TextView)view.findViewById(R.id.montant1);


               /**
                * dialog suprimerou modifier
                */
               final Dialog myDyalog_ajoute;
               myDyalog_ajoute = new Dialog(historique_sinistre.this);
               myDyalog_ajoute.setContentView(R.layout.dialog_historique_ajouter_sinistre);
               TextView textnom;
               textnom =(TextView)myDyalog_ajoute.findViewById(R.id.text_nom);
               textnom.setText("Modifier / Suprimer");
               Button modifier,Suprimer;
               modifier  =(Button)myDyalog_ajoute.findViewById(R.id.btn_historique);
               Suprimer =(Button)myDyalog_ajoute.findViewById(R.id.btn_Ajouter);
               modifier.setText("Modifier");
               Suprimer.setText("Suprimer");

               /**
                * onclick Suprimer
                */
               Suprimer.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       try {
                       SQLiteDatabase DB = db.getWritableDatabase();
                       DB.execSQL("DELETE FROM sinistre WHERE imatriculation_sinistre ='"+b.getString ( "matricule" )+"' and date_sinistre = '"+date.getText().toString()+"' and ganre_daccident='"+genre.getText().toString()+"' and montant_reparation='"+montant.getText().toString()+"' and responsabilite ='"+resp.getText().toString()+"' and MONTANT_PRIS_EN_CHARGE ='"+montant1.getText().toString()+"' and login ="+login+"");
                           myDyalog_ajoute.dismiss();
                           Toast.makeText(historique_sinistre.this, "Bien Suprimer", Toast.LENGTH_SHORT).show();
                           finish();
                           startActivity(getIntent());
                       }catch(Exception Ex){
                           Toast.makeText(historique_sinistre.this, "erreur Suprimetion", Toast.LENGTH_SHORT).show();
                       }

                   }
               });

               /**
                * onclick modifier
                */
               modifier.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       final Dialog myDyalog_modifier;
                       myDyalog_modifier = new Dialog(historique_sinistre.this);
                       myDyalog_modifier.setContentView(R.layout.dialog_ajoute_sinisstre);
                        Button modifier_confirme;
                         modifier_confirme =(Button)myDyalog_modifier.findViewById(R.id.btn_modifier1);

                       /**
                        *
                        * remplire les editext
                        */

                       final EditText text1,text2,text4,text6;
                       final CheckBox CH1,CH2;
                       final Spinner spinner;
                       text1 =(EditText)myDyalog_modifier.findViewById(R.id.text_matricule2);
                       text2 =(EditText)myDyalog_modifier.findViewById(R.id.text_datesinistre2);
                       CH1 = (CheckBox)myDyalog_modifier.findViewById(R.id.text_ganredaccident2);
                       CH2 = (CheckBox)myDyalog_modifier.findViewById(R.id.text_ganredaccident3);
                       text4 =(EditText)myDyalog_modifier.findViewById(R.id.text_vmontant2);
                       spinner =(Spinner)myDyalog_modifier.findViewById(R.id.text_responsabilite2);
                       text6 =(EditText)myDyalog_modifier.findViewById(R.id.text_montant2);
                       text1.setText(b.getString("matricule"));
                       text2.setText(date.getText().toString());
                       text4.setText(montant.getText().toString());
                       text6.setText(montant1.getText().toString());

                       /**
                        *
                        * remplisage list spinner
                        */
                       ArrayList<String> arrayList = new ArrayList<String>();
                       arrayList.add("0%");
                       arrayList.add("50%");
                       arrayList.add("100%");
                       ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(historique_sinistre.this,R.layout.support_simple_spinner_dropdown_item,arrayList);
                       spinner.setAdapter(arrayAdapter);

                       /**
                        * on confirme modification
                        *
                        */

                       modifier_confirme.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               /**
                                *
                                * initialize le id sinistre
                                */
                               id_sinistre ="";
                               SQLiteDatabase table = db.getReadableDatabase();
                               String matr = b.getString ( "matricule" );
                               String requet = "select * from sinistre WHERE imatriculation_sinistre = '"+matr+"' and date_sinistre = '"+date.getText().toString()+"' and ganre_daccident ='"+genre.getText().toString()+"' and montant_reparation ='"+montant.getText().toString()+"' and responsabilite ='"+resp.getText().toString()+"' and MONTANT_PRIS_EN_CHARGE ='"+montant1.getText().toString()+"' and login ="+login+"";
                               Cursor c1 = table.rawQuery ( requet, null );
                               while(c1.moveToNext()){
                                   id_sinistre = c1.getString(0);
                               }


                               /**
                                *
                                * confirme update
                                */
                               try {
                                   String ch11="";
                                   if(CH1.isChecked() && CH2.isChecked() ){
                                       ch11 ="Corporele , Material";
                                   }else if (CH1.isChecked() && !CH2.isChecked()){
                                       ch11 ="Corporele";
                                   }else if(!CH1.isChecked() && CH2.isChecked()){
                                       ch11 ="Material";
                                   }

                                   SQLiteDatabase DB = db.getWritableDatabase();
                                   ContentValues v1 = new ContentValues();
                                   v1.put("date_sinistre", text2.getText().toString());
                                   v1.put("ganre_daccident",ch11);
                                   v1.put("montant_reparation", text4.getText().toString());
                                   v1.put("responsabilite", spinner.getSelectedItem().toString());
                                   v1.put("MONTANT_PRIS_EN_CHARGE", text6.getText().toString());
                                   String id = "id_sinistre";
                                   DB.update("sinistre", v1, "" + id + "=? and login =?", new String[]{id_sinistre,login});
                                   finish();
                                   startActivity(getIntent());
                               }catch (Exception Ex){
                                   Toast.makeText(historique_sinistre.this, "Erreur de modification", Toast.LENGTH_SHORT).show();
                               }



                           }
                       });


                       myDyalog_modifier.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                       myDyalog_modifier.show();

                   }
               });




               //Onclose dyalog
               TextView textClose;
               textClose =(TextView)myDyalog_ajoute.findViewById(R.id.text_close);
               textClose.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       myDyalog_ajoute.dismiss();
                   }
               });


               myDyalog_ajoute.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               myDyalog_ajoute.show();

               return false;
           }
       });


        String matricule =b.getString ( "matriculex" );
    }
}
