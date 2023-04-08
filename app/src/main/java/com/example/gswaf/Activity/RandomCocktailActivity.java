package com.example.gswaf.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
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

public class RandomCocktailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout drawerLayout;
    Animation scaleUp,scaleDown;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    DBHandler db;
    SharedPreferences sp;
    int userID;

    Cocktail cocktail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomcocktail);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.topAppBar);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scaleUp = AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this,R.anim.scale_down);



        sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userID = sp.getInt("username", -1);

        db = new DBHandler(this);
        RequestTask rt = new RequestTask();
        rt.execute();
    }

    public void clic(View v) {
        Intent i;
        if (v.getId() == R.id.unlike) {
            i = new Intent(RandomCocktailActivity.this, RandomCocktailActivity.class);
            startActivity(i);
        }
        else if (v.getId() == R.id.like) {

            // Ajoute le cocktail généré actuellement à la liste des likes de l'utilisateur
            int idCocktail = cocktail.getId();
            String name = cocktail.getName();
            String imageURL = cocktail.getImageURL();
            db.insertLike(idCocktail, name, imageURL, userID);

            Toast.makeText(this, "Cocktail ajouter aux likes ", Toast.LENGTH_SHORT).show();
            i = new Intent(RandomCocktailActivity.this, RandomCocktailActivity.class);
            startActivity(i);
        }
    }


    private class RequestTask extends AsyncTask<Void, Void, Cocktail> {
        /**
         * Lance la tâche asynchrone
         *
         * @return un array list avec les infos du cocktail
         */
        protected Cocktail doInBackground(Void... voids) {
            Cocktail response = new Cocktail();
            try {
                HttpURLConnection connection = null;
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
         * Extrait les attributs du cocktail et les ajoute à l'arrayList
         *
         * @param jso L'objet JSON
         * @return ArrayList<Cocktail>
         * @throws Exception
         */
        private Cocktail decodeJSON(JSONObject jso) throws Exception {
            List<String> measure = new ArrayList<>();
            List<String> ingredients = new ArrayList<>();
            System.out.println("Try decode");

            try {
                System.out.println("Try decode 2");
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
         * @param result
         */
        protected void onPostExecute(Cocktail result) {
            LinearLayout layout = findViewById(R.id.layoutCocktail);
            try{
                generateImageViewCocktail(result.getImageURL(), layout);
                generateTextViewRecipe(result, layout);
                generateNameViewCoktail(result, layout);
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
        private void generateImageViewCocktail(String url, LinearLayout layout) {
            ImageView image = findViewById(R.id.image);
            Glide.with(getBaseContext()).load(url).into(image);
        }

        // genère le textView sous l'image
        private void generateTextViewRecipe(Cocktail cocktail, LinearLayout layout) {
            TextView t = findViewById(R.id.recipe);

            t.setText(cocktail.getRecipe());
        }

        private void generateNameViewCoktail(Cocktail cocktail, LinearLayout layout){
            TextView t = findViewById(R.id.CocktailName);
            t.setText(
                    cocktail.getName()
            );
        }
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
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.random_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

}
