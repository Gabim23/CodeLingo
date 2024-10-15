package com.example.codelingo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
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
    private Button changePasswordButton; // Agregar el botón para cambiar contraseña

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userDescriptionEdit = findViewById(R.id.user_descriptionEdit); // EditText para la descripción
        TextView userEmailTextView = findViewById(R.id.user_email); // TextView para el correo

        // Inicialización
        Button goBack = findViewById(R.id.goBack);
        Button saveButton = findViewById(R.id.save_button); // Asumimos que hay un botón save en el layout

        // Recibir el nombre de usuario desde el Intent
        Intent receivedIntent = getIntent(); // Cambiado el nombre de 'intent' a 'receivedIntent'
        String givenUsername = receivedIntent.getStringExtra("username");
        String email = getEmailForUser(givenUsername);
        String description = getDescForUser(givenUsername);

        if (email != null) {
            // Mostrar el correo en el TextView
            userEmailTextView.setText(email);
        } else {
            Toast.makeText(UserProfileActivity.this, "No se encontró el correo del usuario", Toast.LENGTH_SHORT).show();
        }

        if (description != null) {
            userDescriptionEdit.setText(description);
        }

        saveButton.setOnClickListener(v -> {
            String newDescription = userDescriptionEdit.getText().toString();
            if (saveDescForUser(givenUsername, newDescription)) {
                Toast.makeText(UserProfileActivity.this, "Descripción guardada con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserProfileActivity.this, "Error al guardar la descripción", Toast.LENGTH_SHORT).show();
            }
        });

        // Volver al menú Welcome
        goBack.setOnClickListener(v -> {
            Intent intent2 = new Intent(UserProfileActivity.this, WelcomeActivity.class);
            intent2.putExtra("username", givenUsername);
            startActivity(intent2);
            finish();
        });

        logoutButton = findViewById(R.id.logoutButton);
        changePasswordButton = findViewById(R.id.changePasswordButton); // Inicializar el botón

        // Botón de cerrar sesión
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Botón para cambiar contraseña
        changePasswordButton.setOnClickListener(v -> {
            Intent changePasswordIntent = new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
            changePasswordIntent.putExtra("username", givenUsername); // Pasar el nombre de usuario actual
            startActivity(changePasswordIntent);
        });
    }

    private String getEmailForUser(String username) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 2 && credentials[0].equals(username)) {
                    // Retornar el correo si coincide el nombre de usuario (correo es el primer campo)
                    return credentials[0];
                }
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra el correo
    }

    private String getDescForUser(String username) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 3 && credentials[0].equals(username)) {
                    // Retornar la descripción si coincide el nombre de usuario (correo es el primer campo)
                    return credentials[2];
                }
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra el correo
    }

    private boolean saveDescForUser(String username, String newDescription) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            List<String> lines = new ArrayList<>();
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");

                // Si coincide el username, ya que el primer campo siempre es el email
                if (credentials[0].equals(username)) {
                    if (credentials.length == 2) {
                        // Solo tiene email y contraseña, añadimos la nueva descripción
                        line = credentials[0] + "," + credentials[1] + "," + newDescription;
                    } else if (credentials.length >= 3) {
                        // Tiene email, contraseña y descripción, reemplazamos la descripción
                        credentials[2] = newDescription;
                        line = String.join(",", credentials);
                    }
                    found = true;
                }

                lines.add(line); // Agregar la línea modificada o no a la lista
            }

            reader.close();
            fis.close();

            // Si se encontró el usuario, escribir el archivo actualizado
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
}
