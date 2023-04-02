package com.example.gswaf;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    ListView listView;
    ArrayList<String> cocktail;
    ArrayAdapter<String > adapter;
    Toolbar toolbar;
    List<String> cocktailList;
    String textformater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        toolbar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.search_top_app_bar);

        listView = (ListView) findViewById(R.id.listView);

        listView.setVisibility(View.GONE);

        List<String> cocktailList = Arrays.asList(getResources().getStringArray(R.array.coktail_name));

        System.out.println("test1");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cocktailList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                String text = ""+selectedItem;
                textformater = text.replaceAll("\\s", "_");
                Toast toast = Toast.makeText(SearchActivity.this, textformater, Toast.LENGTH_LONG);
                toast.show();
                Intent i;
                i = new Intent(SearchActivity.this, CocktailActivity.class);
                i.putExtra("cocktailName",textformater);
                startActivity(i);
                }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu search_top_app_bar) {

        System.out.println("test2");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_top_app_bar, search_top_app_bar);
        MenuItem searchViewItem = search_top_app_bar.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // If the list contains the search query than filter the adapter
                // using the filter method with the query as its argument
                if (cocktailList.contains(query)) {
                    adapter.getFilter().filter(query);
                } else {
                    // Search query not found in List View
                    Toast.makeText(SearchActivity.this, "Not found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listView.setVisibility(View.VISIBLE);
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }


    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();
        Intent i ;

        switch (id){
            case R.id.tindercocktail:
                i = new Intent(SearchActivity.this, RandomCocktailActivity.class);
                startActivity(i);
                break;
            case R.id.likes:
                i = new Intent(SearchActivity.this, LikesActivity.class);
                startActivity(i);
                break;
            case R.id.later:
                i = new Intent(SearchActivity.this, LaterActivity.class);
                startActivity(i);
                break;
            case R.id.accueil:
                i = new Intent(SearchActivity.this, MainActivity.class);
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

