package com.example.gswaf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
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


public class CocktailActivity extends AppCompatActivity {

    private String cocktail;
    int cocktailID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail);
        Intent myIntent = getIntent();
        cocktail = myIntent.getStringExtra("cocktailName");
        System.out.println("nom du cocktail: " + cocktail);
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
         * @param result
         */
        protected void onPostExecute(Cocktail result) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.layoutCocktail);
            if (result != null) {
                generateImageViewCocktail(result.getImageURL(), layout);
                generateTextViewRecipe(result, layout);
            } else {
                TextView t;
                t = new TextView(getApplicationContext());
                t.setText("Erreur");
            }
        }

        private void generateImageViewCocktail(String url, LinearLayout layout) {
            ImageView image = findViewById(R.id.image);
            Glide.with(getBaseContext()).load(url).into(image);
        }

        // genère le textView sous l'image
        private void generateTextViewRecipe(Cocktail cocktail, LinearLayout layout) {
            TextView t = findViewById(R.id.recipe);
            t.setText(
                    cocktail.getRecipe()
            );
        }

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
                case R.id.later:
                    i = new Intent(CocktailActivity.this, LaterActivity.class);
                    startActivity(i);
                    break;
                case R.id.accueil:
                    i = new Intent(CocktailActivity.this, MainActivity.class);
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
}