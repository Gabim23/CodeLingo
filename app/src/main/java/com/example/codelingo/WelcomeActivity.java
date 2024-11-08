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
    private Button viewRankingButton;  // Declaramos el botón de ranking

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

        // Encuentra el botón de perfil
        profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lastIntent = getIntent();
                String givenUsername = lastIntent.getStringExtra("username");

                Intent newIntent = new Intent(WelcomeActivity.this, UserProfileActivity.class);
                newIntent.putExtra("username", givenUsername);
                startActivity(newIntent);
            }
        });

        // Encuentra el botón de ver ranking
        viewRankingButton = findViewById(R.id.viewRankingButton);
        viewRankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, RankingActivity.class);
                startActivity(intent);
            }
        });
    }
}
