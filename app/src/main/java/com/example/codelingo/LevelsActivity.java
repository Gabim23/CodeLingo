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
        boolean isLevel1Unlocked = sharedPreferences.getBoolean("level_1", false);
        boolean isLevel2Unlocked = sharedPreferences.getBoolean("level_2", false);
        boolean isLevel3Unlocked = sharedPreferences.getBoolean("level_3", false);
        String[] levels = new String[11];
        levels[0] = "Nivel 0";
        levels[1] = isLevel1Unlocked ? "Nivel 1" : "Nivel 1 (Bloqueado)";
        levels[2] = isLevel2Unlocked ? "Nivel 2" : "Nivel 2 (Bloqueado)";
        levels[3] = isLevel3Unlocked ? "Nivel 3" : "Nivel 3 (Bloqueado)";
        levels[4] = isLevel3Unlocked ? "Nivel 4" : "Nivel 4 (Bloqueado)";
        levels[5] = isLevel3Unlocked ? "Nivel 5" : "Nivel 5 (Bloqueado)";
        levels[6] = isLevel3Unlocked ? "Nivel 6" : "Nivel 6 (Bloqueado)";
        levels[7] = isLevel3Unlocked ? "Nivel 7" : "Nivel 7 (Bloqueado)";
        levels[8] = isLevel3Unlocked ? "Nivel 8" : "Nivel 8 (Bloqueado)";
        levels[9] = isLevel3Unlocked ? "Nivel 9" : "Nivel 9 (Bloqueado)";
        levels[10] = isLevel3Unlocked ? "Nivel 10" : "Nivel 10 (Bloqueado)";
        LevelsAdapter adapter = new LevelsAdapter(this, levels, this);
        rvLevels.setAdapter(adapter);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText("Selecciona un nivel");
    }

    @Override
    public void onLevelClick(int position) {
        int currentLevel = position;
        if (position == 0) {
            Intent intent = new Intent(LevelsActivity.this, QuestionsActivity.class);
            intent.putExtra("CURRENT_LEVEL", currentLevel);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Este nivel está bloqueado", Toast.LENGTH_SHORT).show();
        }
    }

}
