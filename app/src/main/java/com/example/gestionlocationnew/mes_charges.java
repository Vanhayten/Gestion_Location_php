package com.example.gestionlocationnew;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class mes_charges extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String Nom,Prenom,role;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    gestion_location db;
    TextView   datech, montant,design;
    String modpay="";
    ArrayList<list_vihcule> arrayList;
    PageAdapter_vihucle listrep;
    ListView ls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_charge);

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
        role = ""+b.getString("role");
        username.setText(Nom+" "+Prenom);
        role1.setText(role);


        db = new gestion_location(this);
        //remplissage liste des charges
       // boolean c1 = db.insert_charge("01/08/2019",15,"virment","des1");
        ls=(ListView)findViewById(R.id.listcharges);
        ArrayList<list_vihcule> arrayList1;
        SQLiteDatabase table = db.getReadableDatabase ();
        String requet = "select * from charge";
        Cursor c = table.rawQuery ( requet, null );
        if(c.getCount()>=1){
            ls.clearChoices();
            arrayList1= new ArrayList<list_vihcule> ();
            while (c.moveToNext ())
            {
                list_vihcule list = new list_vihcule (c.getString(1),c.getString(2)+" , "+c.getString(3)+" , "+c.getString(4),"");
                arrayList1.add ( list );
            }
            PageAdapter_vihucle adapter_vihucle = new PageAdapter_vihucle (mes_charges.this,arrayList1);
            ls.setAdapter ( adapter_vihucle );
        }else{
            ls.setAdapter (listrep);
        }


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

        }
        return false;
    }

    public void confirmer(View view) {
        final Dialog dyalog_mes_charges;
        dyalog_mes_charges = new Dialog(this);
        dyalog_mes_charges.setContentView(R.layout.dialoge_ajoute_mes_charges);

        CheckBox espéce,chéque,virment;
        TextView close;

        datech= (TextView)dyalog_mes_charges.findViewById(R.id.text_datecha);
        montant= (TextView)dyalog_mes_charges.findViewById(R.id.text_datecha);
        espéce= (CheckBox) dyalog_mes_charges.findViewById(R.id.mode_espéce);
        chéque= (CheckBox) dyalog_mes_charges.findViewById(R.id.mode_chèque);
       virment= (CheckBox) dyalog_mes_charges.findViewById(R.id.mode_virement);
        design= (TextView) dyalog_mes_charges.findViewById(R.id.text_design);

       if (espéce.isChecked()){
           modpay="espéce";
       }
        if (chéque.isChecked()){
            modpay=modpay+" ,chéque";
        }
        if (virment.isChecked()){
            modpay=modpay+" ,virment";
        }
        Button confirmer;
        confirmer = (Button)dyalog_mes_charges.findViewById(R.id.btn_confirmer);
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean c = db.insert_charge(datech.getText().toString(),Integer.parseInt(montant.getText().toString()),modpay,design.getText().toString());
                if (c){
                    Toast.makeText(mes_charges.this,"l'ajoute Reussi",Toast.LENGTH_LONG).show();}

                else{
                    Toast.makeText(mes_charges.this,"Erreur d'ajoute",Toast.LENGTH_LONG).show();}
            }
        });












            dyalog_mes_charges.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dyalog_mes_charges.show();


    }
}
