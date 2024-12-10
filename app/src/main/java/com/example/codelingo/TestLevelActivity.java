package com.example.codelingo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TestLevelActivity extends AppCompatActivity {

    private String[] questions = {
            "¿Cuál es el operador utilizado para la suma en programación?",
            "¿Cómo declaras una variable de tipo entero?",
            "¿Qué significa 'OOP' en programación?",
            "¿Cuál es el tipo de dato adecuado para almacenar una letra?",
            "¿Qué es una función recursiva?",
            "¿Qué hace el comando 'break' en un bucle?",
            "¿Qué es un array en programación?",
            "¿Cuál es la diferencia entre '==' y '===' en lenguajes como JavaScript?",
            "¿Qué es un algoritmo?",
            "¿Qué es una variable en programación?"
    };

    private String[][] options = {
            {"+", "-", "*", "/"},
            {"int", "Integer", "float", "String"},
            {"Orientación a Objetos", "Operador de Operaciones", "Overloaded Object Protocol", "Open Object Programming"},
            {"int", "char", "float", "boolean"},
            {"Es una función que se llama a sí misma", "Es una función que recibe valores", "Es una función que retorna un valor", "Es una función con parámetros fijos"},
            {"Detiene el bucle y continúa con el siguiente bloque de código", "Pausa el bucle por un tiempo", "Hace que el bucle se repita", "No tiene ningún efecto en el bucle"},
            {"Una estructura de datos que almacena elementos de manera secuencial", "Una función que hace operaciones con múltiples valores", "Un tipo de variable que puede tener varios valores", "Un tipo de bucle"},
            {"'==' compara valores, '===' compara tipo y valor", "'==' y '===' hacen lo mismo", "'==' se usa solo en comparación de enteros", "'===' no existe en JavaScript"},
            {"Un conjunto de instrucciones que resuelve un problema", "Una colección de datos", "Un tipo de bucle", "Un conjunto de variables"},
            {"Es un contenedor de datos", "Es un valor que cambia constantemente", "Es un espacio de memoria donde se guarda un dato", "Es un tipo de dato en programación"}
    };

    private int[] correctAnswers = {0, 0, 0, 1, 0, 0, 0, 0, 0, 2}; // Índices de las respuestas correctas
    private int currentQuestionIndex = 0;
    private int score = 0;

    private TextView tvQuestion, tvFeedback, tvScore;
    private Button btnOption1, btnOption2, btnOption3, btnOption4, btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_level);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvFeedback = findViewById(R.id.tvFeedback); // TextView para retroalimentación
        tvScore = findViewById(R.id.tvScore); // TextView para mostrar la puntuación
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);
        btnContinue = findViewById(R.id.btnContinue); // Botón para continuar

        // Muestra la primera pregunta
        displayQuestion();

        // Listener para las opciones
        btnOption1.setOnClickListener(v -> checkAnswer(0));
        btnOption2.setOnClickListener(v -> checkAnswer(1));
        btnOption3.setOnClickListener(v -> checkAnswer(2));
        btnOption4.setOnClickListener(v -> checkAnswer(3));

        // Listener para el botón de continuar
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(TestLevelActivity.this, WelcomeActivity.class); // Redirigir a la pantalla de inicio de sesión
            startActivity(intent);
            finish();
        });
    }

    private void displayQuestion() {
        // Actualiza las opciones y la pregunta
        tvQuestion.setText(questions[currentQuestionIndex]);
        btnOption1.setText(options[currentQuestionIndex][0]);
        btnOption2.setText(options[currentQuestionIndex][1]);
        btnOption3.setText(options[currentQuestionIndex][2]);
        btnOption4.setText(options[currentQuestionIndex][3]);

        // Ocultar la retroalimentación y el botón de continuar
        tvFeedback.setVisibility(View.GONE);
        btnContinue.setVisibility(View.GONE);
    }

    private void checkAnswer(int selectedOption) {
        // Mostrar la retroalimentación después de la respuesta
        if (selectedOption == correctAnswers[currentQuestionIndex]) {
            score++;
            tvFeedback.setText("¡Respuesta Correcta!");
            tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvFeedback.setText("Respuesta Incorrecta. La correcta era: " + options[currentQuestionIndex][correctAnswers[currentQuestionIndex]]);
            tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        // Mostrar retroalimentación
        tvFeedback.setVisibility(View.VISIBLE);

        // Pasar automáticamente a la siguiente pregunta después de un pequeño retraso (ahora 2 segundos)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentQuestionIndex++;

                // Si aún hay más preguntas, mostrar la siguiente
                if (currentQuestionIndex < questions.length) {
                    displayQuestion();
                    tvFeedback.setVisibility(View.GONE); // Ocultar retroalimentación al cambiar de pregunta
                } else {
                    // Cuando todas las preguntas han sido respondidas
                    showTotalScore();
                }
            }
        }, 2000); // Esperar 2 segundos antes de mostrar la siguiente pregunta
    }

    private void showTotalScore() {
        // Ocultar los botones de las opciones y la pregunta
        findViewById(R.id.tvQuestion).setVisibility(View.GONE);
        findViewById(R.id.btnOption1).setVisibility(View.GONE);
        findViewById(R.id.btnOption2).setVisibility(View.GONE);
        findViewById(R.id.btnOption3).setVisibility(View.GONE);
        findViewById(R.id.btnOption4).setVisibility(View.GONE);

        // Ocultar la retroalimentación al mostrar la puntuación final
        tvFeedback.setVisibility(View.GONE);

        // Mostrar la puntuación final
        tvScore.setText("Puntuación total: " + score + " de " + questions.length);
        tvScore.setVisibility(View.VISIBLE); // Hacer visible la puntuación

        // Agregar un mensaje orientativo dependiendo de la puntuación
        String message = "";
        if (score == 10) {
            message = "¡Excelente! ¡Dominaste el tema!";
        } else if (score >= 7) {
            message = "¡Bien hecho! Tienes buenos conocimientos en programación.";
        } else if (score >= 4) {
            message = "¡No está mal! Pero puedes mejorar.";
        } else {
            message = "¡Necesitas más práctica! Revisa los conceptos clave.";
        }

        tvFeedback.setText(message);
        tvFeedback.setVisibility(View.VISIBLE); // Mostrar mensaje orientativo

        // Mostrar el botón de continuar
        btnContinue.setVisibility(View.VISIBLE);
    }
}
