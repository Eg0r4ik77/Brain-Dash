package com.sumsung.braindash.mainmenu.games.shultetablegame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchulteTable {

    private int[][] table;
    private int size = 2;

    public void generate(){
        int buttonNumber = 0;
        table = new int[size][size];

        List<Integer> solution = new ArrayList<>();
        for (int i = 1; i <= size*size; i++) {
            solution.add(i);
        }
        Collections.shuffle(solution);

        for (int i=0; i < size; i++) {
            for (int j=0; j < size; j++) {
                table[i][j] = solution.get(buttonNumber++);
            }
        }
    }

    public void setSize(int size){
        this.size = size;
    }
    public int[][] getTable(){return table;}
}
