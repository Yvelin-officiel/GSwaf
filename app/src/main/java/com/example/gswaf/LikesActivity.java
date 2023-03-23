package com.example.gswaf;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.CollationKey;
import java.util.List;

public class LikesActivity extends AppCompatActivity {
    DBHandler db;
    LinearLayout ll ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_likes);
        ll = (LinearLayout)findViewById(R.id.layout);
        db = new DBHandler(this);
        generateData();
    }

    public void generateData (){
        List<Cocktail> responses = db.selectLike();

        for (int i = 0 ; i < responses.size(); i++){
            TextView textView = new TextView(this);
            textView.setText(responses.get(i).toString());
            ll.addView(textView);
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
