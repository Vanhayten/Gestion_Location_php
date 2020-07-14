package com.example.gestionlocationnew;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class mes_recettes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String Nom,Prenom,role;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    gestion_location db;
    ListView ls;
    ArrayList<list_recette> arrayList1;
    EditText t1;
    PageAdapter_recette listeRecet;



    String Idd,cin,Matr,datedb,datefn,nbjour,prix,Typ_Payment,prix_01;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_recettes);


        db = new gestion_location(this);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //-------------------------


        NavigationView navigationView1 = (NavigationView)findViewById(R.id.navigationView);
        View headerView = navigationView1.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.unser_name);
        TextView role1 = headerView.findViewById(R.id.role);

        Bundle b = getIntent().getExtras();
        Nom = b.getString("nom");
        Prenom =  b.getString("prenom");
        role = b.getString("role");
        username.setText(Nom+" "+Prenom);
        role1.setText(role);


        /**
         * remplire reccete
         */
        ls=(ListView)findViewById(R.id.listRec);
        SQLiteDatabase table = db.getReadableDatabase();
        String requet = "SELECT * FROM  Recette";

        Cursor c = table.rawQuery ( requet, null );
        arrayList1 = new ArrayList<list_recette> ();
        arrayList1.clear ();
        while (c.moveToNext())
        {
            list_recette list = new list_recette (Integer.parseInt(c.getString(5)),c.getString(0));
            arrayList1.add (list);
        }

        listeRecet = new PageAdapter_recette ( this, arrayList1 );
        ls.setAdapter(listeRecet);




        /**
         * on click liste
         */

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Dialog MyDyalog_detaille;
                MyDyalog_detaille = new Dialog(mes_recettes.this);
                MyDyalog_detaille.setContentView(R.layout.dialog_detaille_recette);
                final TextView text1, text2, text3, text4, text5, text6,text7;

                text1 = (TextView)MyDyalog_detaille.findViewById(R.id.text_idreccet);
                text2 = (TextView)MyDyalog_detaille.findViewById(R.id.text_Cin);
                text3 = (TextView)MyDyalog_detaille.findViewById(R.id.text_MAtrc);
                text4 = (TextView)MyDyalog_detaille.findViewById(R.id.text_datedbt);
                text5 = (TextView)MyDyalog_detaille.findViewById(R.id.text_datefin);
                text6 = (TextView)MyDyalog_detaille.findViewById(R.id.text_nbJour);
                text7 = (TextView)MyDyalog_detaille.findViewById(R.id.text_prixt);

                TextView id1 = (TextView)view.findViewById(R.id.marqueV);
                String[] idreccet = id1.getText().toString().split(" ");

                try {

                    SQLiteDatabase table = db.getReadableDatabase();
                    String requet = "SELECT * FROM  Recette where Id_Recette = '"+idreccet[2]+"'";

                    Cursor c = table.rawQuery ( requet, null );
                    while (c.moveToNext()){
                        text1.setText(text1.getText()+" "+c.getString(0));
                        text2.setText(text2.getText()+" "+c.getString(8));
                        text3.setText(text3.getText()+" "+c.getString(7));
                        text4.setText(text4.getText()+" "+c.getString(1));
                        text5.setText(text5.getText()+" "+c.getString(2));
                        text6.setText(text6.getText()+" "+c.getString(3));
                        text7.setText(text7.getText()+" "+c.getString(4));

                        Idd = c.getString(0);
                        cin = c.getString(8);
                        Matr = c.getString(7);
                        datedb = c.getString(1);
                        datefn = c.getString(2);
                        nbjour = c.getString(3);
                        prix = c.getString(4);
                        Typ_Payment = c.getString(6);
                        prix_01 = c.getString(5);
                    }

                }catch (Exception ex){
                    Toast.makeText(mes_recettes.this, ""+ex, Toast.LENGTH_LONG).show();
                }


                Button modifier = (Button)MyDyalog_detaille.findViewById(R.id.btn_modifier_reccet);
                modifier.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog MyDyalog_modifier;
                        MyDyalog_modifier = new Dialog(mes_recettes.this);
                        MyDyalog_modifier.setContentView(R.layout.dialog_modifier_reccete);
                        final EditText text1, text2, text3, text4, text5, text6,text7;

                        text1 = (EditText)MyDyalog_modifier.findViewById(R.id.text_Idrc);
                        text2 = (EditText)MyDyalog_modifier.findViewById(R.id.text_cin1);
                        text3 = (EditText)MyDyalog_modifier.findViewById(R.id.text_matrr);
                        text4 = (EditText)MyDyalog_modifier.findViewById(R.id.text_datrdbb);
                        text5 = (EditText)MyDyalog_modifier.findViewById(R.id.text_datfinn);
                        text6 = (EditText)MyDyalog_modifier.findViewById(R.id.text_nbbjour);
                        text7 = (EditText)MyDyalog_modifier.findViewById(R.id.text_prixtt);

                            text1.setText(Idd);
                            text2.setText(cin);
                            text3.setText(Matr);
                            text4.setText(datedb);
                            text5.setText(datefn);
                            text6.setText(nbjour);
                            text7.setText(prix);

                        /**
                         * confirmation
                         */

                        Button Confirmer_modif;
                        Confirmer_modif = (Button)MyDyalog_modifier.findViewById(R.id.btn_modifierreccet);
                        Confirmer_modif.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Integer prix_TT = Integer.parseInt(text7.getText().toString())*Integer.parseInt(text6.getText().toString());

                                db.modifier_reccete(text1.getText().toString(),text4.getText().toString(),text5.getText().toString(),text6.getText().toString(),text7.getText().toString(),prix_TT.toString(),Typ_Payment,text3.getText().toString(),text2.getText().toString());
                                Toast.makeText(mes_recettes.this, "Modification Réussi", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());


                            }
                        });


                        MyDyalog_modifier.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        MyDyalog_modifier.show();
                    }
                });



                MyDyalog_detaille.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                MyDyalog_detaille.show();

            }
        });


        /**
         * rechairche
         */


        t1=(EditText)findViewById(R.id.chercherIdRe);
        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<list_recette> arrayList2;
                SQLiteDatabase table = db.getReadableDatabase ();

               String requet = "SELECT * FROM  Recette where Id_Recette ='"+t1.getText()+"'";
                Cursor c = table.rawQuery ( requet, null );
                if(c.getCount()>=1){
                    ls.clearChoices();
                    arrayList2= new ArrayList<list_recette> ();
                    while (c.moveToNext ())
                    {
                        list_recette list = new list_recette (Integer.parseInt(c.getString(5)),c.getString(0));
                        arrayList2.add ( list );
                    }
                    PageAdapter_recette adapter_recette = new PageAdapter_recette (mes_recettes.this,arrayList2);
                    ls.setAdapter ( adapter_recette );
                }else{
                    ls.setAdapter (listeRecet);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent T;
        Bundle b = new Bundle();
        switch (menuItem.getItemId()){
            case R.id.vihicule:
                T = new Intent(this, vehicules.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;
            case R.id.assurances:
                T = new Intent(this, assurances.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;
            case R.id.entretiens:
                T = new Intent(this, entretiens.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;
            case R.id.recette:
                T = new Intent(this, mes_recettes.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;
            case R.id.charges:
                T = new Intent(this, mes_charges.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;
            case R.id.calendrier:
                T = new Intent(this, calendrier.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;
            case R.id.clients:
                T = new Intent(this, mes_clients.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;
            case R.id.locations:
                T = new Intent(this, mes_location.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;

        }
        return false;
    }
    public void ajouter(View view) {
        final Dialog MyDyalog_ajou;
        MyDyalog_ajou = new Dialog(mes_recettes.this);
        MyDyalog_ajou.setContentView(R.layout.dialog_ajouter_client);

        final EditText text1, text2, text3, text4, text5, text6, text7, text8, text9,text13;
        final Spinner text10,text11;
        final ListView text12;
        Spinner sp ;


      sp= (Spinner) MyDyalog_ajou.findViewById(R.id.text_typeP);
        ArrayList<String> arr  = new ArrayList<String>();
        arr.add("Chéque");
        arr.add("Virement");
        arr.add("Espèce");
        arr.add("Crédit");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arr);
        sp.setAdapter(arrayAdapter);



/**
 * --------------------------------- filtrer les vehicule ---------------------------
 */


        text11 = (Spinner) MyDyalog_ajou.findViewById(R.id.text_matric);
        ArrayList<String> arrayListMatricule  = new ArrayList<String>();
            arrayListMatricule = filter_vehicule();

            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arrayListMatricule);
            text11.setAdapter(arrayAdapter1);





        text1 = (EditText) MyDyalog_ajou.findViewById(R.id.text_nom1);
        text2 = (EditText) MyDyalog_ajou.findViewById(R.id.text_prenom);
        text3 = (EditText) MyDyalog_ajou.findViewById(R.id.text_cin);
        text4 = (EditText) MyDyalog_ajou.findViewById(R.id.text_tel);
        text5 = (EditText) MyDyalog_ajou.findViewById(R.id.text_activity);
        text13 = (EditText) MyDyalog_ajou.findViewById(R.id.text_adr);
        text6 = (EditText) MyDyalog_ajou.findViewById(R.id.text_dateDebut);
        text7 = (EditText) MyDyalog_ajou.findViewById(R.id.text_dateFin);
        text8 = (EditText) MyDyalog_ajou.findViewById(R.id.text_NombreJour);
        text9 = (EditText) MyDyalog_ajou.findViewById(R.id.text_Prix);


        //Importier les information de Client par Cin
        text3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SQLiteDatabase table = db.getReadableDatabase ();
                String requet = "select * from Clients where cin ='"+text3.getText()+"'";
                Cursor c = table.rawQuery ( requet, null );

                if(c.moveToFirst()){
                    if(c.getString(3).equals(text3.getText().toString())){
                        text1.setText(c.getString(0));
                        text2.setText(c.getString(1));
                        text4.setText(c.getString(4));
                        text5.setText(c.getString(5));
                        text13.setText(c.getString(2));
                    }
                }
            }
        });

        Button btn_ajoute;
        btn_ajoute = (Button) MyDyalog_ajou.findViewById(R.id.btn_ajouterClient);
        btn_ajoute.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                int count=0;
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date(System.currentTimeMillis());
                String[] parts = dateFormat.format(date).split("/");
                String part1 = parts[0];
                String part2 = parts[1];
                String part3 = parts[2];
                SQLiteDatabase table = db.getReadableDatabase();
                String requet = "SELECT Id_Recette FROM  Recette";
                Cursor c = table.rawQuery ( requet, null );
                if(c.moveToLast()){
                    String[] parts1 = c.getString(0).split("-");
                    String part11 = parts1[0];
                    String part22 = parts1[1];
                    count = Integer.parseInt(part22);
                }
                count = count+1;
                String s =part3+""+part2+""+part1+"-"+count;

                SQLiteDatabase table1 = db.getReadableDatabase ();
                String requet1 = "select * from Clients where cin ='"+text3.getText()+"'";
                Cursor c1 = table1.rawQuery ( requet1, null );
                boolean b = false;
                boolean d = false;
                int total = Integer.parseInt(text8.getText().toString()) * Integer.parseInt(text9.getText().toString());
                if (c1.getCount()==0){
                    b = db.insert_client(text1.getText().toString().toUpperCase(), text2.getText().toString().toUpperCase(), text13.getText().toString(), text3.getText().toString().toUpperCase(), text4.getText().toString(), text5.getText().toString());
                    d= db.insert_Recette(s,text6.getText().toString(),text7.getText().toString(),Integer.parseInt(text8.getText().toString()),Integer.parseInt(text9.getText().toString()),total,sp.getSelectedItem().toString(),text11.getSelectedItem().toString(),text3.getText().toString());
                    if (b && d ) {
                        Toast.makeText(mes_recettes.this, "l'enregistrement effecuter", Toast.LENGTH_SHORT).show();
                    }
                }
                // tester if lclient existe        >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                if(c1.moveToFirst()) {

                    if(c1.getString(3).equals(text3.getText().toString())) {
                          d= db.insert_Recette(s,text6.getText().toString(),text7.getText().toString(),Integer.parseInt(text8.getText().toString()),Integer.parseInt(text9.getText().toString()),total,sp.getSelectedItem().toString(),text11.getSelectedItem().toString(),text3.getText().toString());


                        if (d) {
                            Toast.makeText(mes_recettes.this, "l'enregistrement effecuter", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mes_recettes.this, "l'enregistrement ne pas effecuter", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(!c1.getString(3).equals(text3.getText().toString())) {
                   b = db.insert_client(text1.getText().toString(), text2.getText().toString(), text13.getText().toString(), text3.getText().toString(), text4.getText().toString(), text5.getText().toString());
                   d= db.insert_Recette(s,text6.getText().toString(),text7.getText().toString(),Integer.parseInt(text8.getText().toString()),Integer.parseInt(text9.getText().toString()),total,sp.getSelectedItem().toString(),text11.getSelectedItem().toString(),text3.getText().toString());
                    }
                    if(b && d ){
                        Toast.makeText(mes_recettes.this, "l'enregistrement effecuter", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mes_recettes.this, "l'enregistrement ne pas effecuter", Toast.LENGTH_SHORT).show();
                    }
                }


                MyDyalog_ajou.dismiss();

                finish();
                startActivity(getIntent());

            }
        });
        MyDyalog_ajou.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MyDyalog_ajou.show();
    }


    public ArrayList filter_vehicule(){

        ArrayList<String> arrayListfilter  = new ArrayList<String>();
        ArrayList<String> arrayListfinal  = new ArrayList<String>();


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        String requet_reccete ="SELECT Matricule_choisi FROM Recette where date_fin > '"+formattedDate+"'";
        String requet_vehicule ="SELECT immatriculation FROM véhicules";

       SQLiteDatabase table = db.getReadableDatabase ();
       Cursor vehicule = table.rawQuery ( requet_vehicule, null );
       Cursor  reccete = table.rawQuery ( requet_reccete, null );

       while (vehicule.moveToNext()){
           arrayListfinal.add(vehicule.getString(0));
       }
        while (reccete.moveToNext()){
            arrayListfilter.add(reccete.getString(0));
        }
        arrayListfinal.removeAll(arrayListfilter);

        if(arrayListfinal.size() == 0){
            Toast.makeText(this, "Toutes les vehicule sont réservées", Toast.LENGTH_SHORT).show();
        }

        return arrayListfinal;
    }

}
