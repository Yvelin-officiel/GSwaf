package com.example.gswaf;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LikesActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

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


}
