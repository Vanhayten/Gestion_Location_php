package com.example.gestionlocationnew;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.divyanshu.colorseekbar.ColorSeekBar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class Ajoute_vihicule extends AppCompatActivity {
    EditText text1, text2, text3, text4, text5, text6, text;
    TextView state;
    ColorSeekBar Colorseek;
    Spinner spinner;
    gestion_location DB;
    String Nom, Prenom, role, login;
    int intColot;


    ProgressBar progressBar;

    private TextView mDisplayDate, dateEffete, mdateechance;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListenereffete;
    private DatePickerDialog.OnDateSetListener mDateSetListenerechance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajoute_vihicule);
        text = (EditText) findViewById(R.id.vihicule_Imatricule);

        progressBar = findViewById(R.id.progressBar1);
        state = (TextView) findViewById(R.id.state);

        SharedPreferences perfs1 = getSharedPreferences("perfs1", MODE_PRIVATE);
        boolean firststart1 = perfs1.getBoolean("firststart1", true);
        if (firststart1) {
            DialogFirstuse();

        }

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmerMatricule(text.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Colorseek = findViewById(R.id.vihicule_Couleur);
        Colorseek.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int i) {
                intColot = i;
            }
        });


        Bundle b = getIntent().getExtras();
        Nom = b.getString("nom");
        Prenom = b.getString("prenom");
        role = "" + b.getString("role");
        login = "" + b.getString("login");


        DB = new gestion_location(this);
        /**
         * remplire spinner
         */
        spinner = (Spinner) findViewById(R.id.vihicule_marque);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Diesel");
        arrayList.add("Essence");
        arrayList.add("Hybride");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayList);
        spinner.setAdapter(arrayAdapter);


        /**
         * get date circulation
         */
        mDisplayDate = (EditText) findViewById(R.id.vihicule_Date_Circulation);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(Ajoute_vihicule.this
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
                mDisplayDate.setText(datefin);
            }
        };


        /**
         * gete date effete
         */

        mdateechance = (EditText) findViewById(R.id.vihicule_Date_echeance);
        mdateechance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(Ajoute_vihicule.this
                        , android.R.style.Theme_Holo_Dialog_MinWidth
                        , mDateSetListenerechance, Year, Month, Day);

                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        mDateSetListenerechance = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String datefin = dayOfMonth + "/" + month + "/" + year;
                mdateechance.setText(datefin);
            }
        };


        /**
         * gete date echance
         */

        dateEffete = (EditText) findViewById(R.id.vihicule_Date_effet);
        dateEffete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(Ajoute_vihicule.this
                        , android.R.style.Theme_Holo_Dialog_MinWidth
                        , mDateSetListenereffete, Year, Month, Day);

                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        mDateSetListenereffete = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String datefin = dayOfMonth + "/" + month + "/" + year;
                dateEffete.setText(datefin);
            }
        };


    }

    public void Confirmer_lajoute(View view) {
        text1 = (EditText) findViewById(R.id.vihicule_Nom);
        text2 = (EditText) findViewById(R.id.vihicule_Date_Circulation);
        text3 = (EditText) findViewById(R.id.vihicule_Imatricule);
        //--marque spinner
        text4 = (EditText) findViewById(R.id.vihicule_Valeur_entrer);
        text5 = (EditText) findViewById(R.id.vihicule_Date_effet);
        text6 = (EditText) findViewById(R.id.vihicule_Date_echeance);

        if (isConnected(Ajoute_vihicule.this)) {


            if (!TextUtils.isEmpty(text1.getText().toString()) && !TextUtils.isEmpty(text2.getText().toString()) && !TextUtils.isEmpty(text3.getText().toString()) && !TextUtils.isEmpty(text4.getText().toString())) {


                if (intColot != 0) {
                    if (confirmerMatricule(text3.getText().toString()) == false) {


                        AddTask myTask= new AddTask();
                        //start asynctask
                        myTask.execute();


                        /**
                         * need to add database to mysql
                         */


                    } else {
                        Snackbar.make(view, "Respecte la forme de la matricule", Snackbar.LENGTH_SHORT).show();


                    }
                } else {
                    Snackbar.make(view, "Merci de choisire la couleur de la vihicule", Snackbar.LENGTH_SHORT).show();

                }

            } else {
                Snackbar.make(view, "les champs obligatoire", Snackbar.LENGTH_SHORT).show();
            }


        } else {
            buildDialog();
        }

    }

    public void DialogFirstuse() {

        new AlertDialog.Builder(this)
                .setTitle("Ce Messages il s'affiche une seule fois !!")
                .setMessage("veuillez remplir les champs de véhicule  pour consulter les données de votre véhicule prochainement très facilement")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

        SharedPreferences perfs = getSharedPreferences("perfs1", MODE_PRIVATE);
        SharedPreferences.Editor editor = perfs.edit();
        editor.putBoolean("firststart1", false);
        editor.apply();
    }

    boolean k = false;

    public boolean confirmerMatricule(String mat) {
        Pattern con = Pattern.compile("[0-9]{5}-[A-Z]{1}-[0-9]{2}");
        Pattern con1 = Pattern.compile("[0-9]{4}-[A-Z]{1}-[0-9]{2}");
        Pattern con2 = Pattern.compile("[0-9]{3}-[A-Z]{1}-[0-9]{2}");
        if (!con.matcher(mat).find()) {
            k = true;
            text.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Red), PorterDuff.Mode.SRC_ATOP);
        } else if (con.matcher(mat).find()) {
            text.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Green), PorterDuff.Mode.SRC_ATOP);
            k = false;
        }
        if (!con1.matcher(mat).find()) {
            text.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Red), PorterDuff.Mode.SRC_ATOP);
            k = true;
        } else if (con1.matcher(mat).find()) {
            k = false;
            text.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Green), PorterDuff.Mode.SRC_ATOP);
        }
        if (!con2.matcher(mat).find()) {
            k = true;
            text.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Red), PorterDuff.Mode.SRC_ATOP);
        } else if (con2.matcher(mat).find()) {
            k = false;
            text.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Green), PorterDuff.Mode.SRC_ATOP);
        }
        return k;
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button refrech = dialogView.findViewById(R.id.buttonOk);
        refrech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected(Ajoute_vihicule.this)) {
                    alertDialog.dismiss();
                } else {

                    final Animation myAnim = AnimationUtils.loadAnimation(Ajoute_vihicule.this, R.anim.bounce);

                    // Use bounce interpolator with amplitude 0.2 and frequency 20
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 30);
                    myAnim.setInterpolator(interpolator);

                    refrech.startAnimation(myAnim);

                    Toast.makeText(Ajoute_vihicule.this, "No Internet", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }







    class AddTask extends AsyncTask<String,String,Boolean>
    {
        /* Params: 1000 : Integer
           Progress: message content : String
           Result: true : Boolean
         */
        boolean checker;
        View parentLayout = findViewById(android.R.id.content);

        @Override
        protected void onPreExecute()
        {
            //Start ProgressBar first (Set visibility VISIBLE)
            progressBar.setVisibility(View.VISIBLE);

            checker =false;
        }
        @Override
        protected Boolean doInBackground(String... params)
        {
            checker = false;

            publishProgress("open connection");

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());


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


                    //Creating array for data
                    String[] data = new String[10];
                    data[0] = text1.getText().toString();
                    data[1] = text2.getText().toString();
                    data[2] = text3.getText().toString();
                    data[3] = spinner.getSelectedItem().toString();
                    data[4] = text4.getText().toString();
                    data[5] = text5.getText().toString();
                    data[6] = text6.getText().toString();
                    data[7] = intColot+"";
                    data[8] = login;
                    data[9] =  ""+timestamp;

                    //  Log.i("#########",text1.getText().toString()+"  "+text2.getText().toString()+"  "+text3.getText().toString()+"  "+spinner.getSelectedItem().toString()+"  "+text4.getText().toString()+"  "+text5.getText().toString()+"  "+text6.getText().toString()+"   "+intColot+""+"  "+login );

                    String host = getResources().getString(R.string.hosting);
                    PutData putData = new PutData(host+"/gesloc/vehicules/insertvehicule.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();

                            publishProgress("Start Read");


                            if(result.equals("Add Success")){

                                /**
                                 * add database in local
                                 */
                                Boolean resulta = null;
                                try {
                                    resulta = DB.insert_vehiucle(text1.getText().toString(), text2.getText().toString(), text3.getText().toString(), spinner.getSelectedItem().toString(), Integer.parseInt(text4.getText().toString()), text5.getText().toString(), text6.getText().toString(), intColot, login);

                                } catch (Exception E) {

                                }
                                if (resulta) {

                                    publishProgress("L'ajoute Effectué");
                                    checker =true;

                                } else {
                                    checker = false;

                                    //  Snackbar.make(view, "L'ajoute n'est pas Effectué", Snackbar.LENGTH_SHORT).show();
                                }

                            }else{

                                checker = false;
                                //Toast.makeText(Ajoute_vihicule.this, result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    //End Write and Read data with URL
                }
            });



            //-----------------------------------------------------------------------


            return checker;
        }
        @Override
        protected void onProgressUpdate(String... progress)
        {
            //setting text
           // state.setText(progress[0]);
            Snackbar.make(parentLayout, progress[0], Snackbar.LENGTH_SHORT).show();

        }
        @Override
        protected void onPostExecute(Boolean checker1)
        {
            //checking result is true or not
            if(checker){
                Log.i("state","###################### yep");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //snack.dismiss();
                        Intent I = new Intent(Ajoute_vihicule.this, vehicules.class);
                        Bundle b1 = new Bundle();
                        b1.putString("nom", Nom);
                        b1.putString("prenom", Prenom);
                        b1.putString("role", role);
                        b1.putString("login", login);
                        I.putExtras(b1);
                        startActivity(I);
                    }
                }, 5000);




            }else {
                Log.i("state","###################### none");

                Snackbar.make(parentLayout, "L'ajoute n'est pas Effectué", Snackbar.LENGTH_SHORT).show();

            }

            //End ProgressBar (Set visibility to GONE)
            progressBar.setVisibility(View.GONE);
        }
    }




}
