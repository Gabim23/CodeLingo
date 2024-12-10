package com.example.codelingo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPasswordEditText, newPasswordEditText;
    private Button changePasswordButton;
    private String currentUsername; // Almacena el nombre de usuario actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Inicializa los elementos de la UI
        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        // Obtiene el nombre de usuario desde el Intent
        Intent intent = getIntent();
        currentUsername = intent.getStringExtra("username");

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = oldPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();

                if (!oldPassword.isEmpty() && !newPassword.isEmpty()) {
                    // Verifica si la nueva contraseña cumple con las restricciones
                    if (!isValidPassword(newPassword)) {
                        Toast.makeText(ChangePasswordActivity.this, "La contraseña debe contener al menos una letra mayúscula y un carácter especial.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (changeUserPassword(currentUsername, oldPassword, newPassword)) {
                        Toast.makeText(ChangePasswordActivity.this, "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                        finish();  // Finaliza la actividad
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "La contraseña antigua es incorrecta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para validar que la contraseña contenga al menos una mayúscula y un carácter especial
    private boolean isValidPassword(String password) {
        // Expresión regular que requiere al menos una letra mayúscula y un carácter especial
        String passwordPattern = "^(?=.*[A-Z])(?=.*[@#$%^&+=?¿!¡]).{6,}$";
        return Pattern.matches(passwordPattern, password);
    }

    private boolean changeUserPassword(String username, String oldPassword, String newPassword) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder updatedContent = new StringBuilder();
            String line;
            boolean userFound = false;
            boolean oldPasswordCorrect = false;

            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials[0].equals(username)) {
                    // Verificar la contraseña antigua
                    if (credentials[1].equals(oldPassword)) {
                        // Si la contraseña antigua es correcta, actualizar a la nueva contraseña
                        line = username + "," + newPassword + "," + credentials[2] + "," + credentials[3] + "," + credentials[4] + "," + credentials[5];  // Mantén los otros datos (p.ej., descripción y puntaje)
                        oldPasswordCorrect = true;
                    }
                    userFound = true;
                }
                updatedContent.append(line).append("\n");
            }

            reader.close();
            fis.close();

            if (userFound && oldPasswordCorrect) {
                // Escribimos de nuevo todo el archivo con la nueva contraseña
                FileOutputStream fos = openFileOutput("users.txt", MODE_PRIVATE);
                OutputStreamWriter writer = new OutputStreamWriter(fos);
                writer.write(updatedContent.toString());
                writer.close();
                fos.close();
                return true;
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
