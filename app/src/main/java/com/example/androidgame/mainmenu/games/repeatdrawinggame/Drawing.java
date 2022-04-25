package com.example.androidgame.mainmenu.games.repeatdrawinggame;

import java.util.Random;

public class Drawing {
    private int size = 2;

    public int drawingTilesCount  = 2;
    private boolean[][] drawingFlags;


    public int getDrawingTilesCount() {
        return drawingTilesCount;
    }

    public void setDrawingTilesCount(int drawingTilesCount) {
        this.drawingTilesCount = drawingTilesCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean[][] getDrawing(){
        return drawingFlags;
    }

    public void create(){
        int count = drawingTilesCount;
        drawingFlags = new boolean[size][size];

        while(count > 0){
            int i = (int)(Math.random()*size);
            int j = (int)(Math.random()*size);
            if(!drawingFlags[i][j]){
                drawingFlags[i][j] = true;
                count--;
            }
        }
    }
}
