package com.sumsung.minigames.gamecontrollers.gamecomplicators;

import com.sumsung.minigames.mainmenu.games.repeatdrawinggame.Drawing;

public class RepeatDrawingGameComplicator extends GameComplicator{

    private final Drawing drawing;

    public RepeatDrawingGameComplicator(Drawing drawing){
        this.drawing = drawing;
    }

    @Override
    public void complicateGame() {
        incrementGameDifficulty();
        if(getGameDifficulty() > drawing.getDrawingTilesCount()){
            drawing.setDrawingTilesCount(getGameDifficulty());
            if(drawing.getDrawingTilesCount() >= drawing.getSize()* drawing.getSize()){
                drawing.setSize(drawing.getSize()+1);
            }
        }
    }
}
