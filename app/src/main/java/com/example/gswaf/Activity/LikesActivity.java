package com.example.gswaf.Activity;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import java.util.List;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.example.gswaf.Database.DBHandler;
import com.example.gswaf.JavaClass.Cocktail;
import com.example.gswaf.R;
import com.google.android.material.navigation.NavigationView;

public class LikesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout drawerLayout;
    Animation scaleUp,scaleDown;
    public ActionBarDrawerToggle actionBarDrawerToggle;



    DBHandler db;
    SharedPreferences sp;
    int userID;
    List<Cocktail> cocktails;

    LinearLayout bigLinear;
    @Override
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

        sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userID = sp.getInt("username", -1);


        bigLinear = findViewById(R.id.likesLayout);
        db = new DBHandler(this);

        List<Cocktail> cocktails = db.selectLike(userID);

        for (int i = 0; i < cocktails.size();i++){
            generateCocktailInfos(cocktails.get(i));

        }



    }


    private void generateCocktailInfos(Cocktail cocktail){
        LinearLayout singleLayout = new LinearLayout(getApplicationContext());

        String imageURL = cocktail.getImageURL();
        generateImageViewCocktail(imageURL, singleLayout);

        String name = cocktail.getName();
        generateNameViewCocktail(name, singleLayout);

        generateDeleteButtonViewCocktail(singleLayout);

        bigLinear.addView(singleLayout);

    }
    /**
     * Affiche l'image du cocktail généré
     * @param url L'url de l'image a affiché
     */
    private void generateImageViewCocktail(String url, LinearLayout layout) {
        ImageView image = new ImageView(LikesActivity.this);

        try {
            image.setImageResource(R.drawable.cocktailfailed);
            layout.addView(image);
            image.getLayoutParams().height=100;
           // Glide.with(getBaseContext()).load(url).into(image);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

    }

    private void generateNameViewCocktail(String name, LinearLayout layout){
        TextView t = new TextView(getApplicationContext());
        t.setText(name);
        layout.addView(t);
    }
    private void generateDeleteButtonViewCocktail(LinearLayout layout){
        Button bouton = new Button(getApplicationContext());
        bouton.setText("supprimer");

        bouton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                bigLinear.removeView(layout);
            }
        });

        layout.addView(bouton);
    }

public View.OnClickListener deleteCocktail(){

    return null;
}

    protected void onDestroy() {
        super.onDestroy();
        db.close();
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
            case R.id.accueil:
                i = new Intent(LikesActivity.this, MainActivity.class);
                startActivity(i);
                break;
            case R.id.search:
                i = new Intent(LikesActivity.this, SearchActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.like_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


}

