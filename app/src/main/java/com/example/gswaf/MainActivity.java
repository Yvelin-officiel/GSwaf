package com.example.gswaf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    // Animations clic
    Animation scaleUp,scaleDown;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(myToolbar);

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

        scaleUp = AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this,R.anim.scale_down);

    }


    public void clic(View view){

        int id =  view.getId();
        Intent i ;
        switch (id){
            case R.id.RandomCocktail:
                i = new Intent(MainActivity.this, RandomCocktailActivity.class);
                startActivity(i);
                break;
            case R.id.Likes:
                i = new Intent(MainActivity.this, LikesActivity.class);
                startActivity(i);
                break;
            case R.id.Later:
                i = new Intent(MainActivity.this, LaterActivity.class);
                startActivity(i);
                break;
            case R.id.List:
                i = new Intent(MainActivity.this, ListActivity.class);
                startActivity(i);
                break;
            case R.id.Register:
                i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.Login:
                i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);

        }


        // ANIMATION DE CLIC
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP: {
                        view.startAnimation(scaleDown);
                        break;
                    }
                    case MotionEvent.ACTION_DOWN: {
                        view.startAnimation(scaleUp);
                        break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.top_app_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else {
            switch (item.getItemId()) {
                case R.id.favorite:

                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    private void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    private void configureNavigationView(){
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
    }


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
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}





