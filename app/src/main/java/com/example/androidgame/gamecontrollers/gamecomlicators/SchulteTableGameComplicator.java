package com.example.androidgame.gamecontrollers.gamecomlicators;

import com.example.androidgame.mainmenu.games.shultetablegame.SchulteTable;

public class SchulteTableGameComplicator extends GameComplicator{

    private final SchulteTable schulteTable;

    private final int mediumLevelDifficulty = 6;
    private final int hardLevelDifficulty = 15;

    public SchulteTableGameComplicator(SchulteTable schulteTable){
        this.schulteTable = schulteTable;
    }

    @Override
    public void complicateGame() {
        incrementGameDifficulty();
        switch (getGameDifficulty()){
            case mediumLevelDifficulty:
                schulteTable.setSize(3);
                break;
            case hardLevelDifficulty:
                schulteTable.setSize(4);
                break;
            default:
            break;
        }
    }
}
