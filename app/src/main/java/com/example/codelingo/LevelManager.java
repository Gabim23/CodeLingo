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

    public LevelManager(Context context, TextView tvQuestion, TextView tvScore,
                        Button btnOption1, Button btnOption2, Button btnOption3, Button btnOption4, Button btnContinue) {
        this.context = context;
        this.tvQuestion = tvQuestion;
        this.tvScore = tvScore;
        this.btnOption1 = btnOption1;
        this.btnOption2 = btnOption2;
        this.btnOption3 = btnOption3;
        this.btnOption4 = btnOption4;
        this.btnContinue = btnContinue;
        questionManager = new QuestionManager();
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
            btnOption1.setVisibility(View.VISIBLE);
            btnOption2.setVisibility(View.VISIBLE);
            btnOption3.setVisibility(View.VISIBLE);
            btnOption4.setVisibility(View.VISIBLE);
            btnContinue.setVisibility(View.GONE);
            tvScore.setVisibility(View.GONE);
        } else {
            saveLevelProgress(1);
            tvQuestion.setVisibility(View.GONE);
            btnOption1.setVisibility(View.GONE);
            btnOption2.setVisibility(View.GONE);
            btnOption3.setVisibility(View.GONE);
            btnOption4.setVisibility(View.GONE);
            tvScore.setText("Puntuación final: " + scoreManager.getScore());
            btnContinue.setVisibility(View.VISIBLE);
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
                scoreManager.incrementScore();
                Toast.makeText(context, "¡Correcto!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Incorrecto", Toast.LENGTH_SHORT).show();
            }
            if (questionManager.hasNextQuestion()) {
                questionManager.moveToNextQuestion();
                loadQuestion();
            } else {
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

    private void saveLevelProgress(int level) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LevelPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("level_" + level, true);
        editor.apply();
    }

}