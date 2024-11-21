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

        // Initialize SharedPreferences for level state management
        SharedPreferences sharedPreferences = getSharedPreferences("LevelPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Unlock Level 0 by default if first time
        if (!sharedPreferences.contains("level_0")) {
            editor.putBoolean("level_0", true);
            editor.putBoolean("level_1", false);
            editor.putBoolean("level_2", false);
            editor.apply();
        }

        // Prepare levels array dynamically
        int totalLevels = 10; // Adjust as needed
        String[] levels = new String[totalLevels];
        for (int i = 0; i < totalLevels; i++) {
            boolean isUnlocked = sharedPreferences.getBoolean("level_" + i, false);
            levels[i] = isUnlocked ? "Nivel " + i : "Nivel " + i + " (🔒)";
        }

        // Initialize RecyclerView Adapter
        LevelsAdapter adapter = new LevelsAdapter(this, levels, this);
        rvLevels.setAdapter(adapter);

        // Set welcome message
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText("Selecciona un nivel");
    }

    @Override
    public void onLevelClick(int position) {
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

