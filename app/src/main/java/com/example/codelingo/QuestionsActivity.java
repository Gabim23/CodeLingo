package com.example.codelingo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    private Button btnContinue;
    private LevelManager levelManager;
    private int currentLevel;
    private TextView tvFeedback;
    private TextView tvScore;
    private QuestionManager questionManager;
    private Question currentQuestion;
    private int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        btnContinue = findViewById(R.id.btnContinue);
        tvFeedback = findViewById(R.id.tvFeedback);
        tvScore = findViewById(R.id.tvScore);
        currentLevel = getIntent().getIntExtra("CURRENT_LEVEL", 0);
        questionManager = new QuestionManager(currentLevel);
        loadCurrentQuestion();
        TextView tvQuestion = findViewById(R.id.tvQuestion);
        Button btnOption1 = findViewById(R.id.btnOption1);
        Button btnOption2 = findViewById(R.id.btnOption2);
        Button btnOption3 = findViewById(R.id.btnOption3);
        Button btnOption4 = findViewById(R.id.btnOption4);
        LevelManager levelManager = new LevelManager(this, currentLevel, tvQuestion, tvScore, btnOption1, btnOption2, btnOption3, btnOption4, btnContinue);
        setOptionButtonListeners(btnOption1, btnOption2, btnOption3, btnOption4);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLevelProgress(currentLevel);
                updateUserScore();
                Intent intent = new Intent(QuestionsActivity.this, LevelsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btnContinue.setVisibility(View.GONE);
    }

    private void loadCurrentQuestion() {
        if (questionManager.hasNextQuestion()) {
            currentQuestion = questionManager.getCurrentQuestion();
            TextView tvQuestion = findViewById(R.id.tvQuestion);
            Button btnOption1 = findViewById(R.id.btnOption1);
            Button btnOption2 = findViewById(R.id.btnOption2);
            Button btnOption3 = findViewById(R.id.btnOption3);
            Button btnOption4 = findViewById(R.id.btnOption4);
            tvQuestion.setText(currentQuestion.getQuestionText());
            btnOption1.setText(currentQuestion.getOptions()[0]);
            btnOption2.setText(currentQuestion.getOptions()[1]);
            btnOption3.setText(currentQuestion.getOptions()[2]);
            btnOption4.setText(currentQuestion.getOptions()[3]);
            btnOption1.setVisibility(View.VISIBLE);
            btnOption2.setVisibility(View.VISIBLE);
            btnOption3.setVisibility(View.VISIBLE);
            btnOption4.setVisibility(View.VISIBLE);
            tvScore.setVisibility(View.GONE);
            btnContinue.setVisibility(View.GONE);
        } else {
            showTotalScore();
        }
    }

    private void setOptionButtonListeners(Button... buttons) {
        for (Button button : buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(button.getText().toString());
                }
            });
        }
    }

    private void checkAnswer(String userAnswer) {
        String correctAnswer = currentQuestion.getOptions()[currentQuestion.getCorrectAnswerIndex()];
        if (userAnswer.equals(correctAnswer)) {
            correctAnswers++;
            showFeedback(true, "¡Excelente!");
        } else {
            showFeedback(false, "Incorrecto: La respuesta correcta es " + correctAnswer);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                questionManager.moveToNextQuestion();
                loadCurrentQuestion();
            }
        }, 3000);
    }

    private void showFeedback(boolean isCorrect, String message) {
        tvFeedback.setText(message);
        tvFeedback.setTextColor(getResources().getColor(isCorrect ? android.R.color.holo_green_dark : android.R.color.holo_red_dark));
        tvFeedback.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvFeedback.setVisibility(View.GONE);
            }
        }, 3000);
    }

    private void showTotalScore() {
        findViewById(R.id.tvQuestion).setVisibility(View.GONE);
        findViewById(R.id.btnOption1).setVisibility(View.GONE);
        findViewById(R.id.btnOption2).setVisibility(View.GONE);
        findViewById(R.id.btnOption3).setVisibility(View.GONE);
        findViewById(R.id.btnOption4).setVisibility(View.GONE);
        tvScore.setText("Puntuación total: " + correctAnswers + " de " + (questionManager.getTotalQuestions() - 1));
        tvScore.setVisibility(View.VISIBLE);
        btnContinue.setVisibility(View.VISIBLE);
    }

    private void saveLevelProgress(int currentLevel) {
        SharedPreferences sharedPreferences = getSharedPreferences("LevelPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int nextLevel = currentLevel + 1;
        editor.putBoolean("level_" + nextLevel, true);
        editor.apply();
    }

    private void updateUserScore() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        if (username != null) {
            // Obtener el puntaje actual del usuario
            int currentScore = getCurrentUserScore(username);

            // Sumar el puntaje actual con la puntuación del nivel
            int newScore = currentScore + correctAnswers;

            // Guardar el nuevo puntaje
            saveUserScore(username, newScore);
        }
    }

    private int getCurrentUserScore(String username) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials[0].equals(username)) {
                    return Integer.parseInt(credentials[3]);
                }
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void saveUserScore(String username, int newScore) {
        try {
            FileInputStream fis = openFileInput("users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            List<String> lines = new ArrayList<>();
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials[0].equals(username)) {
                    credentials[3] = String.valueOf(newScore);
                    line = String.join(",", credentials);
                    found = true;
                }
                lines.add(line);
            }

            reader.close();
            fis.close();

            if (found) {
                FileOutputStream fos = openFileOutput("users.txt", MODE_PRIVATE);
                for (String l : lines) {
                    fos.write((l + "\n").getBytes());
                }
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
