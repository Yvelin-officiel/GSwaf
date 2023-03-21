package com.example.gswaf;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

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

public class RandomCocktailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomcocktail);




        RequestTask rt = new RequestTask();
        rt.execute();

    }


    private class RequestTask extends AsyncTask<Void, Void,  Cocktail> {
        /**
         * Lance la tâche asynchrone
         * @return un array list avec les questions et les réponses
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
                for (int i = 0; i < jsoCocktail.length(); i++){
                    Spanned id , name , instruction , imageURL,ingreds, measu;
                    id = (Html.fromHtml(jsoCocktail.getJSONObject(i).getString("idDrink"), Html.FROM_HTML_MODE_LEGACY));
                    name = (Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strDrink"), Html.FROM_HTML_MODE_LEGACY));
                    instruction = (Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strInstructions"), Html.FROM_HTML_MODE_LEGACY));

                    // URL à décoder
                    imageURL = (Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strDrinkThumb"), Html.FROM_HTML_MODE_LEGACY));
                    String urlDecoder = URLDecoder.decode(imageURL.toString(), StandardCharsets.UTF_8.name());


                    // L'API n'envoie que 15 ingredients et mesures maximum
                    for (int j = 1 ; j <= 15; j++) {

                        //Verifie si l'ingredient n'est pas null    (pas d'ingredient, pas de mesure)
                        if ((Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strIngredient"+j), Html.FROM_HTML_MODE_LEGACY).toString())
                                .compareTo("null") == 0) {
                            break;
                        } else {
                            ingreds = (Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strIngredient"+j), Html.FROM_HTML_MODE_LEGACY));
                            ingredients.add(ingreds.toString());


                            // Verifie si une mesure correspond a l'ingredient
                            if ((Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strMeasure"+j), Html.FROM_HTML_MODE_LEGACY).toString())
                                    .compareTo("null") == 0) ;
                            else {
                                measu = (Html.fromHtml(jsoCocktail.getJSONObject(i).getString("strMeasure" + URLEncoder.encode(String.valueOf(j), "utf-8")), Html.FROM_HTML_MODE_LEGACY));
                                measure.add(measu.toString());
                            }

                        }
                    }

                    response = new Cocktail(Integer.parseInt(id.toString()), name.toString(), instruction.toString(), urlDecoder, ingredients, measure);

                }
            } catch (Exception e) {
                Log.e("ERROR","\n Code erreur retourné par le serveur :  "  + "\n\n \t Message : " + jso.getString("message"));
            }
            return response;
        }


        /**
         * Méthode qui va être appelée à la fin de la requête asynchrone.
         * Génère les TextView et EditText sur la bas edes données reçues
         * @param result
         */
        protected void onPostExecute(Cocktail result) {
            LinearLayout layout  = (LinearLayout) findViewById(R.id.layoutCocktail);
            if (result != null){
                generateImageViewCocktail(result.getImageURL(),layout);
                generateTextViewRecipe(result,layout);
            } else {
                TextView t;
                t = new TextView(getApplicationContext());
                t.setText("Erreur");
            }
        }

        /**
         * Affiche l'image du cocktail généré
         * @param url L'url de l'image a affiché
         */
        private void generateImageViewCocktail(String url, LinearLayout layout){
            ImageView image = findViewById(R.id.image);
            Glide.with(getBaseContext()).load(url).into(image);
        }

        // genère le textView sous l'image
        private void generateTextViewRecipe (Cocktail cocktail, LinearLayout layout){
            TextView t = findViewById(R.id.recipe);
            t.setText(
                    cocktail.getRecipe()
            );

        }

    }

}
