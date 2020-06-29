package com.example.gestionlocationnew;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.divyanshu.colorseekbar.ColorSeekBar;
import com.google.android.material.navigation.NavigationView;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class Ajoute_vihicule extends AppCompatActivity {
EditText text1,text2,text3,text4,text5,text6;
    ColorSeekBar Colorseek;
Spinner spinner;
gestion_location DB;
String Nom,Prenom,role;
    int intColot;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajoute_vihicule);


        SharedPreferences perfs1 = getSharedPreferences("perfs1",MODE_PRIVATE);
        boolean firststart1 = perfs1.getBoolean("firststart1",true);
        if(firststart1){
            DialogFirstuse();

        }



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
    }

    public void Confirmer_lajoute(View view) {
        text1 = (EditText)findViewById(R.id.vihicule_Nom);
        text2 = (EditText)findViewById(R.id.vihicule_Date_Circulation);
        text3 = (EditText)findViewById(R.id.vihicule_Imatricule);
        //--marque spinner
        text4 = (EditText)findViewById(R.id.vihicule_Valeur_entrer);
        text5 = (EditText)findViewById(R.id.vihicule_Date_effet);
        text6 = (EditText)findViewById(R.id.vihicule_Date_echeance);



        if(intColot != 0){
            Boolean result = DB.insert_vehiucle(text1.getText().toString(),text2.getText().toString(),text3.getText().toString(),spinner.getSelectedItem().toString(), Integer.parseInt(text4.getText().toString()),text5.getText().toString(),text6.getText().toString(),intColot);
            if(result){
                Toast.makeText(Ajoute_vihicule.this, "L'ajoute Effectué", Toast.LENGTH_SHORT).show();
                Intent I = new Intent(Ajoute_vihicule.this,vehicules.class);
                Bundle b1 = new Bundle();
                b1.putString("nom",Nom);
                b1.putString("prenom",Prenom);
                b1.putString("role",role);
                I.putExtras(b1);
                startActivity(I);
            }else{
                Toast.makeText(Ajoute_vihicule.this, "L'ajoute n'est pas Effectué", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Merci de choisire la couleur de la vihicule", Toast.LENGTH_SHORT).show();
        }


    }
  public void  DialogFirstuse(){

        new AlertDialog.Builder(this)
                .setTitle("cette message affiche une seule fois !!")
                .setMessage("Remplire les champs de la véhicule pour la prochain fois tu peux navigé facillment")
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

}
