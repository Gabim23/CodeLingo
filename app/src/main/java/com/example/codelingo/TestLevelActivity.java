package com.example.codelingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TestLevelActivity extends AppCompatActivity {

    private String[] questions = {
            "¿Cuál es el operador para sumar en Java?",
            "¿Cómo declaras una variable entera en Java?",
            "¿Qué palabra clave se usa para crear un objeto?"
    };

    private String[][] options = {
            {"+", "-", "*", "/"},
            {"int", "Integer", "float", "String"},
            {"new", "create", "instance", "object"}
    };

    private int[] correctAnswers = {0, 0, 0}; // Índices de las respuestas correctas
    private int currentQuestionIndex = 0;
    private int score = 0;

    private TextView tvQuestion;
    private Button btnOption1, btnOption2, btnOption3, btnOption4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_level);

        tvQuestion = findViewById(R.id.tvQuestion);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);

        // Muestra la primera pregunta
        displayQuestion();

        // Listener para las opciones
        btnOption1.setOnClickListener(v -> checkAnswer(0));
        btnOption2.setOnClickListener(v -> checkAnswer(1));
        btnOption3.setOnClickListener(v -> checkAnswer(2));
        btnOption4.setOnClickListener(v -> checkAnswer(3));
    }

    private void displayQuestion() {
        // Actualiza las opciones y la pregunta
        tvQuestion.setText(questions[currentQuestionIndex]);
        btnOption1.setText(options[currentQuestionIndex][0]);
        btnOption2.setText(options[currentQuestionIndex][1]);
        btnOption3.setText(options[currentQuestionIndex][2]);
        btnOption4.setText(options[currentQuestionIndex][3]);
    }

    private void checkAnswer(int selectedOption) {
        if (selectedOption == correctAnswers[currentQuestionIndex]) {
            score++;
            Toast.makeText(TestLevelActivity.this, "¡Respuesta Correcta!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(TestLevelActivity.this, "Respuesta Incorrecta. La correcta era: " + options[currentQuestionIndex][correctAnswers[currentQuestionIndex]], Toast.LENGTH_LONG).show();
        }

        // Pasar automáticamente a la siguiente pregunta
        currentQuestionIndex++;

        // Si aún hay más preguntas, mostrar la siguiente
        if (currentQuestionIndex < questions.length) {
            displayQuestion();
        } else {
            // Cuando todas las preguntas han sido respondidas
            Toast.makeText(TestLevelActivity.this, "Prueba completada. Puntuación: " + score, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(TestLevelActivity.this, MainActivity.class); // Redirigir a la pantalla de inicio de sesión
            startActivity(intent);
            finish();
        }
    }
}
