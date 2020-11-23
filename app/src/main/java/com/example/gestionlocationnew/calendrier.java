package com.example.gestionlocationnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class calendrier extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GoogleSignInClient mGoogleSignInClient;


    String Nom,Prenom,role,login;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;



    /**
     * calandar
     */
    CustomCalendarView customCalendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrier);



        /**
         * google
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        Bundle b = getIntent().getExtras();
        Nom = b.getString("nom");
        Prenom =  b.getString("prenom");
        role = ""+b.getString("role");
        login = ""+b.getString("login");




        customCalendarView = findViewById(R.id.custom_calendar_view);




        /**
         * calander
         */



        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //-------------------------


        NavigationView navigationView1 = (NavigationView)findViewById(R.id.navigationView);
        View headerView = navigationView1.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.unser_name);
        TextView role1 = headerView.findViewById(R.id.role);


        CircleImageView profile = (CircleImageView)headerView.findViewById(R.id.profilpic);


        /**
         * get image from google and gut its in profile
         */
        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
        String urlsImage = sp.getString("URLImage","");
        if(!urlsImage.equals("")){
            ImageLoadTask imageLoadTask = (ImageLoadTask) new ImageLoadTask(urlsImage, profile).execute();
        }

        username.setText(Nom+" "+Prenom);
        //role1.setText(role);
        role1.setText(login);





    }






    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent T;
        Bundle b = new Bundle();
        switch (menuItem.getItemId()){
            case R.id.vihicule:
                T = new Intent(this, vehicules.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);


                break;
            case R.id.assurances:
                T = new Intent(this, assurances.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);


                break;
            case R.id.entretiens:
                T = new Intent(this, entretiens.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);


                break;
            case R.id.recette:
                T = new Intent(this, mes_recettes.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);


                break;
            case R.id.charges:
                T = new Intent(this, mes_charges.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);


                break;
            case R.id.calendrier:
                T = new Intent(this, calendrier.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);


                break;
            case R.id.clients:
                T = new Intent(this, mes_clients.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;
            case R.id.locations:
                T = new Intent(this, mes_location.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;

            case R.id.logout:

                /**
                 * Sing out from google
                 */
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(calendrier.this, " déconnecté avec succès", Toast.LENGTH_SHORT).show();
                                // ...
                            }
                        });

                T = new Intent(this, Login.class);
                SharedPreferences sp;
                sp = getSharedPreferences("login",MODE_PRIVATE);
                sp.edit().putBoolean("logged",false).apply();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);
                break;
        }
        return false;
    }

}
