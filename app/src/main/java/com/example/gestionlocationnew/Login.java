package com.example.gestionlocationnew;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 10;
    gestion_location db;
    EditText login;
    EditText Pass;

     String ResultFinal;
    String personName ;
    String personGivenName;
    String personFamilyName;
    String personEmail ;
    String personId ;
    Uri personPhoto;

    boolean checker;

    ProgressBar progressBar;

    SharedPreferences sp;

    GoogleSignInClient mGoogleSignInClient;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();

        if(isConnected(Login.this)){
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            updateUI(account);
        }else {
            buildDialog();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


            progressBar = findViewById(R.id.progressBar1);
            // AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

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

                            /**
                             * first check connection if available
                             */
                            if (isConnected(Login.this)) {
                                signIn();
                            } else {
                                buildDialog();
                            }

                            break;
                        // ...
                    }
                }
            });


            sp = getSharedPreferences("login", MODE_PRIVATE);
            if (sp.getBoolean("logged", false)) {
                goToMainActivity(sp);
            }


            db = new gestion_location(this);
            login = (EditText) findViewById(R.id.editText6);
            Pass = (EditText) findViewById(R.id.editText7);


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


            LinearLayout sinscrireLayout = (LinearLayout) findViewById(R.id.sinscrire);
            sinscrireLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent register = new Intent(Login.this, register.class);
                    startActivity(register);
                }
            });

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void connection(View view) {

        /**
         * connection from server
         */




        if(isConnected(Login.this)){
            MySqlSingIN(login.getText().toString(), Pass.getText().toString());
        }else {
            buildDialog();
        }
        /*
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

         */


    }


    public void goToMainActivity(SharedPreferences SP) {
        Intent i = new Intent(this, vehicules.class);
        Bundle b = new Bundle();
        b.putString("nom", SP.getString("nom", ""));
        b.putString("prenom", SP.getString("prenom", ""));
        b.putString("role", SP.getString("role", ""));
        b.putString("login", SP.getString("login", ""));
        i.putExtras(b);
        startActivity(i);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void firstUpdateUI(GoogleSignInAccount acct) {
        if (acct != null) {
             personName = acct.getDisplayName();
             personGivenName = acct.getGivenName();
             personFamilyName = acct.getFamilyName();
             personEmail = acct.getEmail();
             personId = acct.getId();
             personPhoto = acct.getPhotoUrl();


            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String Datef = formatter.format(date);


            /**
             * get from host
             */

            /**
             * get email from host
             */

            //Start ProgressBar first (Set visibility VISIBLE)
            progressBar.setVisibility(View.VISIBLE);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[1];
                    field[0] = "Email";


                    //Creating array for data
                    String[] data = new String[1];
                    data[0] = personEmail;

                    String host = getResources().getString(R.string.hosting);
                    PutData putData = new PutData(host+"/gesloc/LoginRegister/userEmail.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();

                            //End ProgressBar (Set visibility to GONE)
                            progressBar.setVisibility(View.GONE);
                            ResultFinal =result;




                            if(result.equals("Email wrong")){
                               // Log.i("#########",result);
                                //personFamilyName


                                if(personFamilyName == null || personFamilyName.isEmpty()){
                                    personFamilyName =personGivenName;
                                }


                                if(personGivenName == null || personGivenName.isEmpty()){
                                    personGivenName =personFamilyName;
                                }

                                InsertData(personName, personId, personFamilyName, personGivenName, "Admin", Datef, personEmail);

                            }else{

                                try {
                                    GetUserJson(result,personPhoto.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }




                        }
                    }
                    //End Write and Read data with URL
                }
            });

            //--------------------------------


/*
            Cursor c1 = db.Get_connectionGGL(personEmail);
            if (c1.getCount() == 0) {
                boolean result = db.insert_emp(personName, personId, personFamilyName, personGivenName, "Admin", Datef, personEmail);
               // InsertData(personName, personId, personFamilyName, personGivenName, "Admin", Datef, personEmail);
            }


            Cursor c = db.Get_connectionGGL(personEmail);
            String str1, str2;
            Boolean check = false;
            while (c.moveToNext()) {
                sp.edit().putBoolean("logged", true).apply();
                sp.edit().putString("nom", c.getString(3)).apply();
                sp.edit().putString("prenom", c.getString(4)).apply();
                sp.edit().putString("role", c.getString(5)).apply();
                sp.edit().putString("login", c.getString(1)).apply();


                Intent T = new Intent(this, vehicules.class);
                Bundle b = new Bundle();
                b.putString("nom", c.getString(3).toString());
                b.putString("prenom", c.getString(4).toString());
                b.putString("role", c.getString(5).toString());
                b.putString("login", c.getString(1).toString());
                T.putExtras(b);
                check = true;
                finish();
                startActivity(T, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }

            if (check == false) {
                Toast.makeText(this, "l'email n'existe pas", Toast.LENGTH_SHORT).show();
            }
            */


        } else {
            //  Toast.makeText(this, "erreur login", Toast.LENGTH_SHORT).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateUI(GoogleSignInAccount acct) {

        if (acct != null) {
             personName = acct.getDisplayName();
             personGivenName = acct.getGivenName();
             personFamilyName = acct.getFamilyName();
             personEmail = acct.getEmail();
             personId = acct.getId();
             personPhoto = acct.getPhotoUrl();


            /**
             * deja existe login
             */


            /**
             * get email from host
             */

            //Start ProgressBar first (Set visibility VISIBLE)
            progressBar.setVisibility(View.VISIBLE);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[1];
                    field[0] = "Email";


                    //Creating array for data
                    String[] data = new String[1];
                    data[0] = personEmail;

                    String host = getResources().getString(R.string.hosting);
                    PutData putData = new PutData(host+"/gesloc/LoginRegister/userEmail.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();

                            //End ProgressBar (Set visibility to GONE)
                            progressBar.setVisibility(View.GONE);
                            ResultFinal =result;

                            Log.i("data",ResultFinal);



                            try {
                                GetUserJson(result,personPhoto.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();

                                View parentLayout = findViewById(android.R.id.content);
                                Snackbar snack = Snackbar.make(parentLayout,result,Snackbar.LENGTH_INDEFINITE).setDuration(3000);
                                View sbView = snack.getView();
                                sbView.setBackgroundColor(getResources().getColor(R.color.color_warning_yellow));
                                snack.show();
                            }


                        }
                    }
                    //End Write and Read data with URL
                }
            });

            //--------------------------------


            /*
            Cursor c = db.Get_connectionGGL(personEmail);
            String str1, str2;
            Boolean check = false;
            while (c.moveToNext()) {
                sp.edit().putBoolean("logged", true).apply();
                sp.edit().putString("nom", c.getString(3)).apply();
                sp.edit().putString("prenom", c.getString(4)).apply();
                sp.edit().putString("role", c.getString(5)).apply();
                sp.edit().putString("login", c.getString(1)).apply();
                sp.edit().putString("URLImage", personPhoto.toString()).apply();


                Intent T = new Intent(this, vehicules.class);
                Bundle b = new Bundle();
                b.putString("nom", c.getString(3).toString());
                b.putString("prenom", c.getString(4).toString());
                b.putString("role", c.getString(5).toString());
                b.putString("login", c.getString(1).toString());

                T.putExtras(b);
                check = true;
                finish();
                startActivity(T);
            }

            if (check == false) {
                Toast.makeText(this, "l'email n'existe pas", Toast.LENGTH_SHORT).show();
            }
            */


        } else {
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

            //check connection if available
            if(isConnected(Login.this)) {
                // Signed in successfully, show authenticated UI.
                firstUpdateUI(account);
            }else {
                buildDialog();
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("error login", "signInResult:failed code=" + e.getStatusCode());
            //check connection if available
            if(isConnected(Login.this)) {
                firstUpdateUI(null);
            }else{
                buildDialog();
            }
        }
    }


    public void MySqlSingIN(final String Login, final String Mdp) {



        if(!TextUtils.isEmpty(Login) && !TextUtils.isEmpty(Mdp)){
        //Start ProgressBar first (Set visibility VISIBLE)
        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[2];
                field[0] = "Login";
                field[1] = "Mdp";

                //Creating array for data
                String[] data = new String[2];
                data[0] = Login;
                data[1] = Mdp;

                String host = getResources().getString(R.string.hosting);
                PutData putData = new PutData(host+"/gesloc/LoginRegister/login.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();


                        //End ProgressBar (Set visibility to GONE)
                        progressBar.setVisibility(View.GONE);
                        try {
                            GetUserJson(result,"");
                        } catch (JSONException e) {
                            e.printStackTrace();

                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar snack = Snackbar.make(parentLayout,result,Snackbar.LENGTH_INDEFINITE).setDuration(3000);
                            View sbView = snack.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.color_warning_yellow));
                            snack.show();
                        }

                        //Log.i("PutData", result);

                    }
                }
                //End Write and Read data with URL
            }
        });
        }else{
            Toast.makeText(this, "required field", Toast.LENGTH_SHORT).show();
        }
    }



    //---------------------------------------

    /**
     * get json user
     * @param json
     * @throws JSONException
     */

    private void GetUserJson(String json,String UrlImage) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] webchrz = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);


            sp.edit().putBoolean("logged",true).apply();
            sp.edit().putString("nom",obj.getString("Nom")).apply();
            sp.edit().putString("prenom",obj.getString("Prenom")).apply();
            sp.edit().putString("role",obj.getString("Role")).apply();
            sp.edit().putString("login",obj.getString("Login")).apply();
            sp.edit().putString("URLImage",UrlImage).apply();


            Intent T = new Intent(this, vehicules.class);
            Bundle b = new Bundle();
            b.putString("nom",obj.getString("Nom"));
            b.putString("prenom",obj.getString("Prenom"));
            b.putString("role",obj.getString("Role"));
            b.putString("login",obj.getString("Login"));
            T.putExtras(b);
            finish();

            startActivity(T);
            // startActivity(T);

        }
    }



    public void InsertData(final String Login, final String Mdp,final String Nom,final String Prenom, final String Role, final String DateIdentifie, final String Email){



        //Start ProgressBar first (Set visibility VISIBLE)
        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[7];
                field[0] = "Login";
                field[1] = "Mdp";
                field[2] = "Nom";
                field[3] = "Prenom";
                field[4] = "Role";
                field[5] = "DateIdentifie";
                field[6] = "Email";


                //Creating array for data
                String[] data = new String[7];
                data[0] = Login;
                data[1] = Mdp;
                data[2] = Nom;
                data[3] = Prenom;
                data[4] = Role;
                data[5] = DateIdentifie;
                data[6] = Email;

                Log.i("data","Login "+Login+" Mode pas : "+Mdp+" Nom :  "+Nom+" Prenom "+Prenom+"  "+ Role +": "+Role+"  "+Email+"  "+DateIdentifie);

                String host = getResources().getString(R.string.hosting);
                PutData putData = new PutData(host+"/gesloc/LoginRegister/signup.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();

                        //Toast.makeText(register.this, result, Toast.LENGTH_LONG).show();
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar snack = Snackbar.make(parentLayout,result,Snackbar.LENGTH_INDEFINITE).setDuration(3000);
                        View sbView = snack.getView();
                        sbView.setBackgroundColor(getResources().getColor(R.color.color_warning_green));
                        snack.show();

                        //End ProgressBar (Set visibility to GONE)
                        progressBar.setVisibility(View.GONE);
                        //Log.i("PutData", result);


                        if(result.equals("Sign Up Success")){


                            /**
                             * add data to local
                             */
                            boolean tester = db.insert_emp(Login, Mdp, Nom, Prenom, "Admin", DateIdentifie, Email);

                          //  View llProgressBar = findViewById(R.id.llProgressBar);
                            if (tester) {


                                sp.edit().putBoolean("logged", true).apply();
                                sp.edit().putString("nom", Nom).apply();
                                sp.edit().putString("prenom", Prenom).apply();
                                sp.edit().putString("role", "Admin").apply();
                                sp.edit().putString("login", Login).apply();
                                sp.edit().putString("URLImage", "").apply();


                                Intent T = new Intent(Login.this, vehicules.class);
                                Bundle b = new Bundle();
                                b.putString("nom", Nom);
                                b.putString("prenom", Prenom);
                                b.putString("role", "Admin");
                                b.putString("login", Login);

                                T.putExtras(b);

                                finish();
                                startActivity(T);

                                //Button insideTheIncludedLayout = (Button)includedLayout.findViewById(R.id.button1);
                               // llProgressBar.setVisibility(View.VISIBLE);
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        final Intent mainIntent = new Intent(Login.this, Login.class);
//                                        Login.this.startActivity(mainIntent);
//                                        Login.this.finish();
//                                    }
//                                }, 2000);


                            } else {

//                                ImageView ImageChecker = (ImageView) llProgressBar.findViewById(R.id.ImageDone);
//                                ImageChecker.setImageResource(R.drawable.cancel);
//                                llProgressBar.setVisibility(View.VISIBLE);
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        llProgressBar.setVisibility(View.INVISIBLE);
//                                    }
//                                }, 2000);


                            }

                        }else{




                        }

                    }
                }
                //End Write and Read data with URL
            }
        });

    }


    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }
    public void buildDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_no_internet, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button refrech = dialogView.findViewById(R.id.buttonOk);
        refrech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected(Login.this)){
                    alertDialog.dismiss();
                    Toast.makeText(Login.this, "connected", Toast.LENGTH_SHORT).show();
                }else {

                    final Animation myAnim = AnimationUtils.loadAnimation(Login.this, R.anim.bounce);

                    // Use bounce interpolator with amplitude 0.2 and frequency 20
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 30);
                    myAnim.setInterpolator(interpolator);

                    refrech.startAnimation(myAnim);

                    Toast.makeText(Login.this, "No Internet", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                }

            }
        });


    }




}
