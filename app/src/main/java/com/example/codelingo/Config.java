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
    private Button goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config); // Asegúrate de que este layout está bien

        // Inicializa todos los botones dentro de onCreate después de setContentView
        logoutButton = findViewById(R.id.logoutButton2);
        changePasswordButton = findViewById(R.id.changePasswordButton2);
        goBack = findViewById(R.id.goBack2);

        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String givenUsername = sharedPref.getString("username", null);

        if (givenUsername != null) {
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

            // Botón para regresar
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




