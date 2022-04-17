package com.example.androidgame.gamecontrollers.gamecomlicators;

public abstract class GameComplicator {

    private int gameDifficulty = 0;

    public abstract void complicateGame();

    public int getGameDifficulty() {
        return gameDifficulty;
    }

    public void incrementGameDifficulty() {
        gameDifficulty++;
    }
}
