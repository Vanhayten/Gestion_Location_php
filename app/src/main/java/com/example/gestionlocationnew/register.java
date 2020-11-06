package com.example.gestionlocationnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class register extends AppCompatActivity {

    gestion_location db;

    EditText userName,Prenom,Nom,Email,Num,Password;
    TextView ErrUser,ErrPrenom,ErrNom,ErrEmail,ErrPassword,ErrNum;
    ImageView isBack;
    Button confirme;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new gestion_location(this);


        userName =(EditText)findViewById(R.id.et_userName);
        Prenom =(EditText)findViewById(R.id.et_first_name);
        Nom =(EditText)findViewById(R.id.et_last_name);
        Email =(EditText)findViewById(R.id.et_email);
        Num =(EditText)findViewById(R.id.et_tele);
        Password =(EditText)findViewById(R.id.et_password);

        ErrUser =(TextView) findViewById(R.id.tv_error_userName);
        ErrPrenom =(TextView) findViewById(R.id.tv_error_first_name);
        ErrNom =(TextView) findViewById(R.id.tv_error_last_name);
        ErrEmail =(TextView) findViewById(R.id.tv_error_email);
        ErrPassword =(TextView) findViewById(R.id.tv_error_password);
        ErrNum =(TextView) findViewById(R.id.tv_error_tele);


        OnTextCange();


        /**
         * back to login
         */
        isBack =(ImageView)findViewById(R.id.iv_back);
        isBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void OnTextCange() {

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ErrUser.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        /**
         * On Change Name
         */
        Prenom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ErrPrenom.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Nom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            ErrNom.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ErrEmail.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ErrNum.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ErrPassword.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void Confirm(View view) {

        boolean checker = Check(userName.getText().toString(),Prenom.getText().toString(),Nom.getText().toString(),Email.getText().toString(),Num.getText().toString(),Password.getText().toString());
        if(checker){


            /**
             * get current date
             */
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String Datef = formatter.format(date);


            boolean result = db.insert_emp(userName.getText().toString(),Password.getText().toString(),Nom.getText().toString(),Prenom.getText().toString(),"Admin",Datef,Email.getText().toString());
            View llProgressBar = findViewById(R.id.llProgressBar);
            if(result){


                //Button insideTheIncludedLayout = (Button)includedLayout.findViewById(R.id.button1);
                llProgressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent mainIntent = new Intent(register.this, Login.class);
                        register.this.startActivity(mainIntent);
                        register.this.finish();
                    }
                }, 2000);
            }else {

                ImageView ImageChecker = (ImageView) llProgressBar.findViewById(R.id.ImageDone);
                ImageChecker.setImageResource(R.drawable.cancel);
                llProgressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llProgressBar.setVisibility(View.INVISIBLE);
                    }
                }, 2000);

            }
        }

    }


    public boolean Check(String user,String Prenom,String Nom,String Email,String Num,String Password){

        boolean checkerUser= false;
        boolean checkerPrenom= false;
        boolean checkerNom= false;
        boolean checkerEmail= false;
        boolean checkerNum= false;
        boolean checkerPassword= false;


        /**
         * check User
         */
        if(!TextUtils.isEmpty(user)){

            if(user.length() <=4 ){
                ErrUser.setText("Min 4 caractères!");
                ErrUser.setVisibility(View.VISIBLE);
            }else{



                SQLiteDatabase table1 = db.getReadableDatabase();
                String requet1 = "select count(*) from Identifie where Login ='"+user+"' ";

                try {
                    Cursor userCursor = table1.rawQuery(requet1, null);

                    if (userCursor.moveToNext()) {

                        if(Integer.parseInt(userCursor.getString(0) )== 0){

                            //User done
                            checkerUser =true;

                        }else {
                            ErrUser.setText("Nom d'utilisateur Deja existe");
                            ErrUser.setVisibility(View.VISIBLE);
                        }

                    }

                }catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }else {
            ErrUser.setText("champ vide");
            ErrUser.setVisibility(View.VISIBLE);
        }



        /**
         * check Email
         */
        if(!TextUtils.isEmpty(Email)){

            if(!isValidEmail(Email)){
                ErrEmail.setText("Email est invalide");
                ErrEmail.setVisibility(View.VISIBLE);
            }else{

                SQLiteDatabase table1 = db.getReadableDatabase();
                String requet1 = "select count(*) from Identifie where Email ='"+Email+"' ";

                try {
                Cursor emailCursor = table1.rawQuery(requet1, null);

                    if (emailCursor.moveToNext()) {

                            if(Integer.parseInt(emailCursor.getString(0) )== 0){

                                //User done
                                checkerEmail =true;

                            }else {
                                ErrEmail.setText("Email Deja existe");
                                ErrEmail.setVisibility(View.VISIBLE);
                            }

                        }

                    }catch (Exception e){
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


            }

        }else {
            ErrEmail.setText("champ vide");
            ErrEmail.setVisibility(View.VISIBLE);
        }





        /**
         * check Nom
         */
        if(!TextUtils.isEmpty(Nom) ){

            if(Nom.length() <=4 ){
                ErrNom.setText("Min 4 caractères!");
                ErrNom.setVisibility(View.VISIBLE);
            }else{
                        //User done
                        checkerNom =true;
                }

        }else {
            ErrNom.setText("champ vide");
            ErrNom.setVisibility(View.VISIBLE);
        }



        /**
         * check Prenom
         */
        if(!TextUtils.isEmpty(Prenom)){

            if(Prenom.length() <=4 ){
                ErrPrenom.setText("Min 4 caractères!");
                ErrPrenom.setVisibility(View.VISIBLE);
            }else{
                //User done
                checkerPrenom =true;
            }

        }else {
            ErrPrenom.setText("champ vide");
            ErrPrenom.setVisibility(View.VISIBLE);
        }


        /**
         * check Numero
         */
        if(!TextUtils.isEmpty(Num)){

            if(Num.length() <=9 ){
                ErrNum.setText("le numéro de téléphone doit avoir 10 numéros");
                ErrNum.setVisibility(View.VISIBLE);
            }else{
                //User done
                checkerNum =true;
            }

        }else {
            ErrNum.setText("champ vide");
            ErrNum.setVisibility(View.VISIBLE);
        }


        /**
         * check password
         */
        if(!TextUtils.isEmpty(Password)){

            if(!isValidPassword(Password) ){
                ErrPassword.setText("mot de passe est invalide");
                ErrPassword.setVisibility(View.VISIBLE);
            }else{
                //User done
                checkerPassword =true;
            }

        }else {
            ErrPassword.setText("champ vide");
            ErrPassword.setVisibility(View.VISIBLE);
        }



        if(checkerUser && checkerPrenom && checkerNom && checkerEmail && checkerNum && checkerPassword){
            return true;
        }else{
            return false;
        }
    }



    static boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }


    static boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9]).{8,15}$";
        return password.matches(regex);
    }
}