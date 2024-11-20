package com.example.codelingo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LevelManager {

    private QuestionManager questionManager;
    private ScoreManager scoreManager;
    private TextView tvQuestion, tvScore;
    private Button btnOption1, btnOption2, btnOption3, btnOption4, btnContinue;
    private Context context;

    public LevelManager(Context context, int level, TextView tvQuestion, TextView tvScore,
                        Button btnOption1, Button btnOption2, Button btnOption3, Button btnOption4, Button btnContinue) {
        this.context = context;
        this.tvQuestion = tvQuestion;
        this.tvScore = tvScore;
        this.btnOption1 = btnOption1;
        this.btnOption2 = btnOption2;
        this.btnOption3 = btnOption3;
        this.btnOption4 = btnOption4;
        this.btnContinue = btnContinue;
        questionManager = new QuestionManager(level);
        scoreManager = new ScoreManager();
        setupListeners();
        loadQuestion();
    }

    private void setupListeners() {
        View.OnClickListener answerListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button selectedButton = (Button) view;
                int answerIndex = Integer.parseInt(selectedButton.getTag().toString());
                checkAnswer(answerIndex);
            }
        };
        btnOption1.setOnClickListener(answerListener);
        btnOption2.setOnClickListener(answerListener);
        btnOption3.setOnClickListener(answerListener);
        btnOption4.setOnClickListener(answerListener);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LevelsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }

    private void loadQuestion() {
        Question currentQuestion = questionManager.getCurrentQuestion();
        if (currentQuestion != null) {
            tvQuestion.setText(currentQuestion.getQuestionText());
            String[] options = currentQuestion.getOptions();
            btnOption1.setText(options[0]);
            btnOption2.setText(options[1]);
            btnOption3.setText(options[2]);
            btnOption4.setText(options[3]);
            btnOption1.setTag(0);
            btnOption2.setTag(1);
            btnOption3.setTag(2);
            btnOption4.setTag(3);
            btnContinue.setVisibility(View.GONE);
            tvScore.setVisibility(View.GONE);
        } else {
            saveLevelProgress(questionManager.getLevel());
            tvQuestion.setVisibility(View.GONE);
            btnOption1.setVisibility(View.GONE);
            btnOption2.setVisibility(View.GONE);
            btnOption3.setVisibility(View.GONE);
            btnOption4.setVisibility(View.GONE);
            tvScore.setText("Puntuación final: " + scoreManager.getScore());
            btnContinue.setVisibility(View.VISIBLE);
        }
    }

    private void saveLevelProgress(int currentLevel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LevelPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int nextLevel = currentLevel + 1;
        if (nextLevel <= 2) {
            editor.putBoolean("level_" + nextLevel, true);
            editor.apply();
        }
    }

    private String getCongratulatoryMessage(int score) {
        if (score == questionManager.getTotalQuestions()) {
            return "¡Enhorabuena! Has acertado todas las preguntas. Tu puntuación es: " + score;
        } else {
            return "Fin del cuestionario. Tu puntuación es: " + score;
        }
    }

    private void checkAnswer(int selectedAnswer) {
        Question currentQuestion = questionManager.getCurrentQuestion();
        if (currentQuestion != null) {
            if (currentQuestion.getCorrectAnswerIndex() == selectedAnswer) {
                scoreManager.incrementScore(); // Aumentar la puntuación del nivel
                Toast.makeText(context, "¡Correcto!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Incorrecto", Toast.LENGTH_SHORT).show();
            }

            if (questionManager.hasNextQuestion()) {
                questionManager.moveToNextQuestion();
                loadQuestion();
            } else {
                // Guardar progreso y actualizar la puntuación del usuario al finalizar el nivel
                saveLevelProgress(questionManager.getLevel());
                updateUserScore(scoreManager.getScore()); // Actualizar la puntuación del usuario
                tvScore.setText("Puntuación final: " + scoreManager.getScore());
                Toast.makeText(context, getCongratulatoryMessage(scoreManager.getScore()), Toast.LENGTH_LONG).show();

                tvQuestion.setVisibility(View.GONE);
                btnOption1.setVisibility(View.GONE);
                btnOption2.setVisibility(View.GONE);
                btnOption3.setVisibility(View.GONE);
                btnOption4.setVisibility(View.GONE);
                btnContinue.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateUserScore(int score) {
        // Obtener SharedPreferences donde se guarda el puntaje del usuario
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Recuperar el puntaje actual del usuario
        int currentScore = sharedPreferences.getInt("user_score", 0);

        // Sumar la puntuación del nivel al puntaje total del usuario
        int newScore = currentScore + score;

        // Guardar el nuevo puntaje en SharedPreferences
        editor.putInt("user_score", newScore);
        editor.apply();

        // Opcional: Puedes mostrar el puntaje actualizado inmediatamente o en la siguiente actividad
        Toast.makeText(context, "Puntaje total actualizado: " + newScore, Toast.LENGTH_SHORT).show();
    }


}
