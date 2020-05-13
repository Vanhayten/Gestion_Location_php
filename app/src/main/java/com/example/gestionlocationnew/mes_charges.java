package com.example.gestionlocationnew;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class mes_charges extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String Nom,Prenom,role;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    gestion_location db;
    TextView   datech, montant,design;
    CheckBox espéce,chéque,virment;
    String modpay="";
    Page_Adapter_charge listrep;
    ListView ls;
    Page_Adapter_charge adapter_vihucle;

    Cursor c;
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

        try{
            if(c.getCount()==0) {
              //  boolean c1 = db.insert_charge("01/08/2019", 15, "virment", "des1");
               // Toast.makeText(this,""+c.getString(0),Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){//Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
             }
        ls=(ListView)findViewById(R.id.listcharges);
        ArrayList<list_charge> arrayList1;
        SQLiteDatabase table = db.getReadableDatabase ();
        arrayList1= new ArrayList<list_charge> ();
        String requet = "select * from charge";
        c = table.rawQuery ( requet, null );
        arrayList1.clear();
        if(c.getCount()>=1){
            ls.clearChoices();

            while (c.moveToNext ())
            {
                list_charge list = new list_charge (c.getString(1),c.getString(2)+"DH    "+c.getString(3)+"     "+c.getString(4),c.getString(0).toString());
                arrayList1.add ( list );
            }
             adapter_vihucle = new Page_Adapter_charge (mes_charges.this,arrayList1);
            ls.setAdapter ( adapter_vihucle );
        }else{
            ls.setAdapter (listrep);
        }

         final EditText Recherche;
        Recherche =(EditText)findViewById(R.id.chercherCharge);
Recherche.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        ArrayList<list_charge> arrayList2;
        SQLiteDatabase table = db.getReadableDatabase();
        String requet = "select * from Charge";

        try {


            Cursor c = table.rawQuery(requet, null);


            arrayList2 = new ArrayList<list_charge>();


            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateEditext = Recherche.getText().toString();
            Date date = null;

            date = sdf.parse(dateEditext);
            //   Toast.makeText(this, ""+date, Toast.LENGTH_SHORT).show();

            Date date3;
            int i = 0;


            while (c.moveToNext()) {
                date3 = null;

                  date3 = sdf.parse(c.getString(1));
                //Toast.makeText(this, ""+date3, Toast.LENGTH_SHORT).show();

                if (date3.compareTo(date) == 0) {
                    i++;
                    list_charge list = new list_charge (c.getString(1),c.getString(2)+"DH    "+c.getString(3)+"     "+c.getString(4),c.getString(0).toString());
                    arrayList2.add ( list );
                }

            }

            Page_Adapter_charge adapter_vihucle1 = new Page_Adapter_charge(mes_charges.this, arrayList2);

            if (i > 0) {
                ls.setAdapter(adapter_vihucle1);

            } else {
                ls.setAdapter(adapter_vihucle);
            }


        } catch (Exception Ex) {

        }

    }
});
        //modifer supprimer charge
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dyaloge_modifier_supprimer_mes_charges;
                dyaloge_modifier_supprimer_mes_charges = new Dialog(mes_charges.this);
                dyaloge_modifier_supprimer_mes_charges.setContentView(R.layout.dialoge_modifier_supprimer_mes_charge);
                Button Suprimer, Modifier;
                Suprimer = (Button) dyaloge_modifier_supprimer_mes_charges.findViewById(R.id.btn_charge2);
                Modifier = (Button) dyaloge_modifier_supprimer_mes_charges.findViewById(R.id.btn_charge1);
                final TextView id_charge;
                id_charge =(TextView)view.findViewById(R.id.Id_charge);
                /**
                 * button Suprimer
                 */
                Suprimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mes_charges.this);
                        builder.setCancelable(true);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Voulez-vous vraiment suprimer ?");
                        builder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /**
                                         * confirmer
                                         */

                                        try {
                                            SQLiteDatabase DB = db.getWritableDatabase();
                                             DB.delete("Charge","Id_Charge=?",new String[]{id_charge.getText().toString()});
                                            Toast.makeText(mes_charges.this, "Supprision Réussi", Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(getIntent());

                                        }catch (Exception Ex){
                                            Toast.makeText(mes_charges.this, "Supprision n'est pas Effectué", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /**
                                 * not confirmer
                                 */
                                dyaloge_modifier_supprimer_mes_charges.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();


                    }
                });


                /**
                 * Button Modifier
                 */

                Modifier.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Dialog dyaloge_modifier_mes_charges;
                        dyaloge_modifier_mes_charges = new Dialog(mes_charges.this);
                        dyaloge_modifier_mes_charges.setContentView(R.layout.dialoge_ajoute_mes_charges);
                        Button confirm_modifier;
                        datech= (TextView)dyaloge_modifier_mes_charges.findViewById(R.id.text_datecha);
                        montant= (TextView)dyaloge_modifier_mes_charges.findViewById(R.id.text_Montant);
                        espéce= (CheckBox) dyaloge_modifier_mes_charges.findViewById(R.id.mode_espéce);
                        chéque= (CheckBox) dyaloge_modifier_mes_charges.findViewById(R.id.mode_chèque);
                        virment= (CheckBox) dyaloge_modifier_mes_charges.findViewById(R.id.mode_virement);
                        design= (TextView) dyaloge_modifier_mes_charges.findViewById(R.id.text_design);

                        confirm_modifier =(Button)dyaloge_modifier_mes_charges.findViewById(R.id.btn_confirmer);
                        confirm_modifier.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                /**
                                 * confirmation
                                 */

                                AlertDialog.Builder builder = new AlertDialog.Builder(mes_charges.this);
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
                                                    modpay="";
                                                    if (espéce.isChecked()){
                                                        modpay="espéce";
                                                    }
                                                    if (chéque.isChecked()){
                                                        modpay=modpay+" ,chéque";
                                                    }
                                                    if (virment.isChecked()){
                                                        modpay=modpay+" ,virment";
                                                    }

                                                    SQLiteDatabase DB = db.getWritableDatabase();
                                                    ContentValues v = new ContentValues();
                                                    v.put("Date",datech.getText().toString());
                                                    v.put("Montant",Integer.parseInt(montant.getText().toString()));
                                                    v.put("Payment",modpay);
                                                    v.put("designation",design.getText().toString());

                                                    DB.update("Charge",v,"Id_Charge=?",new String[]{id_charge.getText().toString()});

                                                    Toast.makeText(mes_charges.this, "Modification Réussi", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    startActivity(getIntent());

                                                }catch (Exception Ex){
                                                    Toast.makeText(mes_charges.this, "Modifiction n'est pas Effectué", Toast.LENGTH_SHORT).show();
                                                }


                                            }
                                        });
                                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /**
                                         * not confirmer
                                         */
                                        dyaloge_modifier_mes_charges.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });



                        dyaloge_modifier_mes_charges.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dyaloge_modifier_mes_charges.show();


                    }
                });


                dyaloge_modifier_supprimer_mes_charges.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dyaloge_modifier_supprimer_mes_charges.show();
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

        }
        return false;
    }

    public void confirmer(View view) {
        final Dialog dyalog_mes_charges;
        dyalog_mes_charges = new Dialog(this);
        dyalog_mes_charges.setContentView(R.layout.dialoge_ajoute_mes_charges);

        datech= (TextView)dyalog_mes_charges.findViewById(R.id.text_datecha);
        montant= (TextView)dyalog_mes_charges.findViewById(R.id.text_Montant);
        espéce= (CheckBox) dyalog_mes_charges.findViewById(R.id.mode_espéce);
        chéque= (CheckBox) dyalog_mes_charges.findViewById(R.id.mode_chèque);
       virment= (CheckBox) dyalog_mes_charges.findViewById(R.id.mode_virement);
        design= (TextView) dyalog_mes_charges.findViewById(R.id.text_design);


        Button confirmer;
        confirmer = (Button)dyalog_mes_charges.findViewById(R.id.btn_confirmer);
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (espéce.isChecked()){
                    modpay="espéce";
                }
                if (chéque.isChecked()){
                    modpay=modpay+" ,chéque";
                }
                if (virment.isChecked()){
                    modpay=modpay+" ,virment";
                }
                boolean c = db.insert_charge(datech.getText().toString(),Integer.parseInt(montant.getText().toString()),modpay.toString(),design.getText().toString());
                if (c){
                    Toast.makeText(mes_charges.this,"l'ajoute Reussi",Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(mes_charges.this,"Erreur d'ajoute",Toast.LENGTH_LONG).show();
                }
            }
        });


            dyalog_mes_charges.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dyalog_mes_charges.show();


    }

}
