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

        userDescriptionEdit = findViewById(R.id.user_descriptionEdit);
        TextView userEmailTextView = findViewById(R.id.user_email);
        TextView userScoreTextView = findViewById(R.id.user_score); // TextView para el puntaje

        Button goBack = findViewById(R.id.goBack);
        Button saveButton = findViewById(R.id.save_button);

        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String givenUsername = sharedPref.getString("username", null);

        if (givenUsername != null) {
            String email = getEmailForUser(givenUsername);
            String description = getDescForUser(givenUsername);
            String score = getScoreForUser(givenUsername);

            if (email != null) {
                userEmailTextView.setText(email);
            } else {
                Toast.makeText(this, "No se encontró el correo del usuario", Toast.LENGTH_SHORT).show();
            }

            if (description != null) {
                userDescriptionEdit.setText(description);
            }

            if (score != null) {
                userScoreTextView.setText("Score: " + score);
            } else {
                userScoreTextView.setText("Score: N/A");
            }

            saveButton.setOnClickListener(v -> {
                String newDescription = userDescriptionEdit.getText().toString();
                if (saveDescForUser(givenUsername, newDescription)) {
                    Toast.makeText(this, "Descripción guardada con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al guardar la descripción", Toast.LENGTH_SHORT).show();
                }
            });

            goBack.setOnClickListener(v -> {
                Intent intent2 = new Intent(this, WelcomeActivity.class);
                startActivity(intent2);
                finish();
            });

            logoutButton = findViewById(R.id.logoutButton);
            changePasswordButton = findViewById(R.id.changePasswordButton);

            logoutButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            });

            changePasswordButton.setOnClickListener(v -> {
                Intent changePasswordIntent = new Intent(this, ChangePasswordActivity.class);
                changePasswordIntent.putExtra("username", givenUsername);
                startActivity(changePasswordIntent);
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
                    return credentials[0];
                }
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
                    return credentials[2];
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
                    return credentials[3];
                }
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
                        credentials[2] = newDescription;
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
