package com.example.gestionlocationnew;

import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 10;
    gestion_location db;
    EditText login;
    EditText Pass;

    SharedPreferences sp;

    GoogleSignInClient mGoogleSignInClient;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });






        sp = getSharedPreferences("login",MODE_PRIVATE);
        if(sp.getBoolean("logged",false)){
            goToMainActivity(sp);
        }



        db = new gestion_location(this);
        login =(EditText)findViewById(R.id.editText6);
        Pass =(EditText)findViewById(R.id.editText7);



login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        login.setBackgroundResource(R.drawable.editext_baground_none);
    }
});

        Pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Pass.setBackgroundResource(R.drawable.editext_baground_none);
            }
        });


        LinearLayout sinscrireLayout = (LinearLayout)findViewById(R.id.sinscrire);
        sinscrireLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register=new Intent(Login.this,register.class);
                startActivity(register);
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void connection(View view) {

        Cursor c = db.Get_connection(login.getText().toString(),Pass.getText().toString());
        String str1,str2;
        Boolean check = false;
        while (c.moveToNext()) {

            sp.edit().putBoolean("logged",true).apply();
            sp.edit().putString("nom",c.getString(3)).apply();
            sp.edit().putString("prenom",c.getString(4)).apply();
            sp.edit().putString("role",c.getString(5)).apply();
            sp.edit().putString("login",c.getString(1)).apply();
            sp.edit().putString("URLImage","").apply();


            Intent T = new Intent(this, vehicules.class);
                Bundle b = new Bundle();
                b.putString("nom",c.getString(3).toString());
                b.putString("prenom",c.getString(4).toString());
                b.putString("role",c.getString(5).toString());
                b.putString("login",c.getString(1).toString());
                T.putExtras(b);
                check = true;
                finish();



                startActivity(T, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
               // startActivity(T);

        }
        if(check == false){
            Toast.makeText(this, "nom d'utilisateur ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
        }


    }


    public void goToMainActivity(SharedPreferences SP){
        Intent i = new Intent(this,vehicules.class);
        Bundle b = new Bundle();
        b.putString("nom",SP.getString("nom",""));
        b.putString("prenom",SP.getString("prenom",""));
        b.putString("role",SP.getString("role",""));
        b.putString("login",SP.getString("login",""));
        i.putExtras(b);
        startActivity(i);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void firstUpdateUI(GoogleSignInAccount acct) {
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();


            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String Datef = formatter.format(date);

            Cursor c1 = db.Get_connectionGGL(personEmail);
            if (c1.getCount() ==0){
                boolean result = db.insert_emp(personName,personId,personFamilyName,personGivenName,"Admin",Datef,personEmail);
            }


                Cursor c = db.Get_connectionGGL(personEmail);
                String str1,str2;
                Boolean check = false;
                while (c.moveToNext()) {
                    sp.edit().putBoolean("logged",true).apply();
                    sp.edit().putString("nom",c.getString(3)).apply();
                    sp.edit().putString("prenom",c.getString(4)).apply();
                    sp.edit().putString("role",c.getString(5)).apply();
                    sp.edit().putString("login",c.getString(1)).apply();


                    Intent T = new Intent(this, vehicules.class);
                    Bundle b = new Bundle();
                    b.putString("nom",c.getString(3).toString());
                    b.putString("prenom",c.getString(4).toString());
                    b.putString("role",c.getString(5).toString());
                    b.putString("login",c.getString(1).toString());
                    T.putExtras(b);
                    check = true;
                    finish();
                    startActivity(T, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                }

                if(check == false){
                    Toast.makeText(this, "l'email n'existe pas", Toast.LENGTH_SHORT).show();
                }




        }else{
          //  Toast.makeText(this, "erreur login", Toast.LENGTH_SHORT).show();
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateUI(GoogleSignInAccount acct) {

        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();


            /**
             * deja existe login
             */

            Cursor c = db.Get_connectionGGL(personEmail);
            String str1,str2;
            Boolean check = false;
            while (c.moveToNext()) {
                sp.edit().putBoolean("logged",true).apply();
                sp.edit().putString("nom",c.getString(3)).apply();
                sp.edit().putString("prenom",c.getString(4)).apply();
                sp.edit().putString("role",c.getString(5)).apply();
                sp.edit().putString("login",c.getString(1)).apply();
                sp.edit().putString("URLImage",personPhoto.toString()).apply();




                Intent T = new Intent(this, vehicules.class);
                Bundle b = new Bundle();
                b.putString("nom",c.getString(3).toString());
                b.putString("prenom",c.getString(4).toString());
                b.putString("role",c.getString(5).toString());
                b.putString("login",c.getString(1).toString());

                T.putExtras(b);
                check = true;
                finish();
                startActivity(T, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }

            if(check == false){
                Toast.makeText(this, "l'email n'existe pas", Toast.LENGTH_SHORT).show();
            }

        }else{
            //Toast.makeText(this, "erreur login", Toast.LENGTH_SHORT).show();
        }


    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            firstUpdateUI(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("error login", "signInResult:failed code=" + e.getStatusCode());
            firstUpdateUI(null);
        }
    }

}
