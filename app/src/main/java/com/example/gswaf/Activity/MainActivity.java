package com.example.gswaf.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gswaf.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    SharedPreferences sp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        Toolbar myToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(myToolbar);

        //instanciation des objets concerant le drawer
        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        drawerLayout = findViewById(R.id.main_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        navigationView.setNavigationItemSelectedListener(this);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void clic(View view){

        int id =  view.getId();
        Intent i ;
        switch (id){
            case R.id.Register:
                i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.Login:
                i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.gif:
                i = new Intent(MainActivity.this, RandomCocktailActivity.class);
                startActivity(i);
                break;

        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }

    /**
     *
     * @param item
     * onOptionItemSelected permet de gérer les clis sur les différent item du menu top_app_bar
     * (toolbar principale de l'application)
     * l'unique item nous ammène au search activity
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else {
            if (item.getItemId() == R.id.buttontoolbar) {
                i = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(i);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     * @param item The selected item
     *
     * onNavigationItemSelected permet de gérer les clis sur les différent item du menu navigation_menu
     * (drawer disponible sur le coté gauche de l'appplication)
     * Chaque item nous emmène sur une autre activity ou permet la déconnexion
     *
     */
    public boolean onNavigationItemSelected(MenuItem item) {


        // 4 - Handle Navigation Item Click
        int id = item.getItemId();
        Intent i ;

        switch (id){
            case R.id.tindercocktail:
                i = new Intent(MainActivity.this, RandomCocktailActivity.class);
                startActivity(i);
                break;
            case R.id.likes:
                i = new Intent(MainActivity.this, LikesActivity.class);
                startActivity(i);
                break;
            case R.id.accueil:
                i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                break;
            case R.id.search:
                i = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                logout();
                i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * Déconnecte l'utilisateur
     */
    protected void logout(){
        try {
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
            Toast.makeText(this, "Déconnecté", Toast.LENGTH_SHORT).show();
        }   catch (NullPointerException e){
            Log.e("ERROR", "Utilisateur déconnecté");
        }   catch (Exception e) {
            Log.e("ERROR", "Erreur avec sharedPreferences");
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        logout();
    }
}





