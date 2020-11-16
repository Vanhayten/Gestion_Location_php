package com.example.gestionlocationnew;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.divyanshu.colorseekbar.ColorSeekBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class Ajoute_vihicule extends AppCompatActivity {
EditText text1,text2,text3,text4,text5,text6,text;
    ColorSeekBar Colorseek;
Spinner spinner;
gestion_location DB;
String Nom,Prenom,role,login;
    int intColot;


    private TextView mDisplayDate,dateEffete,mdateechance;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListenereffete;
    private DatePickerDialog.OnDateSetListener mDateSetListenerechance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajoute_vihicule);
         text = (EditText)findViewById(R.id.vihicule_Imatricule);

        SharedPreferences perfs1 = getSharedPreferences("perfs1",MODE_PRIVATE);
        boolean firststart1 = perfs1.getBoolean("firststart1",true);
        if(firststart1){
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
        Prenom =  b.getString("prenom");
        role = ""+b.getString("role");
        login = ""+b.getString("login");


        DB = new gestion_location(this);
        /**
         * remplire spinner
         */
        spinner = (Spinner)findViewById(R.id.vihicule_marque);
        ArrayList<String> arrayList  = new ArrayList<String>();
        arrayList.add("Diesel");
        arrayList.add("Essence");
        arrayList.add("Hybride");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arrayList);
        spinner.setAdapter(arrayAdapter);


        /**
         * get date circulation
         */
        mDisplayDate =(EditText)findViewById(R.id.vihicule_Date_Circulation);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(Ajoute_vihicule.this
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
                mDisplayDate.setText(datefin);
            }
        };


        /**
         * gete date effete
         */

        mdateechance =(EditText)findViewById(R.id.vihicule_Date_echeance);
        mdateechance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(Ajoute_vihicule.this
                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                        ,mDateSetListenerechance,Year, Month,Day);

                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        mDateSetListenerechance = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month =month+1;
                String datefin = dayOfMonth+"/"+month+"/"+year;
                mdateechance.setText(datefin);
            }
        };


        /**
         * gete date echance
         */

        dateEffete =(EditText)findViewById(R.id.vihicule_Date_effet);
        dateEffete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(Ajoute_vihicule.this
                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                        ,mDateSetListenereffete,Year, Month,Day);

                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        mDateSetListenereffete = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month =month+1;
                String datefin = dayOfMonth+"/"+month+"/"+year;
                dateEffete.setText(datefin);
            }
        };




    }

    public void Confirmer_lajoute(View view) {
        text1 = (EditText)findViewById(R.id.vihicule_Nom);
        text2 = (EditText)findViewById(R.id.vihicule_Date_Circulation);
        text3 = (EditText)findViewById(R.id.vihicule_Imatricule);
        //--marque spinner
        text4 = (EditText)findViewById(R.id.vihicule_Valeur_entrer);
        text5 = (EditText)findViewById(R.id.vihicule_Date_effet);
        text6 = (EditText)findViewById(R.id.vihicule_Date_echeance);


          if(!TextUtils.isEmpty(text1.getText().toString()) && !TextUtils.isEmpty(text2.getText().toString()) && !TextUtils.isEmpty(text3.getText().toString()) && !TextUtils.isEmpty(text4.getText().toString()) ){


        if(intColot != 0){
            if(confirmerMatricule(text3.getText().toString())==false) {

                Boolean result = null;
                try {
                        result = DB.insert_vehiucle(text1.getText().toString(), text2.getText().toString(), text3.getText().toString(), spinner.getSelectedItem().toString(), Integer.parseInt(text4.getText().toString()), text5.getText().toString(), text6.getText().toString(), intColot,login);


                }catch (Exception E){
                   
                }
                if (result) {
                    //Toast.makeText(Ajoute_vihicule.this, "L'ajoute Effectué", Toast.LENGTH_SHORT).show();

                    //Snackbar.make(view, "L'ajoute Effectué", Snackbar.LENGTH_SHORT).show();

                    Snackbar snack = Snackbar.make(view,"L'ajoute Effectué",Snackbar.LENGTH_INDEFINITE);
                    View sbView = snack.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.green));
                    snack.setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snack.dismiss();
                            Intent I = new Intent(Ajoute_vihicule.this, vehicules.class);
                            Bundle b1 = new Bundle();
                            b1.putString("nom", Nom);
                            b1.putString("prenom", Prenom);
                            b1.putString("role", role);
                            b1.putString("login", login);
                            I.putExtras(b1);
                            startActivity(I);
                        }
                    }).setActionTextColor(getResources().getColor(R.color.NAVwhite1)).show();

                } else {

                    Snackbar.make(view, "L'ajoute n'est pas Effectué", Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(Ajoute_vihicule.this, "L'ajoute n'est pas Effectué", Toast.LENGTH_SHORT).show();
                }
            }else{
                Snackbar.make(view, "Respecte la forme de la matricule", Snackbar.LENGTH_SHORT).show();
                //Toast.makeText(Ajoute_vihicule.this, "Respecte la forme de la matricule", Toast.LENGTH_SHORT).show();

            }
        }else{
            Snackbar.make(view, "Merci de choisire la couleur de la vihicule", Snackbar.LENGTH_SHORT).show();
            //Toast.makeText(this, "Merci de choisire la couleur de la vihicule", Toast.LENGTH_SHORT).show();
        }

         }else{
                        Snackbar.make(view, "les champs obligatoire", Snackbar.LENGTH_SHORT).show();
                    }


    }
  public void  DialogFirstuse(){

        new AlertDialog.Builder(this)
                .setTitle("Ce Messages il s'affiche une seule fois !!")
                .setMessage("veuillez remplir les champs de véhicule  pour consulter les données de votre véhicule prochainement très facilement")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

      SharedPreferences perfs = getSharedPreferences("perfs1",MODE_PRIVATE);
      SharedPreferences.Editor editor = perfs.edit();
      editor.putBoolean("firststart1",false);
      editor.apply();
  }
boolean k=false;
  public boolean confirmerMatricule(String mat ){
      Pattern con=Pattern.compile("[0-9]{5}-[A-Z]{1}-[0-9]{2}");
      Pattern con1=Pattern.compile("[0-9]{4}-[A-Z]{1}-[0-9]{2}");
      Pattern con2=Pattern.compile("[0-9]{3}-[A-Z]{1}-[0-9]{2}");
      if(!con.matcher(mat).find() ){
        k=true;
          text.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Red), PorterDuff.Mode.SRC_ATOP);
      }else if(con.matcher(mat).find()) {
          text.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Green), PorterDuff.Mode.SRC_ATOP);
          k=false;
      }
     if(!con1.matcher(mat).find() ){
                 text.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Red), PorterDuff.Mode.SRC_ATOP);
         k=true;
      }else if(con1.matcher(mat).find())  {
         k=false;
         text.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Green), PorterDuff.Mode.SRC_ATOP);
      }
      if(!con2.matcher(mat).find() ){
          k=true;
                  text.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Red), PorterDuff.Mode.SRC_ATOP);
      }else if(con2.matcher(mat).find()){
          k=false;
          text.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Green), PorterDuff.Mode.SRC_ATOP);
      }
return k;
  }


}
