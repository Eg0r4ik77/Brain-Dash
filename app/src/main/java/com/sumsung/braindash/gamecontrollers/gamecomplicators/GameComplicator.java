package com.sumsung.braindash.gamecontrollers.gamecomplicators;

public abstract class GameComplicator {

    private int gameDifficulty;

    public abstract void complicateGame();

    public int getGameDifficulty() {
        return gameDifficulty;
    }
    public void incrementGameDifficulty() {
        gameDifficulty++;
    }
}
