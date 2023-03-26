package com.example.gswaf;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class LikesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout drawerLayout;
    Animation scaleUp,scaleDown;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(myToolbar);

        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        drawerLayout = findViewById(R.id.like_drawer_layout);
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

        List<Cocktail> image_details = getListData();
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new CustomListAdapter(this,image_details));

        // When the user clicks on the ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Cocktail Cocktail = (Cocktail) o;
                Toast.makeText(LikesActivity .this, "Selected :" + " " + Cocktail, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Liste des coktail dans les likes qui doit être afficher
    private List<String> ingredients;
    private List<String> measures;
    private  List<Cocktail> getListData() {
        List<Cocktail> list = new ArrayList<Cocktail>();
        Cocktail mojito = new Cocktail(1, "mojito","","menthe", ingredients, measures);
        Cocktail CubaLibre = new Cocktail(2, "Cuba Libre","", "", ingredients, measures);
        Cocktail CubaLibre2 = new Cocktail(2, "Cuba Libre","", "", ingredients, measures);
        Cocktail CubaLibre3 = new Cocktail(2, "Cuba Libre","", "", ingredients, measures);
        Cocktail CubaLibre4 = new Cocktail(2, "Cuba Libre","", "", ingredients, measures);
        Cocktail CubaLibre5 = new Cocktail(2, "Cuba Libre","", "", ingredients, measures);
        Cocktail CubaLibre6 = new Cocktail(2, "Cuba Libre","", "", ingredients, measures);
        Cocktail CubaLibre7 = new Cocktail(2, "Cuba Libre","", "", ingredients, measures);

        list.add(mojito);
        list.add(CubaLibre);
        list.add(CubaLibre2);
        list.add(CubaLibre3);
        list.add(CubaLibre4);
        list.add(CubaLibre5);
        list.add(CubaLibre6);
        list.add(CubaLibre7);


        return list;
    }

    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();
        Intent i ;

        switch (id){
            case R.id.tindercocktail:
                i = new Intent(LikesActivity.this, RandomCocktailActivity.class);
                startActivity(i);
                break;
            case R.id.likes:
                i = new Intent(LikesActivity.this, LikesActivity.class);
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
