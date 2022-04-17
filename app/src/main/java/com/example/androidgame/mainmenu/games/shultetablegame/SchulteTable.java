package com.example.androidgame.mainmenu.games.shultetablegame;

import android.graphics.Color;
import android.text.BoringLayout;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.androidgame.gamecontrollers.gamecomlicators.GameComplicator;
import com.example.androidgame.gamecontrollers.gamecomlicators.SchulteTableGameComplicator;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SchulteTable {

    private int[][] table;
    private int size = 2;

    public void generate(){
        int number = 0;
        table = new int[size][size];

        List<Integer> solution = new ArrayList<>();
        for (int i = 1; i <= size*size; i++) {
            solution.add(i);
        }
        Collections.shuffle(solution);

        for (int i=0; i < size; i++) {
            for (int j=0; j < size; j++) {
                table[i][j] = solution.get(number++);
            }
        }
    }

    public void setSize(int size){
        this.size = size;
    }
    public int[][] getTable(){return table;}
}
