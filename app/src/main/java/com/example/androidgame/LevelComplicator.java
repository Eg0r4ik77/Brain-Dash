package com.example.androidgame;

public abstract class LevelComplicator {

    private int levelDifficulty = 0;

    public abstract void complicateLevel();

    public int getLevelDifficulty() {
        return levelDifficulty;
    }

    public void incrementLevelDifficulty() {
        levelDifficulty++;
    }

    public void resetLevelDifficulty() {
        levelDifficulty = 0;
    }
}
