package com.sumsung.braindash.gamecontrollers.gamecomplicators;

import com.sumsung.braindash.mainmenu.games.repeatdrawinggame.Drawing;

public class RepeatDrawingGameComplicator extends GameComplicator{

    private final Drawing drawing;

    public RepeatDrawingGameComplicator(Drawing drawing){
        this.drawing = drawing;
    }

    @Override
    public void complicateGame() {
        incrementGameDifficulty();
        drawing.setDrawingTilesCount((int)(Math.random()*(drawing.getSize()*drawing.getSize()-2)+2));
        switch (getGameDifficulty()){
            case 5:
                drawing.setSize(3);
                break;
            case 13:
                drawing.setSize(4);
                break;
        }
    }
}
