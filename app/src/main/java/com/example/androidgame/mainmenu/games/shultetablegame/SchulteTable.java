package com.example.androidgame.mainmenu.games.shultetablegame;

import android.text.Layout;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SchulteTable {

    private Button[][] table;

    private int size;

    public void generate(TableLayout layout){
        size = (int)(Math.random()*2+2.9);
        int number = 0;
        table = new Button[size][size];

        List<Integer> solution = new ArrayList<>();
        for (int i = 1; i <= size*size; i++) {
            solution.add(i);
        }
        Collections.shuffle(solution);

        for (int i=0; i < size; i++) {
            TableRow row = new TableRow(layout.getContext());
            for (int j=0; j < size; j++) {
                table[i][j] = new Button(layout.getContext());

                table[i][j].setText(String.valueOf(solution.get(number++)));
                row.addView(table[i][j]);
            }
            layout.addView(row);
        }
    }
}
