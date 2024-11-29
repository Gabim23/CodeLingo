package com.example.codelingo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;



public class WelcomeActivity extends AppCompatActivity {

    private Button btnPlay;
    private Button profileButton;
    private Button viewRankingButton;  // Botón de ranking
    private Button settings;
    private Button SelectionLevel;
    private Button btnToggleDarkMode;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Encuentra el botón de "Jugar"
        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LevelsActivity.class);
            startActivity(intent);
        });

        // Encuentra el botón de perfil
        profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(v -> {
            // Recuperamos el nombre de usuario desde SharedPreferences
            SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String givenUsername = sharedPref.getString("username", null);

            if (givenUsername != null) {
                Intent newIntent = new Intent(WelcomeActivity.this, UserProfileActivity.class);
                newIntent.putExtra("username", givenUsername);
                startActivity(newIntent);
            } else {
                // Si no se encuentra el nombre de usuario, redirigimos al login
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Encuentra el botón de ver ranking
        viewRankingButton = findViewById(R.id.viewRankingButton);
        viewRankingButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, RankingActivity.class);
            startActivity(intent);
        });

        // Encuentra el botón de configuraciones
        settings = findViewById(R.id.settings);
        settings.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, Config.class);
            startActivity(intent);
        });

        // Encuentra el botón de selección de nivel
        SelectionLevel = findViewById(R.id.SelectionLevel);
        SelectionLevel.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, TestLevelActivity.class);
            startActivity(intent);
        });

        // Botón de cambiar el modo oscuro
        btnToggleDarkMode = findViewById(R.id.btnToggleDarkMode);

        // Recuperar el estado preferido del modo oscuro desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);

        // Configurar el modo oscuro según la preferencia guardada
        AppCompatDelegate.setDefaultNightMode(isDarkModeEnabled ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        // Configurar el listener del botón
        btnToggleDarkMode.setOnClickListener(v -> {
            // Animar el botón
            btnToggleDarkMode.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_animation));

            // Cambiar el modo oscuro después de la animación
            btnToggleDarkMode.postDelayed(() -> {
                boolean newDarkModeState = !isDarkModeEnabled;
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Guardar el nuevo estado
                editor.putBoolean("dark_mode", newDarkModeState);
                editor.apply();

                // Cambiar el modo
                AppCompatDelegate.setDefaultNightMode(newDarkModeState ?
                        AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

                // Actualizar la apariencia del botón
                updateButtonAppearance(newDarkModeState);
            }, 400); // Tiempo que dura la animación
        });

        // Actualiza la apariencia del botón según el estado del modo oscuro
        updateButtonAppearance(isDarkModeEnabled);
    }

    /**
     * Actualiza la apariencia del botón según el estado del modo oscuro.
     */
    private void updateButtonAppearance(boolean isDarkModeEnabled) {
        if (isDarkModeEnabled) {
            btnToggleDarkMode.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sun, 0, 0);
            btnToggleDarkMode.setText("Modo Claro");
        } else {
            btnToggleDarkMode.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.moon_stars_, 0, 0);
            btnToggleDarkMode.setText("Modo Oscuro");
        }
    }
}
