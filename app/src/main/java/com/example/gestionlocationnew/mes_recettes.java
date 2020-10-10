package com.example.gestionlocationnew;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class mes_recettes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "mes_recettes";

    private LineChart mChart;

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
    TextView totale;
    EditText Recherche,Recherche1;

    String Cintext;

    ArrayList<Entry> yValues =new ArrayList<>();

    String Idd,cin,Matr,datedb,datefn,nbjour,prix,Typ_Payment,prix_01;


    Float NbRate;



    List<String> dateshh;
    List<Double> allAmountsss;



    private DatePickerDialog.OnDateSetListener mDateSetListenerrecherche1;
    private DatePickerDialog.OnDateSetListener mDateSetListenerrecherche2;

    private DatePickerDialog.OnDateSetListener mDateSetListenerdebut;
    private DatePickerDialog.OnDateSetListener mDateSetListenerfin;


    private DatePickerDialog.OnDateSetListener mDateSetListenermodif;
    private DatePickerDialog.OnDateSetListener mDateSetListenermodif1;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_recettes);

        db = new gestion_location(this);


        /**
         * create CHART -------------------------------------------
         */



        mChart = findViewById(R.id.Linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);


        LimitLine ll1 = new LimitLine(30f,"Title");
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




        /**
         *   //testtt
         */


        mChart.getDescription().setEnabled(true);
        Description description = new Description();
        // description.setText(UISetters.getFullMonthName());//commented for weekly reporting
        description.setText("Jour");
        description.setTextSize(15f);
        mChart.getDescription().setPosition(0f, 0f);
        mChart.setDescription(description);
        mChart.getAxisRight().setEnabled(false);









        /**
         * initialitation les donner don chart
         */

        SQLiteDatabase table1 = db.getReadableDatabase();
        String requet1 = "SELECT * FROM  Recette ORDER BY date_début ASC";
        Cursor c1 = table1.rawQuery ( requet1, null);


        //LocalDate now1 = LocalDate.now();
        // String dateYear1 = now1.format(DateTimeFormatter.ofPattern("yyyy"));
        //String dateMonth1 = now1.format(DateTimeFormatter.ofPattern("MM"));

        Calendar datecalendar = Calendar.getInstance();
        final int alarmYear = datecalendar.get(Calendar.YEAR);
        int alarmMonth = datecalendar.get(Calendar.MONTH);
        String dateYear1 =  ""+alarmYear;
        alarmMonth++;
        String dateMonth1 =""+alarmMonth;



        String dateYearcon1 =null ;
        String dateMonthcon1 =null;
        String dateDaycon1 =null;
        int day =0;
        double prixx = 0;


        Integer test1 =0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date4  =new Date();
        Date date5 =new Date();



        if(c1.getCount() != 0){
            dateshh =new ArrayList<>();
            allAmountsss =new ArrayList<>();
        }





        while (c1.moveToNext())
        {

            date5 = null;
            date4 = null;
            prixx=0;

            dateYearcon1 =c1.getString(1).split("/")[2];
            dateMonthcon1 = c1.getString(1).split("/")[1];
            dateDaycon1 = c1.getString(1).split("/")[0];

            int dateyy = Integer.parseInt(dateYearcon1);
            int datemm = Integer.parseInt(dateMonthcon1);

            if(dateyy == alarmYear && datemm == alarmMonth){


                /**
                 * test test test test test test ------------------
                 */

                String requet2 = "SELECT * FROM  Recette ORDER BY date_début ASC";
                Cursor c2 = table1.rawQuery ( requet2, null);
                test1 =0;
                try {
                    date4 = new SimpleDateFormat("dd/MM/yyyy").parse(c1.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                while (c2.moveToNext()){



                    try {
                        date5 = new SimpleDateFormat("dd/MM/yyyy").parse(c2.getString(1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(date5.compareTo(date4) == 0){
                        test1 = test1+Integer.parseInt(c2.getString(5));

                    }

                }




                prixx = test1;
                day = Integer.parseInt(dateDaycon1);
                //prixx = Integer.parseInt(c1.getString(5));


                //yValues.add(new Entry(day,prixx));

                boolean repitition = false;

                for (int i=0 ;i <dateshh.size();i++){

                    if(dateshh.get(i).equals(c1.getString(1)) ){
                    repitition =true;
                    }

                }

                if(repitition == false){

                    if(c1.getCount() != 0){
                        dateshh.add(c1.getString(1));

                        allAmountsss.add(prixx);
                    }

                }



            }

        }


        if(dateshh != null && allAmountsss != null){
            renderData(dateshh, allAmountsss);
        }



        Recherche = (EditText)findViewById(R.id.textrecherche);
        Recherche1 = (EditText)findViewById(R.id.textrecherche1);



        /**
         * recherche entre deux date nChqrt
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

                /**
                 * methode recherche
                 */


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
                    rechercheEntreDeuxDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                dateshh.clear();
                allAmountsss.clear();


                try {
                    rechercheEntreDeuxDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });




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

                DatePickerDialog dialogDate = new DatePickerDialog(mes_recettes.this
                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                        ,mDateSetListenerrecherche1,Year, Month,Day);

                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        mDateSetListenerrecherche1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month =month+1;
                String datefin = dayOfMonth+"/"+month+"/"+year;
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

                DatePickerDialog dialogDate = new DatePickerDialog(mes_recettes.this
                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                        ,mDateSetListenerrecherche2,Year, Month,Day);


                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        mDateSetListenerrecherche2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month =month+1;
                String datefin = dayOfMonth+"/"+month+"/"+year;
                Recherche1.setText(datefin);
                Toast.makeText(mes_recettes.this, "Clicker sur le diagramme pour voir le resultats de recherche  ", Toast.LENGTH_LONG).show();


            }
        };




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
        arrayList1 = new ArrayList<list_recette>();
        arrayList1.clear();
        int somme = 0;
        totale =(TextView)findViewById(R.id.ttg);



        // LocalDate now = LocalDate.now();
        //String dateYear = now.format(DateTimeFormatter.ofPattern("yyyy"));
        //String dateMonth = now.format(DateTimeFormatter.ofPattern("MM"));

        String dateYear =  ""+alarmYear;
        String dateMonth =""+alarmMonth;


        //DateTimeFormatter formatterY = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        String dateYearcon =null ;
        String dateMonthcon =null;

        while (c.moveToNext())
        {



            dateYearcon =c.getString(1).split("/")[2];
            dateMonthcon = c.getString(1).split("/")[1];

            int Dateyy =Integer.parseInt(dateYearcon);
            int Datemm =Integer.parseInt(dateMonthcon);


            if( alarmYear == Dateyy  && alarmMonth == Datemm ){
                somme = somme+Integer.parseInt(c.getString(5));
            }


            list_recette list = new list_recette (Integer.parseInt(c.getString(5)),c.getString(0));
            arrayList1.add (list);
        }

        Collections.reverse(arrayList1);
        listeRecet = new PageAdapter_recette ( this, arrayList1 );
        ls.setAdapter(listeRecet);

        totale.setText("Total generale par mois: "+somme+" DH");




        /**
         * on click liste
         */

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Dialog MyDyalog_detaille;
                MyDyalog_detaille = new Dialog(mes_recettes.this);
                MyDyalog_detaille.setContentView(R.layout.dialog_detaille_recette);

                MyDyalog_detaille.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

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
                         * gete date modifier date debute
                         */


                        text4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar cal = Calendar.getInstance();
                                int Year = cal.get(Calendar.YEAR);
                                int Month = cal.get(Calendar.MONTH);
                                int Day = cal.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog dialogDate = new DatePickerDialog(mes_recettes.this
                                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                                        ,mDateSetListenermodif,Year, Month,Day);

                                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogDate.show();

                            }
                        });

                        mDateSetListenermodif = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month =month+1;
                                String datefin = dayOfMonth+"/"+month+"/"+year;
                                text4.setText(datefin);
                            }
                        };




                        /**
                         * gete date modifier date debute
                         */


                        text5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar cal = Calendar.getInstance();
                                int Year = cal.get(Calendar.YEAR);
                                int Month = cal.get(Calendar.MONTH);
                                int Day = cal.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog dialogDate = new DatePickerDialog(mes_recettes.this
                                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                                        ,mDateSetListenermodif1,Year, Month,Day);

                                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogDate.show();

                            }
                        });

                        mDateSetListenermodif1 = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month =month+1;
                                String datefin = dayOfMonth+"/"+month+"/"+year;
                                text5.setText(datefin);
                            }
                        };




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

                TextView close;
                close = (TextView) MyDyalog_detaille.findViewById(R.id.text_close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDyalog_detaille.dismiss();
                    }
                });



                /**
                 * Code Of Rate Client ¤ ¤ ¤ ¤ ¤
                 */





                LinearLayout DownLayout = (LinearLayout)MyDyalog_detaille.findViewById(R.id.downLayout);

                DownLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        LinearLayout contentLayout = (LinearLayout)MyDyalog_detaille.findViewById(R.id.contentLayout);
                        contentLayout.setVisibility(View.VISIBLE);

                        /**
                         * return value of Rating
                         */


                        RatingBar rateBAre = (RatingBar)MyDyalog_detaille.findViewById(R.id.ratingBar);
                        rateBAre.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                NbRate = rateBAre.getRating();




                                LinearLayout descLayout = (LinearLayout)MyDyalog_detaille.findViewById(R.id.descLayout);
                                descLayout.setVisibility(view.VISIBLE);

                                LinearLayout downLayout = (LinearLayout)MyDyalog_detaille.findViewById(R.id.downLayout);
                                downLayout.setVisibility(view.INVISIBLE);


                                ImageView upImage = (ImageView)MyDyalog_detaille.findViewById(R.id.downImage);
                                upImage.setImageResource(0);

                                Button ratingBTN = MyDyalog_detaille.findViewById(R.id.ratingBTN);
                                ratingBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        EditText descript = (EditText)MyDyalog_detaille.findViewById(R.id.descript);

                                        /**
                                         * Save Rate of Client in database
                                         */
                                        Calendar datecalendar = Calendar.getInstance();
                                        final int alarmYear = datecalendar.get(Calendar.YEAR);
                                        final int alarmMonth = datecalendar.get(Calendar.MONTH);
                                        final int alarmday = datecalendar.get(Calendar.DAY_OF_MONTH);
                                        String Dateformat =alarmday+"/"+alarmMonth+"/"+alarmYear;



                                        boolean c = db.insert_feedback(cin,NbRate,descript.getText().toString(),Dateformat);
                                        if (c) {
                                            Toast.makeText(mes_recettes.this, "L'évaluation a réussi", Toast.LENGTH_LONG).show();
                                            finish();
                                            startActivity(getIntent());
                                        } else {
                                            Toast.makeText(mes_recettes.this, "Erreur", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });

                            }
                        });









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
                    Collections.reverse(arrayList2);
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

    /**
     * Ajouter nouveaux Recettes
     * @param view
     */


    @RequiresApi(api = Build.VERSION_CODES.O)
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


/*
        RatingBar ratingBar = (RatingBar) MyDyalog_ajou.findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
*/



        /**
         * gete date debute
         */

        text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(mes_recettes.this
                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                        ,mDateSetListenerdebut,Year, Month,Day);

                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        mDateSetListenerdebut = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month =month+1;
                String datefin = dayOfMonth+"/"+month+"/"+year;
                text6.setText(datefin);
            }
        };



        /**
         * gete date fin
         */

        text7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(mes_recettes.this
                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                        ,mDateSetListenerfin,Year, Month,Day);

                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        mDateSetListenerfin = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month =month+1;
                String datefin = dayOfMonth+"/"+month+"/"+year;
                text7.setText(datefin);


                /**
                 * get nombre jour
                 */

                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();

                String[] datettt = text6.getText().toString().split("/");
                int daynb = Integer.parseInt(datettt[0]);
                int monthnb = Integer.parseInt(datettt[1]);
                int yearnb = Integer.parseInt(datettt[2]);

                String[] datetttt = text7.getText().toString().split("/");
                int daynb1 = Integer.parseInt(datetttt[0]);
                int monthnb1 = Integer.parseInt(datetttt[1]);
                int yearnb1 = Integer.parseInt(datetttt[2]);



                cal1.set(yearnb, monthnb, daynb);
                cal2.set(yearnb1, monthnb1, daynb1);
                long milis1 = cal1.getTimeInMillis();
                long milis2 = cal2.getTimeInMillis();
                long diff = milis2 - milis1;
                long days = diff / (24 * 60 * 60 * 1000);


                text8.setText(days+"");







            }
        };








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
                Cintext = text3.getText().toString().toLowerCase();
                String requet = "select * from Clients where cin ='"+Cintext+"'";
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



                /**
                 * return feedback
                 */

                GetFeesBAck(Cintext,MyDyalog_ajou);




            }
        });

        Button btn_ajoute;
        btn_ajoute = (Button) MyDyalog_ajou.findViewById(R.id.btn_ajouterClient);
        btn_ajoute.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                String nom = text1.getText().toString();
                String prenom = text2.getText().toString();
                String adr = text13.getText().toString();
                String cin = text3.getText().toString();
                String tele = text4.getText().toString();
                String activ = text5.getText().toString();
                String ddate_debut = text6.getText().toString();
                String ddate_fin = text7.getText().toString();
                int NB_jour = Integer.parseInt(text8.getText().toString());
                int prix = Integer.parseInt(text9.getText().toString());
                int prix_total = Integer.parseInt(text8.getText().toString()) * Integer.parseInt(text9.getText().toString());
                String type = sp.getSelectedItem().toString();
                String matricule = text11.getSelectedItem().toString();
                String CinCl = text3.getText().toString();



                int count = 0;
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date(System.currentTimeMillis());
                String[] parts = dateFormat.format(date).split("/");

                String part1year = parts[0];
                String part2month = parts[1];
                String part3day = parts[2];

                SQLiteDatabase table = db.getReadableDatabase();
                String requet = "SELECT Id_Recette FROM  Recette";
                Cursor c = table.rawQuery ( requet, null );


                String input = null;     //input string
                String firstFourChars = "";     //substring containing first 4 characters

                if(c.moveToLast()){

                    input = null;
                    input = c.getString(0);
                    firstFourChars = input.substring(0, 2);



                    String[] parts1 = c.getString(0).split("-");
                    String part22 = parts1[1];

                    if( Integer.parseInt(firstFourChars) == Integer.parseInt(part3day) ){
                        count = Integer.parseInt(part22);
                    }else {
                        count = 0;
                    }

                }
                count++;

                String s =part3day+""+part2month+""+part1year+"-"+count;

                Toast.makeText(mes_recettes.this, ""+s, Toast.LENGTH_SHORT).show();

                /**
                 * function pour ajouter nouveaux recettes
                 */
                 Ajouter_recettes(nom,prenom,adr,cin,tele,activ,s,ddate_debut,ddate_fin,NB_jour,prix,prix_total,type,matricule,CinCl);

                MyDyalog_ajou.dismiss();
                finish();
                startActivity(getIntent());

            }

        });
    //en click feedback
        LinearLayout l=(LinearLayout) MyDyalog_ajou.findViewById(R.id.feedbackLayout);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dilogfeed;
                dilogfeed = new Dialog(mes_recettes.this);
                dilogfeed.setContentView(R.layout.dialog_fedeback);
ListView ls1=(ListView)dilogfeed.findViewById(R.id.listefed) ;

                SQLiteDatabase table = db.getReadableDatabase ();

                String requet = "select * from feedback where cin ='"+Cintext+"'";
                Cursor c1 = table.rawQuery ( requet, null );
             ArrayList arrayList3 = new ArrayList<liste_feedback>();
                arrayList3.clear();
              while (c1.moveToNext()){
                  liste_feedback list = new liste_feedback (Float.parseFloat(c1.getString(2)),c1.getString(4),c1.getString(3));
                  arrayList3.add (list);
              }
                pageAdapeter_feedback pg;

                pg = new pageAdapeter_feedback ( mes_recettes.this, arrayList3 );
                ls1.setAdapter(pg);


                dilogfeed.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dilogfeed.show();
            }
        });


        MyDyalog_ajou.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MyDyalog_ajou.show();
    }














    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList filter_vehicule(){

        ArrayList<String> arrayListfilter  = new ArrayList<String>();
        ArrayList<String> arrayListfinal  = new ArrayList<String>();


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        String requet_reccete ="SELECT Matricule_choisi,date_fin FROM Recette";
        String requet_vehicule ="SELECT immatriculation FROM véhicules";

        SQLiteDatabase table = db.getReadableDatabase ();
        Cursor vehicule = table.rawQuery ( requet_vehicule, null );
        Cursor  reccete = table.rawQuery ( requet_reccete, null );

        Date datenow = null;
        Date datereccete = null;
        try {
            datenow = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        while (vehicule.moveToNext()){
            arrayListfinal.add(vehicule.getString(0));
        }
        while (reccete.moveToNext()){

            datereccete = null;
            try {
                datereccete = df.parse(reccete.getString(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(datereccete.compareTo(datenow) > 0 ){
                arrayListfilter.add(reccete.getString(0));
            }




        }
        arrayListfinal.removeAll(arrayListfilter);

        if(arrayListfinal.size() == 0){
            Toast.makeText(this, "Toutes les vehicule sont réservées", Toast.LENGTH_SHORT).show();
        }

        return arrayListfinal;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void rechercheEntreDeuxDate() throws ParseException {


        ArrayList<list_recette> arrayList2;
        arrayList2 = new ArrayList<list_recette> ();



        SQLiteDatabase table1 = db.getReadableDatabase();
        String requet1 = "SELECT * FROM  Recette ORDER BY date_début ASC";


        Cursor c1 = table1.rawQuery ( requet1, null);



        Calendar datecalendar = Calendar.getInstance();
        final int alarmYear = datecalendar.get(Calendar.YEAR);
        final int alarmMonth = datecalendar.get(Calendar.MONTH);
        String dateYear1 =  ""+alarmYear;
        String dateMonth1 =""+alarmMonth;

        String dateYearcon1 =null ;
        String dateMonthcon1 =null;
        String dateDaycon1 =null;
        int day =0;
        double prixx = 0;

        // yValues.add(new Entry(0,0));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateEditext = Recherche.getText().toString();
        String dateEditext1 = Recherche1.getText().toString();
        Date date = null;
        Date date1 = null;

        date = sdf.parse(dateEditext);
        date1 = sdf.parse(dateEditext1);



        Date date3;
        int i = 0;

        Integer test1 =0;
        Date date4;
        Date date5;



        while (c1.moveToNext())
        {
            date3 = null;
            date5 = null;
            date4 = null;
            prixx=0;

            date3 = sdf.parse(c1.getString(1));

            dateYearcon1 = c1.getString(1).split("/")[2];
            dateMonthcon1= c1.getString(1).split("/")[1];
            dateDaycon1  = c1.getString(1).split("/")[0];

            if(date3.after(date)  &&  date3.before(date1)){

                /**
                 * test test test test test test ------------------
                 */

                String requet2 = "SELECT * FROM  Recette";
                Cursor c2 = table1.rawQuery ( requet2, null);
                test1 =0;
                try {
                    date4 = sdf.parse(c1.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                while (c2.moveToNext()){
                    try {
                        date5 = sdf.parse(c2.getString(1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(date5.compareTo(date4) == 0){
                        test1 = test1+Integer.parseInt(c2.getString(5));
                    }

                }

                prixx = test1;

                day = Integer.parseInt(dateDaycon1);
                //prixx = Integer.parseInt(c1.getString(5));
                //Toast.makeText(this, ""+day+" "+prixx, Toast.LENGTH_SHORT).show();
             //   yValues.add(new Entry(day,prixx));


                boolean repitition = false;

                for (int ii=0 ;ii <dateshh.size();ii++){

                    if(dateshh.get(ii).equals(c1.getString(1)) ){
                        repitition =true;
                    }

                }

                if(repitition == false){

                    if(c1.getCount() != 0){
                        dateshh.add(c1.getString(1));

                        allAmountsss.add(prixx);
                    }

                }




                i++;
                list_recette list = new list_recette (Integer.parseInt(c1.getString(5)),c1.getString(0));
                arrayList2.add (list);


            }

        }


        if(dateshh != null && allAmountsss != null) {
            renderData(dateshh, allAmountsss);
        }



        Collections.reverse(arrayList2);
        PageAdapter_recette   adapter_vihucle1 = new PageAdapter_recette ( this, arrayList2 );


        if (i > 0) {
            ls.setAdapter(adapter_vihucle1);

        } else {
            ls.setAdapter(listeRecet);
        }



    }


    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
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
        // description.setText(UISetters.getFullMonthName());//commented for weekly reporting
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

    public void Ajouter_recettes(String nom , String prenom, String adr, String cin, String tel, String activi ,String IDrecette , String d1, String d2, int nb, int prix,int pt,String typ,String ma,String ci){



        SQLiteDatabase table1 = db.getReadableDatabase ();
        String requet1 = "select * from Clients where cin ='"+ci+"'";
        Cursor c1 = table1.rawQuery ( requet1, null );
        boolean b = false;
        boolean d = false;

        if (c1.getCount()==0){
            b = db.insert_client(nom,prenom,adr,cin, tel,activi);
            d= db.insert_Recette(IDrecette,d1,d2,nb,prix,pt,typ,ma,ci);
            if (b && d ) {
                Toast.makeText(mes_recettes.this, "l'enregistrement effecuter", Toast.LENGTH_SHORT).show();
            }
        }
        // tester if lclient existe        >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        if(c1.moveToFirst()) {

            if(c1.getString(3).equals(ci)) {
                d= db.insert_Recette(IDrecette,d1,d2,nb,prix,pt,typ,ma,ci);

                if (d) {
                    Toast.makeText(mes_recettes.this, "l'enregistrement effecuter", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mes_recettes.this, "l'enregistrement ne pas effecuter", Toast.LENGTH_SHORT).show();
                }
            }
            if(!c1.getString(3).equals(ci)) {
                b = db.insert_client(nom,prenom,adr,cin, tel,activi);
                d= db.insert_Recette(IDrecette,d1,d2,nb,prix,pt,typ,ma,ci);

                if(b && d ){
                    Toast.makeText(mes_recettes.this, "l'enregistrement effecuter", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mes_recettes.this, "l'enregistrement ne pas effecuter", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }


    public void GetFeesBAck(String GetFeesBAck ,Dialog MyDyalog_ajou){

        LinearLayout feedbackLayout = (LinearLayout)MyDyalog_ajou.findViewById(R.id.feedbackLayout);
        float feeds = 0,TotalFeeds =0;
        int counter = 0,nbdesc = 0;

        String test ="";


        SQLiteDatabase table1 = db.getReadableDatabase ();
        String requet1 = "select * from feedback where cin ='"+Cintext+"'";
        Cursor c1 = table1.rawQuery ( requet1, null );

        if(c1.getCount() !=0) {

            feedbackLayout.setVisibility(View.VISIBLE);

            while (c1.moveToNext()) {
                if (!c1.getString(3).equals("")) {
                    nbdesc++;
                }
                counter++;
                test ="";
                    test = c1.getString(2).toString();

                    try {
                        feeds =feeds+Float.valueOf(test.trim()).floatValue();
                    }
                    catch (NumberFormatException e) {
                        Toast.makeText(mes_recettes.this, "NumberFormatException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

            }



            TotalFeeds = feeds /counter;

            TextView rateText = (TextView)MyDyalog_ajou.findViewById(R.id.rateText);
            rateText.setText(TotalFeeds+"");

            TextView NBText = (TextView)MyDyalog_ajou.findViewById(R.id.feedText);

            DecimalFormat df = new DecimalFormat("#.##");
            String formatted = df.format(nbdesc);


            String feedback =  formatted+" feedback  ";
            NBText.setText(feedback);
        }

    }



}

