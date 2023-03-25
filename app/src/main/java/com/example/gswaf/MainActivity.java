package com.example.gswaf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {


    // Animations clic
    Animation scaleUp,scaleDown;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        scaleUp = AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this,R.anim.scale_down);

    }

    public void clic(View view){

        int id =  view.getId();
        Intent i ;
        System.out.println("SWAG "+id);
        switch (id){
            case R.id.RandomCocktail:
                System.out.println("SWAG");
                i = new Intent(MainActivity.this, RandomCocktailActivity.class);
                startActivity(i);
                break;
            case R.id.Likes:
                i = new Intent(MainActivity.this, LikesActivity.class);
                startActivity(i);
                break;
            case R.id.Later:
                i = new Intent(MainActivity.this, LaterActivity.class);
                startActivity(i);
                break;
            case R.id.List:
                i = new Intent(MainActivity.this, ListActivity.class);
                startActivity(i);
                break;

        }


        // ANIMATION DE CLIC
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP: {
                        view.startAnimation(scaleDown);
                        break;
                    }
                    case MotionEvent.ACTION_DOWN: {
                        view.startAnimation(scaleUp);
                        break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.top_app_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.favorite:
                Toast.makeText(this, "Item favorite selected", Toast.LENGTH_SHORT).show();
            case R.id.search:
                Toast.makeText(this, "Item serach selected", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}



