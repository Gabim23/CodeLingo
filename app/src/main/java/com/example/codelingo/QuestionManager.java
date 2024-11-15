package com.example.codelingo;

import java.util.ArrayList;
import java.util.List;

public class QuestionManager {

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int level;

    public QuestionManager(int level) {
        this.level = level;
        questions = new ArrayList<>();
        loadQuestions(level);
    }

    public int getLevel() {
        return level;
    }

    void loadQuestions(int level) {
        switch (level) {
            case 0:
                loadLevel0Questions();
                break;
            case 1:
                loadLevel1Questions();
                break;
            case 2:
                loadLevel2Questions();
                break;
            default:
                System.out.println("Nivel no válido");
                break;
        }
    }

    // Preguntas para el nivel 0
    private void loadLevel0Questions() {
        questions.add(new Question("¿Qué palabra reservada se usa para crear un objeto?",
                new String[]{"new", "class", "return", "package"}, 0));
        questions.add(new Question("¿Cuál es el tipo de dato para números enteros?",
                new String[]{"double", "int", "boolean", "char"}, 1));
        questions.add(new Question("¿Cuál de estos es un bucle?",
                new String[]{"switch", "if", "while", "case"}, 2));
        questions.add(new Question("¿Cómo se llama al método principal?",
                new String[]{"main", "start", "run", "init"}, 0));
        questions.add(new Question("¿Qué método se utiliza para imprimir en la consola?",
                new String[]{"display()", "System.out.println()", "write()", "print()"}, 1));
        questions.add(new Question("¿Qué operador se usa para comparar igualdad?",
                new String[]{"=", "==", "!=", "<>"}, 1));
    }

    // Preguntas para el nivel 1
    private void loadLevel1Questions() {
        questions.add(new Question("¿Cómo se declara una variable en Java?",
                new String[]{"variable x;", "int x;", "x = 10;", "var x;"}, 1));
        questions.add(new Question("¿Cuál es la función de 'this' en un método?",
                new String[]{"Referirse al objeto actual", "Llamar al constructor", "Crear una nueva clase", "Terminar la ejecución"}, 0));
        questions.add(new Question("¿Cómo se define una condición 'si-no'?",
                new String[]{"do-while", "for-if", "if-else", "try-catch"}, 2));
        questions.add(new Question("¿Qué hace el método equals() en un objeto?",
                new String[]{"Asigna valores", "Retorna el valor del objeto", "Crea un objeto", "Compara dos objetos"}, 3));
        questions.add(new Question("¿Cuál es la palabra clave para la herencia en Java?",
                new String[]{"extends", "inherits", "super", "extendsFrom"}, 0));
        questions.add(new Question("¿Cómo se define una clase en Java?",
                new String[]{"def MiClase {}", "function MiClase {}", "class MiClase {}", "String MiClase() {}"}, 2));
    }

    // Preguntas para el nivel 2
    private void loadLevel2Questions() {
        questions.add(new Question("¿Cuál es el valor predeterminado de un boolean en Java?",
                new String[]{"true","null", "0", "false"}, 3));
        questions.add(new Question("¿Qué operador se usa para realizar una comparación entre dos valores?",
                new String[]{"==", "=", ">", "<="}, 0));
        questions.add(new Question("¿Cuál de estos es un modificador de acceso?",
                new String[]{"publicStatic", "protected", "constant", "return"}, 1));
        questions.add(new Question("¿Qué se usa para crear una interfaz?",
                new String[]{"interface", "class", "abstract", "implement"}, 0));
        questions.add(new Question("¿Qué operador lógico representa el 'Y' lógico?",
                new String[]{"||", "&", "&&", "!"}, 2));
        questions.add(new Question("¿Qué se utiliza para detener la ejecución de un bucle?",
                new String[]{"stop", "exit", "break", "terminate"}, 2));
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
