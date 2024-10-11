package com.example.codelingo;

import androidx.appcompat.app.AppCompatActivity;
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

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText usernameEditText, newPasswordEditText;
    private Button changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        usernameEditText = findViewById(R.id.usernameEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();

                if (!username.isEmpty() && !newPassword.isEmpty()) {
                    if (changeUserPassword(username, newPassword)) {
                        Toast.makeText(ChangePasswordActivity.this, "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                        finish();  // Finaliza la actividad
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean changeUserPassword(String username, String newPassword) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder updatedContent = new StringBuilder();
            String line;
            boolean userFound = false;

            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials[0].equals(username)) {
                    // Si el usuario coincide, actualiza la contraseña
                    line = username + "," + newPassword;
                    userFound = true;
                }
                updatedContent.append(line).append("\n");
            }

            reader.close();
            fis.close();

            if (userFound) {
                // Escribimos de nuevo todo el archivo con la nueva contraseña
                FileOutputStream fos = openFileOutput("users.txt", MODE_PRIVATE);
                OutputStreamWriter writer = new OutputStreamWriter(fos);
                writer.write(updatedContent.toString());
                writer.close();
                fos.close();
            }

            return userFound;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
