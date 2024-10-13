package com.example.codelingo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class UserProfileActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView userEmailTextView = findViewById(R.id.user_email); // TextView para el correo
        //Inicializacion
        Button goBack = findViewById(R.id.goBack);

        // Recibir el nombre de usuario desde el Intent
        Intent intent = getIntent();
        String givenUsername = intent.getStringExtra("username");
        String email = getEmailForUser(givenUsername);

        if (email != null) {
            // Mostrar el correo en el TextView
            userEmailTextView.setText(email);
        } else {
            Toast.makeText(UserProfileActivity.this, "No se encontró el correo del usuario", Toast.LENGTH_SHORT).show();
        }

        //Volver al menu Welcome
        goBack.setOnClickListener(v -> {
            Intent intent2 = new Intent(UserProfileActivity.this, WelcomeActivity.class);
             intent2.putExtra("username", givenUsername);
            startActivity(intent2);
            finish();
        });


    }

    private String getEmailForUser(String username) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 2 && credentials[0].equals(username)) {
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


}
