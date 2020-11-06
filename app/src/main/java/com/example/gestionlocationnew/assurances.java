package com.example.gestionlocationnew;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class assurances extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GoogleSignInClient mGoogleSignInClient;

    String Nom,Prenom,role,login;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ListView ls;
    EditText t1;
    ArrayList<list_vihcule> arrayList;
    PageAdapter_vihucle listrep;
    gestion_location db;
    Dialog myDyalog;
    TextView matr;

    Dialog dialog_asurance_dialog;

    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assurances);



        /**
         * google
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        db = new gestion_location(this);


        /**
         * Menu
         */

        NavigationView navigationView1 = (NavigationView)findViewById(R.id.navigationView);
        View headerView = navigationView1.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.unser_name);
        TextView role1 = headerView.findViewById(R.id.role);
        CircleImageView profile = (CircleImageView)headerView.findViewById(R.id.profilpic);


        /**
         * get image from google and gut its in profile
         */
        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
        String urlsImage = sp.getString("URLImage","");
        if(!urlsImage.equals("")){
            ImageLoadTask imageLoadTask = (ImageLoadTask) new ImageLoadTask(urlsImage, profile).execute();
        }


        Bundle b = getIntent().getExtras();
        Nom = b.getString("nom");
        Prenom =  b.getString("prenom");
        role = ""+b.getString("role");
        login = ""+b.getString("login");
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
        // recherche par matrucil
        t1=(EditText)findViewById(R.id.Recherche_assurance);
        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<list_vihcule> arrayList1;
                SQLiteDatabase table = db.getReadableDatabase ();
                String requet = "select * from véhicules where immatriculation ='"+t1.getText()+"' and login ='"+login+"'";
                Cursor c = table.rawQuery ( requet, null );
                if(c.getCount()>=1){
                    ls.clearChoices();
                    arrayList1= new ArrayList<list_vihcule> ();
                    while (c.moveToNext ())
                    {
                        list_vihcule list = new list_vihcule (c.getString(0),c.getString(2),Integer.parseInt(c.getString(7)));
                        arrayList1.add ( list );
                    }
                    PageAdapter_vihucle adapter_vihucle = new PageAdapter_vihucle (assurances.this,arrayList1);
                    ls.setAdapter ( adapter_vihucle );
                }else{
                    ls.setAdapter (listrep);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //-------------------------
        ls=(ListView)findViewById(R.id.list2);
        //onclick on listner aficher le dialog
        myDyalog = new Dialog(this);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Button sinistre,assurance;
                TextView textClose,textnom;
                myDyalog.setContentView(R.layout.dialog_assurance_sinistre);

                myDyalog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                sinistre   =(Button)myDyalog.findViewById(R.id.btn_sinistres);
                assurance  =(Button)myDyalog.findViewById(R.id.btn_assurance);
                textClose =(TextView)myDyalog.findViewById(R.id.text_close);
                textnom =(TextView)myDyalog.findViewById(R.id.text_nom);


                /**
                 * get matricule to dialog
                 */

                matr =(TextView)view.findViewById(R.id.matrV);
                textnom.setText("le matricule : "+matr.getText());







                /**
                 * assurance
                 *
                 */
                assurance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        TextView textnom,textClose;
                        Button ajoute;
                        final Dialog dialog;
                        dialog = new Dialog(assurances.this);
                        dialog.setContentView(R.layout.dialog_assurance);
                        textnom =(TextView)dialog.findViewById(R.id.text_matr);
                        textnom.setText("le matricule : "+matr.getText());


                        //Onclose dyalog
                        textClose =(TextView)dialog.findViewById(R.id.text_close);
                        textClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });


                        /**
                         * recupiration last asurance
                         */
                        Button proch_assurance;
                        proch_assurance =(Button) dialog.findViewById(R.id.ajouter_assurance);
                        SQLiteDatabase table = db.getReadableDatabase ();
                        String requet = "select date_fin from assurance where imatriculation_asurance ='"+matr.getText().toString()+"' and login ='"+login+"'";
                        Cursor c = table.rawQuery ( requet, null );
                        while (c.moveToNext()){
                            proch_assurance.setText("Prochaine Assurance : "+c.getString(0));
                        }


                        /**
                         *
                         * on click button detaille
                         */

                        proch_assurance.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                /**
                                 * dialog detaile assurance
                                 */


                                dialog_asurance_dialog = new Dialog(assurances.this);
                                dialog_asurance_dialog.setContentView(R.layout.dialog_detaille_asurance);
                                    TextView matr1,date1,date2,companie,prim;
                                    matr1=(TextView) dialog_asurance_dialog.findViewById(R.id.text_matricule);
                                    date1=(TextView) dialog_asurance_dialog.findViewById(R.id.date_debut);
                                    date2=(TextView) dialog_asurance_dialog.findViewById(R.id.date_fin);
                                    companie=(TextView) dialog_asurance_dialog.findViewById(R.id.compagnie_assurance);
                                    prim=(TextView) dialog_asurance_dialog.findViewById(R.id.texttype_vidange);

                                        matr1.setText("Matricule : "+matr.getText().toString());

                                SQLiteDatabase table = db.getReadableDatabase ();
                                String requet = "select * from assurance where imatriculation_asurance ='"+matr.getText().toString()+"' and login ='"+login+"'";
                                Cursor c = table.rawQuery ( requet, null );
                                if (c.moveToLast()){
                                    date1.setText("Date debut :"+c.getString(1).toString());
                                    date2.setText("Date fin : "+c.getString(2).toString());
                                    companie.setText("Compagnie assurance : "+c.getString(3).toString());
                                    prim.setText("prime assurance : "+c.getString(4).toString()+" DH");
                                }

                                /**
                                 *
                                 * onclick close
                                 */
                                TextView close;
                                close=(TextView) dialog_asurance_dialog.findViewById(R.id.text_close);
                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog_asurance_dialog.dismiss();
                                    }
                                });



                                dialog_asurance_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog_asurance_dialog.show();
                            }
                        });




                        /**
                         * button ajouter assurance
                         */
                        final Button ajouter_assurance;
                        ajouter_assurance = (Button) dialog.findViewById(R.id.btn_Ajouter1);
                        ajouter_assurance.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent I = new Intent(assurances.this, com.example.gestionlocationnew.ajouter_assurance.class);
                                Bundle B= new Bundle();
                                B.putString("matricule",matr.getText().toString());
                                        //----------------
                                B.putString("nom",Nom);
                                B.putString("prenom",Prenom);
                                B.putString("role",role);
                                B.putString("login",login);
                                I.putExtras(B);
                                startActivity(I);
                                finish();


                            }
                        });



                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });










                /**
                 * button sinistre
                 */

                sinistre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        TextView textnom,textClose;
                        Button historique,ajoute;
                        final Dialog myDyalog_ajoute;
                        myDyalog_ajoute = new Dialog(assurances.this);
                        myDyalog_ajoute.setContentView(R.layout.dialog_historique_ajouter_sinistre);
                        textnom =(TextView)myDyalog_ajoute.findViewById(R.id.text_nom);
                        textnom.setText("le matricule : "+matr.getText());

                        //Onclose dyalog
                        textClose =(TextView)myDyalog_ajoute.findViewById(R.id.text_close);
                        textClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDyalog_ajoute.dismiss();
                            }
                        });




                        /**
                         * buton historique
                         */

                        historique  =(Button)myDyalog_ajoute.findViewById(R.id.btn_historique);
                        historique.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                SQLiteDatabase table = db.getReadableDatabase ();
                                String requet = "select count(*) from sinistre where imatriculation_sinistre ='"+matr.getText().toString()+"' and login ='"+login+"'";
                                Cursor c = table.rawQuery ( requet, null );
                                if(c.moveToNext()) {
                                    if (Integer.parseInt(c.getString(0)) == 0) {

                                        Toast.makeText(assurances.this, "sinistre vide", Toast.LENGTH_SHORT).show();
                                    } else {

                                        Intent I = new Intent(assurances.this, historique_sinistre.class);
                                        Bundle B = new Bundle();
                                        B.putString("matricule", matr.getText().toString());
                                        B.putString("login", login);
                                        I.putExtras(B);
                                        startActivity(I);
                                    }
                                }
                                }


                        });



                        /**
                         * buton ajoute
                         */
                        ajoute =(Button)myDyalog_ajoute.findViewById(R.id.btn_Ajouter);
                        ajoute.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final Dialog myDyalog_ajoute_conformation;
                                myDyalog_ajoute_conformation = new Dialog(assurances.this);
                                myDyalog_ajoute_conformation.setContentView(R.layout.dialog_ajoute_sinisstre);
                                final TextView text1,text2,text4,text6;
                                final CheckBox CH1,CH2;
                                final Spinner spinner;
                                Button btnConfirmation;
                                text1 = (EditText)myDyalog_ajoute_conformation.findViewById(R.id.text_matricule2);
                                text2 = (EditText)myDyalog_ajoute_conformation.findViewById(R.id.text_datesinistre2);
                                CH1 = (CheckBox)myDyalog_ajoute_conformation.findViewById(R.id.text_ganredaccident2);
                                CH2 = (CheckBox)myDyalog_ajoute_conformation.findViewById(R.id.text_ganredaccident3);
                                text4 = (EditText)myDyalog_ajoute_conformation.findViewById(R.id.text_vmontant2);
                                spinner = (Spinner)myDyalog_ajoute_conformation.findViewById(R.id.text_responsabilite2);
                                text6 = (EditText)myDyalog_ajoute_conformation.findViewById(R.id.text_montant2);
                                text1.setText(matr.getText());



                                /**
                                 * get date circulation
                                 */
                                text2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Calendar cal = Calendar.getInstance();
                                        int Year = cal.get(Calendar.YEAR);
                                        int Month = cal.get(Calendar.MONTH);
                                        int Day = cal.get(Calendar.DAY_OF_MONTH);

                                        DatePickerDialog dialogDate = new DatePickerDialog(assurances.this
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
                                        text2.setText(datefin);
                                    }
                                };








                                    ArrayList<String> arrayList = new ArrayList<String>();
                                    arrayList.add("0%");
                                    arrayList.add("50%");
                                    arrayList.add("100%");
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(assurances.this,R.layout.support_simple_spinner_dropdown_item,arrayList);
                                    spinner.setAdapter(arrayAdapter);


                                /**
                                 *
                                 * click button d'ajoute
                                 */
                                btnConfirmation =(Button)myDyalog_ajoute_conformation.findViewById(R.id.btn_modifier1);
                                btnConfirmation.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        /**
                                         *
                                         * dialog confirmation on joute
                                         */

                                        AlertDialog.Builder builder = new AlertDialog.Builder(assurances.this);
                                        builder.setCancelable(true);
                                        builder.setTitle("Neveaux sinistre");
                                        builder.setMessage("vous vouller ajouter");
                                        builder.setPositiveButton("Confirm",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        String ch11="";
                                                            if(CH1.isChecked() && CH2.isChecked() ){
                                                                ch11 ="Corporele , Material";
                                                            }else if (CH1.isChecked() && !CH2.isChecked()){
                                                                ch11 ="Corporele";
                                                            }else if(!CH1.isChecked() && CH2.isChecked()){
                                                                ch11 ="Material";
                                                                }
                                                        boolean confirm = db.insert_sinistre(text1.getText().toString(),text2.getText().toString(),ch11,Integer.parseInt(text4.getText().toString()),spinner.getSelectedItem().toString(),Integer.parseInt(text6.getText().toString()),login);
                                                        if(confirm){
                                                            Toast.makeText(assurances.this, "Bien ajoute", Toast.LENGTH_SHORT).show();
                                                            myDyalog_ajoute_conformation.dismiss();
                                                        }else{
                                                            Toast.makeText(assurances.this, "Erreur d'ajoute", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                    }
                                });


                                myDyalog_ajoute_conformation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                myDyalog_ajoute_conformation.show();



                            }
                        });


                        myDyalog_ajoute.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        myDyalog_ajoute.show();

                    }
                });





                //Onclose dyalog
                textClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDyalog.dismiss();
                    }
                });
                myDyalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDyalog.show();

            }
        });






        SQLiteDatabase table = db.getReadableDatabase ();
        String requet = "select * from véhicules where login ='"+login+"'";
        Cursor c = table.rawQuery ( requet, null );
        if(c.getCount()==0){
            Intent i=new Intent(this,Ajoute_vihicule.class);
            b.putString("nom",Nom);
            b.putString("prenom",Prenom);
            b.putString("role",role);
            b.putString("login",login);
            i.putExtras(b);
            startActivity(i);
        }
        arrayList = new ArrayList<list_vihcule> ();
        arrayList.clear ();
        while (c.moveToNext ())
        {
            list_vihcule list = new list_vihcule (c.getString(0),c.getString(2),Integer.parseInt(c.getString(7)));
            arrayList.add ( list );
        }
       listrep = new PageAdapter_vihucle ( this, arrayList);
        ls.setAdapter ( listrep );


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
                b.putString("login",login);
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
                b.putString("login",login);
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
                b.putString("login",login);
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
                b.putString("login",login);
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
                b.putString("login",login);
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
                b.putString("login",login);
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
                b.putString("login",login);
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
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);
                break;

            case R.id.logout:

                /**
                 * Sing out from google
                 */
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(assurances.this, " déconnecté avec succès", Toast.LENGTH_SHORT).show();
                                // ...
                            }
                        });


                T = new Intent(this, Login.class);
                SharedPreferences sp;
                sp = getSharedPreferences("login",MODE_PRIVATE);
                sp.edit().putBoolean("logged",false).apply();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);
                break;

        }
        return false;
    }

}
