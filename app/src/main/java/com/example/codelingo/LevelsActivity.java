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

import android.os.Handler;
import java.util.Calendar;

public class LevelsActivity extends AppCompatActivity implements OnLevelClickListener {

    private RecyclerView rvLevels;
    private TextView tvMessage;
    private TextView tvLives;
    private int lives;
    private static final int MAX_LIVES = 5;
    private static final int LIFE_RESTORE_INTERVAL = 600000; // 10 minutos en milisegundos
    private Handler lifeRestoreHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        rvLevels = findViewById(R.id.rvLevels);
        rvLevels.setLayoutManager(new LinearLayoutManager(this));

        tvMessage = findViewById(R.id.tvMessage);

        loadUserLives();
        restoreLives();
        // Leer y restaurar las vidas del usuario

        scheduleLifeRestore(); // Programar restauración periódica de vidas

        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText("Selecciona un nivel");

        SharedPreferences sharedPreferences = getSharedPreferences("LevelPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!sharedPreferences.contains("level_0")) {
            editor.putBoolean("level_0", true);
            editor.apply();
        }

        String[] levels = new String[10];
        for (int i = 0; i < levels.length; i++) {
            boolean isUnlocked = sharedPreferences.getBoolean("level_" + i, false);
            levels[i] = isUnlocked ? "Nivel " + i : "Nivel " + i + " (🔒)";
        }

        LevelsAdapter adapter = new LevelsAdapter(this, levels, this);
        rvLevels.setAdapter(adapter);
    }

    private void restoreLives() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        if (username != null) {
            // Cargar las vidas actuales del usuario
            try {
                FileInputStream fis = openFileInput("users.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] credentials = line.split(",");
                    if (credentials[0].equals(username)) {
                        lives = Integer.parseInt(credentials[2]); // Cargar las vidas actuales
                        break;
                    }
                }
                reader.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
                lives = 0; // Valor predeterminado si hay un error
            }

            // Cálculo de vidas restauradas
            long lastLifeLostTime = sharedPreferences.getLong("last_life_lost_time", 0);
            long currentTime = Calendar.getInstance().getTimeInMillis();

            if (lastLifeLostTime == 0) {
                // Si no hay registro previo, inicializar el tiempo actual
                sharedPreferences.edit().putLong("last_life_lost_time", currentTime).apply();
                return;
            }

            // Asegúrate de que la diferencia de tiempo esté en milisegundos
            long timeElapsed = currentTime - lastLifeLostTime;
            int livesToRestore = (int) (timeElapsed / LIFE_RESTORE_INTERVAL);

            if (livesToRestore > 0) {
                // Sumar vidas restauradas y limitar a MAX_LIVES
                lives = Math.min(lives + livesToRestore, MAX_LIVES);

                if (lives < MAX_LIVES) {
                    // Actualizar el tiempo restante para la siguiente restauración
                    sharedPreferences.edit()
                            .putLong("last_life_lost_time", currentTime - (timeElapsed % LIFE_RESTORE_INTERVAL))
                            .apply();
                } else {
                    // Si las vidas están al máximo, eliminar el registro del último tiempo
                    sharedPreferences.edit().remove("last_life_lost_time").apply();
                }
            }
        }

        updateLivesDisplay(lives); // Actualizar la interfaz con las vidas actuales
    }


    private void scheduleLifeRestore() {
        lifeRestoreHandler.postDelayed(() -> {
            restoreLives();
            scheduleLifeRestore();
        }, LIFE_RESTORE_INTERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifeRestoreHandler.removeCallbacksAndMessages(null); // Eliminar callbacks al destruir la actividad
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
            TextView noLivesMessage = new TextView(this);
            noLivesMessage.setText("No hay Vidas");
            noLivesMessage.setTextSize(18);
            noLivesMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            noLivesMessage.setGravity(Gravity.CENTER);
            llLivesContainer.addView(noLivesMessage);
        } else {
            for (int i = 0; i < lives; i++) {
                TextView heart = new TextView(this);
                heart.setText("❤️");
                heart.setTextSize(24);
                heart.setPadding(8, 0, 8, 0);
                llLivesContainer.addView(heart);
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


