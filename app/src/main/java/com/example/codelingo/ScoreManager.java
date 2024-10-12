package com.example.codelingo;

public class ScoreManager {

    private int score = 0;

    public void incrementScore() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        score = 0;
    }

}
