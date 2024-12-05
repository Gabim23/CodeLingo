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
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText newUsernameEditText, newPasswordEditText;
    private Button registerNewUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        newUsernameEditText = findViewById(R.id.newUsernameEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        registerNewUserButton = findViewById(R.id.registerNewUserButton);
        TextView signInTextView = findViewById(R.id.haveAnAccountSignIn);

        registerNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = newUsernameEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();

                if (!newUsername.isEmpty() && !newPassword.isEmpty()) {
                    if (checkIfUserExists(newUsername)) {
                        Toast.makeText(RegisterActivity.this, "El usuario ya existe. Por favor, elija otro nombre.", Toast.LENGTH_SHORT).show();
                    } else if (!isValidPassword(newPassword)) {
                        Toast.makeText(RegisterActivity.this, "La contraseña debe contener al menos una letra mayúscula y un carácter especial.", Toast.LENGTH_SHORT).show();
                    } else {
                        saveNewUser(newUsername, newPassword);
                        Toast.makeText(RegisterActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                        // Redirigir a la pagina de iniciar sesion
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //si tiene una cuenta clica en iniciar sesion
        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la nueva actividad SignInActivity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // Método para verificar si el nombre de usuario ya existe en el archivo users.txt
    private boolean checkIfUserExists(String username) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials[0].equals(username)) {
                    return true;  // El usuario ya existe
                }
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;  // El usuario no existe
    }

    // Método para guardar un nuevo usuario en el archivo users.txt
    private void saveNewUser(String username, String password) {
        try {
            FileOutputStream fos = openFileOutput("users.txt", MODE_APPEND);
            String userData = username + "," + password + ",5,0,0\n"; // Añadido el atributo vidas con valor 5
            fos.write(userData.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Método para validar que la contraseña contenga al menos una mayúscula y un carácter especial
    private boolean isValidPassword(String password) {
        // Expresión regular que requiere al menos una letra mayúscula y un carácter especial
        String passwordPattern = "^(?=.*[A-Z])(?=.*[@#$%^&+=?¿!¡]).{6,}$";
        return Pattern.matches(passwordPattern, password);
    }
}
