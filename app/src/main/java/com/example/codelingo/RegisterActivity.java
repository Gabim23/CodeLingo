package com.example.codelingo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;

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

        registerNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = newUsernameEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();

                if (!newUsername.isEmpty() && !newPassword.isEmpty()) {
                    saveNewUser(newUsername, newPassword);
                    Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                    finish();  // Volver a la pantalla de inicio de sesión
                } else {
                    Toast.makeText(RegisterActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveNewUser(String username, String password) {
        try {
            FileOutputStream fos = openFileOutput("users.txt", MODE_APPEND);
            String userData = username + "," + password + "\n";
            fos.write(userData.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}