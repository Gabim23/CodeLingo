package com.example.codelingo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class Config extends AppCompatActivity {

    private Button logoutButton;
    private Button changePasswordButton;
    Button goBack = findViewById(R.id.goBack2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String givenUsername = sharedPref.getString("username", null);

        if (givenUsername != null) {
            logoutButton = findViewById(R.id.logoutButton2);
            changePasswordButton = findViewById(R.id.changePasswordButton);

            // Botón para cerrar sesión
            logoutButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            });

            // Botón para cambiar contraseña
            changePasswordButton.setOnClickListener(v -> {
                Intent changePasswordIntent = new Intent(this, ChangePasswordActivity.class);
                changePasswordIntent.putExtra("username", givenUsername);
                startActivity(changePasswordIntent);
            });

            goBack.setOnClickListener(v -> {
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            });
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}



