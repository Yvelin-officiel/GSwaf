package com.example.gswaf.Activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.gswaf.Database.DBHandler;
import com.example.gswaf.JavaClass.Cocktail;
import com.example.gswaf.R;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

public class LikesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    DBHandler db;
    SharedPreferences sp;
    int userID;
    LinearLayout bigLinear;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        Toolbar myToolbar = findViewById(R.id.topAppBar);
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
        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            Log.e("ERROR", "NullPointerException dans getSupportActionBar");
        }

        sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userID = sp.getInt("userID", -1);
        System.out.println("User loged : "+ userID);


        bigLinear = findViewById(R.id.likesLayout);
        db = new DBHandler(this);

        List<Cocktail> cocktails = db.selectLike(userID);

        if (userID == -1){
            TextView textView = new TextView(this);
            textView.setText("Veuillez vous connecter pour accéder à vos likes !");
            textView.setTextSize(40);

            bigLinear.addView(textView);
        }else
            for (int i = 0; i < cocktails.size();i++){
                generateCocktailInfos(cocktails.get(i));
            }
    }

    /**
     * creer un layout possedant l'image, le nom du cocktail et un bouton supprimant le cocktail des likes
     * @param cocktail permet de recuperer les donnees
     */
    private void generateCocktailInfos(Cocktail cocktail){
        LinearLayout singleLayout = new LinearLayout(getApplicationContext());
        LayoutParams params = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        singleLayout.setLayoutParams(params);
        singleLayout.setGravity(Gravity.CENTER);
        singleLayout.setBackgroundResource(R.color.likeLayoutColor);

        String imageURL = cocktail.getImageURL();
        generateImageViewCocktail(imageURL, singleLayout);

        String name = cocktail.getName();
        generateNameViewCocktail(name, singleLayout);

        int id = cocktail.getId();
        generateDeleteButtonViewCocktail(id, singleLayout);

        bigLinear.addView(singleLayout);

    }
    /**
     * Affiche l'image du cocktail généré
     * @param url L'url de l'image a affiché
     */
    private void generateImageViewCocktail(String url, LinearLayout layout) {
        ImageView image = new ImageView(LikesActivity.this);


        try {
            layout.addView(image);
            image.getLayoutParams().height=300;
            if (url.equals(""))
                image.setImageResource(R.drawable.cocktailfailed);
            else
                Glide.with(getBaseContext()).load(url).into(image);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Genère un Textview affichant le nom du cocktail
     * @param name le nom à afficher
     * @param layout dans lequel afficher le textView
     */
    private void generateNameViewCocktail(String name, LinearLayout layout){
        TextView t = new TextView(getApplicationContext());

        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT, 1f);
        t.setLayoutParams(params);

        t.setText(name);
        t.setTextSize(24);
        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        t.setGravity(Gravity.CENTER);
        t.setTextColor(Color.parseColor("#000000"));

        layout.addView(t);

        t.setOnClickListener(v -> {
            Intent i;
            i = new Intent(LikesActivity.this, CocktailActivity.class);
            i.putExtra("cocktailName",name);
            startActivity(i);
        });
    }

    /**
     * Genère un bouton qui supprime le cocktail des likes
     * @param idCocktail L'id du cocktail à supprimer
     * @param layout dans lequel afficher le boutton
     */
    private void generateDeleteButtonViewCocktail(int idCocktail, LinearLayout layout){
        ImageButton deleteButton = new ImageButton(getApplicationContext());
        deleteButton.setImageResource(R.drawable.baseline_delete_48);
        deleteButton.setBackgroundResource(R.color.likeLayoutColor);

        deleteButton.setOnClickListener(v -> {
            bigLinear.removeView(layout);
            boolean success = db.deleteLike(idCocktail, userID);
            if (success)
                Toast.makeText(this, "Le cocktail à bien été supprimé", Toast.LENGTH_SHORT).show();
        });

        layout.addView(deleteButton);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }

    /**
     * @param item
     * onOptionItemSelected permet de gérer les clis sur les différent item du menu top_app_bar
     * (toolbar principale de l'application)
     * l'unique item nous ammène au search activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else {
            if (item.getItemId() == R.id.buttontoolbar) {
                i = new Intent(LikesActivity.this, SearchActivity.class);
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
    @SuppressLint("NonConstantResourceId")
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
            case R.id.logout:
                logout();
                i = new Intent(LikesActivity.this, MainActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
        DrawerLayout drawer = findViewById(R.id.like_drawer_layout);
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
        db.close();
        logout();
    }
}

