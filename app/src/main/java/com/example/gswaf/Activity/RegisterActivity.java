package com.example.gswaf.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gswaf.Database.DBHandler;
import com.example.gswaf.R;

public class RegisterActivity extends AppCompatActivity {

    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DBHandler(this);

    }

    /**
     * Méthode qui verifie la validité du formulaire et
     * ajoute les données dans la db si tout est bon
     * @param view boutton submit du formulaire
     */
    public void register(View view) {
        EditText name = findViewById(R.id.username);
        EditText pass = findViewById(R.id.password);
        EditText repass = findViewById(R.id.repass);

        String username = String.valueOf(name.getText());
        String password = String.valueOf(pass.getText());
        String repassword = String.valueOf(repass.getText());

        if (username.length() < 4 || password.length() < 4 || repassword.length() < 4) {
            Toast.makeText(this, "Veuillez renseigner tout les champs avec au moins 4 caractère", Toast.LENGTH_SHORT).show();
        } else {
            if (db.alreadyExist(username)) {
                Toast.makeText(this, "Nom d'utilisateur déjà existant", Toast.LENGTH_SHORT).show();
                name.setError("Nom d'utilisateur déjà existant");
            }
            if (!password.equals(repassword)) {
                Toast.makeText(this, "Veuillez confirmer correctement votre mot de passe", Toast.LENGTH_SHORT).show();
                repass.setError("Confirmation incrorrect");
            }
            if (!db.alreadyExist(username) & password.equals(repassword)) {
                boolean success = db.insertUser(username, password);
                if (success) {
                    Toast.makeText(this, "Inscription réussi", Toast.LENGTH_SHORT).show();
                    toLogin(view);
                } else Toast.makeText(this, "Inscription échouée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Renvoit vers la page de connexion
     * @param view  boutton "déjà inscrit"
     */
        public void toLogin (View view){
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }
