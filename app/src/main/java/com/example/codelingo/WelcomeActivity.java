package com.example.codelingo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button btnPlay;
    private Button profileButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Encuentra el botón de "Jugar"
        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LevelsActivity.class);
                startActivity(intent);
            }
        });

        profileButton = findViewById(R.id.profileButton);


        // Boton interfaz perfil de usuario
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lastIntent = getIntent();
                String givenUsername = lastIntent.getStringExtra("username");

                Intent newIntent = new Intent(WelcomeActivity.this, UserProfileActivity.class);
                newIntent.putExtra("username", givenUsername);
                startActivity(newIntent);
                finish();
            }
        });
    }
}
