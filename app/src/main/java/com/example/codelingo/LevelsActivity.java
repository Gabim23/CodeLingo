package com.example.codelingo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class LevelsActivity extends AppCompatActivity implements OnLevelClickListener {

    private RecyclerView rvLevels;
    private TextView tvMessage;
    private TextView tvLives;  // Nueva variable para mostrar las vidas
    private int lives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        rvLevels = findViewById(R.id.rvLevels);
        rvLevels.setLayoutManager(new LinearLayoutManager(this));

        tvMessage = findViewById(R.id.tvMessage);

        // Leer las vidas del usuario
        loadUserLives();  // Cargar las vidas desde el archivo y actualizar la visualización

        // Mostrar mensaje de bienvenida
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText("Selecciona un nivel");

        // Inicializar SharedPreferences para gestionar los niveles desbloqueados
        SharedPreferences sharedPreferences = getSharedPreferences("LevelPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Desbloquear el Nivel 0 por defecto si es la primera vez
        if (!sharedPreferences.contains("level_0")) {
            editor.putBoolean("level_0", true);
            editor.putBoolean("level_1", false);
            editor.putBoolean("level_2", false);
            editor.apply();
        }

        // Preparar el array de niveles
        int totalLevels = 10; // Ajustar según sea necesario
        String[] levels = new String[totalLevels];
        for (int i = 0; i < totalLevels; i++) {
            boolean isUnlocked = sharedPreferences.getBoolean("level_" + i, false);
            levels[i] = isUnlocked ? "Nivel " + i : "Nivel " + i + " (🔒)";
        }

        // Inicializar el adaptador de RecyclerView
        LevelsAdapter adapter = new LevelsAdapter(this, levels, this);
        rvLevels.setAdapter(adapter);
    }


    // Método para cargar las vidas del usuario desde el archivo users.txt
    private void loadUserLives() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        if (username != null) {
            try {
                FileInputStream fis = openFileInput("users.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] credentials = line.split(",");
                    if (credentials[0].equals(username)) {
                        lives = Integer.parseInt(credentials[2]);  // Cargar las vidas
                        break;
                    }
                }
                reader.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            lives = 0;  // Si no se encuentra el usuario, establecer 0 vidas
        }

        // Llamar a la función para actualizar la visualización de las vidas
        updateLivesDisplay(lives);
    }

    private void updateLivesDisplay(int lives) {
        LinearLayout llLivesContainer = findViewById(R.id.llLivesContainer);
        llLivesContainer.removeAllViews(); // Limpiar los corazones actuales

        if (lives <= 0) {
            // Si no hay vidas, mostrar el mensaje "No hay Vidas"
            TextView noLivesMessage = new TextView(this);
            noLivesMessage.setText("No hay Vidas");
            noLivesMessage.setTextSize(18);  // Ajusta el tamaño del texto si es necesario
            noLivesMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));  // Color del texto
            noLivesMessage.setGravity(Gravity.CENTER);  // Centra el texto
            llLivesContainer.addView(noLivesMessage);  // Añadir el mensaje al contenedor
        } else {
            // Si hay vidas, añadir los corazones
            for (int i = 0; i < lives; i++) {
                TextView heart = new TextView(this);
                heart.setText("❤️");
                heart.setTextSize(24);  // Tamaño del corazón
                heart.setPadding(8, 0, 8, 0);  // Espaciado entre los corazones
                llLivesContainer.addView(heart);  // Añadir al contenedor
            }
        }
    }



    @Override
    public void onLevelClick(int position) {
        // Si las vidas son 0, no permitir seleccionar un nivel
        if (lives <= 0) {
            Toast.makeText(this, "⚠️ No tienes vidas disponibles. ¡Compra más en la tienda o espera para recuperarlas!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si el nivel está desbloqueado o es el nivel 0
        SharedPreferences sharedPreferences = getSharedPreferences("LevelPrefs", MODE_PRIVATE);
        boolean isLevelUnlocked = sharedPreferences.getBoolean("level_" + position, false);

        if (position == 0 || isLevelUnlocked) {
            Intent intent = new Intent(LevelsActivity.this, QuestionsActivity.class);
            intent.putExtra("CURRENT_LEVEL", position);
            startActivity(intent);
        } else {
            Toast.makeText(this, "⚠️ Este nivel está bloqueado", Toast.LENGTH_SHORT).show();
        }
    }
}


