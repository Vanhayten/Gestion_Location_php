package com.example.gestionlocationnew;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ajouter_assurance extends AppCompatActivity {
    EditText t1,t2,t3,t4,t5;
    gestion_location db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_assurance);
        t1=(EditText)findViewById(R.id.editText19);
        t2=(EditText)findViewById(R.id.editText9);
        t3=(EditText)findViewById(R.id.editText16);
        t4=(EditText)findViewById(R.id.editText17);
        t5=(EditText)findViewById(R.id.editText18);
        Bundle b = getIntent().getExtras();
        t1.setText(b.getString("matricule"));
        db = new gestion_location(this);
    }
    public void ajouterAssurance(View view) {
        try{
            boolean c=db.insert_assurance(t1.getText().toString(),t2.getText().toString(),t3.getText().toString(),t4.getText().toString(),Integer.parseInt(t5.getText().toString()));
            Toast.makeText(this,"l'ajoute Reussi",Toast.LENGTH_LONG).show();

        }catch (Exception ex){
            Toast.makeText(this,"Erreur d'ajoute",Toast.LENGTH_LONG).show();

        }

    }
}
