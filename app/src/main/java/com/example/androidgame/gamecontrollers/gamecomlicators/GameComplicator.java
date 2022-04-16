package com.example.androidgame.gamecontrollers.gamecomlicators;

public abstract class GameComplicator {

    private int levelDifficulty = 0;

    public abstract void complicateLevel();

    public int getLevelDifficulty() {
        return levelDifficulty;
    }

    public void incrementLevelDifficulty() {
        levelDifficulty++;
    }
}
