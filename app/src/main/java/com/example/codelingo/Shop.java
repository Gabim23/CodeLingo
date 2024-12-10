package com.example.codelingo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class Shop extends AppCompatActivity {
    private TextView tvLives;
    private TextView tvCoins;// TextView para mostrar las vidas
    private int lives;
    private int coins;// Variable para almacenar las vidas del usuario

    private Button buttonComprarVida1;
    private Button buttonComprarVida3;
    private Button buttonComprarVida5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        tvLives = findViewById(R.id.tvLives);
        tvCoins = findViewById(R.id.tvCoins);

        buttonComprarVida1 = findViewById(R.id.button_comprar_vida1);
        buttonComprarVida3 = findViewById(R.id.button_comprar_vida3);
        buttonComprarVida5 = findViewById(R.id.button_comprar_vida5);

        // Cargar las vidas del usuario
        loadUserLives();
        loadUserCoins();

        // Configurar los botones de compra
        buttonComprarVida1.setOnClickListener(v -> buyLife(1, 15));
        buttonComprarVida3.setOnClickListener(v -> buyLife(3, 40));
        buttonComprarVida5.setOnClickListener(v -> buyLife(5, 60));
    }


    private void buyLife(int livesToBuy, int cost) {
        if (livesToBuy == 1 && lives >= 5) {
            Toast.makeText(this, "No puedes tener más de 5 vidas", Toast.LENGTH_SHORT).show();
            return;
        } else if (livesToBuy == 3 && lives >= 3) {
            Toast.makeText(this, "No puedes tener más de 5 vidas", Toast.LENGTH_SHORT).show();
            return;
        } else if (livesToBuy == 5 && lives > 0) {
            Toast.makeText(this, "No puedes tener más de 5 vidas", Toast.LENGTH_SHORT).show();
            return;
        }

        if (coins >= cost) {
            // Actualizar vidas y monedas
            lives += livesToBuy;
            coins -= cost;

            // Guardar los cambios en el archivo
            saveUserData();

            // Actualizar las vistas
            updateLivesDisplay(lives);
            updateCoinsDisplay(coins);

        } else {
            Toast.makeText(this, "No tienes suficientes monedas", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        if (username != null) {
            try {
                FileInputStream fis = openFileInput("users.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                StringBuilder fileContent = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] credentials = line.split(",");
                    if (credentials[0].equals(username)) {
                        credentials[2] = String.valueOf(lives); // Actualizar las vidas
                        credentials[5] = String.valueOf(coins); // Actualizar las monedas
                        line = String.join(",", credentials);
                    }
                    fileContent.append(line).append("\n");
                }
                reader.close();
                fis.close();

                // Escribir los datos actualizados en el archivo
                FileOutputStream fos = openFileOutput("users.txt", MODE_PRIVATE);
                fos.write(fileContent.toString().getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
            }
        }
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

    private void loadUserCoins() {
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
                        coins = Integer.parseInt(credentials[5]); // Asumimos que las vidas están en la tercera columna
                        break;
                    }
                }
                reader.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
                coins = 0; // Valor predeterminado si hay un error
            }
        } else {
            coins = 0; // Valor predeterminado si no hay usuario
        }

        // Actualizar el TextView con las vidas actuales
        updateCoinsDisplay(coins);
    }

    // Método para actualizar el TextView con las vidas
    private void updateCoinsDisplay(int coins) {
        tvCoins.setText(String.valueOf(coins)); // Convierte el número a String antes de asignarlo
    }
}


