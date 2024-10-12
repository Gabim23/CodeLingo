package com.example.codelingo;

import java.util.ArrayList;
import java.util.List;

public class QuestionManager {
    private List<Question> questions;
    private int currentQuestionIndex = 0;

    public QuestionManager() {
        questions = new ArrayList<>();
        loadQuestions();
    }

    private void loadQuestions() {
        questions.add(new Question("¿Qué palabra reservada se usa para crear un objeto?",
                new String[]{"new", "class", "return", "package"}, 0));
        questions.add(new Question("¿Cuál es el tipo de dato para números enteros?",
                new String[]{"double", "int", "boolean", "char"}, 1));
        questions.add(new Question("¿Cuál de estos es un bucle en Java?",
                new String[]{"switch", "if", "while", "case"}, 2));
        questions.add(new Question("¿Cómo se llama al método principal en Java?",
                new String[]{"main", "start", "run", "init"}, 0));
        questions.add(new Question("¿Qué método se utiliza para imprimir en la consola en Java?",
                new String[]{"display()", "System.out.println()", "write()", "print()"}, 1));
        questions.add(new Question("¿Qué operador se usa para comparar igualdad?",
                new String[]{"=", "==", "!=", "<>"}, 1));
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean hasNextQuestion() {
        return currentQuestionIndex < questions.size() - 1;
    }

    public void moveToNextQuestion() {
        if (hasNextQuestion()) {
            currentQuestionIndex++;
        }
    }

    public int getTotalQuestions() {
        return questions.size();
    }

}
