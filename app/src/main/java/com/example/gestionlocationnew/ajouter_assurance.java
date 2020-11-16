package com.example.gestionlocationnew;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class ajouter_assurance extends AppCompatActivity {
    EditText t1,t2,t3,t4,t5;
    gestion_location db;
    String Nom,Prenom,role,login;
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

    int alarmYear,alarmMonth,alarmDay,alarmHour,alarmMinuit;


    private DatePickerDialog.OnDateSetListener mDateSetListenerdatedebute;
    private DatePickerDialog.OnDateSetListener mDateSetListenerdatefin;

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
        login = b.getString("login");


        /**
         * get date debut assurance
         */
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(ajouter_assurance.this
                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                        ,mDateSetListenerdatedebute,Year, Month,Day);

                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        mDateSetListenerdatedebute = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month =month+1;
                String datefin = dayOfMonth+"/"+month+"/"+year;
                t2.setText(datefin);
            }
        };



        /**
         * get date fin assurance
         */
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(ajouter_assurance.this
                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                        ,mDateSetListenerdatefin,Year, Month,Day);

                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        mDateSetListenerdatefin = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month =month+1;
                String datefin = dayOfMonth+"/"+month+"/"+year;
                t3.setText(datefin);
            }
        };







    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ajouterAssurance(View view) {


        if(!TextUtils.isEmpty(t1.getText().toString()) && !TextUtils.isEmpty(t2.getText().toString()) && !TextUtils.isEmpty(t3.getText().toString()) && !TextUtils.isEmpty(t4.getText().toString()) && !TextUtils.isEmpty(t5.getText().toString())){

            boolean c=db.insert_assurance(t1.getText().toString(),t2.getText().toString(),t3.getText().toString(),t4.getText().toString(),Integer.parseInt(t5.getText().toString()),login);


            if(c){

                /**
                 * ajouter assurance event on calendar
                 */

                try {
                    Calendar c1 = Calendar.getInstance();

                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

                    String formattedDate = df.format(c1.getTime());

                    addEventsassurance(t3.getText().toString(),t1.getText().toString(),formattedDate,login);



                }catch (Exception EX){
                    Toast.makeText(this, ""+EX.getMessage(), Toast.LENGTH_LONG).show();
                }

                Snackbar snack = Snackbar.make(view,"l'ajoute Reussi",Snackbar.LENGTH_INDEFINITE);
                View sbView = snack.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.color_warning_green));
                snack.setAction("Close", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snack.dismiss();
                        Intent T;
                        Bundle b= new Bundle();
                        T = new Intent(ajouter_assurance.this, assurances.class);
                        b.putString("nom",Nom);
                        b.putString("prenom",Prenom);
                        b.putString("role",role);
                        b.putString("login",login);
                        T.putExtras(b);
                        finish();
                        startActivity(T);
                    }
                }).setActionTextColor(getResources().getColor(R.color.NAVwhite1)).show();



            }else {
                //Toast.makeText(this,"Erreur d'ajoute",Toast.LENGTH_LONG).show();
                Snackbar snack = Snackbar.make(view,"Erreur d'ajoute",Snackbar.LENGTH_SHORT);
                View sbView = snack.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.color_warning_red));
                snack.show();

            }
        }else{
            Snackbar snack = Snackbar.make(view,"les champs obligatoire",Snackbar.LENGTH_SHORT);
            View sbView = snack.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.color_warning_yellow));
            snack.show();

            //Snackbar.make(view, "les champs obligatoire", Snackbar.LENGTH_SHORT).show();

        }

    }

    public  void addEventsassurance(String sdate,String discription,String formattedDate,String login){

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
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

        String formattedDate1 = df.format(c.getTime());



        SaveEvent(Events,formattedDate,DateF,monthString,part3,login);

        SetUpCalendar(login);



        alarmHour = c.get(Calendar.HOUR_OF_DAY);
        alarmMinuit = c.get(Calendar.MINUTE);

        String[] dateslp = sdate.split("/");

        alarmYear = Integer.parseInt(dateslp[2]);
        alarmMonth = Integer.parseInt(dateslp[1]);
        alarmDay = Integer.parseInt(dateslp[0]);

        Calendar calendar = Calendar.getInstance();

        calendar.set(alarmYear,alarmMonth,alarmDay,alarmHour,alarmMinuit);

        //calendar.add(Calendar.DAY_OF_MONTH, -2); //add 15 jour

        setAlarm(calendar,Events,formattedDate,getRequestCode(DateF
                ,Events,formattedDate,login));





    }

    private int getRequestCode(String date,String event,String time,String login){
        int code = 0;
        dbOpenHelper = new DBOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadIDEvents(date,event,time,login,database);
        while (cursor.moveToNext()){
            code = cursor.getInt(cursor.getColumnIndex(DBStructure.ID));
        }
        cursor.close();
        dbOpenHelper.close();
        Log.d(TAG, "getRequestCode: "+code);

        return code;
    }



    private void setAlarm(Calendar calendar,String event,String time,int RequestCOde){

        calendar.add(Calendar.DAY_OF_MONTH, -15); //add 15 jour
        //calendar.add(Calendar.MINUTE, +3); //add 15 jour

        Intent intent = new Intent(this,AlarmReceiver.class);
        intent.putExtra("event",event);
        intent.putExtra("time",time);
        intent.putExtra("id",RequestCOde);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,RequestCOde,intent,PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager)this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }




    private void SaveEvent(String event,String time,String date, String month,String year,String login){

        dbOpenHelper = new DBOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event,time,date,month,year,"on",login,database);
        dbOpenHelper.close();
        Toast.makeText(this, "Event Saved", Toast.LENGTH_SHORT).show();

    }


    private void SetUpCalendar(String login){
        String currwntDate = dateFormat.format(calendar.getTime());
        //CurrentDate.setText(currwntDate);
        dates.clear();
        Calendar monthCalendar= (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormate.format(calendar.getTime()),login);

        while (dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        myGridAdapter = new MyGridAdapter(this,dates,calendar,eventsList);
        gridView.setAdapter(myGridAdapter);

    }


    private void CollectEventsPerMonth(String Month,String year,String login){
        eventsList.clear();
        dbOpenHelper= new DBOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsperMonth(Month,year,login,database);
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


    private void updateEvent(String date,String event,String time,String notify,String login ){
        dbOpenHelper = new DBOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.updateEvent(date,event,time,notify,login,database);
        dbOpenHelper.close();
    }






}
