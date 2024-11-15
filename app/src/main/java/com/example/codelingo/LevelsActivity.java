package com.example.codelingo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LevelsActivity extends AppCompatActivity implements OnLevelClickListener {

    private RecyclerView rvLevels;
    private TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        rvLevels = findViewById(R.id.rvLevels);
        rvLevels.setLayoutManager(new LinearLayoutManager(this));
        tvMessage = findViewById(R.id.tvMessage);
        SharedPreferences sharedPreferences = getSharedPreferences("LevelPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!sharedPreferences.contains("level_0")) {
            editor.putBoolean("level_0", true);
            editor.putBoolean("level_1", false);
            editor.putBoolean("level_2", false);
            editor.apply();
        }
        boolean isLevel1Unlocked = sharedPreferences.getBoolean("level_1", false);
        boolean isLevel2Unlocked = sharedPreferences.getBoolean("level_2", false);
        String[] levels = new String[11];
        levels[0] = "Nivel 0";
        levels[1] = isLevel1Unlocked ? "Nivel 1" : "Nivel 1 (Bloqueado)";
        levels[2] = isLevel2Unlocked ? "Nivel 2" : "Nivel 2 (Bloqueado)";
        for (int i = 3; i < levels.length; i++) {
            levels[i] = "Nivel " + i + " (Bloqueado)";
        }
        LevelsAdapter adapter = new LevelsAdapter(this, levels, this);
        rvLevels.setAdapter(adapter);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText("Selecciona un nivel");
    }

    @Override
    public void onLevelClick(int position) {
        int currentLevel = position;
        SharedPreferences sharedPreferences = getSharedPreferences("LevelPrefs", MODE_PRIVATE);
        boolean isLevelUnlocked = sharedPreferences.getBoolean("level_" + currentLevel, false);
        if (currentLevel == 0 || isLevelUnlocked) {
            Intent intent = new Intent(LevelsActivity.this, QuestionsActivity.class);
            intent.putExtra("CURRENT_LEVEL", currentLevel);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Este nivel está bloqueado", Toast.LENGTH_SHORT).show();
        }
    }

}
