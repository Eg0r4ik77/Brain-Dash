package com.example.androidgame.gamecontrollers.gamecomplicators;

import com.example.androidgame.mainmenu.games.repeatdrawinggame.Drawing;

public class RepeatDrawingGameComplcator extends GameComplicator{

    private Drawing drawing;

    public RepeatDrawingGameComplcator(Drawing drawing){
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
