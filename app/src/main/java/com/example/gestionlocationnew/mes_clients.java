package com.example.gestionlocationnew;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class mes_clients extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String Nom, Prenom, role;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ListView ls;
    ArrayList<list_client> arrayList;
    PageAdapter_client listrep;
    gestion_location db;
    TextView Cin;
    EditText t1;
    String cinlist;

    /**
     * list choix vehicule
     */

    //Page_Adapter_choix_matr page_adapter_choix_matr;
    ArrayList<String> arrayList_choix = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter2;
    String vihicule_choix = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_clients);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //-------------------------------------------client

        //-------------------------


        NavigationView navigationView1 = (NavigationView) findViewById(R.id.navigationView);
        View headerView = navigationView1.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.unser_name);
        TextView role1 = headerView.findViewById(R.id.role);

        Bundle b = getIntent().getExtras();
        Nom = b.getString("nom");
        Prenom = b.getString("prenom");
        role = "" + b.getString("role");
        username.setText(Nom + " " + Prenom);
        role1.setText(role);
        //charge liste view par les clients


        db = new gestion_location(this);
        //  boolean h=db.insert_client("hadini","mohamed","fes","cn33820","06514665","etudiant");


        ls = (ListView) findViewById(R.id.listClient);
        t1 = (EditText) findViewById(R.id.chercherCharge);
        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<list_client> arrayList1;
                SQLiteDatabase table = db.getReadableDatabase();
                String requet = "select * from Clients where nom ='" + t1.getText().toString().toUpperCase() + "' or prenom ='" + t1.getText().toString().toUpperCase() + "'";
                Cursor c = table.rawQuery(requet, null);
                if (c.getCount() >= 1) {
                    ls.clearChoices();
                    arrayList1 = new ArrayList<list_client>();
                    while (c.moveToNext()) {
                        list_client list = new list_client(c.getString(0) + " " + c.getString(1), c.getString(3));
                        arrayList1.add(list);
                    }
                    PageAdapter_client adapter_vihucle = new PageAdapter_client(mes_clients.this, arrayList1);
                    ls.setAdapter(adapter_vihucle);
                } else {
                    ls.setAdapter(listrep);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        SQLiteDatabase table = db.getReadableDatabase();
        String requet = "select * from Clients ";
        Cursor c = table.rawQuery(requet, null);
        if (c.getCount() == 0) {
            /*
            finish();
            Intent i=new Intent(this,Ajoute_vihicule.class);
            Bundle b1 = new Bundle();
            b1.putString("nom",Nom);
            b1.putString("prenom",Prenom);
            b1.putString("role",role);
            i.putExtras(b1);
            startActivity(i);*/
        }
        arrayList = new ArrayList<list_client>();
        arrayList.clear();
        while (c.moveToNext()) {
            list_client list = new list_client(c.getString(0) + " " + c.getString(1), c.getString(3));
            arrayList.add(list);
        }
        listrep = new PageAdapter_client(this, arrayList);
        ls.setAdapter(listrep);

/**
 * onselect from liste view
 */

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cin = (TextView) view.findViewById(R.id.marqueV);
               cinlist = Cin.getText().toString();



                ImageView imageView = (ImageView) view.findViewById(R.id.appele_client);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TextView cin = (TextView)view.findViewById(R.id.marqueV);
                        SQLiteDatabase table = db.getReadableDatabase();
                        String requet = "select tel from Clients where cin ='" + cin.getText() + "'";
                        Cursor c = table.rawQuery(requet, null);
                        String teephone = null;
                        if(c.moveToNext()){
                            teephone = c.getString(0);
                        }


                         final int REQUEST_PHONE_CALL = 1;
                        Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + teephone));
                        if (ActivityCompat.checkSelfPermission(mes_clients.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            ActivityCompat.requestPermissions(mes_clients.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                            return;
                        }else {
                            startActivity(intent1);
                        }


                    }
                });


                /**
                 * on click dialog
                 */

                LinearLayout app_layer = (LinearLayout) view.findViewById (R.id.layout_recet);
                app_layer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /**
                         * suprimer ----------
                         */

                        final Dialog MyDyalog;

                        MyDyalog = new Dialog(mes_clients.this);
                        MyDyalog.setContentView(R.layout.dialog_mise_jour_client);
                        Button modifier, suprimer;
                        TextView text1, text_nom;
                        text1 = (TextView) MyDyalog.findViewById(R.id.text_close);
                        text_nom = (TextView) MyDyalog.findViewById(R.id.text_nom);
                        text_nom.setText(Cin.getText().toString());
                        text1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyDyalog.dismiss();
                            }
                        });





                        suprimer = MyDyalog.findViewById(R.id.btn_suprimer_clients);
                        suprimer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                AlertDialog.Builder builder = new AlertDialog.Builder(mes_clients.this);
                                builder.setCancelable(true);
                                builder.setTitle("suppression");
                                builder.setMessage("voullez-vous vraiment suprimer ?");
                                builder.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                                int i = db.suprimer_client(text_nom.getText().toString());
                                                finish();
                                                startActivity(getIntent());
                                                MyDyalog.dismiss();
                                            }
                                        });
                                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //  Toast.makeText(mes_clients.this, "close", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }
                        });

                        modifier = MyDyalog.findViewById(R.id.btn_assurance);
                        modifier.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Dialog ModifierDialog;
                                ModifierDialog= new Dialog(mes_clients.this);
                                ModifierDialog.setContentView(R.layout.dialog_modifier_client);

                                EditText cinndialog,nomdialog,prenomdialog,adressdialog,nemerodialog,activiterdialog;

                               cinndialog = (EditText)ModifierDialog.findViewById(R.id.text_Cinnn);
                               nomdialog = (EditText)ModifierDialog.findViewById(R.id.text_nomclient);
                               prenomdialog = (EditText)ModifierDialog.findViewById(R.id.text_prenomclient);
                               adressdialog = (EditText)ModifierDialog.findViewById(R.id.text_Adress);
                               nemerodialog = (EditText)ModifierDialog.findViewById(R.id.text_num_tele);
                               activiterdialog = (EditText)ModifierDialog.findViewById(R.id.text_activ);


                               SQLiteDatabase table = db.getReadableDatabase();
                               String requette = "select * from Clients where cin ='"+cinlist+"'";
                               Cursor c = table.rawQuery(requette,null);

                               while (c.moveToNext()){

                                   cinndialog.setText(c.getString(3));
                                   nomdialog.setText(c.getString(0));
                                   prenomdialog.setText(c.getString(1));
                                   adressdialog.setText(c.getString(2));
                                   nemerodialog.setText(c.getString(4));
                                   activiterdialog.setText(c.getString(5));

                               }



                               Button confirmer = (Button)ModifierDialog.findViewById(R.id.btn_modifier1);
                                confirmer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(mes_clients.this);
                                        builder.setCancelable(true);
                                        builder.setTitle("Confirmation");
                                        builder.setMessage("Voulez-vous vraiment modifier ?");
                                        builder.setPositiveButton("Ok",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        /**
                                                         * confirmer
                                                         */

                                                        try {
                                                            db.modifier_clients(cinndialog.getText().toString(),nomdialog.getText().toString(),prenomdialog.getText().toString(),nemerodialog.getText().toString(),adressdialog.getText().toString(),activiterdialog.getText().toString());
                                                            Toast.makeText(mes_clients.this, "Modification Réussi", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                            startActivity(getIntent());

                                                        }catch (Exception Ex){
                                                            Toast.makeText(mes_clients.this, "Modifiction n'est pas Effectué", Toast.LENGTH_SHORT).show();
                                                        }


                                                    }
                                                });
                                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                /**
                                                 * not confirmer
                                                 */
                                                ModifierDialog.dismiss();
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();



                                    }
                                });

                                ModifierDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                ModifierDialog.show();

                            }
                        });








                        MyDyalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        MyDyalog.show();

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

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);


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
