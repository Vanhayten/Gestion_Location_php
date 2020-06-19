package com.example.gestionlocationnew;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class Login extends AppCompatActivity {
    gestion_location db;
    EditText login;
    EditText Pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new gestion_location(this);
        login =(EditText)findViewById(R.id.editText6);
        Pass =(EditText)findViewById(R.id.editText7);

        boolean result = db.insert_emp("1111","1111","Ayoub","chaib","Admin");

    }


    public void connection(View view) {

        Cursor c = db.Get_connection();
        String str1,str2;
        while (c.moveToNext()) {

            if (c.getString(0).equals(login.getText().toString()) && c.getString(1).equals(Pass.getText().toString())) {
                Intent T = new Intent(this, vehicules.class);
                Bundle b = new Bundle();
                b.putString("nom",c.getString(2).toString());
                b.putString("prenom",c.getString(3).toString());
                b.putString("role",c.getString(4).toString());
                T.putExtras(b);

                finish();
                startActivity(T);

            }
        }


    }

    public void creer(View view) {
        Intent in=new Intent(this,creer_compte.class);
        startActivity(in);
    }
}
