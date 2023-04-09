package com.example.gswaf.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RandomCocktailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout drawerLayout;

    public ActionBarDrawerToggle actionBarDrawerToggle;

    DBHandler db;
    SharedPreferences sp;
    int userID;

    Cocktail cocktail;
    int nbCocktailLike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomcocktail);


        Toolbar myToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(myToolbar);

        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        drawerLayout = findViewById(R.id.random_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        navigationView.setNavigationItemSelectedListener(this);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userID = sp.getInt("userID", -1);
        nbCocktailLike = sp.getInt("nbCocktailLike", 0);

        db = new DBHandler(this);
        RequestTask rt = new RequestTask();
        rt.execute();
    }

    /**
     * Relance l'activité pour générer un nouveau cocktail,
     * l'ajoute à la DB si bouton like cliqué
     * @param v récupère le boutton appuyé
     */
    public void clic(View v) {

        Intent i;
        switch (v.getId()) {
            case R.id.unlike:
                i = new Intent(RandomCocktailActivity.this, RandomCocktailActivity.class);
                startActivity(i);
                break;
            case R.id.like:
                if (userID == -1)
                    Toast.makeText(this, "Veuillez vous connectez pour avoir accès aux likes !", Toast.LENGTH_SHORT).show();
                else {
                    int idCocktail = cocktail.getId();
                    String name = cocktail.getName();
                    String imageURL = cocktail.getImageURL();
                    db.insertLike(idCocktail, name, imageURL, userID);

                    nbCocktailLike++;
                    System.out.println(nbCocktailLike);
                    if (nbCocktailLike >= 4) {
                        nbCocktailLike = 0;
                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        final long[] pattern = {1000, 1000};
                        vibrator.vibrate(pattern, -1);
                        Toast.makeText(this, "Il serait temps de les boires aussi !", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Cocktail ajouté aux likes ", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("nbCocktailLike", nbCocktailLike);
                    editor.apply();
                    i = new Intent(RandomCocktailActivity.this, RandomCocktailActivity.class);
                    startActivity(i);
                }
                break;
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class RequestTask extends AsyncTask<Void, Void, Cocktail> {
        /**
         * Lance la tâche asynchrone
         * @return un array list avec les infos du cocktail
         */
        protected Cocktail doInBackground(Void... voids) {
            Cocktail response = new Cocktail();
            try {
                HttpURLConnection connection;
                URL url = new URL("https://www.thecocktaildb.com/api/json/v1/1/random.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String totalLine = "";
                String ligne = bufferedReader.readLine();
                while (ligne != null) {
                    totalLine += ligne;
                    ligne = bufferedReader.readLine();
                }
                JSONObject toDecode = new JSONObject(totalLine);
                // Decode l'objet JSON et récupère le ArrayList
                response = decodeJSON(toDecode);
            } catch (UnsupportedEncodingException e) {
                Log.e("ERROR", "problème d'encodage");
            } catch (MalformedURLException e) {
                Log.e("ERROR", "problème d'url");
            } catch (IOException e) {
                Log.e("ERROR", "problème d'entrée sortie");
            } catch (Exception e) {
                Log.e("ERROR", "autre erreur");
            }
            return response;
        }

        //décodage du JSON et retourne la chaîne de caractère à afficher

        /**
         * Méthode qui décode l'objet JSON
         * Extrait les attributs du cocktail et les ajoutes au cocktail renvoyé
         * @param jso L'objet JSON
         * @return Cocktail
         */
        private Cocktail decodeJSON(JSONObject jso) throws Exception {
            List<String> measure = new ArrayList<>();
            List<String> ingredients = new ArrayList<>();

            try {
                JSONArray jsoCocktail = jso.getJSONArray("drinks");
                for (int i = 0; i < jsoCocktail.length(); i++) {
                    Spanned id, name, instruction, imageURL, ingreds, measu;
                    id = (Html.fromHtml(jsoCocktail.getJSONObject(i).getString("idDrink"), Html.FROM_HTML_MODE_LEGACY));
                    name = (Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strDrink"), Html.FROM_HTML_MODE_LEGACY));
                    instruction = (Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strInstructions"), Html.FROM_HTML_MODE_LEGACY));

                    // URL à décoder
                    imageURL = (Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strDrinkThumb"), Html.FROM_HTML_MODE_LEGACY));
                    String urlDecoder = URLDecoder.decode(imageURL.toString(), StandardCharsets.UTF_8.name());


                    // L'API n'envoie que 15 ingredients et mesures maximum
                    for (int j = 1; j <= 15; j++) {

                        //Verifie si l'ingredient n'est pas null    (pas d'ingredient, pas de mesure)
                        if ((Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strIngredient" + j), Html.FROM_HTML_MODE_LEGACY).toString())
                                .equals("null")) {
                            break;
                        } else {
                            ingreds = (Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strIngredient" + j), Html.FROM_HTML_MODE_LEGACY));
                            ingredients.add(ingreds.toString());


                            // Verifie si une mesure correspond a l'ingredient
                            if ((Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strMeasure" + j), Html.FROM_HTML_MODE_LEGACY).toString())
                                    .equals("null"))
                                measure.add(" Pas d'info ");
                            else {
                                measu = (Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strMeasure" + URLEncoder.encode(String.valueOf(j), "utf-8")), Html.FROM_HTML_MODE_LEGACY));
                                measure.add(measu.toString());
                            }
                        }
                    }
                    cocktail = new Cocktail(Integer.parseInt(id.toString()), name.toString(), instruction.toString(), urlDecoder, ingredients, measure);
                }
            } catch (Exception e) {
                Log.e("ERROR", "\n Code erreur retourné par le serveur :  " + "\n\n \t Message : " + jso.getString("message"));
            }
            return cocktail;
        }


        /**
         * Méthode qui va être appelée à la fin de la requête asynchrone.
         * Génère les TextView et EditText sur la bas edes données reçues
         *
         */
        @SuppressLint("SetTextI18n")
        protected void onPostExecute(Cocktail result) {
            LinearLayout layout = findViewById(R.id.layoutCocktail);
            try{
                generateImageViewCocktail(result.getImageURL());
                generateTextViewRecipe(result);
                generateNameViewCoktail(result);
            }  catch (java.lang.NullPointerException e) {
                TextView t;
                t = new TextView(getApplicationContext());
                t.setText("Nous n'avons pas réussi à nous connecter");
                t.setGravity(Gravity.CENTER);
                t.setTextSize(30);
                t.setTextColor(Color.parseColor("#bdbdbd"));
                layout.addView(t);
                Log.e("ERROR", "cocktail non trouvé");
            }
        }

        /**
         * Affiche l'image du cocktail généré
         *
         * @param url L'url de l'image a affiché
         */
        private void generateImageViewCocktail(String url) {
            ImageView image = findViewById(R.id.image);
            Glide.with(getBaseContext()).load(url).into(image);
        }

        // genère le textView sous l'image
        private void generateTextViewRecipe(Cocktail cocktail) {
            TextView t = findViewById(R.id.recipe);

            t.setText(cocktail.getRecipe());
        }

        private void generateNameViewCoktail(Cocktail cocktail){
            TextView t = findViewById(R.id.CocktailName);
            t.setText(
                    cocktail.getName()
            );
        }
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
                i = new Intent(RandomCocktailActivity.this, SearchActivity.class);
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
                i = new Intent(RandomCocktailActivity.this, RandomCocktailActivity.class);
                startActivity(i);
                break;
            case R.id.likes:
                i = new Intent(RandomCocktailActivity.this, LikesActivity.class);
                startActivity(i);
                break;
            case R.id.accueil:
                i = new Intent(RandomCocktailActivity.this, MainActivity.class);
                startActivity(i);
                break;
            case R.id.search:
                i = new Intent(RandomCocktailActivity.this, SearchActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                logout();
                i = new Intent(RandomCocktailActivity.this, MainActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.random_drawer_layout);
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
