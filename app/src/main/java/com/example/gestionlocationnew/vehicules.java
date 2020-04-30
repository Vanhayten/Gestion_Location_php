package com.example.gestionlocationnew;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class vehicules extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String Nom,Prenom,role;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ListView ls;
    ArrayList<list_vihcule> arrayList;
    PageAdapter_vihucle listrep;
    gestion_location db;
    TextView matr;
    Dialog myDyalog;
    Dialog AjouteDialog;
    EditText t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db= new gestion_location(this);

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
        ls=(ListView)findViewById(R.id.list1);
        t1=(EditText)findViewById(R.id.chercherMatr);

        // recherche par matrucule
        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<list_vihcule> arrayList1;
                SQLiteDatabase table = db.getReadableDatabase ();
                String requet = "select * from véhicules where immatriculation ='"+t1.getText()+"'";
                Cursor c = table.rawQuery ( requet, null );
                if(c.getCount()>=1){
                    ls.clearChoices();
                    arrayList1= new ArrayList<list_vihcule> ();
                    while (c.moveToNext ())
                    {
                        list_vihcule list = new list_vihcule (c.getString(0),c.getString(2),c.getString(7));
                        arrayList1.add ( list );
                    }
                    PageAdapter_vihucle adapter_vihucle = new PageAdapter_vihucle (vehicules.this,arrayList1);
                    ls.setAdapter ( adapter_vihucle );
                }else{
                    ls.setAdapter (listrep);
                }
//
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //-------------------------

                    SQLiteDatabase table = db.getReadableDatabase ();
                    String requet = "select * from véhicules ";
                    Cursor c = table.rawQuery ( requet, null );
                    if(c.getCount()==0){
                        Intent i=new Intent(this,Ajoute_vihicule.class);
                        startActivity(i);
                    }
                    arrayList = new ArrayList<list_vihcule> ();
                    arrayList.clear ();
                    while (c.moveToNext ())
                    {
                        list_vihcule list = new list_vihcule (c.getString(0),c.getString(2),c.getString(7));
                        arrayList.add ( list );
                    }
                    listrep = new PageAdapter_vihucle ( this, arrayList );
                    ls.setAdapter ( listrep );
        NavigationView navigationView1 = (NavigationView)findViewById(R.id.navigationView);
        View headerView = navigationView1.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.unser_name);
        TextView role1 = headerView.findViewById(R.id.role);

        Bundle b = getIntent().getExtras();
        Nom = b.getString("nom");
        Prenom =  b.getString("prenom");
        role = ""+b.getString("role");
        username.setText(Nom+" "+Prenom);
        role1.setText(role);

        //onclick on listner aficher le dialog
        myDyalog = new Dialog(this);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView text1,text2,text3,text4,text5,text6,text7,text8,text9;
                Button Suprimer,Modifier;

                myDyalog.setContentView(R.layout.dialog_vihicule);
                text1 = (TextView)myDyalog.findViewById(R.id.text_nom);
                text2 = (TextView)myDyalog.findViewById(R.id.text_matricule);
                text3 = (TextView)myDyalog.findViewById(R.id.text_datecirulation);
                text4 = (TextView)myDyalog.findViewById(R.id.text_marqueCombision);
                text5 = (TextView)myDyalog.findViewById(R.id.text_valeur_entrer);
                text6 = (TextView)myDyalog.findViewById(R.id.text_dateeffet);
                text7 = (TextView)myDyalog.findViewById(R.id.text_dateechance);
                text8 = (TextView)myDyalog.findViewById(R.id.text_couleur);
                text9 = (TextView)myDyalog.findViewById(R.id.text_close);
                Suprimer =(Button)myDyalog.findViewById(R.id.btn_suprimer);
                Modifier =(Button)myDyalog.findViewById(R.id.btn_modifier);


                matr =(TextView)view.findViewById(R.id.marqueV);

                //Onclose dyalog
                text9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDyalog.dismiss();
                    }
                });



                Modifier.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * code modifier
                         */
                        final EditText text1,text2,text3,text4,text5,text6,text7,text8;
                        Button Confirmer;
                        AjouteDialog= new Dialog(vehicules.this);
                        AjouteDialog.setContentView(R.layout.dialog_ajoute_vihicule);
                        text1 = (EditText)AjouteDialog.findViewById(R.id.text_nom1);
                        text2 = (EditText)AjouteDialog.findViewById(R.id.text_matricule1);
                        text3 = (EditText)AjouteDialog.findViewById(R.id.text_datecirulation1);
                        text4 = (EditText)AjouteDialog.findViewById(R.id.text_marqueCombision1);
                        text5 = (EditText)AjouteDialog.findViewById(R.id.text_valeur_entrer1);
                        text6 = (EditText)AjouteDialog.findViewById(R.id.text_dateeffet1);
                        text7 = (EditText)AjouteDialog.findViewById(R.id.text_dateechance1);
                        text8 = (EditText)AjouteDialog.findViewById(R.id.text_couleur1);
                        Confirmer =(Button)AjouteDialog.findViewById(R.id.btn_modifier1);

                        SQLiteDatabase table = db.getReadableDatabase ();
                        String requet = "select * from véhicules where immatriculation ='"+matr.getText()+"'";
                        Cursor c = table.rawQuery ( requet, null );

                        while (c.moveToNext()){
                            text1.setText(c.getString(0));
                            text2.setText(c.getString(2));
                            text3.setText(c.getString(1));
                            text4.setText(c.getString(3));
                            text5.setText(c.getString(4));
                            text6.setText(c.getString(5));
                            text7.setText(c.getString(6));
                            text8.setText(c.getString(7));
                        }

                        Confirmer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /**
                                 * confirmation suprimer
                                 *
                                 */
                                AlertDialog.Builder builder = new AlertDialog.Builder(vehicules.this);
                                builder.setCancelable(true);
                                builder.setTitle("Confirmation");
                                builder.setMessage("Vous vouller vraiment modifier");
                                builder.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                /**
                                                 * confirmer
                                                 */
                                                try {
                                                    db.modifier_vihucle(text1.getText().toString(),text3.getText().toString(),text2.getText().toString(),text4.getText().toString(),Integer.parseInt(text5.getText().toString()),text6.getText().toString(),text7.getText().toString(),text8.getText().toString());
                                                    Toast.makeText(vehicules.this, "Bien Modification", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    startActivity(getIntent());

                                                }catch (Exception Ex){
                                                    Toast.makeText(vehicules.this, "Erreur Modification", Toast.LENGTH_SHORT).show();
                                                }


                                            }
                                        });
                                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /**
                                         * not confirmer
                                         */
                                        AjouteDialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }
                        });

                        AjouteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        AjouteDialog.show();
                    }
                });


                Suprimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(vehicules.this);
                        builder.setCancelable(true);
                        builder.setTitle("suppression");
                        builder.setMessage("Vous vouller vraiment suprimer");
                        builder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.suprimer_vihucle(matr.getText().toString());
                                        finish();
                                        startActivity(getIntent());
                                        myDyalog.dismiss();
                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });

                //inisialise les donner from bas donner
                SQLiteDatabase table = db.getReadableDatabase ();
                String requet = "select * from véhicules where immatriculation ='"+matr.getText()+"'";
                Cursor c = table.rawQuery ( requet, null );

                while (c.moveToNext()){
                    text1.setText("Nom vihicule :  "+c.getString(0));
                    text2.setText("Imatriculation :  "+c.getString(2));
                    text3.setText("Date Circulation :  "+c.getString(1));
                    text4.setText("Marque Combustion :  "+c.getString(3));
                    text5.setText("Valeur Dentrée :  "+c.getString(4));
                    text6.setText("Date Effet Assurance :  "+c.getString(5));
                    text7.setText("Date Echeance :  "+c.getString(6));
                    text8.setText("Couleur Vehicule :  "+c.getString(7));
                }



                myDyalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDyalog.show();




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
                startActivity(T);
                break;
            case R.id.assurances:
                T = new Intent(this, assurances.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                startActivity(T);
                break;
            case R.id.entretiens:
                T = new Intent(this, entretiens.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                startActivity(T);
                break;

            case R.id.recette:
                T = new Intent(this, mes_recettes.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                startActivity(T);
                break;
            case R.id.charges:
                T = new Intent(this, mes_charges.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                startActivity(T);
                break;
            case R.id.calendrier:
                T = new Intent(this, calendrier.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                startActivity(T);
                break;
            case R.id.clients:
                T = new Intent(this, mes_clients.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                startActivity(T);
                break;

        }
        return false;
    }

    public void Page_Ajoute(View view) {
        Intent Ajouter = new Intent(this,Ajoute_vihicule.class);
        startActivity(Ajouter);
    }
}
