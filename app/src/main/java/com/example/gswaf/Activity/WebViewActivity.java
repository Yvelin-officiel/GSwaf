package com.example.gswaf.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gswaf.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class WebViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String name;
    String Url;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    SharedPreferences sp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Toolbar myToolbar = findViewById(R.id.topAppBarWeb);
        setSupportActionBar(myToolbar);

        //création du WebView avec l'url qui prend en parametre le nom du cocktail
        // (recherche google)
        Intent myIntent = getIntent();
        name = myIntent.getStringExtra("rechercheName");
        System.out.println("Cocktail Rechercher : " + name);
        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        Url = "https://www.google.fr/search?q="+name;
        webView.loadUrl(Url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        drawerLayout = findViewById(R.id.webview_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        navigationView.setNavigationItemSelectedListener(this);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            Log.e("ERROR", "NullPointerException dans getSupportActionBar");
        }

        sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
    }

    /**
     * Permet le retour en arrière dans le WebView
     */
    @Override
    public void onBackPressed() {
        WebView webView = findViewById(R.id.webview);
        if (webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else {
            switch (item.getItemId()) {
                case R.id.buttontoolbar:
                    i = new Intent(WebViewActivity.this, SearchActivity.class);
                    startActivity(i);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
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
        Intent i;

        switch (id) {
            case R.id.tindercocktail:
                i = new Intent(WebViewActivity.this, RandomCocktailActivity.class);
                startActivity(i);
                break;
            case R.id.likes:
                i = new Intent(WebViewActivity.this, LikesActivity.class);
                startActivity(i);
                break;
            case R.id.accueil:
                i = new Intent(WebViewActivity.this, MainActivity.class);
                startActivity(i);
                break;
            case R.id.search:
                i = new Intent(WebViewActivity.this, SearchActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                logout();
                i = new Intent(WebViewActivity.this, MainActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.webview_drawer_layout);
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