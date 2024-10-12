package com.example.codelingo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class UserProfileActivity extends AppCompatActivity{

    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Inicializacion
        usernameEditText = findViewById(R.id.usernameEditText);
        Button saveButton = findViewById(R.id.saveButton);
        Button goBack = findViewById(R.id.goBack);

        // Recibir el nombre de usuario desde el Intent
        Intent intent = getIntent();
        String Givenusername = intent.getStringExtra("username");

        if (Givenusername != null && !Givenusername.isEmpty()) {
            // Mostrar el nombre de usuario en el EditText
            usernameEditText.setText(Givenusername);
        } else {
            Toast.makeText(UserProfileActivity.this, "Error: No se pudo obtener el nombre de usuario", Toast.LENGTH_SHORT).show();
        }

        //Boton de guardado
        saveButton.setOnClickListener(v -> {
             String username = usernameEditText.getText().toString();

            if (!(username.isEmpty())){
                Toast.makeText(UserProfileActivity.this, "Cambios guardados", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(UserProfileActivity.this, "No ha introducido un nombre valido", Toast.LENGTH_SHORT).show();
            }
        });

        //Volver al menu Welcome
        goBack.setOnClickListener(v -> {
            Intent intent2 = new Intent(UserProfileActivity.this, WelcomeActivity.class);
            startActivity(intent2);
            finish();
        });


    }


}
