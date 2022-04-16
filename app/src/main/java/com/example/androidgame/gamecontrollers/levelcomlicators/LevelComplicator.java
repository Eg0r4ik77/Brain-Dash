package com.example.androidgame.gamecontrollers.levelcomlicators;

public abstract class LevelComplicator {

    private int levelDifficulty = 0;

    public abstract void complicateLevel();

    public int getLevelDifficulty() {
        return levelDifficulty;
    }

    public void incrementLevelDifficulty() {
        levelDifficulty++;
    }
}