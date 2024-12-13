package com.example.codelingo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private EditText userDescriptionEdit;
    private Button logoutButton;
    private Button changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Obtener las referencias de los elementos de la interfaz
        userDescriptionEdit = findViewById(R.id.user_descriptionEdit);
        TextView userScoreTextView = findViewById(R.id.user_score); // TextView para el puntaje
        TextView userNameTextView = findViewById(R.id.user_name); // Nuevo TextView para mostrar el nombre
        TextView userCoinsTextView = findViewById(R.id.user_coins); // TextView para las monedas

        Button goBack = findViewById(R.id.goBack);
        Button saveButton = findViewById(R.id.save_button);

        // Obtener el nombre de usuario desde SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String givenUsername = sharedPref.getString("username", null);

        if (givenUsername != null) {
            // Si se ha encontrado un nombre de usuario, obtener los detalles del usuario
            String email = getEmailForUser(givenUsername);
            String description = getDescForUser(givenUsername);
            String score = getScoreForUser(givenUsername);
            String coins = getCoinsForUser(givenUsername);

            // Mostrar el nombre de usuario en el TextView correspondiente
            userNameTextView.setVisibility(View.VISIBLE);  // Hacer visible el TextView
            userNameTextView.setText("Nombre de usuario: " + givenUsername); // Establecer el nombre de usuario

            // Mostrar la descripción del usuario
            if (description != null) {
                userDescriptionEdit.setText(description);
            }

            // Mostrar el puntaje del usuario
            if (score != null) {
                userScoreTextView.setText("Puntaje: " + score); // Mostrar el puntaje
                userScoreTextView.setVisibility(View.VISIBLE);  // Asegurarse de que el puntaje sea visible
            } else {
                userScoreTextView.setText("Puntaje: 0");
                userScoreTextView.setVisibility(View.VISIBLE);  // Asegurarse de que el puntaje sea visible
            }

            // Mostrar las monedas del usuario
            if (coins != null) {
                userCoinsTextView.setText("Monedas: " + coins + " 🪙"); // Mostrar las monedas
                userCoinsTextView.setVisibility(View.VISIBLE); // Asegurar que sea visible
            } else {
                userCoinsTextView.setText("Monedas: 0 🪙");
                userCoinsTextView.setVisibility(View.VISIBLE); // Asegurar que sea visible
            }

            // Guardar cambios en la descripción
            saveButton.setOnClickListener(v -> {
                String newDescription = userDescriptionEdit.getText().toString();
                if (saveDescForUser(givenUsername, newDescription)) {
                    Toast.makeText(this, "Descripción guardada con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al guardar la descripción", Toast.LENGTH_SHORT).show();
                }
            });

            // Volver a la actividad principal
            goBack.setOnClickListener(v -> {
                Intent intent2 = new Intent(this, WelcomeActivity.class);
                startActivity(intent2);
                finish();
            });
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // Método para obtener el correo del usuario
    private String getEmailForUser(String username) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 2 && credentials[0].equals(username)) {
                    return credentials[1]; // Devuelve el correo electrónico
                }
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra el usuario
    }

    // Método para obtener la descripción del usuario
    private String getDescForUser(String username) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 3 && credentials[0].equals(username)) {
                    return credentials[6]; // Devuelve la descripción
                }
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para obtener el puntaje del usuario
    private String getScoreForUser(String username) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 4 && credentials[0].equals(username)) {
                    return credentials[3]; // Devuelve el puntaje
                }
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para obtener las monedas del usuario
    private String getCoinsForUser(String username) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 5 && credentials[0].equals(username)) {
                    return credentials[5]; // Devuelve la cantidad de monedas
                }
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra el usuario
    }

    // Método para guardar la descripción del usuario
    private boolean saveDescForUser(String username, String newDescription) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            List<String> lines = new ArrayList<>();
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");

                if (credentials[0].equals(username)) {
                    if (credentials.length == 2) {
                        line = credentials[0] + "," + credentials[1] + "," + newDescription;
                    } else if (credentials.length >= 3) {
                        credentials[6] = newDescription;
                        line = String.join(",", credentials);
                    }
                    found = true;
                }
                lines.add(line);
            }

            reader.close();
            fis.close();

            if (found) {
                FileOutputStream fos = openFileOutput("users.txt", MODE_PRIVATE);
                for (String l : lines) {
                    fos.write((l + "\n").getBytes());
                }
                fos.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para actualizar el puntaje del usuario
    private boolean updateScoreForUser(String username, int newScore) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            List<String> lines = new ArrayList<>();
            String line;
            boolean found = false;

            // Leemos las líneas del archivo
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");

                // Si encontramos al usuario, actualizamos su puntaje
                if (credentials[0].equals(username)) {
                    if (credentials.length >= 4) {
                        credentials[3] = String.valueOf(newScore); // Actualizamos el puntaje
                        line = String.join(",", credentials);
                    }
                    found = true;
                }
                lines.add(line);
            }

            reader.close();
            fis.close();

            // Si el usuario fue encontrado y su puntaje fue actualizado
            if (found) {
                FileOutputStream fos = openFileOutput("users.txt", MODE_PRIVATE);
                for (String l : lines) {
                    fos.write((l + "\n").getBytes());
                }
                fos.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para ser llamado cuando un nivel termine
    protected void onLevelCompleted(String username, int finalScore) {
        boolean success = updateScoreForUser(username, finalScore);
        if (success) {
            Toast.makeText(this, "Puntaje actualizado: " + finalScore, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar el puntaje", Toast.LENGTH_SHORT).show();
        }

        // Aquí también podrías actualizar el puntaje en el TextView
        TextView userScoreTextView = findViewById(R.id.user_score);
        userScoreTextView.setText("Score: " + finalScore);
    }
}
