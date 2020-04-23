package com.example.gestionlocationnew;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class creer_compte extends AppCompatActivity {
    gestion_location db;
    EditText t1,t2,t3,t4,t5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_compte);
        db = new gestion_location(this);
        t1=(EditText)findViewById(R.id.editText);
        t2=(EditText)findViewById(R.id.editText2);
        t3=(EditText)findViewById(R.id.editText3);
        t4=(EditText)findViewById(R.id.editText4);
        t5=(EditText)findViewById(R.id.editText5);
    }
    public void insert(View view) {
        String ch1,ch2,ch3,ch4,ch5;
        ch1 = t1.getText().toString();
        ch2 = t2.getText().toString();
        ch3 = t3.getText().toString();
        ch4 = t4.getText().toString();
        ch5 = t5.getText().toString();
        boolean result = db.insert_emp(ch1,ch2,ch3,ch4,ch5);
        if(result == true){
            //    StyleableToast.makeText(this,"Ajoute succesful",R.style.crereToast).show();
            Toast.makeText(this,"Ajoute succesful",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Ajoute Not succesful",Toast.LENGTH_LONG).show();
        }

    }

    public void retour(View view) {
        Intent in=new Intent(this,Login.class);
        startActivity(in);
    }
}
