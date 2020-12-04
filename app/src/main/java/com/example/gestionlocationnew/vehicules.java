package com.example.gestionlocationnew;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.divyanshu.colorseekbar.ColorSeekBar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class vehicules extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    GoogleSignInClient mGoogleSignInClient;

    SwipeRefreshLayout mSwipeRefreshLayout;

    String Nom, Prenom, role, login;
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
    Cursor c, c1;

    ProgressBar progressBar;


    int intColot = 0;


    private DatePickerDialog.OnDateSetListener mDateSetListenerdebute;
    private DatePickerDialog.OnDateSetListener mDateSetListenerfin;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onStart() {
        super.onStart();

    }

    CircleImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /**
         * google
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        progressBar = findViewById(R.id.progressBar1);


        NavigationView navigationView1 = (NavigationView) findViewById(R.id.navigationView);
        View headerView = navigationView1.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.unser_name);
        TextView role1 = headerView.findViewById(R.id.role);
        profile = (CircleImageView) headerView.findViewById(R.id.profilpic);


        /**
         * get image from google and gut its in profile
         */
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        String urlsImage = sp.getString("URLImage", "");
        if (!urlsImage.equals("")) {
            ImageLoadTask imageLoadTask = (ImageLoadTask) new ImageLoadTask(urlsImage, profile).execute();
        }


        Bundle b = getIntent().getExtras();
        Nom = b.getString("nom");
        Prenom = b.getString("prenom");
        role = "" + b.getString("role");
        login = "" + b.getString("login");
        username.setText(Nom + " " + Prenom);
        role1.setText(role);


        /**
         * start service on background for sync data
         *
         * before check connection if enable
         */
        if (isConnected(vehicules.this)) {

            Intent serviceIntent = new Intent(vehicules.this, service_vehicule.class);
            serviceIntent.putExtra("Login", login);
            startService(serviceIntent);

        } else {
            //buildDialog();
        }


        db = new gestion_location(this);

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
        ls = (ListView) findViewById(R.id.list1);
        t1 = (EditText) findViewById(R.id.chercherMatr);

        t1.setSelected(false);
        //t1.setFocusable(false);


        /**
         * refrech Layout
         */

        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);

                if (isConnected(vehicules.this)) {

                    Intent serviceIntent = new Intent(vehicules.this, service_vehicule.class);
                    serviceIntent.putExtra("Login", login);
                    startService(serviceIntent);


                    /**
                     * refresh list of vihicule
                     */
                    SQLiteDatabase table1 = db.getReadableDatabase();
                    String requet1 = "select * from véhicules where login ='" + login + "'";
                    Cursor cc = table1.rawQuery(requet1, null);

                    if (cc.getCount() > arrayList.size()) {
                        arrayList.clear();
                        listrep.clear();
                        ls.clearChoices();

                        while (cc.moveToNext()) {

                            list_vihcule list = new list_vihcule(cc.getString(0), cc.getString(2), Integer.parseInt(cc.getString(7)));
                            arrayList.add(list);
                        }
                        Collections.reverse(arrayList);
                        listrep = new PageAdapter_vihucle(vehicules.this, arrayList);
                        listrep.notifyDataSetChanged();
                        ls.setAdapter(listrep);
                    }

                } else {

                    buildDialog();
                }


            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);


                if (isConnected(vehicules.this)) {

                    Intent serviceIntent = new Intent(vehicules.this, service_vehicule.class);
                    serviceIntent.putExtra("Login", login);
                    startService(serviceIntent);


                    /**
                     * refresh list of vihicule
                     */
                    SQLiteDatabase table1 = db.getReadableDatabase();
                    String requet1 = "select * from véhicules where login ='" + login + "'";
                    Cursor cc = table1.rawQuery(requet1, null);

                    if (cc.getCount() > arrayList.size()) {
                        arrayList.clear();
                        listrep.clear();
                        ls.clearChoices();

                        while (cc.moveToNext()) {

                            list_vihcule list = new list_vihcule(cc.getString(0), cc.getString(2), Integer.parseInt(cc.getString(7)));
                            arrayList.add(list);
                        }
                        Collections.reverse(arrayList);
                        listrep = new PageAdapter_vihucle(vehicules.this, arrayList);
                        listrep.notifyDataSetChanged();
                        ls.setAdapter(listrep);
                    }

                } else {

                    buildDialog();
                }
            }
        });


//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.Red, R.color.Orange, R.color.BlueDarck, R.color.Green);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Log.v("TAG", "************** SYNC DATABASE !!!!!");
//
//                /**
//                 * start service on background for sync data
//                 *
//                 * before check connection if enable
//                 */
//                if(isConnected(vehicules.this)){
//
//                    Intent serviceIntent = new Intent(vehicules.this,service_vehicule.class);
//                    serviceIntent.putExtra("Login", login);
//                    startService(serviceIntent);
//
//                }else {
//                    buildDialog();
//                }
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });


        /**
         * animation
         * On first use
         */

        //Animation  fromnav = AnimationUtils.loadAnimation(this,R.anim.fromnav);
        //  navigationView.setAnimation(fromnav);

        t1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Toast.makeText(vehicules.this, "Recherche par Matricule", Toast.LENGTH_SHORT).show();
            }
        });


        // recherche par matrucule
        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ArrayList<list_vihcule> arrayList1;
                SQLiteDatabase table = db.getReadableDatabase();
                String requet = "select * from véhicules where immatriculation ='" + t1.getText() + "' and login ='" + login + "'";
                c = table.rawQuery(requet, null);
                if (c.getCount() >= 1) {
                    ls.clearChoices();
                    arrayList1 = new ArrayList<list_vihcule>();
                    while (c.moveToNext()) {
                        list_vihcule list = new list_vihcule(c.getString(0), c.getString(2), Integer.parseInt(c.getString(7)));
                        arrayList1.add(list);
                    }
                    Collections.reverse(arrayList1);
                    PageAdapter_vihucle adapter_vihucle = new PageAdapter_vihucle(vehicules.this, arrayList1);
                    ls.setAdapter(adapter_vihucle);
                } else {
                    ls.setAdapter(listrep);
                }
//
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //-------------------------

        SQLiteDatabase table = db.getReadableDatabase();

        String requet = "select * from véhicules where login ='" + login + "'";
        c1 = table.rawQuery(requet, null);
                      /*
                   if(c1.getCount()==0){
                        finish();
                       Intent i=new Intent(this,Ajoute_vihicule.class);
                        Bundle b1 = new Bundle();
                        b1.putString("nom",Nom);
                        b1.putString("prenom",Prenom);
                        b1.putString("role",role);
                        i.putExtras(b1);
                       startActivity(i);
                   }
                   */

        arrayList = new ArrayList<list_vihcule>();
        arrayList.clear();

        while (c1.moveToNext()) {

            list_vihcule list = new list_vihcule(c1.getString(0), c1.getString(2), Integer.parseInt(c1.getString(7)));
            arrayList.add(list);
        }
        Collections.reverse(arrayList);
        listrep = new PageAdapter_vihucle(this, arrayList);
        ls.setAdapter(listrep);


        //onclick on listner aficher le dialog

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myDyalog = new Dialog(vehicules.this);
                myDyalog.setContentView(R.layout.dialog_vihicule);

                // myDyalog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

                myDyalog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


                TextView text1, text2, text3, text4, text5, text6, text7, text8, text9;
                Button Suprimer, Modifier;


                text1 = (TextView) myDyalog.findViewById(R.id.text_nom);
                text2 = (TextView) myDyalog.findViewById(R.id.text_matricule);
                text3 = (TextView) myDyalog.findViewById(R.id.text_datecirulation);
                text4 = (TextView) myDyalog.findViewById(R.id.text_marqueCombision);
                text5 = (TextView) myDyalog.findViewById(R.id.text_valeur_entrer);
                text6 = (TextView) myDyalog.findViewById(R.id.text_dateeffet);
                text7 = (TextView) myDyalog.findViewById(R.id.text_dateechance);
                text8 = (TextView) myDyalog.findViewById(R.id.text_couleur);
                text9 = (TextView) myDyalog.findViewById(R.id.text_close);
                Suprimer = (Button) myDyalog.findViewById(R.id.btn_suprimer);
                Modifier = (Button) myDyalog.findViewById(R.id.btn_modifier);


                matr = (TextView) view.findViewById(R.id.matrV);


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
                        final EditText text11, text22, text33, text55, text66, text77;
                        ColorSeekBar Colorseek;
                        ;
                        final Spinner text44;
                        Button Confirmer;

                        AjouteDialog = new Dialog(vehicules.this);
                        AjouteDialog.setContentView(R.layout.dialog_ajoute_vihicule);

                        text11 = (EditText) AjouteDialog.findViewById(R.id.text_nom1);
                        text22 = (EditText) AjouteDialog.findViewById(R.id.text_prenom);
                        text33 = (EditText) AjouteDialog.findViewById(R.id.text_cin);
                        text44 = (Spinner) AjouteDialog.findViewById(R.id.text_marqueCombision1);
                        text55 = (EditText) AjouteDialog.findViewById(R.id.text_tel);
                        text66 = (EditText) AjouteDialog.findViewById(R.id.text_activity);
                        text77 = (EditText) AjouteDialog.findViewById(R.id.text_dateDebut);
                        Colorseek = (ColorSeekBar) AjouteDialog.findViewById(R.id.vihicule_Couleur);
                        Confirmer = (Button) AjouteDialog.findViewById(R.id.btn_modifier1);


                        /**
                         * get date debute
                         */
                        text33.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar cal = Calendar.getInstance();
                                int Year = cal.get(Calendar.YEAR);
                                int Month = cal.get(Calendar.MONTH);
                                int Day = cal.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog dialogDate = new DatePickerDialog(vehicules.this
                                        , android.R.style.Theme_Holo_Dialog_MinWidth
                                        , mDateSetListener, Year, Month, Day);

                                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogDate.show();

                            }
                        });

                        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                String datefin = dayOfMonth + "/" + month + "/" + year;
                                text33.setText(datefin);
                            }
                        };


                        /**
                         * get date debute
                         */
                        text66.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar cal = Calendar.getInstance();
                                int Year = cal.get(Calendar.YEAR);
                                int Month = cal.get(Calendar.MONTH);
                                int Day = cal.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog dialogDate = new DatePickerDialog(vehicules.this
                                        , android.R.style.Theme_Holo_Dialog_MinWidth
                                        , mDateSetListenerdebute, Year, Month, Day);

                                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogDate.show();

                            }
                        });

                        mDateSetListenerdebute = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                String datefin = dayOfMonth + "/" + month + "/" + year;
                                text66.setText(datefin);
                            }
                        };


                        /**
                         * get date debute
                         */
                        text77.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar cal = Calendar.getInstance();
                                int Year = cal.get(Calendar.YEAR);
                                int Month = cal.get(Calendar.MONTH);
                                int Day = cal.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog dialogDate = new DatePickerDialog(vehicules.this
                                        , android.R.style.Theme_Holo_Dialog_MinWidth
                                        , mDateSetListenerfin, Year, Month, Day);

                                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogDate.show();

                            }
                        });

                        mDateSetListenerfin = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                String datefin = dayOfMonth + "/" + month + "/" + year;
                                text77.setText(datefin);
                            }
                        };


                        ArrayList<String> arrayList = new ArrayList<String>();
                        arrayList.add("Diesel");
                        arrayList.add("Essence");
                        arrayList.add("Hybride");
                        ArrayAdapter<String> arraspinner = new ArrayAdapter<String>(vehicules.this, R.layout.support_simple_spinner_dropdown_item, arrayList);
                        text44.setAdapter(arraspinner);

                        SQLiteDatabase table = db.getReadableDatabase();
                        String requet = "select * from véhicules where immatriculation ='" + matr.getText() + "' and login ='" + login + "'";
                        Cursor c = table.rawQuery(requet, null);

                        while (c.moveToNext()) {
                            text11.setText(c.getString(0));
                            text22.setText(c.getString(2));
                            text33.setText(c.getString(1));
                            text55.setText(c.getString(4));
                            text66.setText(c.getString(5));
                            text77.setText(c.getString(6));
                            intColot = Integer.parseInt(c.getString(7));
                        }
                        /**
                         * get color
                         */
                        Colorseek.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                            @Override
                            public void onColorChangeListener(int i) {
                                intColot = i;
                            }
                        });

                        if (isConnected(vehicules.this)) {
                            Confirmer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /**
                                     * confirmation modifier
                                     *
                                     */
                                    AlertDialog.Builder builder = new AlertDialog.Builder(vehicules.this);
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

                                                    //Start ProgressBar first (Set visibility VISIBLE)
                                                    progressBar.setVisibility(View.VISIBLE);
                                                    Handler handler = new Handler(Looper.getMainLooper());
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //Starting Write and Read data with URL
                                                            //Creating array for parameters
                                                            String[] field = new String[10];
                                                            field[0] = "MarqueVihicule";
                                                            field[1] = "DateCirculation";
                                                            field[2] = "immatriculation";
                                                            field[3] = "MarqueCombustion";
                                                            field[4] = "ValeurDentrée";
                                                            field[5] = "Date_Effet_Assurance";
                                                            field[6] = "Date_Echeance";
                                                            field[7] = "Couleur_Vehicule";
                                                            field[8] = "Login";
                                                            field[9] = "on_update";

                                                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                                                            //Creating array for data
                                                            String[] data = new String[10];
                                                            data[0] = text11.getText().toString();
                                                            data[1] = text33.getText().toString();
                                                            data[2] = text22.getText().toString();
                                                            data[3] = text44.getSelectedItem().toString();
                                                            data[4] = text55.getText().toString();
                                                            data[5] = text66.getText().toString();
                                                            data[6] = text77.getText().toString();
                                                            data[7] = intColot + "";
                                                            data[8] = login;
                                                            data[9] = String.valueOf(timestamp);


                                                            String host = getResources().getString(R.string.hosting);
                                                            PutData putData = new PutData(host + "/gesloc/vehicules/updateVehicule.php", "POST", field, data);
                                                            if (putData.startPut()) {
                                                                if (putData.onComplete()) {
                                                                    String result = putData.getResult();

                                                                    if (result.equals("update seccess")) {


                                                                        db.modifier_vihucle(text11.getText().toString(), text33.getText().toString(), text22.getText().toString(), text44.getSelectedItem().toString(), Integer.parseInt(text55.getText().toString()), text66.getText().toString(), text77.getText().toString(), intColot, login, timestamp);
                                                                        Toast.makeText(vehicules.this, "Modification Réussi", Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                        startActivity(getIntent());

                                                                        Log.i("Time_tampe", String.valueOf(timestamp));


                                                                    } else {

                                                                        Toast.makeText(vehicules.this, "Modification n'est pas Effectué", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                    Log.i("data", "#################  " + result);


                                                                }
                                                            }
                                                            //End Write and Read data with URL
                                                        }
                                                    });
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    //--------------------------------

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
                        } else {
                            buildDialog();
                        }
                    }
                });


                Suprimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(vehicules.this);
                        builder.setCancelable(true);
                        builder.setTitle("suppression");
                        builder.setMessage("voullez-vous vraiment suprimer ?");
                        builder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Start ProgressBar first (Set visibility VISIBLE)
                                        progressBar.setVisibility(View.VISIBLE);
                                        Handler handler = new Handler(Looper.getMainLooper());
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Starting Write and Read data with URL
                                                //Creating array for parameters
                                                String[] field = new String[2];
                                                field[0] = "Login";
                                                field[1] = "immatriculation";


                                                //Creating array for data
                                                String[] data = new String[2];
                                                data[0] = login;
                                                data[1] = matr.getText().toString();

                                                String host = getResources().getString(R.string.hosting);
                                                PutData putData = new PutData(host + "/gesloc/vehicules/deleteVehicule.php", "POST", field, data);
                                                if (putData.startPut()) {
                                                    if (putData.onComplete()) {
                                                        String result = putData.getResult();

                                                        if (result.equals("delete seccess")) {
                                                            db.suprimer_vihucle(matr.getText().toString(), login);
                                                            finish();
                                                            startActivity(getIntent());
                                                            myDyalog.dismiss();

                                                            /**
                                                             * delete complete
                                                             */

                                                            View parentLayout = findViewById(android.R.id.content);
                                                            Snackbar snack = Snackbar.make(parentLayout, result, Snackbar.LENGTH_INDEFINITE).setDuration(3000);
                                                            View sbView = snack.getView();
                                                            sbView.setBackgroundColor(getResources().getColor(R.color.color_warning_light_green));
                                                            snack.show();


                                                        }
                                                        Log.i("data", "#################  " + result);


                                                    }
                                                }
                                                //End Write and Read data with URL
                                            }
                                        });
                                        progressBar.setVisibility(View.INVISIBLE);
                                        //--------------------------------


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
                SQLiteDatabase table = db.getReadableDatabase();
                String requet = "select * from véhicules where immatriculation ='" + matr.getText() + "' and login = '" + login + "'";
                Cursor c = table.rawQuery(requet, null);

                while (c.moveToNext()) {
                    text1.setText("Nom Vehicule :  " + c.getString(0));
                    text2.setText("Imatriculation :  " + c.getString(2));
                    text3.setText("Date Circulation :  " + c.getString(1));
                    text4.setText("Marque Combustion :  " + c.getString(3));
                    text5.setText("Valeur d'entrée :  " + c.getString(4));
                    text6.setText("Date Effet Assurance :  " + c.getString(5));
                    text7.setText("Date Echeance :  " + c.getString(6));
                    text8.setTextColor(Integer.parseInt(c.getString(7)));
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

            case R.id.logout:

                /**
                 * Sing out from google
                 */
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(vehicules.this, " déconnecté avec succès", Toast.LENGTH_SHORT).show();
                                // ...
                            }
                        });


                T = new Intent(this, Login.class);
                SharedPreferences sp;
                sp = getSharedPreferences("login", MODE_PRIVATE);
                sp.edit().putBoolean("logged", false).apply();
                startActivity(T);
                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);
                break;
        }
        return false;
    }

    public void Page_Ajoute(View view) {
        Intent Ajouter = new Intent(this, Ajoute_vihicule.class);
        Bundle b = new Bundle();
        b.putString("nom", Nom);
        b.putString("prenom", Prenom);
        b.putString("role", role);
        b.putString("login", login);
        Ajouter.putExtras(b);
        startActivity(Ajouter);
    }


    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    public void buildDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_no_internet, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button refrech = dialogView.findViewById(R.id.buttonOk);
        refrech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected(vehicules.this)) {
                    alertDialog.dismiss();

                    Toast.makeText(vehicules.this, "connected", Toast.LENGTH_SHORT).show();
                } else {

                    final Animation myAnim = AnimationUtils.loadAnimation(vehicules.this, R.anim.bounce);

                    // Use bounce interpolator with amplitude 0.2 and frequency 20
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 30);
                    myAnim.setInterpolator(interpolator);

                    refrech.startAnimation(myAnim);

                    Toast.makeText(vehicules.this, "No Internet", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                }

            }
        });


    }

}


