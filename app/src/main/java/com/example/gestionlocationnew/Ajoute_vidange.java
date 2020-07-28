package com.example.gestionlocationnew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class Ajoute_vidange extends AppCompatActivity {
    Spinner type_spinner;
    gestion_location db;
    EditText kilomaitrage,matricule;
    String matr;
    EditText Date;
    CheckBox ch1,ch2,ch3;
    Bundle b;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String Nom,Prenom,role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajoute_vidange);
        db = new gestion_location(this);
        type_spinner = (Spinner)findViewById(R.id.spinner_type);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.add("5000");
        arrayAdapter.add("10000");
        arrayAdapter.add("15000");
        type_spinner.setAdapter(arrayAdapter);
        matricule = (EditText)findViewById(R.id.Edit_matricule);
        Date = (EditText)findViewById(R.id.Edit_date);
        kilomaitrage = (EditText)findViewById(R.id.Edit_kilomaitrage);
        ch1 =(CheckBox)findViewById(R.id.filter1);
        ch2 =(CheckBox)findViewById(R.id.filter2);
        ch3 =(CheckBox)findViewById(R.id.filter3);


        b = getIntent().getExtras();
        matr = b.getString ( "Matricule" );
        Nom = b.getString ( "nom" );
        Prenom = b.getString ( "prenom" );
        role = b.getString ( "role" );
        matricule.setText(matr);


        /**
         * gete date effete
         */


        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(Ajoute_vidange.this
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
                Date.setText(datefin);
            }
        };




    }

    public void Ajoute(View view) {
        String choix ="";
        if(ch1.isChecked()){
            choix =" air";
        }
        if(ch2.isChecked()){
            choix=choix+" ,  huile";
        }
        if(ch3.isChecked()){
            choix=choix+" ,  carburant";
        }

        boolean res = db.insert_vidange(matr , Date.getText().toString() , Integer.parseInt(kilomaitrage.getText().toString()) , choix , Integer.parseInt(type_spinner.getSelectedItem().toString()));
        if(res){
            Toast.makeText(this,"Bien Ajoute",Toast.LENGTH_LONG).show();

            Intent I = new Intent(Ajoute_vidange.this, entretiens.class);
            Bundle B = new Bundle();
            B.putString("nom", Nom);
            B.putString("prenom", Prenom);
            B.putString("role", role);
            I.putExtras(B);
            startActivity(I);
            finish();

        }else{
            Toast.makeText(this,"erreur d'ajoute",Toast.LENGTH_LONG).show();
        }



    }
}
