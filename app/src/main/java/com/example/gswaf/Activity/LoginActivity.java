package com.example.gswaf.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gswaf.Database.DBHandler;
import com.example.gswaf.R;

public class LoginActivity extends AppCompatActivity {

    DBHandler db;
    SharedPreferences sp;
    int userID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHandler(this);
        sp = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
    }

    /**
     * Méthode qui verifie la validité du formulaire et
     * si les identifiants correspondent à un utilisateur dans la db
     * Partage l'id de l'utilisateur aux autres classes si tout est bon
     * @param view boutton submit du formulaire
     */
    public void login(View view) {
        EditText name = findViewById(R.id.username);
        EditText pass = findViewById(R.id.password);

        String username = String.valueOf(name.getText());
        String password = String.valueOf(pass.getText());

        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "Veuillez renseigner tout les champs ", Toast.LENGTH_SHORT).show();

        } else {
            if (!db.alreadyExist(username)) {
                Toast.makeText(this, "Nom d'utilisateur incorrect", Toast.LENGTH_SHORT).show();
                name.setError("Nom d'utilisateur incorrect");
            } else if (db.checkUsernamePaswword(username, password)) {
                Toast.makeText(this, "Autenthification réussi", Toast.LENGTH_SHORT).show();
                userID = db.selectUserIdByUsername(username);   // Renvoit l'id de l'utilisateur correspondent ou -1 si rien trouvé
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("userID", userID);
                editor.apply();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "Autenthification échouée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Renvoit vers la page de d'inscription
     * @param view  boutton "pas encore inscrit"
     */
    public void toRegister(View view){
        Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(i);
    }

}
