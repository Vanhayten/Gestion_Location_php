package com.example.gestionlocationnew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.snackbar.Snackbar;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class register extends AppCompatActivity {

    boolean checker;
    gestion_location db;
    EditText userName, Prenom, Nom, Email, Num, Password;
    TextView ErrUser, ErrPrenom, ErrNom, ErrEmail, ErrPassword, ErrNum;
    ImageView isBack;
    Button confirme;
    String date;
    View llProgressBar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new gestion_location(this);


        llProgressBar = findViewById(R.id.llProgressBar);


        userName = (EditText) findViewById(R.id.et_userName);
        Prenom = (EditText) findViewById(R.id.et_first_name);
        Nom = (EditText) findViewById(R.id.et_last_name);
        Email = (EditText) findViewById(R.id.et_email);
        Num = (EditText) findViewById(R.id.et_tele);
        Password = (EditText) findViewById(R.id.et_password);

        ErrUser = (TextView) findViewById(R.id.tv_error_userName);
        ErrPrenom = (TextView) findViewById(R.id.tv_error_first_name);
        ErrNom = (TextView) findViewById(R.id.tv_error_last_name);
        ErrEmail = (TextView) findViewById(R.id.tv_error_email);
        ErrPassword = (TextView) findViewById(R.id.tv_error_password);
        ErrNum = (TextView) findViewById(R.id.tv_error_tele);

        progressBar = findViewById(R.id.progressBar1);

        OnTextCange();


        /**
         * back to login
         */
        isBack = (ImageView) findViewById(R.id.iv_back);
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

        boolean checker = Check(userName.getText().toString(), Prenom.getText().toString(), Nom.getText().toString(), Email.getText().toString(), Num.getText().toString(), Password.getText().toString());
        if (checker) {


            /**
             * get current date
             */
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String Datef = formatter.format(date);

            if (isConnected(register.this)) {
                InsertData(userName.getText().toString(), Password.getText().toString(), Nom.getText().toString(), Prenom.getText().toString(), "Admin", Datef, Email.getText().toString());
            } else {
                buildDialog();
            }

            //Toast.makeText(this, tester+"", Toast.LENGTH_SHORT).show();
        }


    }


    public boolean Check(String user, String Prenom, String Nom, String Email, String Num, String Password) {

        boolean checkerUser = false;
        boolean checkerPrenom = false;
        boolean checkerNom = false;
        boolean checkerEmail = false;
        boolean checkerNum = false;
        boolean checkerPassword = false;


        /**
         * check User
         */
        if (!TextUtils.isEmpty(user)) {

            if (user.length() <= 4) {
                ErrUser.setText("Min 4 caractères!");
                ErrUser.setVisibility(View.VISIBLE);
            } else {


                SQLiteDatabase table1 = db.getReadableDatabase();
                String requet1 = "select count(*) from Identifie where Login ='" + user + "' ";

                try {
                    Cursor userCursor = table1.rawQuery(requet1, null);

                    if (userCursor.moveToNext()) {

                        if (Integer.parseInt(userCursor.getString(0)) == 0) {

                            //User done
                            checkerUser = true;

                        } else {
                            ErrUser.setText("Nom d'utilisateur Deja existe");
                            ErrUser.setVisibility(View.VISIBLE);
                        }

                    }

                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        } else {
            ErrUser.setText("champ vide");
            ErrUser.setVisibility(View.VISIBLE);
        }


        /**
         * check Email
         */
        if (!TextUtils.isEmpty(Email)) {

            if (!isValidEmail(Email)) {
                ErrEmail.setText("Email est invalide");
                ErrEmail.setVisibility(View.VISIBLE);
            } else {

                SQLiteDatabase table1 = db.getReadableDatabase();
                String requet1 = "select count(*) from Identifie where Email ='" + Email + "' ";

                try {
                    Cursor emailCursor = table1.rawQuery(requet1, null);

                    if (emailCursor.moveToNext()) {

                        if (Integer.parseInt(emailCursor.getString(0)) == 0) {

                            //User done
                            checkerEmail = true;

                        } else {
                            ErrEmail.setText("Email Deja existe");
                            ErrEmail.setVisibility(View.VISIBLE);
                        }

                    }

                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

        } else {
            ErrEmail.setText("champ vide");
            ErrEmail.setVisibility(View.VISIBLE);
        }


        /**
         * check Nom
         */
        if (!TextUtils.isEmpty(Nom)) {

            if (Nom.length() <= 4) {
                ErrNom.setText("Min 4 caractères!");
                ErrNom.setVisibility(View.VISIBLE);
            } else {
                //User done
                checkerNom = true;
            }

        } else {
            ErrNom.setText("champ vide");
            ErrNom.setVisibility(View.VISIBLE);
        }


        /**
         * check Prenom
         */
        if (!TextUtils.isEmpty(Prenom)) {

            if (Prenom.length() <= 4) {
                ErrPrenom.setText("Min 4 caractères!");
                ErrPrenom.setVisibility(View.VISIBLE);
            } else {
                //User done
                checkerPrenom = true;
            }

        } else {
            ErrPrenom.setText("champ vide");
            ErrPrenom.setVisibility(View.VISIBLE);
        }


        /**
         * check Numero
         */
        if (!TextUtils.isEmpty(Num)) {

            if (Num.length() <= 9) {
                ErrNum.setText("le numéro de téléphone doit avoir 10 numéros");
                ErrNum.setVisibility(View.VISIBLE);
            } else {
                //User done
                checkerNum = true;
            }

        } else {
            ErrNum.setText("champ vide");
            ErrNum.setVisibility(View.VISIBLE);
        }


        /**
         * check password
         */
        if (!TextUtils.isEmpty(Password)) {

            if (!isValidPassword(Password)) {
                ErrPassword.setText("mot de passe est invalide");
                ErrPassword.setVisibility(View.VISIBLE);
            } else {
                //User done
                checkerPassword = true;
            }

        } else {
            ErrPassword.setText("champ vide");
            ErrPassword.setVisibility(View.VISIBLE);
        }


        if (checkerUser && checkerPrenom && checkerNom && checkerEmail && checkerNum && checkerPassword) {
            return true;
        } else {
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


    public void InsertData(final String Login, final String Mdp, final String Nom, final String Prenom, final String Role, final String DateIdentifie, final String Email) {


        String[] data = new String[7];
        data[0] = Login;
        data[1] = Mdp;
        data[2] = Nom;
        data[3] = Prenom;
        data[4] = Role;
        data[5] = DateIdentifie;
        data[6] = Email;

        ADDAsync myTask = new ADDAsync();
        //start asynctask
        myTask.execute(data);

    }

    public void goBack(View view) {
        final Intent mainIntent = new Intent(register.this, Login.class);
        register.this.startActivity(mainIntent);
        register.this.finish();
        finish();
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
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
                if (isConnected(register.this)) {
                    alertDialog.dismiss();
                } else {

                    final Animation myAnim = AnimationUtils.loadAnimation(register.this, R.anim.bounce);

                    // Use bounce interpolator with amplitude 0.2 and frequency 20
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 30);
                    myAnim.setInterpolator(interpolator);

                    refrech.startAnimation(myAnim);

                    Toast.makeText(register.this, "No Internet", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }


    class ADDAsync extends AsyncTask<String[], String, String> {

        boolean checher;

        protected void onPreExecute() {
            super.onPreExecute();
            checher = true;

            //Start ProgressBar first (Set visibility VISIBLE)
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String[]... strings) {



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
                    data = strings[0];

//                    data[0] = String.valueOf(strings[0][0]);
//                    data[1] = String.valueOf(strings[0][1]);
//                    data[2] = String.valueOf(strings[0][2]);
//                    data[3] = String.valueOf(strings[0][3]);
//                    data[4] = String.valueOf(strings[0][4]);
//                    data[5] = String.valueOf(strings[0][5]);
//                    data[6] = String.valueOf(strings[0][6]);



                   // Log.i("data","Login  "+strings[0][0]+"  MDP"+strings[0][1]+"       "+strings[0][2]+"      "+strings[0][3]+"    "+strings[0][4]+"   "+strings[0][5]+"   "+strings[0][6]);


                    String host = getResources().getString(R.string.hosting);
                    PutData putData = new PutData(host + "/gesloc/LoginRegister/signup.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {

                            String result = putData.getResult();

                            //Toast.makeText(register.this, result, Toast.LENGTH_LONG).show();

                            publishProgress(result);

                            if (result.equals("Sign Up Success")) {


                                /**
                                 * add data to local
                                 */
                                boolean tester = db.insert_emp(String.valueOf(strings[0]), String.valueOf(strings[1]), String.valueOf(strings[2]), String.valueOf(strings[3]), "Admin", String.valueOf(strings[5]), String.valueOf(strings[6]));


                                if (tester) {
                                    checker = true;
                                } else {
                                    checker = false;
                                }

                            } else {
                                checker = false;
                            }

                        }
                    }
                    //End Write and Read data with URL
                }
            });


            return "You are at PostExecute";
        }

        protected void onProgressUpdate(String... a) {
            super.onProgressUpdate(a);

            View parentLayout = findViewById(android.R.id.content);
            Snackbar snack = Snackbar.make(parentLayout, a[0], Snackbar.LENGTH_INDEFINITE).setDuration(3000);
            View sbView = snack.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.color_warning_green));
            snack.show();

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            //End ProgressBar (Set visibility to GONE)
            progressBar.setVisibility(View.GONE);

            if (checker) {

                llProgressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llProgressBar.setVisibility(View.INVISIBLE);
                    }
                }, 3000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent mainIntent = new Intent(register.this, Login.class);
                        register.this.startActivity(mainIntent);
                        register.this.finish();
                    }
                }, 1000);

            } else {

                TextView Text = (TextView) llProgressBar.findViewById(R.id.pbText);
                Text.setText("Votre compte ne pas créé");
                ImageView ImageChecker = (ImageView) llProgressBar.findViewById(R.id.ImageDone);
                ImageChecker.setImageResource(R.drawable.cancel);
                llProgressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llProgressBar.setVisibility(View.INVISIBLE);
                    }
                }, 3000);
            }

        }
    }


}