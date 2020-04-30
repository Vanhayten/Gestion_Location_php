package com.example.gestionlocationnew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
        db = new gestion_location(this);
        ls=(ListView)findViewById(R.id.list2);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_sinistre,ls,false);
        ls.addHeaderView(header);

        ArrayList<liste_sinistre> list = new ArrayList<>();
        textView=(TextView)findViewById (R.id.matricule);

        b = getIntent().getExtras();
        String matr =b.getString ( "matricule" );
        textView.setText ("la liste des sinistres de matricule : "+b.getString ( "matricule" ) );
        SQLiteDatabase table = db.getReadableDatabase ();
        String requet = "select * from sinistre where imatriculation_sinistre ='"+matr+"'";
        Cursor c = table.rawQuery ( requet, null );
        if(c.getCount()==0){
            Toast.makeText(this,"Aucun Sinistre pour cette Vihucle",Toast.LENGTH_SHORT).show();

        }
        while (c.moveToNext ()){
            liste_sinistre listeS= new liste_sinistre();
            listeS.setdate(c.getString(1));
            listeS.setgenre(c.getString(2));
            listeS.setmontant(c.getString(3));
            listeS.setresp(c.getString(4));
            listeS.setmontant1(c.getString(5));
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
                       DB.execSQL("DELETE FROM sinistre WHERE imatriculation_sinistre ='"+b.getString ( "matricule" )+"' and date_sinistre = '"+date.getText().toString()+"' and ganre_daccident='"+genre.getText().toString()+"' and montant_reparation='"+montant.getText().toString()+"' and responsabilite ='"+resp.getText().toString()+"' and MONTANT_PRIS_EN_CHARGE ='"+montant1.getText().toString()+"'");
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
                               String requet = "select * from sinistre WHERE imatriculation_sinistre = '"+matr+"' and date_sinistre = '"+date.getText().toString()+"' and ganre_daccident ='"+genre.getText().toString()+"' and montant_reparation ='"+montant.getText().toString()+"' and responsabilite ='"+resp.getText().toString()+"' and MONTANT_PRIS_EN_CHARGE ='"+montant1.getText().toString()+"'";
                               Cursor c1 = table.rawQuery ( requet, null );
                               if(c1.getCount() == 0){
                                   Toast.makeText(historique_sinistre.this, "null"+date.getText().toString(), Toast.LENGTH_SHORT).show();
                               }
                               while(c1.moveToNext()){
                                   Toast.makeText(historique_sinistre.this, c1.getString(0), Toast.LENGTH_SHORT).show();
                               }





                                /*
                              EditText text1,text2,text3,text4,text5,text6;
                              text1 =(EditText)myDyalog_modifier.findViewById(R.id.text_matricule2);
                              text2 =(EditText)myDyalog_modifier.findViewById(R.id.text_datesinistre2);
                              text3 =(EditText)myDyalog_modifier.findViewById(R.id.text_ganredaccident2);
                              text4 =(EditText)myDyalog_modifier.findViewById(R.id.text_vmontant2);
                              text5 =(EditText)myDyalog_modifier.findViewById(R.id.text_responsabilite2);
                              text6 =(EditText)myDyalog_modifier.findViewById(R.id.text_montant2);
                              text1.setText(b.getString("matricule"));

                                 */




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
