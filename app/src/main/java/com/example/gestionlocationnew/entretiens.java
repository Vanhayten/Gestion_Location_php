
package com.example.gestionlocationnew;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

    public class entretiens extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

        String Nom,Prenom,role;
        DrawerLayout drawerLayout;
        Toolbar toolbar;
        NavigationView navigationView;
        ActionBarDrawerToggle toggle;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_entretiens);

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

            Bundle b = getIntent().getExtras();
            Nom = b.getString("nom");
            Prenom =  b.getString("prenom");
            role = ""+b.getString("role");
            username.setText(Nom+" "+Prenom);
            role1.setText(role);



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
                    T.putExtras(b);
                    startActivity(T);
                    break;
                case R.id.assurances:
                    T = new Intent(this, com.example.gestionlocationnew.assurances.class);
                    b.putString("nom",Nom);
                    b.putString("prenom",Prenom);
                    b.putString("role",role);
                    T.putExtras(b);
                    startActivity(T);
                    break;
                case R.id.entretiens:
                    T = new Intent(this, entretiens.class);
                    b.putString("nom",Nom);
                    b.putString("prenom",Prenom);
                    b.putString("role",role);
                    T.putExtras(b);
                    startActivity(T);
                    break;

                case R.id.recette:
                    T = new Intent(this, mes_recettes.class);
                    b.putString("nom",Nom);
                    b.putString("prenom",Prenom);
                    b.putString("role",role);
                    T.putExtras(b);
                    startActivity(T);
                    break;
                case R.id.charges:
                    T = new Intent(this, mes_charges.class);
                    b.putString("nom",Nom);
                    b.putString("prenom",Prenom);
                    b.putString("role",role);
                    T.putExtras(b);
                    startActivity(T);
                    break;
                case R.id.calendrier:
                    T = new Intent(this, calendrier.class);
                    b.putString("nom",Nom);
                    b.putString("prenom",Prenom);
                    b.putString("role",role);
                    T.putExtras(b);
                    startActivity(T);
                    break;
                case R.id.clients:
                    T = new Intent(this, mes_clients.class);
                    b.putString("nom",Nom);
                    b.putString("prenom",Prenom);
                    b.putString("role",role);
                    T.putExtras(b);
                    startActivity(T);
                    break;

            }
            return false;
        }

    }
