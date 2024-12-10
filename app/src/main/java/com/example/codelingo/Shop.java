package com.example.codelingo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Shop extends AppCompatActivity {
    private TextView tvLives; // TextView para mostrar las vidas
    private int lives; // Variable para almacenar las vidas del usuario


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        tvLives = findViewById(R.id.tvLives);

        // Cargar las vidas del usuario
        loadUserLives();
    }


    // Método para cargar las vidas del usuario desde el archivo users.txt
    private void loadUserLives() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        if (username != null) {
            try {
                FileInputStream fis = openFileInput("users.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] credentials = line.split(",");
                    if (credentials[0].equals(username)) {
                        lives = Integer.parseInt(credentials[2]); // Asumimos que las vidas están en la tercera columna
                        break;
                    }
                }
                reader.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
                lives = 0; // Valor predeterminado si hay un error
            }
        } else {
            lives = 0; // Valor predeterminado si no hay usuario
        }

        // Actualizar el TextView con las vidas actuales
        updateLivesDisplay(lives);
    }

    // Método para actualizar el TextView con las vidas
    private void updateLivesDisplay(int lives) {
        tvLives.setText(String.valueOf(lives)); // Convierte el número a String antes de asignarlo
    }
}


