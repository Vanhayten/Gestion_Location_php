package com.example.gestionlocationnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class mes_location extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String Nom,Prenom,role;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    gestion_location db;
    ArrayList<list_client> arrayList;
    ArrayList<list_client> arrayList1;
    PageAdapter_client listrep;
    ListView ls;
    EditText t1;

    ArrayList<String> arrayList_choix = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter2;
    String vihicule_choix = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_location);



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




        db = new gestion_location(this);

        ls = (ListView) findViewById(R.id.listLocation);
        t1 = (EditText) findViewById(R.id.chercherLocation);

        Date c1 = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        String formattedDate = df.format(c1);

        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });


/**
 * initialisation location
 */


        arrayList = new ArrayList<list_client>();
        arrayList.clear();


        SQLiteDatabase table = db.getReadableDatabase();
        String requet = "select * from Recette";
        Cursor c = table.rawQuery(requet, null);

        Date dateNow=null;
        Date datereccet=null;
        try {
             dateNow = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        while (c.moveToNext()) {
            try {
                datereccet = df.parse(c.getString(2));
            } catch (ParseException e) {
                e.printStackTrace();
            }



            if(dateNow.compareTo(datereccet) < 0){


                SQLiteDatabase table1 = db.getReadableDatabase();
                String requetclient = "select * from Clients where cin = '"+c.getString(8)+"'";
                Cursor cclint = table1.rawQuery(requetclient, null);

                String requetvehicule = "select * from véhicules where immatriculation = '"+c.getString(7)+"'";
                Cursor cvhcl = table1.rawQuery(requetvehicule, null);



                while (cclint.moveToNext() && cvhcl.moveToNext()){

                    list_client list = new list_client(cclint.getString(0) + " " + cclint.getString(1), cvhcl.getString(0));
                    arrayList.add(list);
                }


            }

        }
        listrep = new PageAdapter_client(this, arrayList);
        ls.setAdapter(listrep);


        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                ImageView imageView = (ImageView) view.findViewById(R.id.appele_client);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TextView cin = (TextView)view.findViewById(R.id.matrV);

                        String[] fullname = cin.getText().toString().split(" ");

                        SQLiteDatabase table = db.getReadableDatabase();
                        String requet = "select tel from Clients where nom ='" +fullname[0]+ "' and prenom ='"+fullname[1]+"'";
                        Cursor c = table.rawQuery(requet, null);
                        String teephone = null;
                        if(c.moveToNext()){
                            teephone = c.getString(0);
                        }


                        final int REQUEST_PHONE_CALL = 1;
                        Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + teephone));
                        if (ActivityCompat.checkSelfPermission(mes_location.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions


                            ActivityCompat.requestPermissions(mes_location.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                            return;
                        }else {
                            startActivity(intent1);
                        }

                    }
                });

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

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);



                break;
            case R.id.assurances:
                T = new Intent(this, assurances.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);



                break;
            case R.id.entretiens:
                T = new Intent(this, entretiens.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);


                break;

            case R.id.recette:
                T = new Intent(this, mes_recettes.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);


                break;
            case R.id.charges:
                T = new Intent(this, mes_charges.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);


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

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);


                break;
            case R.id.locations:
                T = new Intent(this, mes_location.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);


                break;

        }
        return false;
    }
}