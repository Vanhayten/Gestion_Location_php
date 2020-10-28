package com.example.gestionlocationnew;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class mes_charges extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    String Nom, Prenom, role,login;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    gestion_location db;
    TextView datech, montant, design;
    CheckBox espéce, chéque, virment;
    String modpay = "";
    Page_Adapter_charge listrep;
    ListView ls;
    Page_Adapter_charge adapter_vihucle;
    EditText Recherche;
    EditText Recherche1;
    private LineChart mChart;


    private DatePickerDialog.OnDateSetListener mDateSetListenerRecherche;
    private DatePickerDialog.OnDateSetListener mDateSetListenerRecherche1;
    private DatePickerDialog.OnDateSetListener DateSetListenerche;
    private DatePickerDialog.OnDateSetListener DateSetListenerche1;

    List<String> dateshh;
    List<Double> allAmountsss;

    Cursor c;
    private Dialog MyDyalog_detaille;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_charge);


        Bundle b = getIntent().getExtras();
        Nom = b.getString("nom");
        Prenom = b.getString("prenom");
        role = "" + b.getString("role");
        login = "" + b.getString("login");


        db = new gestion_location(this);

        /**
         * pdf
         *
         *
         */
        try{
            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
            m.invoke(null);
        }catch(Exception e){
            e.printStackTrace();
        }


        /* clic sur le bouton */
        findViewById(R.id.button_pdf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = getApplicationContext();
                try {
                    File fileInDataDir = copyFileFromAssetsToDownloads(c, "", "file.pdf");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(fileInDataDir), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                } catch(Exception e) {
                    Log.e(getClass().getSimpleName(), "Exception: " + e.getMessage(), e);
                }
            }
        });

        /* Permissions Nougat et +*/
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        }, 1);





        /**
         * create CHART -------------------------------------------        start
         */


        mChart = findViewById(R.id.Linecharcherge);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);


        LimitLine ll1 = new LimitLine(30f, "Title");
        ll1.setLineColor(getResources().getColor(R.color.NAVblack_theme75));
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(35f, "");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);


        YAxis leftAxis = mChart.getAxisLeft();
        XAxis xAxis = mChart.getXAxis();


        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        leftAxis.setValueFormatter(new ClaimsYAxisValueFormatter());


        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);


        //xAxis.setLabelCount(31);


        mChart.getDescription().setEnabled(true);
        Description description = new Description();
        // description.setText(UISetters.getFullMonthName());//commented for weekly reporting
        description.setText("Jour");
        description.setTextSize(15f);
        mChart.getDescription().setPosition(0f, 0f);
        mChart.setDescription(description);
        mChart.getAxisRight().setEnabled(false);



        String requet1 = "select * from charge where login ='"+login+"' ORDER BY Date ASC";
        SQLiteDatabase table1 = db.getReadableDatabase();
        Cursor c1 = table1.rawQuery(requet1, null);
        //////////////////


        //LocalDate now1 = LocalDate.now();
        //String dateYear1 = now1.format(DateTimeFormatter.ofPattern("yyyy"));
        //String dateMonth1 = now1.format(DateTimeFormatter.ofPattern("MM"));

        Calendar datecalendar = Calendar.getInstance();
        final int alarmYear = datecalendar.get(Calendar.YEAR);
        int alarmMonth = datecalendar.get(Calendar.MONTH);
        alarmMonth++;
        String dateYear1 = "" + alarmYear;
        String dateMonth1 = "" + alarmMonth;


        String dateYearcon1 = null;
        String dateMonthcon1 = null;
        String dateDaycon1 = null;
        int day = 0;
        double prixx = 0;

        Integer test1 = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date3=new Date();
        Date date4=new Date();


        if (c1.getCount() == 0) {


        } else {
            dateshh = new ArrayList<>();
            allAmountsss = new ArrayList<>();

            while (c1.moveToNext()) {
                date3 = null;
                date4 = null;
                prixx = 0;

                dateYearcon1 = c1.getString(1).split("/")[2];
                dateMonthcon1 = c1.getString(1).split("/")[1];
                dateDaycon1 = c1.getString(1).split("/")[0];

                int dateyy = Integer.parseInt(dateYearcon1);
                int datemm = Integer.parseInt(dateMonthcon1);

                if (dateyy == alarmYear && datemm == alarmMonth) {

                    /**
                     * test test test test test test ------------------
                     */

                    String requet2 = "select * from charge where login ='"+login+"' ORDER BY Date ASC";
                    Cursor c2 = table1.rawQuery(requet2, null);
                    test1 = 0;

                    try {
                        date3 = sdf.parse(c1.getString(1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    while (c2.moveToNext()) {
                        try {
                            date4 = sdf.parse(c2.getString(1));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (date3.compareTo(date4) == 0) {
                            test1 = test1 + Integer.parseInt(c2.getString(2));
                        }

                    }


                    prixx = test1;

                    day = Integer.parseInt(dateDaycon1);
                    //prixx = Integer.parseInt(c1.getString(2));
                    // yValues.add(new Entry(day,prixx));
                    boolean repitition = false;

                    for (int i = 0; i < dateshh.size(); i++) {

                        if (dateshh.get(i).equals(c1.getString(1))) {
                            repitition = true;
                        }

                    }

                    if (repitition == false) {

                        if (c1.getCount() != 0) {
                            dateshh.add(c1.getString(1));

                            allAmountsss.add(prixx);
                        }

                    }
                }

            }
            if (dateshh != null && allAmountsss != null) {
                renderData(dateshh, allAmountsss);
            }
        }


        /**
         * create CHART -------------------------------------------     end
         */

        Recherche = (EditText) findViewById(R.id.chercherCharge);
        Recherche1 = (EditText) findViewById(R.id.chercherCharge1);


        /**
         * get date recherche
         */
        Recherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(mes_charges.this
                        , android.R.style.Theme_Holo_Dialog_MinWidth
                        , mDateSetListenerRecherche, Year, Month, Day);


                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        mDateSetListenerRecherche = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String datefin = dayOfMonth + "/" + month + "/" + year;
                Recherche.setText(datefin);
            }
        };


        /**
         * get date recherche
         */
        Recherche1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(mes_charges.this
                        , android.R.style.Theme_Holo_Dialog_MinWidth
                        , mDateSetListenerRecherche1, Year, Month, Day);

                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        mDateSetListenerRecherche1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String datefin = dayOfMonth + "/" + month + "/" + year;
                Recherche1.setText(datefin);
                Toast.makeText(mes_charges.this, "Clicker sur le diagramme pour voir le resultats de recherche  ", Toast.LENGTH_LONG).show();
            }
        };


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


        NavigationView navigationView1 = (NavigationView) findViewById(R.id.navigationView);
        View headerView = navigationView1.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.unser_name);
        TextView role1 = headerView.findViewById(R.id.role);


        username.setText(Nom + " " + Prenom);
        role1.setText(role);


        /**
         *remplissage liste des charges
         */

        ls = (ListView) findViewById(R.id.listcharges);
        ArrayList<list_charge> arrayList1;
        SQLiteDatabase table = db.getReadableDatabase();
        arrayList1 = new ArrayList<list_charge>();
        String requet = "select * from Charge where login ='"+login+"'";
        c = table.rawQuery(requet, null);
        arrayList1.clear();
        if (c.getCount() >= 1) {
            ls.clearChoices();

            while (c.moveToNext()) {
                list_charge list = new list_charge(c.getString(0), c.getString(4), c.getString(2), c.getString(3), c.getString(1));
                arrayList1.add(list);
            }
            Collections.reverse(arrayList1);
            adapter_vihucle = new Page_Adapter_charge(mes_charges.this, arrayList1);
            ls.setAdapter(adapter_vihucle);
        } else {

            ls.setAdapter(listrep);
        }

/**
 * recherche
 */
        Recherche.addTextChangedListener(new TextWatcher() {
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

        Recherche1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                dateshh.clear();
                allAmountsss.clear();

                try {
                    rechercheEntreDeuxDate(login);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                dateshh.clear();
                allAmountsss.clear();

                try {
                    rechercheEntreDeuxDate(login);
                } catch (ParseException e) {
                    e.printStackTrace();
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

                dyaloge_modifier_supprimer_mes_charges.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                Button Suprimer, Modifier;
                Suprimer = (Button) dyaloge_modifier_supprimer_mes_charges.findViewById(R.id.btn_charge2);
                Modifier = (Button) dyaloge_modifier_supprimer_mes_charges.findViewById(R.id.btn_charge1);
                final TextView id_charge;

                id_charge = (TextView) view.findViewById(R.id.Id_charge);
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
                                            DB.delete("Charge", "Id_Charge=? and login=?", new String[]{id_charge.getText().toString(),login});
                                            Toast.makeText(mes_charges.this, "Supprision Réussi", Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(getIntent());

                                        } catch (Exception Ex) {
                                            Toast.makeText(mes_charges.this, "Suppression n'est pas Effectué", Toast.LENGTH_SHORT).show();
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
                        datech = (TextView) dyaloge_modifier_mes_charges.findViewById(R.id.text_datecha);
                        montant = (TextView) dyaloge_modifier_mes_charges.findViewById(R.id.text_Montant);
                        espéce = (CheckBox) dyaloge_modifier_mes_charges.findViewById(R.id.mode_espéce);
                        chéque = (CheckBox) dyaloge_modifier_mes_charges.findViewById(R.id.mode_chèque);
                        virment = (CheckBox) dyaloge_modifier_mes_charges.findViewById(R.id.mode_virement);
                        design = (TextView) dyaloge_modifier_mes_charges.findViewById(R.id.text_design);


                        String requuet = "select * from Charge where Id_Charge ='" + id_charge.getText().toString() + "' and login ='"+login+"'";
                        Cursor c = table.rawQuery(requuet, null);
                        while (c.moveToNext()) {
                            datech.setText(c.getString(1));
                            montant.setText(c.getString(2));
                            design.setText(c.getString(4));
                        }


                        /**
                         * get date modif
                         */

                        datech.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar cal = Calendar.getInstance();
                                int Year = cal.get(Calendar.YEAR);
                                int Month = cal.get(Calendar.MONTH);
                                int Day = cal.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog dialogDate = new DatePickerDialog(mes_charges.this
                                        , android.R.style.Theme_Holo_Dialog_MinWidth
                                        , DateSetListenerche, Year, Month, Day);

                                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogDate.show();

                            }
                        });

                        DateSetListenerche = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                String datefin = dayOfMonth + "/" + month + "/" + year;
                                datech.setText(datefin);
                            }
                        };


                        confirm_modifier = (Button) dyaloge_modifier_mes_charges.findViewById(R.id.btn_confirmer);
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
                                                    modpay = "";
                                                    if (espéce.isChecked()) {
                                                        modpay = "espéce";
                                                    }
                                                    if (chéque.isChecked()) {
                                                        modpay = modpay + " ,chéque";
                                                    }
                                                    if (virment.isChecked()) {
                                                        modpay = modpay + " ,virment";
                                                    }

                                                    if (!espéce.isChecked() && !chéque.isChecked() && !virment.isChecked()) {
                                                        modpay = "Crèdit";
                                                    }

                                                    SQLiteDatabase DB = db.getWritableDatabase();
                                                    ContentValues v = new ContentValues();
                                                    v.put("Date", datech.getText().toString());
                                                    v.put("Montant", Integer.parseInt(montant.getText().toString()));
                                                    v.put("Payment", modpay);
                                                    v.put("designation", design.getText().toString());

                                                    DB.update("Charge", v, "Id_Charge=? and login =?", new String[]{id_charge.getText().toString(),login});

                                                    Toast.makeText(mes_charges.this, "Modification Réussi", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    startActivity(getIntent());

                                                } catch (Exception Ex) {
                                                    Toast.makeText(mes_charges.this, "Modification n'est pas Effectué", Toast.LENGTH_SHORT).show();
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
                TextView close;
                close = (TextView) dyaloge_modifier_supprimer_mes_charges.findViewById(R.id.text_close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dyaloge_modifier_supprimer_mes_charges.dismiss();
                    }
                });

                dyaloge_modifier_supprimer_mes_charges.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dyaloge_modifier_supprimer_mes_charges.show();
            }
        });
        MyDyalog_detaille = new Dialog(this);
        ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                MyDyalog_detaille = new Dialog(mes_charges.this);
                MyDyalog_detaille.setContentView(R.layout.detaile_charge);

                MyDyalog_detaille.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                final TextView text1, text2, text3, text4;

                text1 = (TextView) MyDyalog_detaille.findViewById(R.id.text_DateCharge);
                text2 = (TextView) MyDyalog_detaille.findViewById(R.id.text_mntCharge);
                text3 = (TextView) MyDyalog_detaille.findViewById(R.id.text_payment);
                text4 = (TextView) MyDyalog_detaille.findViewById(R.id.text_desCharge);
                final TextView id_charge;

                id_charge = (TextView) arg1.findViewById(R.id.Id_charge);

                SQLiteDatabase table = db.getReadableDatabase();
                String requet = "SELECT * FROM  Charge where Id_Charge = '" + id_charge.getText().toString() + "' and login ='"+login+"'";

                Cursor c = table.rawQuery(requet, null);
                while (c.moveToNext()) {
                    text1.setText(text1.getText() + " " + c.getString(1));
                    text2.setText(text2.getText() + " " + c.getString(2));
                    text3.setText(text3.getText() + " " + c.getString(3));
                    text4.setText(text4.getText() + " " + c.getString(4));

                }
                TextView close;
                close = (TextView) MyDyalog_detaille.findViewById(R.id.text_close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDyalog_detaille.dismiss();
                    }
                });








                MyDyalog_detaille.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                MyDyalog_detaille.show();


                return true;
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
                T = new Intent(this, assurances.class);
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

        }
        return false;
    }

    public void confirmer(View view) {
        final Dialog dyalog_mes_charges;
        dyalog_mes_charges = new Dialog(this);
        dyalog_mes_charges.setContentView(R.layout.dialoge_ajoute_mes_charges);

        datech = (TextView) dyalog_mes_charges.findViewById(R.id.text_datecha);
        montant = (TextView) dyalog_mes_charges.findViewById(R.id.text_Montant);
        espéce = (CheckBox) dyalog_mes_charges.findViewById(R.id.mode_espéce);
        chéque = (CheckBox) dyalog_mes_charges.findViewById(R.id.mode_chèque);
        virment = (CheckBox) dyalog_mes_charges.findViewById(R.id.mode_virement);
        design = (TextView) dyalog_mes_charges.findViewById(R.id.text_design);


        /**
         * get date ajoute
         */
        datech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(mes_charges.this
                        , android.R.style.Theme_Holo_Dialog_MinWidth
                        , DateSetListenerche1, Year, Month, Day);

                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        DateSetListenerche1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String datefin = dayOfMonth + "/" + month + "/" + year;
                datech.setText(datefin);
            }
        };


        Button confirmer;
        confirmer = (Button) dyalog_mes_charges.findViewById(R.id.btn_confirmer);
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (espéce.isChecked()) {
                    modpay = "espéce";
                }
                if (chéque.isChecked()) {
                    modpay = modpay + " ,chéque";
                }
                if (virment.isChecked()) {
                    modpay = modpay + " ,virment";
                }
                if (!espéce.isChecked() && !chéque.isChecked() && !virment.isChecked()) {
                    modpay = "Crèdit";
                }
                boolean c = db.insert_charge(datech.getText().toString(), Integer.parseInt(montant.getText().toString()), modpay.toString(), design.getText().toString(),login);
                if (c) {
                    Toast.makeText(mes_charges.this, "l'ajoute Reussi", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(mes_charges.this, "Erreur d'ajoute", Toast.LENGTH_LONG).show();
                }
            }
        });


        dyalog_mes_charges.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dyalog_mes_charges.show();


    }


    public void recherche(View view) {

        LinearLayout propLayout = (LinearLayout) findViewById(R.id.layout_recherche);
        Button btnrecherche = (Button) findViewById(R.id.visiblecharche);
        btnrecherche.setVisibility(View.INVISIBLE);
        if (propLayout.getVisibility() == View.VISIBLE) {
            propLayout.setVisibility(View.INVISIBLE);
        } else {
            propLayout.setVisibility(View.VISIBLE);
        }


    }

    /**
     *
     * recherche entre deux date
     * @throws ParseException
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void rechercheEntreDeuxDate(String login) throws ParseException {

        mChart.notifyDataSetChanged();


        ArrayList<list_charge> arrayList2;
        arrayList2 = new ArrayList<list_charge>();

        SQLiteDatabase table = db.getReadableDatabase();
        String requet = "SELECT * FROM Charge where login ='"+login+"' ORDER BY date(Date) DESC";


            Cursor c = table.rawQuery(requet, null);


            Calendar datecalendar = Calendar.getInstance();
            final int alarmYear = datecalendar.get(Calendar.YEAR);
            final int alarmMonth = datecalendar.get(Calendar.MONTH);
            String dateYear1 =""+alarmYear;
            String dateMonth1 =""+alarmMonth;


            String dateYearcon1 = null;
            String dateMonthcon1 = null;
            String dateDaycon1 = null;
            int day = 0;
            double prixx = 0;





            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateEditext = Recherche.getText().toString();
            String dateEditext1 = Recherche1.getText().toString();
            Date date = null;
            Date date1 = null;

            date = sdf.parse(dateEditext);
            date1 = sdf.parse(dateEditext1);

            //   Toast.makeText(this, ""+date, Toast.LENGTH_SHORT).show();

            Date date3;
            int i = 0;

            Integer test1 = 0;

            Date date5;
            Date date4;


            while (c.moveToNext()) {
                date3 = null;
                date5 = null;
                date4 = null;
                prixx = 0;

                date3 = sdf.parse(c.getString(1));

                //Toast.makeText(this, ""+date3, Toast.LENGTH_SHORT).show();
                dateYearcon1 = c.getString(1).split("/")[2];
                dateMonthcon1= c.getString(1).split("/")[1];
                dateDaycon1  = c.getString(1).split("/")[0];


                if (date3.after(date) && date3.before(date1)) {

                    /**
                     * test test test test test test ------------------
                     */

                    String requet2 = "select * from Charge where login ='"+login+"' ORDER BY date(Date) DESC";
                    Cursor c2 = table.rawQuery(requet2, null);
                    test1 = 0;
                    try {
                        date4 = sdf.parse(c.getString(1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    while (c2.moveToNext()) {
                        try {
                            date5 = sdf.parse(c2.getString(1));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (date5.compareTo(date4) == 0) {
                            test1 = test1 + Integer.parseInt(c2.getString(2));
                        }

                    }

                    prixx = test1;

                    day = Integer.parseInt(dateDaycon1);
                    //prixx = Integer.parseInt(c.getString(2));
                    //Toast.makeText(this, ""+day+" "+prixx, Toast.LENGTH_SHORT).show();
                    // yValues.add(new Entry(day, prixx));

                    boolean repitition = false;

                    for (int ii=0 ;ii <dateshh.size();ii++){

                        if(dateshh.get(ii).equals(c.getString(1)) ){
                            repitition =true;
                        }

                    }

                    if(repitition == false){

                        if(c.getCount() != 0){
                            dateshh.add(c.getString(1));

                            allAmountsss.add(prixx);
                        }

                    }



                    i++;
                    list_charge list = new list_charge(c.getString(0), c.getString(4), c.getString(2), c.getString(3), c.getString(1));
                    arrayList2.add(list);
                    // Log.i("TAG", "message "+c.getString(0)+"  "+c.getString(4)+"  "+c.getString(2)+"  "+c.getString(3)+"  "+c.getString(1));
                }

            }

            if(dateshh != null && allAmountsss != null) {
                renderData(dateshh, allAmountsss);
            }


            Collections.reverse(arrayList2);
            Page_Adapter_charge adapter_vihucle1 = new Page_Adapter_charge(mes_charges.this, arrayList2);

            if (i > 0) {
                ls.setAdapter(adapter_vihucle1);

            } else {
                ls.setAdapter(adapter_vihucle);
            }



    }

    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public void renderData(List<String> dates, List<Double> allAmounts) {

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("1");
        xAxisLabel.add("7");
        xAxisLabel.add("14");
        xAxisLabel.add("21");
        xAxisLabel.add("28");
        xAxisLabel.add("30");

        XAxis xAxis = mChart.getXAxis();
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        xAxis.enableGridDashedLine(2f, 7f, 0f);
        xAxis.setAxisMaximum(5f);
        xAxis.setAxisMinimum(0f);
        xAxis.setLabelCount(6, true);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(7f);
        xAxis.setLabelRotationAngle(315f);

        xAxis.setValueFormatter(new ClaimsXAxisValueFormatter(dates));

        xAxis.setCenterAxisLabels(true);


        xAxis.setDrawLimitLinesBehindData(true);


        LimitLine ll2 = new LimitLine(35f, "");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setLineColor(Color.parseColor("#FFFFFF"));


        YAxis leftAxis = mChart.getAxisLeft();


        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);
        //XAxis xAxis = mBarChart.getXAxis();
        leftAxis.setValueFormatter(new ClaimsYAxisValueFormatter());

        mChart.getDescription().setEnabled(true);
        Description description = new Description();
        // description.setText(UISetters.getFullMonthName());
        // commented for weekly reporting
        description.setText("Week");
        description.setTextSize(15f);
        mChart.getDescription().setPosition(0f, 0f);
        mChart.setDescription(description);
        mChart.getAxisRight().setEnabled(false);

        //setData()-- allAmounts is data to display;
        setDataForWeeksWise(allAmounts);

    }

    private void setDataForWeeksWise(List<Double> amounts) {

        ArrayList<Entry> values = new ArrayList<>();

        int x=0;
        for(int i=0; i< amounts.size(); i++){
            x=i+1;
            values.add(new Entry(x, amounts.get(i).floatValue()));
        }


        //values.add(new Entry(1, amounts.get(0).floatValue()));
        //values.add(new Entry(2, amounts.get(1).floatValue()));
        //values.add(new Entry(3, amounts.get(2).floatValue()));
        //values.add(new Entry(4, amounts.get(3).floatValue()));


        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Total volume");
            set1.setDrawCircles(true);
            set1.enableDashedLine(10f, 0f, 0f);
            set1.enableDashedHighlightLine(10f, 0f, 0f);
            set1.setColor(getResources().getColor(R.color.green));
            set1.setCircleColor(getResources().getColor(R.color.green));
            set1.setLineWidth(2f);//line size
            set1.setCircleRadius(5f);
            set1.setDrawCircleHole(true);
            set1.setValueTextSize(10f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(5f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(5.f);

            if (Utils.getSDKInt() >= 18) {
//                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.blue_bg);
//                set1.setFillDrawable(drawable);
                set1.setFillColor(Color.WHITE);

            } else {
                set1.setFillColor(Color.WHITE);
            }
            set1.setDrawValues(true);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);

            mChart.setData(data);
        }
    }
    //
    //
    //contuner pdf

    private static File copyFileFromAssetsToDownloads(final Context context, final String assetFolder, final String fileName)
            throws IOException {
        /* utilisation du répertoire download pour pour que l'application externe puisse lire le fichier sinon ça semble coincer. */
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName);
        if (f.exists())
            //noinspection ResultOfMethodCallIgnored
            f.delete();
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = context.getAssets().open(assetFolder + fileName);
            out = new FileOutputStream(f);
            int read;
            final byte[] buffer = new byte[4096];
            while ((read = in.read(buffer)) > 0)
                out.write(buffer, 0, read);
        } catch (IOException ioe) {
            throw new IOException(ioe);
        } finally {
            if (out != null)
                out.close();
            if (in != null)
                in.close();
        }
        return f;
    }
    


}