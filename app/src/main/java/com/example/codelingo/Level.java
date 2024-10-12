package com.example.codelingo;

public class Level {

    private final int number;
    private boolean isUnlocked;

    public Level(int number, boolean isUnlocked) {
        this.number = number;
        this.isUnlocked = isUnlocked;
    }

    public int getNumber() {
        return number;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void unlock() {
        this.isUnlocked = true;
    }

}
