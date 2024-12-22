package com.example.codelingo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private int coins;
    private int lives;  // Inicializar con 5 vidas
    private LinearLayout llLivesContainer;
    private boolean isGameOver = false; // Bandera para evitar mostrar el puntaje total después de Game Over




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        llLivesContainer = findViewById(R.id.llLivesContainer);
        btnContinue = findViewById(R.id.btnContinue);
        tvFeedback = findViewById(R.id.tvFeedback);
        tvScore = findViewById(R.id.tvScore);
        currentLevel = getIntent().getIntExtra("CURRENT_LEVEL", 0);
        questionManager = new QuestionManager(currentLevel);
        loadUserLives();
        updateLivesDisplay(lives); // Mostrar las vidas iniciales

        loadCurrentQuestion();
        TextView tvQuestion = findViewById(R.id.tvQuestion);
        Button btnOption1 = findViewById(R.id.btnOption1);
        Button btnOption2 = findViewById(R.id.btnOption2);
        Button btnOption3 = findViewById(R.id.btnOption3);
        Button btnOption4 = findViewById(R.id.btnOption4);
        LevelManager levelManager = new LevelManager(
                this,
                currentLevel,
                getIntent().getIntExtra("LIVES_REMAINING", 5), // Recoger vidas restantes
                tvQuestion,
                tvScore,
                btnOption1,
                btnOption2,
                btnOption3,
                btnOption4,
                btnContinue
        );        setOptionButtonListeners(btnOption1, btnOption2, btnOption3, btnOption4);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLevelProgress(currentLevel);
                updateUserScore();
                saveUserCoins();
                Intent intent = new Intent(QuestionsActivity.this, LevelsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btnContinue.setVisibility(View.GONE);
    }

    private void updateLivesDisplay(int lives) {
        llLivesContainer.removeAllViews(); // Limpiar los corazones actuales

        // Mostrar "Vidas:" seguido de los corazones
        TextView livesLabel = new TextView(this);
        livesLabel.setText("Vidas: ");
        livesLabel.setTextSize(18);  // Tamaño del texto
        llLivesContainer.addView(livesLabel);

        // Añadir corazones en función de las vidas restantes
        for (int i = 0; i < lives; i++) {
            TextView heart = new TextView(this);
            heart.setText("❤️");
            heart.setTextSize(24);  // Tamaño del corazón
            heart.setPadding(8, 0, 8, 0);  // Espaciado entre los corazones
            llLivesContainer.addView(heart); // Añadir al contenedor
        }
    }


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
        }

        updateLivesDisplay(lives); // Actualizar la UI
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

    private void saveUserLives() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        if (username != null) {
            try {
                FileInputStream fis = openFileInput("users.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                List<String> lines = new ArrayList<>();
                String line;
                boolean found = false;

                while ((line = reader.readLine()) != null) {
                    String[] credentials = line.split(",");
                    if (credentials[0].equals(username)) {
                        credentials[2] = String.valueOf(lives); // Actualizar las vidas en el archivo
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

        // También guarda las vidas en SharedPreferences como respaldo
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lives", lives);
        editor.apply();
    }


    private void saveUserCoins() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        if (username != null) {
            try {
                FileInputStream fis = openFileInput("users.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                List<String> lines = new ArrayList<>();
                String line;
                boolean found = false;

                while ((line = reader.readLine()) != null) {
                    String[] credentials = line.split(",");
                    if (credentials[0].equals(username)) {
                        credentials[5] = String.valueOf(coins);
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


    private void showGameOver() {
        tvFeedback.setText("¡Has perdido todas tus vidas! Elige una opción:");
        tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        tvFeedback.setVisibility(View.VISIBLE);

        btnContinue.setVisibility(View.VISIBLE);
        btnContinue.setText("Volver al menú principal");

        Button btnGoToShop = new Button(this);
        btnGoToShop.setText("Comprar más vidas");
        btnGoToShop.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        btnGoToShop.setTextColor(getResources().getColor(android.R.color.white));
        btnGoToShop.setPadding(16, 16, 16, 16);

        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.llLivesContainer);
        parentLayout.addView(btnGoToShop); // Agrega el botón al diseño dinámicamente

        btnContinue.setOnClickListener(v -> {
            saveUserLives(); // Guarda las vidas antes de regresar
            Intent intent = new Intent(QuestionsActivity.this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Asegura que se reinicie el menú principal
            startActivity(intent);
            finish();
        });

        btnGoToShop.setOnClickListener(v -> {
            saveUserLives(); // Guarda las vidas antes de redirigir
            Intent intent = new Intent(QuestionsActivity.this, Shop.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Asegura que se reinicie la tienda
            startActivity(intent);
            finish();
        });
    }


    private void checkAnswer(String userAnswer) {
        String correctAnswer = currentQuestion.getOptions()[currentQuestion.getCorrectAnswerIndex()];

        if (userAnswer.equals(correctAnswer)) {
            correctAnswers++;
            coins += 10;
            showFeedback(true, "¡Excelente!");
        } else {
            showFeedback(false, "Incorrecto: La respuesta correcta es " + correctAnswer);

            // Restar una vida si la respuesta es incorrecta
            lives--;
            updateLivesDisplay(lives); // Actualizar UI con las vidas restantes
            saveUserLives(); // Guardar las vidas restantes (que serán 0)

            // Si el jugador se queda sin vidas
            if (lives <= 0) {
                lives = 0; // Asegurarse de que las vidas no sean negativas
                showGameOver(); // Mostrar mensaje de "Game Over"
                isGameOver = true; // Marcar que el juego ha terminado
                return; // Salir del método sin pasar a la siguiente pregunta
            }
        }

        // Si aún quedan vidas y el juego no ha terminado, pasa a la siguiente pregunta
        if (lives > 0 && !isGameOver) {
            new Handler().postDelayed(() -> {
                questionManager.moveToNextQuestion();
                loadCurrentQuestion(); // Cargar la nueva pregunta
            }, 2000);
        }
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
        }, 2000);
    }

    private void showTotalScore() {
        if (isGameOver) {
            // No mostrar la puntuación total si el juego ha terminado
            return;
        }

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
