package com.example.gswaf.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gswaf.R;
import com.google.android.material.navigation.NavigationView;

public class WebViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String name;
    String Url;

    SharedPreferences sp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Intent myIntent = getIntent();
        name = myIntent.getStringExtra("rechercheName");
        System.out.println("Cocktail Rechercher : " + name);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        Url = "https://www.google.fr/search?q="+name;
        webView.loadUrl(Url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
        WebView webView = (WebView) findViewById(R.id.webview);
        if (webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

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

    protected void logout(){
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(this, "Déconnecté", Toast.LENGTH_SHORT).show();
    }

    protected void onDestroy() {
        super.onDestroy();
        logout();
    }
}