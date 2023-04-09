package com.example.gswaf.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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


public class CocktailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String cocktail;
    int cocktailID;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    Animation scaleUp,scaleDown;
    SharedPreferences sp;

    DBHandler db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail);
        Intent myIntent = getIntent();
        cocktail = myIntent.getStringExtra("cocktailName");
        System.out.println("nom du cocktail: " + cocktail);

        //appel de la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(myToolbar);

        //instanciation des objets concerant le drawer
        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        drawerLayout = findViewById(R.id.cocktail_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        navigationView.setNavigationItemSelectedListener(this);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        CocktailActivity.RequestTaskId rtid = new RequestTaskId();
        rtid.execute();

        sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        db = new DBHandler(this);
    }

    @SuppressLint("StaticFieldLeak")
    private class RequestTaskId extends AsyncTask<Void, Void, Cocktail> {
        /**
         * Lance la tâche asynchrone
         *
         * @return un array list avec les infos du cocktail
         */
        protected Cocktail doInBackground(Void... voids) {
            Cocktail response = new Cocktail();
            try {
                HttpURLConnection connection;
                URL url = new URL("https://www.thecocktaildb.com/api/json/v1/1/search.php?s="+cocktail);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder totalLine = new StringBuilder();
                String ligne = bufferedReader.readLine();
                while (ligne != null) {
                    totalLine.append(ligne);
                    ligne = bufferedReader.readLine();
                }
                JSONObject toDecode = new JSONObject(totalLine.toString());
                // Decode l'objet JSON et récupère le int
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

        /**
         * Méthode qui décode l'objet JSON
         * Extrait l'id du cocktail
         * @param jso L'objet JSON
         * @return Cocktail
         */
        private Cocktail decodeJSON(JSONObject jso) throws Exception {
            Cocktail response = new Cocktail();
            try {
                JSONArray jsoCocktail = jso.getJSONArray("drinks");
                for (int i = 0; i < jsoCocktail.length(); i++) {
                    Spanned id;
                    id = (Html.fromHtml(jsoCocktail.getJSONObject(i).getString("idDrink"), Html.FROM_HTML_MODE_LEGACY));
                    cocktailID = Integer.parseInt(id.toString());
                }
            } catch (Exception e) {
                Log.e("ERROR", "\n Code erreur retourné par le serveur :  " + "\n\n \t Message : " + jso.getString("message"));
            }
            CocktailActivity.RequestTask rt = new CocktailActivity.RequestTask();
            rt.execute();
            return response;

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class RequestTask extends AsyncTask<Void, Void, Cocktail> {
        /**
         * Lance la tâche asynchrone
         *
         * @return un array list avec les infos du cocktail
         */
        protected Cocktail doInBackground(Void... voids) {
            Cocktail response = new Cocktail();
            try {
                HttpURLConnection connection;
                URL url = new URL("https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i="+cocktailID);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder totalLine = new StringBuilder();
                String ligne = bufferedReader.readLine();
                while (ligne != null) {
                    totalLine.append(ligne);
                    ligne = bufferedReader.readLine();
                }
                JSONObject toDecode = new JSONObject(totalLine.toString());
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
         * Extrait les attributs du cocktail et les ajoute à l'arrayList
         *
         * @param jso L'objet JSON
         * @return ArrayList<Cocktail>
         */
        private Cocktail decodeJSON(JSONObject jso) throws Exception {
            Cocktail response = new Cocktail();
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

                    response = new Cocktail(Integer.parseInt(id.toString()), name.toString(), instruction.toString(), urlDecoder, ingredients, measure);
                    cocktailID = Integer.parseInt(id.toString());
                }
            } catch (Exception e) {
                Log.e("ERROR", "\n Code erreur retourné par le serveur :  " + "\n\n \t Message : " + jso.getString("message"));
            }
            return response;
        }


        /**
         * Méthode qui va être appelée à la fin de la requête asynchrone.
         * Génère les TextView et EditText sur la bas edes données reçues
         *
         */
        @SuppressLint("SetTextI18n")
        protected void onPostExecute(Cocktail result) {
            if (result != null) {
                generateImageViewCocktail(result.getImageURL());
                generateTextViewRecipe(result);
                generateNameViewCoktail(result);
            } else {
                TextView t;
                t = new TextView(getApplicationContext());
                t.setText("Erreur");
            }
        }

        /**
         * @param url
         * Génére l'image à partir de l'url
         */
        private void generateImageViewCocktail(String url) {
            ImageView image = findViewById(R.id.image);
            Glide.with(getBaseContext()).load(url).into(image);
        }

        // genère le textView sous l'image
        private void generateTextViewRecipe(Cocktail cocktail) {
            TextView t = findViewById(R.id.recipe);
            t.setText(
                    cocktail.getRecipe()
            );
        }

        /**
         * @param cocktail
         * Génére le nom à partir du cocktail
         */
        private void generateNameViewCoktail(Cocktail cocktail){
            TextView t = findViewById(R.id.CocktailName);
            t.setText(
                    cocktail.getName()
            );
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
                    i = new Intent(CocktailActivity.this, RandomCocktailActivity.class);
                    startActivity(i);
                    break;
                case R.id.likes:
                    i = new Intent(CocktailActivity.this, LikesActivity.class);
                    startActivity(i);
                    break;
                case R.id.accueil:
                    i = new Intent(CocktailActivity.this, MainActivity.class);
                    startActivity(i);
                    break;
                case R.id.search:
                    i = new Intent(CocktailActivity.this, SearchActivity.class);
                    startActivity(i);
                    break;
                case R.id.logout:
                    logout();
                    i = new Intent(CocktailActivity.this, MainActivity.class);
                    startActivity(i);
                    break;
                default:
                    break;
            }

            DrawerLayout drawer = findViewById(R.id.cocktail_drawer_layout);
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
