package com.example.gestionlocationnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class historique_sinistre extends AppCompatActivity {
gestion_location db;
ListView ls;
TextView textView;
    private ArrayList<liste_sinistre> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_sinistre);
        db = new gestion_location(this);
        ls=(ListView)findViewById(R.id.list2);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_sinistre,ls,false);
        ls.addHeaderView(header);

        ArrayList<liste_sinistre> list = new ArrayList<>();
        textView=(TextView)findViewById (R.id.matricule);

        Bundle b = getIntent().getExtras();
        String matr =b.getString ( "matricule" );
        textView.setText ("la liste des sinistres de matricule : "+b.getString ( "matricule" ) );
        SQLiteDatabase table = db.getReadableDatabase ();
        String requet = "select * from sinistre where imatriculation_sinistre ='"+matr+"'";
        Cursor c = table.rawQuery ( requet, null );
        while (c.moveToNext ()){
            liste_sinistre listeS= new liste_sinistre();
            listeS.setdate(c.getString(1));
            listeS.setgenre(c.getString(2));
            listeS.setmontant(c.getString(3));
            listeS.setresp(c.getString(4));
            listeS.setmontant1(c.getString(5));
            list.add(listeS);

        }

        arrayList = list;
        PageAdapter_sinistre adapter_sinistre = new PageAdapter_sinistre(this,arrayList);
        ls.setAdapter(adapter_sinistre);




        String matricule =b.getString ( "matriculex" );
    }
}
