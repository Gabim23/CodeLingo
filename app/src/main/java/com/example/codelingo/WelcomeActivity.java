package com.example.codelingo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button logoutButton;
    private Button changePasswordButton; // Agregar el botón para cambiar contraseña
    private Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Encuentra el botón de "Jugar"
        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LevelsActivity.class);
                startActivity(intent);
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        changePasswordButton = findViewById(R.id.changePasswordButton); // Inicializar el botón

        // Botón de cerrar sesión
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Botón para cambiar contraseña
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
