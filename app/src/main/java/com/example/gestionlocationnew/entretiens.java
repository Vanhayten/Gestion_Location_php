
package com.example.gestionlocationnew;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class entretiens extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String Nom, Prenom, role,login;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    EditText t1;
    gestion_location db;
    ListView ls;
    PageAdapter_vihucle listrep;
    TextView Matricule;
    Button btn_atende;
    Dialog dialog_ajoute_visite;


    DBOpenHelper dbOpenHelper;
    TextView CurrentDate;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();
    private static final int MAX_CALENDAR_DAYS = 42;
    MyGridAdapter myGridAdapter;
    GridView gridView;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM",Locale.ENGLISH);
    SimpleDateFormat yearFormate = new SimpleDateFormat("yyyy",Locale.ENGLISH);
    SimpleDateFormat eventDateFormate = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);


    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListenervisite;
    private DatePickerDialog.OnDateSetListener mDateSetListenerfinvisite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entretiens);


        /**
         * get Intent values
         */
        Bundle b = getIntent().getExtras();
        Nom = b.getString("nom");
        Prenom = b.getString("prenom");
        role = "" + b.getString("role");
        login = "" + b.getString("login");



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
        //-------------------------
        db = new gestion_location(this);
        t1 = (EditText) findViewById(R.id.recherche);
        ls = (ListView) findViewById(R.id.list3);
        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<list_vihcule> arrayList1;
                SQLiteDatabase table = db.getReadableDatabase();
                String requet = "select * from véhicules where immatriculation ='" + t1.getText() + "' and login ='"+login+"'";
                Cursor c = table.rawQuery(requet, null);
                if (c.getCount() >= 1) {
                    ls.clearChoices();
                    arrayList1 = new ArrayList<list_vihcule>();
                    while (c.moveToNext()) {
                        list_vihcule list = new list_vihcule(c.getString(0), c.getString(2),Integer.parseInt(c.getString(7)));
                        arrayList1.add(list);
                    }
                    PageAdapter_vihucle adapter_vihucle = new PageAdapter_vihucle(entretiens.this, arrayList1);
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
        ArrayList<list_vihcule> arrayList;
        String requet = "select * from véhicules and login ='"+login+"'";
        Cursor c = table.rawQuery(requet, null);
        arrayList = new ArrayList<list_vihcule>();
        arrayList.clear();



        while (c.moveToNext()) {

            list_vihcule list = new list_vihcule(c.getString(0), c.getString(2),Integer.parseInt(c.getString(7)));
            arrayList.add(list);
        }
        listrep = new PageAdapter_vihucle(this, arrayList);
        ls.setAdapter(listrep);

        NavigationView navigationView1 = (NavigationView) findViewById(R.id.navigationView);
        View headerView = navigationView1.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.unser_name);
        TextView role1 = headerView.findViewById(R.id.role);


        username.setText(Nom + " " + Prenom);
        role1.setText(role);

        /**
         *
         * on select la vihicule
         */

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog MyDyalog;
                MyDyalog = new Dialog(entretiens.this);
                MyDyalog.setContentView(R.layout.dialog_vidange_reparation_visitetechnique);

                MyDyalog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                ImageButton img1_reparation, img2_vidange, img3_visite;
                TextView text1, text_nom;
                text1 = (TextView) MyDyalog.findViewById(R.id.text_close);
                text_nom = (TextView) MyDyalog.findViewById(R.id.text_nom);
                img1_reparation = (ImageButton) MyDyalog.findViewById(R.id.reparation);
                img2_vidange = (ImageButton) MyDyalog.findViewById(R.id.vidange);
                img3_visite = (ImageButton) MyDyalog.findViewById(R.id.visite_technique);


                Matricule = (TextView) view.findViewById(R.id.matrV);


                /**
                 *onclick reparation
                 */

                img1_reparation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      Cursor c;
                        SQLiteDatabase table = db.getReadableDatabase();
                        String requet = "select count(*) from reparation where imatriculation ='" + Matricule.getText().toString() + "' and login ='"+login+"'";
                      c = table.rawQuery(requet, null);


                        /**
                         *
                         * if list reparation vide most be remplire
                         */
                        
                       if (c.moveToNext()) {

                       if (Integer.parseInt(c.getString(0)) == 0) {

                            final Dialog MyDyalog_ajou;
                            MyDyalog_ajou = new Dialog(entretiens.this);
                            MyDyalog_ajou.setContentView(R.layout.dialog_ajoute_reparation);
                            final EditText text1, text2, text3, text4, text5, text6;
                            text1 = (EditText) MyDyalog_ajou.findViewById(R.id.text_prenom);
                            text2 = (EditText) MyDyalog_ajou.findViewById(R.id.text_piece);
                            text3 = (EditText) MyDyalog_ajou.findViewById(R.id.text_main);
                            text4 = (EditText) MyDyalog_ajou.findViewById(R.id.text_ref_facture);
                            text5 = (EditText) MyDyalog_ajou.findViewById(R.id.text_date_reparation);
                            text6 = (EditText) MyDyalog_ajou.findViewById(R.id.text_Montant);
                            text1.setText(Matricule.getText().toString());


                           /**
                            * gete date reparation
                            */
                           text5.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   Calendar cal = Calendar.getInstance();
                                   int Year = cal.get(Calendar.YEAR);
                                   int Month = cal.get(Calendar.MONTH);
                                   int Day = cal.get(Calendar.DAY_OF_MONTH);

                                   DatePickerDialog dialogDate = new DatePickerDialog(entretiens.this
                                           ,android.R.style.Theme_Holo_Dialog_MinWidth
                                           ,mDateSetListener,Year, Month,Day);

                                   dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                   dialogDate.show();

                               }
                           });

                           mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                               @Override
                               public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                   month =month+1;
                                   String datefin = dayOfMonth+"/"+month+"/"+year;
                                   text5.setText(datefin);
                               }
                           };





                            Button btn_ajoute;
                            btn_ajoute = (Button) MyDyalog_ajou.findViewById(R.id.btn_modifier1);
                            btn_ajoute.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    boolean b = false;
                                        try {
                                            b = db.insert_reparation(Matricule.getText().toString(), text2.getText().toString(), text3.getText().toString(), text4.getText().toString(), text5.getText().toString(), Integer.parseInt(text6.getText().toString()),login);
                                        }catch (Exception E){

                                        }
                                    if (b) {
                                        Toast.makeText(entretiens.this, "l'enregistrement effecuter", Toast.LENGTH_SHORT).show();
                                        MyDyalog_ajou.dismiss();
                                    } else {
                                        Toast.makeText(entretiens.this, "l'enregistrement ne pas effectuer", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            MyDyalog_ajou.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            MyDyalog_ajou.show();
                           Toast.makeText(entretiens.this, "Pour accéder à cette page, vous devez au moins remplir 1 réparation", Toast.LENGTH_LONG).show();


                        } else {

                            Intent I = new Intent(entretiens.this, reparation.class);
                            Bundle B = new Bundle();

                            B.putString("matricule", Matricule.getText().toString());
                            B.putString("login", login);
                           nouvel_reparation fragobj = new nouvel_reparation();
                            fragobj.setArguments(B);

                            I.putExtras(B);
                            startActivity(I);

                        }
                   }


                    }
                });

                text_nom.setText("Matricule : " + Matricule.getText().toString());
                
                /**
                 *
                 * on click close
                 */
                
                text1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDyalog.dismiss();
                    }
                });

                /**
                 *
                 * button vidange
                 */

                img2_vidange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dyalog_vidange;
                        dyalog_vidange = new Dialog(entretiens.this);
                        dyalog_vidange.setContentView(R.layout.dialog_vidange);
                        TextView text1, text_nom;
                        text1 = (TextView) dyalog_vidange.findViewById(R.id.text_close);
                        text_nom = (TextView) dyalog_vidange.findViewById(R.id.text_nom);
                        text_nom.setText("Matricule : " + Matricule.getText().toString());
                        /**
                         *
                         * on click close
                         */
                        text1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dyalog_vidange.dismiss();
                            }
                        });

                        /**
                         * close last dialog
                         */
                        MyDyalog.dismiss();

                        /**
                         *
                         * inser kilometrage
                         */
                        try {


                            btn_atende = (Button) dyalog_vidange.findViewById(R.id.btn_atende);
                            String requet = "select kilomaitrage,type_vidage from vidange where imatriculation_vidange ='" + Matricule.getText() + "' and login ='"+login+"'";
                            SQLiteDatabase table = db.getReadableDatabase();
                            Cursor c = table.rawQuery(requet, null);
                            Integer kilom = 0;
                            while (c.moveToNext()) {
                                kilom = Integer.parseInt(c.getString(0)) + Integer.parseInt(c.getString(1));
                            }
                            btn_atende.setText("Prochaine Vidange : " + kilom + " KM");
                        } catch (Exception EX) {
                            Toast.makeText(entretiens.this, "Aucun vidage", Toast.LENGTH_SHORT).show();
                        }

                        /**
                         * button detaille vidange
                         */
                        btn_atende.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                final Dialog dyalog_detaille_vidange;
                                dyalog_detaille_vidange = new Dialog(entretiens.this);
                                dyalog_detaille_vidange.setContentView(R.layout.dialog_detaille_vidange);
                                TextView text1, text2, text3, text4, text5;
                                TextView close;
                                close = (TextView) dyalog_detaille_vidange.findViewById(R.id.text_close);
                                text1 = (TextView) dyalog_detaille_vidange.findViewById(R.id.text_matricule);
                                text2 = (TextView) dyalog_detaille_vidange.findViewById(R.id.text_datevidange);
                                text3 = (TextView) dyalog_detaille_vidange.findViewById(R.id.text_kilomaitrage);
                                text4 = (TextView) dyalog_detaille_vidange.findViewById(R.id.text_filtre);
                                text5 = (TextView) dyalog_detaille_vidange.findViewById(R.id.texttype_vidange);
                                try {
                                    String requet = "select * from vidange where imatriculation_vidange ='" + Matricule.getText() + "' and login ='"+login+"'";
                                    SQLiteDatabase table = db.getReadableDatabase();
                                    Cursor c = table.rawQuery(requet, null);
                                    while (c.moveToNext()) {
                                        text1.setText("matricule : " + c.getString(0));
                                        text2.setText("date vodange : " + c.getString(1));
                                        text3.setText("kilomaitrage : " + c.getString(2) + " KM");
                                        text4.setText("filtre : " + c.getString(3));
                                        text5.setText("type vidange : " + c.getString(4) + " KM");
                                    }
                                } catch (Exception Ex) {
                                }

                                /**
                                 * close dialog
                                 */

                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dyalog_detaille_vidange.dismiss();
                                    }
                                });

                                dyalog_detaille_vidange.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dyalog_detaille_vidange.show();
                            }
                        });


                        /**
                         *
                         * button Ajoute vidange
                         */

                        final Button Ajoute_vidange;
                        Ajoute_vidange = (Button) dyalog_vidange.findViewById(R.id.btn_Ajouter1);
                        Ajoute_vidange.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent I = new Intent(entretiens.this, com.example.gestionlocationnew.Ajoute_vidange.class);


                                Bundle B = new Bundle();
                                B.putString("Matricule", Matricule.getText().toString());
                                B.putString("nom", Nom);
                                B.putString("prenom", Prenom);
                                B.putString("role", role);
                                B.putString("login", login);
                                I.putExtras(B);
                                startActivity(I);
                                finish();


                            }
                        });

                        dyalog_vidange.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dyalog_vidange.show();
                    }
                });

                final Dialog dialog_visite;
                dialog_visite = new Dialog(entretiens.this);
                dialog_visite.setContentView(R.layout.dialog_visite_texhnique);
                img3_visite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /**
                         * close last dialog
                         */
                        MyDyalog.dismiss();


                        /**
                         * bundel visite technique
                         */


                        TextView Nom;
                        Button date_visite;
                        Nom = (TextView) dialog_visite.findViewById(R.id.text_nom);
                        date_visite = (Button) dialog_visite.findViewById(R.id.btn_atende);
                        Nom.setText("matricule : " + Matricule.getText());


                        /**
                         * recupiration date prochaine
                         */
                        
                        try {

                            SQLiteDatabase table = db.getReadableDatabase();
                            String requet = "select * from visite_technique where imatriculation_visite = '" + Matricule.getText() + "' and login ='"+login+"'";
                            Cursor c = table.rawQuery(requet, null);
                            while (c.moveToNext()) {
                                date_visite.setText("prochaine visite technique : " + c.getString(2));
                            }
                        } catch (Exception Ex) {
                            Toast.makeText(entretiens.this, "aucun visite", Toast.LENGTH_SHORT).show();
                        }

                        dialog_visite.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog_visite.show();
                    }
                });


                /**
                 * on click close
                 */
                
                TextView close;
                close = (TextView) dialog_visite.findViewById(R.id.text_close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_visite.dismiss();
                    }
                });

                /**
                 * on click ajouter
                 */

                // final Dialog dialog_ajoute_visite;

                Button btn_Ajouter;
                btn_Ajouter = (Button) dialog_visite.findViewById(R.id.btn_Ajouter1);
                btn_Ajouter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_visite.dismiss();

                        dialog_ajoute_visite = new Dialog(entretiens.this);
                        dialog_ajoute_visite.setContentView(R.layout.dialog_ajoute_visite_technique);

                        final EditText matricule, date1, Date2;
                        Button confirme;

                        matricule = (EditText) dialog_ajoute_visite.findViewById(R.id.text_prenom);
                        date1 = (EditText) dialog_ajoute_visite.findViewById(R.id.text_date);
                        Date2 = (EditText) dialog_ajoute_visite.findViewById(R.id.text_date_proch);
                        confirme = (Button) dialog_ajoute_visite.findViewById(R.id.btn_modifier1);
                        matricule.setText(Matricule.getText().toString());


                        /**
                         * gete date visite
                         */


                        date1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar cal = Calendar.getInstance();
                                int Year = cal.get(Calendar.YEAR);
                                int Month = cal.get(Calendar.MONTH);
                                int Day = cal.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog dialogDate = new DatePickerDialog(entretiens.this
                                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                                        ,mDateSetListenervisite,Year, Month,Day);

                                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogDate.show();

                            }
                        });

                        mDateSetListenervisite = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month =month+1;
                                String datefin = dayOfMonth+"/"+month+"/"+year;
                                date1.setText(datefin);
                            }
                        };





                        /**
                         * gete date fin visite
                         */

                        Date2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar cal = Calendar.getInstance();
                                int Year = cal.get(Calendar.YEAR);
                                int Month = cal.get(Calendar.MONTH);
                                int Day = cal.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog dialogDate = new DatePickerDialog(entretiens.this
                                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                                        ,mDateSetListenerfinvisite,Year, Month,Day);

                                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogDate.show();

                            }
                        });

                        mDateSetListenerfinvisite = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month =month+1;
                                String datefin = dayOfMonth+"/"+month+"/"+year;
                                Date2.setText(datefin);
                            }
                        };





                        /**
                         * confirmation dajoute
                         */
                        
                        confirme.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean res = false;
                                SQLiteDatabase DB = db.getWritableDatabase();
                                ContentValues tab_ch1 = new ContentValues();
                                tab_ch1.put("imatriculation_visite", Matricule.getText().toString());
                                tab_ch1.put("date_visite", date1.getText().toString());
                                tab_ch1.put("prch_visite", Date2.getText().toString());
                                tab_ch1.put("login", login);
                                long result = DB.insert("visite_technique", null, tab_ch1);
                                if (result == -1) {
                                    res = false;
                                } else {
                                    res = true;
                                }
                                if (res) {
                                    dialog_ajoute_visite.dismiss();
                                    Toast.makeText(entretiens.this, "Bien Ajouter", Toast.LENGTH_SHORT).show();

                                    /**
                                     * AJOUTE SUR CELENDAR
                                     */

                                    String Event = "Prochaine visite de la vihicule "+Matricule.getText().toString();
                                    try {
                                        addEventsassurance(Date2.getText().toString(),Event,login);
                                    }catch (Exception EX){

                                    }

                                } else {
                                    Toast.makeText(entretiens.this, "erreur d'joute", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                        dialog_ajoute_visite.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog_ajoute_visite.show();


                    }
                });


                MyDyalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                MyDyalog.show();

            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent T;
        Bundle b = new Bundle();
        switch (menuItem.getItemId()) {
            case R.id.vihicule:
                T = new Intent(this, vehicules.class);
                b.putString("nom", Nom);
                b.putString("prenom", Prenom);
                b.putString("role", role);
                b.putString("login", login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;
            case R.id.assurances:
                T = new Intent(this, com.example.gestionlocationnew.assurances.class);
                b.putString("nom", Nom);
                b.putString("prenom", Prenom);
                b.putString("role", role);
                b.putString("login", login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;
            case R.id.entretiens:
                T = new Intent(this, entretiens.class);
                b.putString("nom", Nom);
                b.putString("prenom", Prenom);
                b.putString("role", role);
                b.putString("login", login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;

            case R.id.recette:
                T = new Intent(this, mes_recettes.class);
                b.putString("nom", Nom);
                b.putString("prenom", Prenom);
                b.putString("role", role);
                b.putString("login", login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;
            case R.id.charges:
                T = new Intent(this, mes_charges.class);
                b.putString("nom", Nom);
                b.putString("prenom", Prenom);
                b.putString("role", role);
                b.putString("login", login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;
            case R.id.calendrier:
                T = new Intent(this, calendrier.class);
                b.putString("nom", Nom);
                b.putString("prenom", Prenom);
                b.putString("role", role);
                b.putString("login", login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;
            case R.id.clients:
                T = new Intent(this, mes_clients.class);
                b.putString("nom", Nom);
                b.putString("prenom", Prenom);
                b.putString("role", role);
                b.putString("login", login);
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
                b.putString("login", login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;

        }
        return false;
    }




    public  void addEventsassurance(String sdate,String discription ,String login){

        String string = sdate;
        //t3.getText().toString();
        String[] parts = string.split("/");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        String DateF = part3+"-"+part2+"-"+part1;

        String monthString;
        switch (Integer.parseInt(part2)) {
            case 1:  monthString = "January";       break;
            case 2:  monthString = "February";      break;
            case 3:  monthString = "March";         break;
            case 4:  monthString = "April";         break;
            case 5:  monthString = "May";           break;
            case 6:  monthString = "June";          break;
            case 7:  monthString = "July";          break;
            case 8:  monthString = "August";        break;
            case 9:  monthString = "September";     break;
            case 10: monthString = "October";       break;
            case 11: monthString = "November";      break;
            case 12: monthString = "December";      break;
            default: monthString = "Invalid month"; break;
        }

        String Events = discription;
        //t1.getText().toString();
        Calendar c = Calendar.getInstance();


        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        SaveEvent(Events,formattedDate,DateF,monthString,part3,login);
        SetUpCalendar(login);

    }


     private void SaveEvent(String event,String time,String date, String month,String year,String login){

        dbOpenHelper = new DBOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event,time,date,month,year,"on",login,database);
        dbOpenHelper.close();
        Toast.makeText(this, "Event Saved", Toast.LENGTH_SHORT).show();

    }


    private void SetUpCalendar(String login){
        String currwntDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currwntDate);
        dates.clear();
        Calendar monthCalendar= (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormate.format(calendar.getTime()),login);

        while (dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        myGridAdapter = new MyGridAdapter(this,dates,calendar,eventsList);
        gridView.setAdapter(myGridAdapter);

    }


    private void CollectEventsPerMonth(String Month,String year,String login){
        eventsList.clear();
        dbOpenHelper= new DBOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsperMonth(Month,year,login,database);
        while (cursor.moveToNext()){
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Events events = new Events(event,time,date,month,Year);
            eventsList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();

    }


}
