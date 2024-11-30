package com.example.codelingo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private Button btnToggleDarkMode;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnToggleDarkMode = findViewById(R.id.btnToggleDarkMode);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Recuperar el estado preferido del modo oscuro desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);

        // Configurar el modo oscuro según la preferencia guardada
        AppCompatDelegate.setDefaultNightMode(isDarkModeEnabled ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        // Configurar el estado inicial del botón
        updateButtonAppearance(isDarkModeEnabled);

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
            }, 700); // Tiempo que dura la animación
        });

        // Botón de iniciar sesión
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Verificar credenciales y obtener la puntuación
            int score = checkUserCredentials(username, password);
            if (score != -1) {  // -1 indica que las credenciales son incorrectas
                // Guardar el nombre de usuario en SharedPreferences
                SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("username", username);
                editor.putInt("score", score); // Guardar la puntuación
                editor.apply();

                // Iniciar la actividad de bienvenida
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón de registrarse
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
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

    // Método para eliminar todos los usuarios del archivo users.txt
    private void deleteAllUsers() {
        try {
            // Abre el archivo en modo de escritura, lo que elimina su contenido
            FileOutputStream fos = openFileOutput("users.txt", MODE_PRIVATE); // MODE_PRIVATE borra el contenido
            fos.close();  // No es necesario escribir nada, solo cerrarlo
            Toast.makeText(this, "Todos los usuarios han sido eliminados", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al eliminar los usuarios", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Verifica las credenciales del usuario y devuelve su puntuación si las credenciales son válidas.
     * @return La puntuación del usuario si es válido; -1 si las credenciales no son válidas.
     */
    private int checkUserCredentials(String username, String password) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");

                // Verificar que el nombre de usuario y la contraseña coincidan
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    reader.close();
                    fis.close();

                    // Convertir la puntuación de texto a un número entero y devolverlo
                    return Integer.parseInt(credentials[2]);  // Puntuación del usuario
                }
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;  // Retorna -1 si las credenciales no son válidas
    }
}
