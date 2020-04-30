package com.example.gestionlocationnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Ajoute_vihicule extends AppCompatActivity {
EditText text1,text2,text3,text4,text5,text6,text7;
Spinner spinner;
gestion_location DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajoute_vihicule);
        DB = new gestion_location(this);
        spinner = (Spinner)findViewById(R.id.vihicule_marque);
        ArrayList<String> arrayList  = new ArrayList<String>();
        arrayList.add("Diesel");
        arrayList.add("Essence");
        arrayList.add("Hybride");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arrayList);
        spinner.setAdapter(arrayAdapter);
    }

    public void Confirmer_lajoute(View view) {
        text1 = (EditText)findViewById(R.id.vihicule_Nom);
        text2 = (EditText)findViewById(R.id.vihicule_Date_Circulation);
        text3 = (EditText)findViewById(R.id.vihicule_Imatricule);
        //--marque spinner
        text4 = (EditText)findViewById(R.id.vihicule_Valeur_entrer);
        text5 = (EditText)findViewById(R.id.vihicule_Date_effet);
        text6 = (EditText)findViewById(R.id.vihicule_Date_echeance);
        text7 = (EditText)findViewById(R.id.vihicule_Couleur);
        Boolean result = DB.insert_vehiucle(text1.getText().toString(),text2.getText().toString(),text3.getText().toString(),spinner.getSelectedItem().toString(), Integer.parseInt(text4.getText().toString()),text5.getText().toString(),text6.getText().toString(),text7.getText().toString());
        if(result){
            Toast.makeText(this, "L'ajoute Effectué", Toast.LENGTH_SHORT).show();
            Intent I = new Intent(this,vehicules.class);
            startActivity(I);
        }else{
            Toast.makeText(this, "L'ajoute n'est pas Effectué", Toast.LENGTH_SHORT).show();
        }

    }
}
