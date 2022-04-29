package com.example.androidgame.gamecontrollers.gamecomplicators;

import com.example.androidgame.mainmenu.games.shultetablegame.SchulteTable;

public class SchulteTableGameComplicator extends GameComplicator{

    private static final int MEDIUM_LEVEL_DIFFICULTY = 6;
    private static final int HARD_LEVEL_DIFFICULTY = 15;

    private final SchulteTable schulteTable;

    public SchulteTableGameComplicator(SchulteTable schulteTable){
        this.schulteTable = schulteTable;
    }

    @Override
    public void complicateGame() {
        incrementGameDifficulty();
        switch (getGameDifficulty()){
            case MEDIUM_LEVEL_DIFFICULTY:
                schulteTable.setSize(3);
                break;
            case HARD_LEVEL_DIFFICULTY:
                schulteTable.setSize(4);
                break;
            default:
                break;
        }
    }
}
