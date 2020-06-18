package com.example.gestionlocationnew;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.BufferUnderflowException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ajouter_assurance extends AppCompatActivity {
    EditText t1,t2,t3,t4,t5;
    gestion_location db;
    String Nom,Prenom,role;
    DBOpenHelper dbOpenHelper;

    TextView CurrentDate;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();
    private static final int MAX_CALENDAR_DAYS = 42;
    MyGridAdapter myGridAdapter;
    GridView gridView;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM",Locale.ENGLISH);
    SimpleDateFormat yearFormate = new SimpleDateFormat("yyyy",Locale.ENGLISH);
    SimpleDateFormat eventDateFormate = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);


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

        Nom = b.getString("nom");
        Prenom = b.getString("prenom");
        role = b.getString("role");

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ajouterAssurance(View view) {

            boolean c=db.insert_assurance(t1.getText().toString(),t2.getText().toString(),t3.getText().toString(),t4.getText().toString(),Integer.parseInt(t5.getText().toString()));


            if(c){
                Toast.makeText(this,"l'ajoute Reussi",Toast.LENGTH_LONG).show();
                /**
                 * ajouter assurance event on calendar
                 */
                try {
                    addEventsassurance(t3.getText().toString(),t1.getText().toString());
                }catch (Exception EX){

                }
                Intent T;
                Bundle b= new Bundle();
                T = new Intent(this, assurances.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
            }else {
                Toast.makeText(this,"Erreur d'ajoute",Toast.LENGTH_LONG).show();
            }


    }

    public  void addEventsassurance(String sdate,String discription){

        String string = sdate;
        //t3.getText().toString();
        String[] parts = string.split("/");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        String DateF = part3+"-"+part2+"-"+part1;

        String monthString;
        switch (Integer.parseInt(part2)) {
            case 1:  monthString = "January";       break;
            case 2:  monthString = "February";      break;
            case 3:  monthString = "March";         break;
            case 4:  monthString = "April";         break;
            case 5:  monthString = "May";           break;
            case 6:  monthString = "June";          break;
            case 7:  monthString = "July";          break;
            case 8:  monthString = "August";        break;
            case 9:  monthString = "September";     break;
            case 10: monthString = "October";       break;
            case 11: monthString = "November";      break;
            case 12: monthString = "December";      break;
            default: monthString = "Invalid month"; break;
        }

        String Events = "Payment assurance "+discription;
        //t1.getText().toString();

        SaveEvent(Events,null,DateF,monthString,part3);
        SetUpCalendar();

    }


    private void SaveEvent(String event,String time,String date, String month,String year){

        dbOpenHelper = new DBOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event,time,date,month,year,database);
        dbOpenHelper.close();
        Toast.makeText(this, "Event Saved", Toast.LENGTH_SHORT).show();

    }


    private void SetUpCalendar(){
        String currwntDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currwntDate);
        dates.clear();
        Calendar monthCalendar= (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormate.format(calendar.getTime()));

        while (dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        myGridAdapter = new MyGridAdapter(this,dates,calendar,eventsList);
        gridView.setAdapter(myGridAdapter);

    }


    private void CollectEventsPerMonth(String Month,String year){
        eventsList.clear();
        dbOpenHelper= new DBOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsperMonth(Month,year,database);
        while (cursor.moveToNext()){
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Events events = new Events(event,time,date,month,Year);
            eventsList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();

    }






}
